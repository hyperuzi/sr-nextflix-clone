package com.utkarsh.gupta.netflix_clone.service;

import com.utkarsh.gupta.netflix_clone.model.User;
import com.utkarsh.gupta.netflix_clone.repository.UserRepository;
import com.utkarsh.gupta.netflix_clone.security.JwtUtil;
import com.utkarsh.gupta.netflix_clone.util.UtilFunctions;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.utkarsh.gupta.netflix_clone.security.JwtUtil.JWT_COOKIE_NAME;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UtilFunctions utilFunctions;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private final List<String> profilePics = List.of("/avatar1.png", "/avatar2.png", "/avatar3.png", "/avatar4.png", "/avatar5.png", "/avatar6.png");

    @Override
    public Cookie signUp(User user) throws Exception {
        if(user.getEmail() == null || user.getPassword() == null || user.getUsername() == null || user.getUsername().isEmpty())
            throw new Exception("All fields are required.");

        if(!UtilFunctions.isValidEmail(user.getEmail()))
            throw new Exception("Please enter a valid email");

        if(user.getPassword().length() < 6) {
            throw new Exception("Please enter a password with length more than 8.");
        }
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new Exception("Email already exists. Please use a different email.");
        }
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new Exception("Username already exists. Please use a different username.");
        }

        user.setImage(profilePics.get(new Random().nextInt(profilePics.size())));
        user.setSearchHistory(new ArrayList<>());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        user.setRoles(roles);
        user = userRepository.save(user);
        user.setPassword(null);
        return jwtUtil.getCookie(user);
    }

    @Override
    public Cookie logIn(User user) throws Exception {
        if(user.getEmail() == null || user.getPassword() == null || user.getPassword().isEmpty())
            throw new Exception("All fields are required");
        if(!UtilFunctions.isValidEmail(user.getEmail()))
            throw new Exception("Enter a valid email");
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if(optionalUser.isEmpty())
            throw new Exception("User does not exists.");
        User userDetails = optionalUser.get();
        if(!passwordEncoder.matches(user.getPassword(), userDetails.getPassword())){
            throw new Exception("Invalid password");
        }
        user.setRoles(userDetails.getRoles());
        user.setId(userDetails.getId());
        user.setSearchHistory(userDetails.getSearchHistory());
        user.setImage(userDetails.getImage());
        user.setPassword(null);
        return jwtUtil.getCookie(user);
    }

    @Override
    public Cookie logOut(){
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}

package com.utkarsh.gupta.netflix_clone.security;

import com.utkarsh.gupta.netflix_clone.model.User;
import com.utkarsh.gupta.netflix_clone.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;


@Component
@Order(2)
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromCookies(request);
        String userId = null;
        if(token != null) {
            userId = jwtUtil.extractUserId(token);
        }

        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
            Optional<User> user = userRepository.findById(userId);
            if(user.isPresent() && jwtUtil.validateToken(token, user.get())){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user.get(), null, user.get().getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> JwtUtil.JWT_COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();
            if(jwtCookie.isPresent()){
                return jwtCookie.get().getValue();
            }
        }
        return null;
    }

    public boolean doesEndpointExist(String path) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        return handlerMethods.keySet().stream()
                .anyMatch(requestMappingInfo -> requestMappingInfo.getPatternsCondition().getPatterns()
                        .stream()
                        .anyMatch(pattern -> pattern.matches(path)));
    }
}

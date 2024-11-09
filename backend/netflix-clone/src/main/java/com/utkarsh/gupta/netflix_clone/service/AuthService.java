package com.utkarsh.gupta.netflix_clone.service;

import com.utkarsh.gupta.netflix_clone.model.User;
import jakarta.servlet.http.Cookie;


public interface AuthService {
    Cookie signUp(User user) throws Exception;
    Cookie logOut();
    Cookie logIn(User user) throws Exception;

}

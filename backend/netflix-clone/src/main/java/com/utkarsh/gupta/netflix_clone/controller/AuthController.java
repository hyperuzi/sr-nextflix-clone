package com.utkarsh.gupta.netflix_clone.controller;

import com.utkarsh.gupta.netflix_clone.model.User;
import com.utkarsh.gupta.netflix_clone.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class AuthController {
    @Autowired
    private AuthService authService;


    @PostMapping("login")
    ResponseEntity<Object> login(@RequestBody User user, HttpServletResponse response){
        Map<String, Object> responseBody = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        try {
            Cookie cookie = authService.logIn(user);
            response.addCookie(cookie);
            responseBody.put("message", "Logged in successfully.");
            responseBody.put("user", user);
        } catch(Exception e){
            responseBody.put("error", e.getMessage());
            System.out.println("error : " + e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(responseBody, status);
    }

    @PostMapping("signup")
    ResponseEntity<Object> signUp(@RequestBody User user, HttpServletResponse response){
        Map<String, Object> responseBody = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        try {
            Cookie cookie = authService.signUp(user);
            response.addCookie(cookie);
            responseBody.put("message", "User created successfully");
            responseBody.put("user", user);
        } catch (Exception e) {
            System.out.println("Error in signup page - " + e.getMessage());
            responseBody.put("error", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        }
        responseBody.put("status", status);
        return new ResponseEntity<>(responseBody, status);
    }

    @PostMapping("logout")
    ResponseEntity<Object> logOut(HttpServletResponse response){
            Map<String, Object> responseBody = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        try {
            Cookie cookie = authService.logOut();
            response.addCookie(cookie);
            responseBody.put("message", "Logged out successfully.");
        } catch(Exception e){
            System.out.println("Error while logging out user : " + e.getMessage());
            responseBody.put("error", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        responseBody.put("status", status);
        return new ResponseEntity<>(responseBody, status);
    }

    @GetMapping("authcheck")
    ResponseEntity<Object> authCheck(){
        Map<String, Object> responseBody = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        try {
            responseBody.put("user", null);
            responseBody.put("success", true);
        } catch (Exception e) {
            responseBody.put("message", e.getMessage());
            responseBody.put("success", false);
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        }
        responseBody.put("status", status);
        return new ResponseEntity<>(responseBody, status);
    }
}

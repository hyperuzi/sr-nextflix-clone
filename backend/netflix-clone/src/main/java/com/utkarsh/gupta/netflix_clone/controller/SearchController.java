package com.utkarsh.gupta.netflix_clone.controller;

import com.utkarsh.gupta.netflix_clone.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/search/")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("person/{person}/{userId}")
    public ResponseEntity<Object> searchPerson(@PathVariable String person, @PathVariable String userId){
        Map<String, Object> response = new HashMap<>();
        try {
            String responseBody = searchService.fetchPersons(person, userId);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", e.getMessage());
            System.out.println("Error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("movie/{movie}/{userId}")
    public ResponseEntity<Object> searchMovie(@PathVariable String movie, @PathVariable String userId){
        Map<String, Object> response = new HashMap<>();
        try {
            String responseBody = searchService.fetchMovies(movie, userId);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", e.getMessage());
            System.out.println("Error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("tv/{tvShow}/{userId}")
    public ResponseEntity<Object> searchTVShow(@PathVariable String tvShow, @PathVariable String userId){
        Map<String, Object> response = new HashMap<>();
        try {
            String responseBody = searchService.fetchTVShows(tvShow, userId);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", e.getMessage());
            System.out.println("Error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("history/{userId}")
    public ResponseEntity<Object> searchHistory(@PathVariable String userId){
        Map<String, Object> response = new HashMap<>();
        try {
            String responseBody = searchService.fetchHistory(userId);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", e.getMessage());
            System.out.println("Error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("history/{searchId}/{userId}")
    public ResponseEntity<Object> deleteSearchHistory(@PathVariable String searchId, @PathVariable String userId){
        Map<String, Object> response = new HashMap<>();
        try {
            String responseBody = searchService.deleteSearchHistory(searchId, userId);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", e.getMessage());
            System.out.println("Error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

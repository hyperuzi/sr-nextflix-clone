package com.utkarsh.gupta.netflix_clone.controller;

import com.utkarsh.gupta.netflix_clone.service.TVShowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/api/v1/tv/")
public class TVShowsController {
    @Autowired
    private TVShowsService tvShowsService;

    @GetMapping("trending")
    public ResponseEntity<Object> fetchPopularTVShows(){
        Map<String, Object> response = new HashMap<>();
        try {
            String responseBody = tvShowsService.fetchPopularTVShows();
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", e.getMessage());
            System.out.println("Error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}/trailers")
    public ResponseEntity<Object> getTVShowTrailers(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        try {
            String tvShowTrailers = tvShowsService.getTVShowTrailers(id);
            return new ResponseEntity<>(tvShowTrailers, HttpStatus.OK);
        } catch(Exception e){
            response.put("message", e.getMessage());
            System.out.println("Error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}/details")
    public ResponseEntity<Object> getTVShowDetails(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        try {
            String tvShowDetails = tvShowsService.getTVShowDetails(id);
            return new ResponseEntity<>(tvShowDetails, HttpStatus.OK);
        } catch(Exception e){
            response.put("message", e.getMessage());
            System.out.println("error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}/similar")
    public ResponseEntity<Object> getSimilarTVShows(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        try{
            String similarTVShows = tvShowsService.getSimilarTVShows(id);
            return new ResponseEntity<>(similarTVShows, HttpStatus.OK);
        } catch(Exception e){
            response.put("message", e.getMessage());
            System.out.println("error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{category}")
    public ResponseEntity<Object> getTVShowsByCategory(@PathVariable String category){
        Map<String, Object> response = new HashMap<>();
        try{
            String tvShowsByCategory = tvShowsService.getTVShowsByCategory(category);
            return new ResponseEntity<>(tvShowsByCategory, HttpStatus.OK);
        } catch(Exception e){
            response.put("error", e.getMessage());
            System.out.println("error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}

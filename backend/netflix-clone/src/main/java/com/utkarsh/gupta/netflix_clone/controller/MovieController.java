package com.utkarsh.gupta.netflix_clone.controller;

import com.utkarsh.gupta.netflix_clone.service.MovieService;
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
@RequestMapping("/api/v1/movie/")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("trending")
    public ResponseEntity<Object> getTrendingMovies(){
        Map<String, Object> response = new HashMap<>();
        try {
            String responseBody = movieService.fetchPopularMovie();
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", e.getMessage());
            System.out.println("Error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}/trailers")
    public ResponseEntity<Object> getMovieTrailers(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        try {
            String movieTrailers = movieService.getMovieTrailers(id);
            return new ResponseEntity<>(movieTrailers, HttpStatus.OK);
        } catch(Exception e){
            response.put("message", e.getMessage());
            System.out.println("Error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}/details")
    public ResponseEntity<Object> getMovieDetail(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        try {
            String movieDetails = movieService.getMovieDetails(id);
            return new ResponseEntity<>(movieDetails, HttpStatus.OK);
        } catch(Exception e){
            response.put("message", e.getMessage());
            System.out.println("error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}/similar")
    public ResponseEntity<Object> getSimilarMovies(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        try{
            String similarMovies = movieService.getSimilarMovies(id);
            return new ResponseEntity<>(similarMovies, HttpStatus.OK);
        } catch(Exception e){
            response.put("message", e.getMessage());
            System.out.println("error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{category}")
    public ResponseEntity<Object> getMoviesByCategory(@PathVariable String category){
        Map<String, Object> response = new HashMap<>();
        try{
            String categoryMovies = movieService.getMoviesByCategory(category);
            return new ResponseEntity<>(categoryMovies, HttpStatus.OK);
        } catch(Exception e){
            response.put("error", e.getMessage());
            System.out.println("error : " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

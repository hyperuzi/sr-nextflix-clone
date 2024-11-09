package com.utkarsh.gupta.netflix_clone.service;

public interface MovieService {

    String fetchPopularMovie() throws Exception;
    String getMovieTrailers(String id) throws Exception;
    String getMovieDetails(String id) throws Exception;
    String getSimilarMovies(String id) throws Exception;
    String getMoviesByCategory(String category) throws Exception;
}

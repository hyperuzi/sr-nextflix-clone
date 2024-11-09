package com.utkarsh.gupta.netflix_clone.service;

public interface TVShowsService {
    String fetchPopularTVShows() throws Exception;
    String getTVShowTrailers(String id) throws Exception;
    String getTVShowDetails(String id) throws Exception;
    String getSimilarTVShows(String id) throws Exception;
    String getTVShowsByCategory(String category) throws Exception;
}

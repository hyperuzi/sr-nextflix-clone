package com.utkarsh.gupta.netflix_clone.service;

public interface SearchService {
    String fetchPersons(String person, String userId) throws Exception;
    String fetchMovies(String movie, String userId) throws Exception;
    String fetchTVShows(String tvShow, String userId) throws Exception;
    String fetchHistory(String userId) throws Exception;
    String deleteSearchHistory(String searchId, String userId) throws Exception;
}

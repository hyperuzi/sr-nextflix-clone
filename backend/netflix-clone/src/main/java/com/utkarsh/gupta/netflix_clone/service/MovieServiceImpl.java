package com.utkarsh.gupta.netflix_clone.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;

@Service
public class MovieServiceImpl implements MovieService {

    @Value("${tmdb.access.token}")
    private String TMDB_ACCESS_TOKEN;

    @Override
    public String fetchPopularMovie() throws Exception{
        JSONObject responseBody = new JSONObject();
        Response response = fetchDataFromTMDB("movie/popular?language=en-US");
        if(!response.isSuccessful()){
            throw new Exception("Error getting trending movie, please try again later.");
        }
        JSONObject jsonMoviePage = new JSONObject(response.body().string());
        JSONArray jsonMovies = (JSONArray) jsonMoviePage.get("results");
        JSONObject trendingMovie = (JSONObject) jsonMovies.get(new Random().nextInt(jsonMovies.length()));
        responseBody.put("success", true);
        responseBody.put("content", trendingMovie);
        return responseBody.toString();
    }

    @Override
    public String getMovieTrailers(String movieId) throws Exception {
        JSONObject responseBody = new JSONObject();
        Response response = fetchDataFromTMDB("movie/" + movieId + "/videos?language=en-US");
        if(!response.isSuccessful()){
            throw new Exception("Error getting movie trailers, please try again later.");
        }

        JSONObject jsonResponse = new JSONObject(response.body().string());
        JSONArray movieVideos = new JSONArray(jsonResponse.get("results").toString());
        JSONArray trailers = new JSONArray();
        for(int i=0;i < movieVideos.length();i++){
            JSONObject movieVideo = (JSONObject) movieVideos.get(i);
            if(movieVideo.get("type").equals("Teaser") || movieVideo.get("type").equals("Trailer")) {
                trailers.put(movieVideo);
            }
        }
        responseBody.put("trailers", trailers);
        responseBody.put("success", true);
        return responseBody.toString();
    }

    @Override
    public String getMovieDetails(String movieId) throws Exception {
        JSONObject responseBody = new JSONObject();
        Response response = fetchDataFromTMDB("movie/" + movieId + "?language=en-US");
        if(!response.isSuccessful())
            throw new Exception("Error getting movie details, please try again later.");
        JSONObject movieDetails = new JSONObject(response.body().string());
        responseBody.put("success", true);
        responseBody.put("content", movieDetails);
        return responseBody.toString();
    }

    @Override
    public String getSimilarMovies(String movieId) throws Exception {
        JSONObject responseBody = new JSONObject();
        Response response = fetchDataFromTMDB("movie/" + movieId + "/similar?language=en-US");
        if(!response.isSuccessful())
            throw new Exception("Error getting similar movies, please try again later.");
        JSONObject similarMovies = new JSONObject(response.body().string());
        responseBody.put("success", true);
        responseBody.put("content", similarMovies.get("results"));
        return responseBody.toString();
    }

    @Override
    public String getMoviesByCategory(String category) throws Exception {
        JSONObject responseBody = new JSONObject();
        Response response = fetchDataFromTMDB("movie/" + category + "?language=en-US&page=1");
        if(!response.isSuccessful())
            throw new Exception("Error getting " + category + " movies, please try again later.");
        JSONObject getMoviesByCategory = new JSONObject(response.body().string());
        responseBody.put("success", true);
        responseBody.put("content", getMoviesByCategory.get("results"));
        return responseBody.toString();
    }


    public Response fetchDataFromTMDB(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/" + url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + TMDB_ACCESS_TOKEN)
                .build();

        return client.newCall(request).execute();
    }
}

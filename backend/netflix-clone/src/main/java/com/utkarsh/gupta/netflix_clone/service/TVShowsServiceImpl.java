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
public class TVShowsServiceImpl implements TVShowsService {
    @Value("${tmdb.access.token}")
    private String TMDB_ACCESS_TOKEN;

    @Override
    public String fetchPopularTVShows() throws Exception{
        JSONObject responseBody = new JSONObject();
        Response response = fetchDataFromTMDB("tv/popular?language=en-US");
        if(!response.isSuccessful()){
            throw new Exception("Error getting trending tv shows, please try again later.");
        }
        JSONObject jsonTVShowPage = new JSONObject(response.body().string());
        JSONArray jsonArray = (JSONArray) jsonTVShowPage.get("results");
        JSONObject trendingTvShow = (JSONObject) jsonArray.get(new Random().nextInt(jsonArray.length()));
        responseBody.put("success", true);
        responseBody.put("content", trendingTvShow);
        return responseBody.toString();
    }

    @Override
    public String getTVShowTrailers(String id) throws Exception {
        JSONObject responseBody = new JSONObject();
        Response response = fetchDataFromTMDB("tv/" + id + "/videos?language=en-US");
        if(!response.isSuccessful()){
            throw new Exception("Error getting tv show trailers, please try again later.");
        }

        JSONObject jsonResponse = new JSONObject(response.body().string());
        JSONArray tvVideos = new JSONArray(jsonResponse.get("results").toString());
        JSONArray trailers = new JSONArray();
        for(int i=0;i < tvVideos.length();i++){
            JSONObject movieVideo = (JSONObject) tvVideos.get(i);
            if(movieVideo.get("type").equals("Teaser") || movieVideo.get("type").equals("Trailer")) {
                trailers.put(movieVideo);
            }
        }
        responseBody.put("trailers", trailers);
        responseBody.put("success", true);
        return responseBody.toString();
    }

    @Override
    public String getTVShowDetails(String id) throws Exception {
        JSONObject responseBody = new JSONObject();
        Response response = fetchDataFromTMDB("tv/" + id + "?language=en-US");
        if(!response.isSuccessful())
            throw new Exception("Error getting tv show details, please try again later.");
        JSONObject tvShowDetails = new JSONObject(response.body().string());
        responseBody.put("success", true);
        responseBody.put("content", tvShowDetails);
        return responseBody.toString();
    }

    @Override
    public String getSimilarTVShows(String movieId) throws Exception {
        JSONObject responseBody = new JSONObject();
        Response response = fetchDataFromTMDB("tv/" + movieId + "/similar?language=en-US");
        if(!response.isSuccessful())
            throw new Exception("Error getting similar tv shows, please try again later.");
        JSONObject similarShows = new JSONObject(response.body().string());
        responseBody.put("success", true);
        responseBody.put("content", similarShows.get("results"));
        return responseBody.toString();
    }

    @Override
    public String getTVShowsByCategory(String category) throws Exception {
        JSONObject responseBody = new JSONObject();
        Response response = fetchDataFromTMDB("tv/" + category + "?language=en-US&page=1");
        if(!response.isSuccessful())
            throw new Exception("Error getting " + category + " tv shows, please try again later.");
        JSONObject shows = new JSONObject(response.body().string());
        responseBody.put("success", true);
        responseBody.put("content", shows.get("results"));
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

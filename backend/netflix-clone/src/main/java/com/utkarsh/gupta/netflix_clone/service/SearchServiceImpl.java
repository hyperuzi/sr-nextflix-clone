package com.utkarsh.gupta.netflix_clone.service;

import com.utkarsh.gupta.netflix_clone.model.User;
import com.utkarsh.gupta.netflix_clone.repository.UserRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SearchServiceImpl implements SearchService{

    @Value("${tmdb.access.token}")
    private String TMDB_ACCESS_TOKEN;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String fetchPersons(String person, String userId) throws Exception {
        JSONObject responseBody = new JSONObject();
        Response response = fetchDataFromTMDB("search/person?query=" + person + "&include_adult=true&language=en-US");
        if(!response.isSuccessful()){
            throw new Exception("Error searching person, please try again later.");
        }
        JSONObject search = new JSONObject(response.body().string());
        JSONArray searchResults = (JSONArray) search.get("results");
        responseBody.put("success", true);
        responseBody.put("content", searchResults);

        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            User usr = user.get();
            List<Document> searchHistory = usr.getSearchHistory();
            JSONObject searchValue = new JSONObject();
            searchValue.put("id", ((JSONObject)searchResults.get(0)).get("id"));
            searchValue.put("image", ((JSONObject)searchResults.get(0)).get("profile_path"));
            searchValue.put("title", ((JSONObject)searchResults.get(0)).get("name"));
            searchValue.put("searchType", "person");
            searchValue.put("createdAt", new Date());
            searchHistory.add(Document.parse(searchValue.toString()));
            usr.setSearchHistory(searchHistory);
            userRepository.save(usr);
            System.out.println(usr);
        }

        return responseBody.toString();
    }

    @Override
    public String fetchMovies(String movie, String userId) throws Exception {
        JSONObject responseBody = new JSONObject();
        Response response = fetchDataFromTMDB("search/movie?query=" + movie + "&include_adult=true&language=en-US");
        if(!response.isSuccessful()){
            throw new Exception("Error searching movie, please try again later.");
        }
        JSONObject search = new JSONObject(response.body().string());
        JSONArray searchResults = (JSONArray) search.get("results");

        responseBody.put("success", true);
        responseBody.put("content", searchResults);

        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            User usr = user.get();
            List<Document> searchHistory = usr.getSearchHistory();
            JSONObject searchValue = new JSONObject();
            searchValue.put("id", ((JSONObject)searchResults.get(0)).get("id"));
            searchValue.put("image", ((JSONObject)searchResults.get(0)).get("poster_path"));
            searchValue.put("title", ((JSONObject)searchResults.get(0)).get("title"));
            searchValue.put("searchType", "movie");
            searchValue.put("createdAt", new Date());
            searchHistory.add(Document.parse(searchValue.toString()));
            usr.setSearchHistory(searchHistory);
            userRepository.save(usr);
            System.out.println(usr);
        }

        return responseBody.toString();
    }

    @Override
    public String fetchTVShows(String tvShow, String userId) throws Exception {
        JSONObject responseBody = new JSONObject();
        Response response = fetchDataFromTMDB("search/tv?query=" + tvShow + "&include_adult=true&language=en-US");
        if(!response.isSuccessful())
            throw new Exception("Error getting movie details, please try again later.");
        JSONObject search = new JSONObject(response.body().string());
        JSONArray searchResults = (JSONArray) search.get("results");
        responseBody.put("success", true);
        responseBody.put("content", searchResults);

        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            User usr = user.get();
            List<Document> searchHistory = usr.getSearchHistory();
            JSONObject searchValue = new JSONObject();
            searchValue.put("id", ((JSONObject)searchResults.get(0)).get("id"));
            searchValue.put("image", ((JSONObject)searchResults.get(0)).get("poster_path"));
            searchValue.put("title", ((JSONObject)searchResults.get(0)).get("name"));
            searchValue.put("searchType", "tv");
            searchValue.put("createdAt", new Date());
            searchHistory.add(Document.parse(searchValue.toString()));
            usr.setSearchHistory(searchHistory);
            userRepository.save(usr);
            System.out.println(usr);
        }

        return responseBody.toString();
    }

    @Override
    public String fetchHistory(String userId) {

        JSONObject responseBody = new JSONObject();
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            responseBody.put("content", user.get().getSearchHistory());
            responseBody.put("message", "success");
        } else {
            responseBody.put("content", new ArrayList<>());
            responseBody.put("message", "failed! User not present.");
        }
        return responseBody.toString();
    }

    @Override
    public String deleteSearchHistory(String searchId, String userId) throws Exception {
        JSONObject responseBody = new JSONObject();
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            User usr = user.get();
            List<Document> newSearchHistory = usr.getSearchHistory().stream().filter(item ->
                  !item.get("id").toString().equals(searchId)
            ).toList();
            usr.setSearchHistory(newSearchHistory);




            userRepository.save(usr);
            responseBody.put("message", "Deleted History Successfully.");
        } else {
            responseBody.put("message", "Failed! Could not delete data.");
        }



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

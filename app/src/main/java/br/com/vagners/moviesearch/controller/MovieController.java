package br.com.vagners.moviesearch.controller;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Streams;

import br.com.vagners.moviesearch.api.OmdbAPI;
import br.com.vagners.moviesearch.model.MovieModel;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vagnerss on 17/07/16.
 */
public class MovieController {

    public static Retrofit retrofit;
    public static OmdbAPI api;
    private static MovieController instance = null;

    public static MovieController getInstance() {
        if (instance == null) {
            Class _class = MovieController.class;
            synchronized (_class) {
                instance = new MovieController();
            }
        }
        return instance;
    }

    public MovieController() {

        String base_url = "http://www.omdbapi.com/";

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(OmdbAPI.class);
    }

    public static void getMovie(String title, Callback<MovieModel> callback) {
        api.getJson(title).enqueue(callback);
    }

}

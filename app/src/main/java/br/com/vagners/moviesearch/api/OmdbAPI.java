package br.com.vagners.moviesearch.api;

import br.com.vagners.moviesearch.model.MovieModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by vagnerss on 17/07/16.
 */
public interface OmdbAPI {
    @GET("/")
    Call<MovieModel> getJson(@Query("t") String title);
}

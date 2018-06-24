package com.example.akshay.moviestageapp.Rest;

import com.example.akshay.moviestageapp.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by akshay on 17/6/18.
 */

public interface ApiInterface{

    @GET("top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);


}

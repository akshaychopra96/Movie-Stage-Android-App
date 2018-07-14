package com.example.akshay.moviestageapp.rest;

import com.example.akshay.moviestageapp.model.MovieResponse;
import com.example.akshay.moviestageapp.model.ReviewResponse;
import com.example.akshay.moviestageapp.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by akshay on 17/6/18.
 */

public interface ApiInterface{
// http:api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]

    @GET("top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);
// http:api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]

    // https://api.themoviedb.org/3/movie/351286/videos?api_key=90d8eb35baac73b15f9eb22037556bf5

    @GET("{user_id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path(value = "user_id", encoded = true) String userId, @Query("api_key") String apiKey);

    @GET("{user_id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path(value = "user_id", encoded = true) String userId, @Query("api_key") String apiKey);



}



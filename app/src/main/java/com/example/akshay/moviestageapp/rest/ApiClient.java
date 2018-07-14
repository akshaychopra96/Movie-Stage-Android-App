package com.example.akshay.moviestageapp.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by akshay on 17/6/18.
 */

public class ApiClient {

    final static String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static Retrofit retrofit = null;

    public  static Retrofit getClient(){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIE_DB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;

    }


}

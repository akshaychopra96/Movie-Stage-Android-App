package com.example.akshay.moviestageapp.Utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

public class NetworkUtils {

     final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p";
    public final static String IMAGE_SIZE_PATH = "w342";
    public final static String BACKDROP_IMAGE_SIZE_PATH = "w500";

    // Movie API URL example
    // http:api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]

    // Image URL example
    // http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg

    public static URL getImageOfMovieDbUrl(String imageURL,String imagePath){

        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(imagePath)
                .appendEncodedPath(imageURL)
                .build();

        URL imageOfMovieDbUrl=null;

        try {
            imageOfMovieDbUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return imageOfMovieDbUrl;
    }
}

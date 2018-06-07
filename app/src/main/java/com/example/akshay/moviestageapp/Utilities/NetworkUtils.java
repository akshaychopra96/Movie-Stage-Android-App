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

    final static String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie";
    final static String MOVIE_POPULAR_PATH = "popular";
    final static String MOVIE_TOP_RATED_PATH = "top_rated";
    final static String PARAM_API_KEY_QUERY = "api_key";

    final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p";
    final static String IMAGE_SIZE_PATH = "w185";


    final static String API_KEY = "90d8eb35baac73b15f9eb22037556bf5";

//     http:api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]

    public static URL getPopularMovieDbUrl(){

        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(MOVIE_POPULAR_PATH)
                .appendQueryParameter(PARAM_API_KEY_QUERY,API_KEY)
                .build();

        URL movieDbUrl=null;

        try {
            movieDbUrl = new URL(builtUri.toString());
//            Log.d("tag",""+movieDbUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return movieDbUrl;
    }

    //http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg

    public static URL getImageOfMovieDbUrl(String imageURL){

        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_SIZE_PATH)
                .appendEncodedPath(imageURL)
                .build();

        URL imageOfMovieDbUrl=null;

        try {
            imageOfMovieDbUrl = new URL(builtUri.toString());
//            String encodedUrl = URLEncoder.encode(imageOfMovieDbUrl.toString(), "UTF-8");

//                        Log.d("tag",""+encodedUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//         catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        return imageOfMovieDbUrl;
    }

    public static URL getTopRatedMovieDbUrl(){

        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(MOVIE_TOP_RATED_PATH)
                .appendQueryParameter(PARAM_API_KEY_QUERY,API_KEY)
                .build();

        URL movieDbUrl=null;

        try {
            movieDbUrl = new URL(builtUri.toString());
//            Log.d("tag",""+movieDbUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return movieDbUrl;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = null;
        String current="";
        try {
            //Create a connection with it to fetch data from that url
            urlConnection = (HttpURLConnection) url.openConnection();

            //Using InputStream to get data from the connected URL
            InputStream inputStream = urlConnection.getInputStream();

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            //Fetching data from the URL and storing it in a String called current
            int data = inputStreamReader.read();
            while (data != -1) {
                current += (char) data;
                data = inputStreamReader.read();
            }
        }

        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            urlConnection.disconnect();
        }
        return current;
    }

}

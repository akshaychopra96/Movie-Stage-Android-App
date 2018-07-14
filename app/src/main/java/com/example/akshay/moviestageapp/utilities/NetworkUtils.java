package com.example.akshay.moviestageapp.utilities;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

     final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p";
    public final static String IMAGE_SIZE_PATH = "w342";
    public final static String BACKDROP_IMAGE_SIZE_PATH = "w500";
    public final static String TRAILER_BASE_URL = "http://www.youtube.com/watch?v=";
    public final static String TRAILER_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi";


    // Movie API URL example
    // http:api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]

    // Image URL example
    // http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg

    // TrailerResponse URLexample
    // https://api.themoviedb.org/3/movie/351286/videos?api_key=[YOUR_API_KEY]

    // TrailerImage Thumbnail URL example
    // https://img.youtube.com/vi/vn9mMeWcgoM/0.jpg

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

//

    public static URL getTrailerOfMovieDbUrl(String videoPath){

        Uri builtUri = Uri.parse(TRAILER_BASE_URL).buildUpon()
                .appendPath(videoPath)
                .build();

        URL trailerOfMovieDbUrl=null;

        try {
            trailerOfMovieDbUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return trailerOfMovieDbUrl;
    }

    public static URL getThumbnailImageOfTrailerURL(String videoPath,int id){

        Uri builtUri = Uri.parse(TRAILER_THUMBNAIL_BASE_URL).buildUpon()
                .appendPath(videoPath)
                .appendPath(String.valueOf(id)+".jpg")
                .build();

        URL trailerOfMovieDbUrl=null;

        try {
            trailerOfMovieDbUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return trailerOfMovieDbUrl;
    }
}

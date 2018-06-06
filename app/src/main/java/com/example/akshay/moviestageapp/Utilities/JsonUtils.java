package com.example.akshay.moviestageapp.Utilities;

import android.util.Log;

import com.example.akshay.moviestageapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {


    public static Movie parseMoiveJson(String json) {

        ArrayList<String> originalTitle;
        ArrayList<String> userRating;
        ArrayList<String> releaseDate;
        ArrayList<String> posterURL;
        ArrayList<String> plotSynopsis;

        final String KEY_ORIGINAL_TITLE="original_title";
        final String KEY_PLOT_SYNOPSIS="overview";
        final String KEY_USER_RATING="vote_average";
        final String KEY_IMAGE_URL="poster_path";
        final String KEY_RELEASE_DATE="release_date";

        Movie movie = null;

        try {

            //This forms the Root JSONObject of the JSON File
            JSONObject jsonObject = new JSONObject(json);

            JSONArray resultsArray = jsonObject.getJSONArray("results");

            //TODO also need to check the internet connection

            originalTitle = new ArrayList<>();
            userRating = new ArrayList<>();
            releaseDate = new ArrayList<>();
            posterURL = new ArrayList<>();
            plotSynopsis = new ArrayList<>();

            JSONObject[] jsonObjectArray = new JSONObject[resultsArray.length()];

            for (int i = 0; i <resultsArray.length() ; i++) {

                jsonObjectArray[i] = resultsArray.getJSONObject(i);

                originalTitle.add(jsonObjectArray[i].getString(KEY_ORIGINAL_TITLE));
                plotSynopsis.add(jsonObjectArray[i].getString(KEY_PLOT_SYNOPSIS));
                userRating.add(jsonObjectArray[i].getString(KEY_USER_RATING));
                posterURL.add(jsonObjectArray[i].getString(KEY_IMAGE_URL));
                releaseDate.add(jsonObjectArray[i].getString(KEY_RELEASE_DATE));

//                Log.d("tag",originalTitle.get(i));

            }


            //Getting name JSONObject from the Root JSONObject

            movie= new Movie(originalTitle,posterURL,plotSynopsis,userRating,releaseDate);

            return movie;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
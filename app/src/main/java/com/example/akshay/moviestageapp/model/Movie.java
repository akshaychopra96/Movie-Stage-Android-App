package com.example.akshay.moviestageapp.model;

import android.util.Log;

import com.example.akshay.moviestageapp.Utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Movie {



    private ArrayList<String> originalTitle;
    private ArrayList<String> posterImage;
    private ArrayList<String> plotSynopsis;
    private ArrayList<String> userRating;
    private ArrayList<String> releaseDate;
    private ArrayList<String> backdropImage;

    /**
     * No args constructor for use in serialization
     */
    public Movie() {
    }

    public Movie(ArrayList<String> originalTitle, ArrayList<String> posterImage, ArrayList<String> plotSynopsis,ArrayList<String> userRating, ArrayList<String> releaseDate, ArrayList<String> backdropImage) {
        this.originalTitle= originalTitle;
        this.posterImage = posterImage;
        this.plotSynopsis= plotSynopsis;
        this.userRating= userRating;
        this.releaseDate = releaseDate;
        this.backdropImage = backdropImage;
    }

    public ArrayList<String> getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(ArrayList<String> originalTitle) {
        this.originalTitle= originalTitle;
    }

    public ArrayList<String> getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(ArrayList<String> plotSynopsis) {
        this.plotSynopsis= plotSynopsis;
    }

    public ArrayList<String> getUserRating() {
        return userRating;
    }

    public void setUserRating(ArrayList<String> userRating) {
        this.userRating= userRating;
    }

    public ArrayList<String> getImage() {
        return posterImage;
    }

    public void setImage(ArrayList<String> image) {
        this.posterImage = image;
    }

    public ArrayList<String> getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(ArrayList<String> releaseDate) {
        this.releaseDate= releaseDate;
    }

    public ArrayList<String> getBackdropImage() {return  backdropImage;}

}

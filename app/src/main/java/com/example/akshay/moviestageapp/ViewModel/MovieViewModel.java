package com.example.akshay.moviestageapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.akshay.moviestageapp.Database.MovieRoomDatabase;
import com.example.akshay.moviestageapp.model.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> moviesList;

    public LiveData<List<Movie>> getMoviesList() {
        return moviesList;
    }

    public MovieViewModel(@NonNull Application application) {
        super(application);
        MovieRoomDatabase database = MovieRoomDatabase.getDatabase(this.getApplication());
        moviesList =  database.movieDao().getAllMovies();
    }
}

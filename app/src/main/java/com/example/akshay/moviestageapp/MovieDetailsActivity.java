package com.example.akshay.moviestageapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshay.moviestageapp.InternetConnection.NetworkChangeReceiver;
import com.example.akshay.moviestageapp.Utilities.NetworkUtils;
import com.example.akshay.moviestageapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_PARCEL = "extra_parcel";
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    android.support.v7.widget.Toolbar toolbar;

    TextView userRatingTV,plotSynopsisTV,releaseDateTV,titleTV;
    ImageView backdropIV,posterIV;

    int position;

    CoordinatorLayout coordinatorLayout;

    Movie movieObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        MainActivity.secondActivityVisited = true;
        NetworkChangeReceiver.otherActivityVisited  =true;

        position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);

        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        movieObject = intent.getParcelableExtra(EXTRA_PARCEL);

        if (movieObject == null) {
            // movieObject not found in intent
            closeOnError();
            return;
        }

        init();

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setData();


    }

    private void setData() {

        Picasso.get()
                .load(String.valueOf(NetworkUtils.getImageOfMovieDbUrl(movieObject.getBackdropPath(),NetworkUtils.BACKDROP_IMAGE_SIZE_PATH)))
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.image_not_found)
                .into(backdropIV);

        Picasso.get()
                .load(String.valueOf(NetworkUtils.getImageOfMovieDbUrl(movieObject.getPosterPath(),NetworkUtils.IMAGE_SIZE_PATH)))
                .into(posterIV);

        titleTV.setText(movieObject.getOriginalTitle());
        plotSynopsisTV.setText(movieObject.getOverview());
        userRatingTV.setText(movieObject.getVoteAverage()+" / 10");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;

        try {
            date = dateFormat.parse(movieObject.getReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat finalDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = finalDateFormat.format(date);

         releaseDateTV.setText(formattedDate);

    }

    private void init() {

        titleTV = findViewById(R.id.title_tv);
        plotSynopsisTV = findViewById(R.id.plotSynopsis_tv);
        userRatingTV = findViewById(R.id.userRating_tv);
        releaseDateTV = findViewById(R.id.releaseDate_tv);

        backdropIV = findViewById(R.id.image_iv);
        posterIV = findViewById(R.id.poster_iv);

        coordinatorLayout =  findViewById(R.id.homeactivitycoordinator);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

        private void closeOnError() {
            finish();
            Toast.makeText(this, "Error sir", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("back pressed in detail", "here");
        finish();
    }
}

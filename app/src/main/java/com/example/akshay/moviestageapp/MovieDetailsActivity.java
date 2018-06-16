package com.example.akshay.moviestageapp;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.akshay.moviestageapp.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import static com.example.akshay.moviestageapp.MainActivity.movieObject;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    android.support.v7.widget.Toolbar toolbar;

    TextView userRatingTV,plotSynopsisTV,releaseDateTV,titleTV;
    ImageView backdropIV,posterIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);

        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(movieObject.getOriginalTitle().get(position));

        init();

        MainActivity.secondActivityVisited = true;

        String date = movieObject.getReleaseDate().get(position);
        String[] split = date.split("-");
        String dateFinal = split[2]+"-"+split[1]+"-"+split[0];

        titleTV.setText(movieObject.getOriginalTitle().get(position));
        plotSynopsisTV.setText(movieObject.getPlotSynopsis().get(position));
        userRatingTV.setText(movieObject.getUserRating().get(position)+" / 10");
        releaseDateTV.setText(dateFinal);


        Picasso.get()
                .load(String.valueOf(NetworkUtils.getBackdropImageOfMovieDbUrl(movieObject.getBackdropImage().get(position))))
                .placeholder(R.drawable.progress_animation)
                .into(backdropIV);

        Picasso.get()
                .load(String.valueOf(NetworkUtils.getImageOfMovieDbUrl(movieObject.getImage().get(position))))
                .into(posterIV);


    }

    private void init() {

        titleTV = findViewById(R.id.title_tv);
        plotSynopsisTV = findViewById(R.id.plotSynopsis_tv);
        userRatingTV = findViewById(R.id.userRating_tv);
        releaseDateTV = findViewById(R.id.releaseDate_tv);

        backdropIV = findViewById(R.id.image_iv);
        posterIV = findViewById(R.id.poster_iv);
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

}

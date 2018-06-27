package com.example.akshay.moviestageapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.akshay.moviestageapp.Database.MovieRoomDatabase;
import com.example.akshay.moviestageapp.InternetConnection.NetworkChangeReceiver;
import com.example.akshay.moviestageapp.RecyclerView.MovieRecyclerItemClickListener;
import com.example.akshay.moviestageapp.RecyclerView.MovieRecyclerViewAdapter;
import com.example.akshay.moviestageapp.RecyclerView.ReviewAdapter;
import com.example.akshay.moviestageapp.RecyclerView.TrailerRecyclerItemClickListener;
import com.example.akshay.moviestageapp.RecyclerView.TrailerRecyclerViewAdapter;
import com.example.akshay.moviestageapp.Rest.ApiClient;
import com.example.akshay.moviestageapp.Rest.ApiInterface;
import com.example.akshay.moviestageapp.Utilities.NetworkUtils;
import com.example.akshay.moviestageapp.model.Movie;
import com.example.akshay.moviestageapp.model.Review;
import com.example.akshay.moviestageapp.model.ReviewResponse;
import com.example.akshay.moviestageapp.model.Trailer;
import com.example.akshay.moviestageapp.model.TrailerResponse;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.akshay.moviestageapp.MainActivity.API_KEY;


public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_PARCEL = "extra_parcel";
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private final Executor executor = Executors.newFixedThreadPool(2);

    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.userRating_tv) TextView userRatingTV;
    @BindView(R.id.plotSynopsis_tv) TextView plotSynopsisTV;
    @BindView(R.id.releaseDate_tv) TextView releaseDateTV;
    @BindView(R.id.title_tv) TextView titleTV;

    @BindView(R.id.backdropImage_iv) ImageView backdropIV;
    @BindView(R.id.poster_iv)  ImageView  posterIV;

    @BindView(R.id.trailerRecyclerView)
    RecyclerView trailerRecyclerView;

    @BindView(R.id.reviewRecyclerView) RecyclerView reviewRecyclerView;

    @BindView(R.id.favourite_button)
    Button favouriteButton;

    private MovieRoomDatabase mDb;

    int position;

    CoordinatorLayout coordinatorLayout;

    Movie movieObject;

    ApiInterface apiService;
    List<Trailer> trailers;
    List<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        Stetho.initializeWithDefaults(this);

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

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiService = ApiClient.getClient().create(ApiInterface.class);
//        Log.d("tag","user id: "+movieObject.getId());
        Call<TrailerResponse> call1 = apiService.getMovieTrailers(""+movieObject.getId(),API_KEY);
        getMovieTrailers(call1);

        Call<ReviewResponse> call2 = apiService.getMovieReviews(""+movieObject.getId(),API_KEY);
        getMovieReviews(call2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        trailerRecyclerView.setLayoutManager(layoutManager);
        trailerRecyclerView.setHasFixedSize(true);

        trailerRecyclerView.addOnItemTouchListener(
                new TrailerRecyclerItemClickListener(this, trailerRecyclerView, new TrailerRecyclerItemClickListener.OnItemClickListener() {


                    @Override
                    public void onItemClick(View view, int position) {

                        String videoId = trailers.get(position).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+videoId));
                        intent.putExtra("VIDEO_ID", videoId);
                        try {
                            startActivity(intent);
                        }
                        catch (ActivityNotFoundException e){
                            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                            startActivity(webIntent);
                        }
                    }


                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewRecyclerView.setHasFixedSize(true);

        mDb = MovieRoomDatabase.getDatabase(getApplicationContext());

        setData();


    }


    private void getMovieTrailers(Call<TrailerResponse> call) {

        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {

                if (trailers != null)
                    trailers.clear();

               trailers = response.body().getResults();

                trailerRecyclerView.setAdapter(new TrailerRecyclerViewAdapter(getApplicationContext(),trailers));
           }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.e("tag", t.toString());
                Snackbar.make(coordinatorLayout, "Failed Loading List", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void getMovieReviews(Call<ReviewResponse> call) {

        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {

                if (reviews != null)
                    reviews.clear();

                reviews = response.body().getResults();

                reviewRecyclerView.setAdapter(new ReviewAdapter(getApplicationContext(),reviews));
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.e("tag", t.toString());

                Snackbar.make(coordinatorLayout, "Failed Loading List", Snackbar.LENGTH_LONG).show();
            }
        });
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

    public void favouriteButton(View v){

        if (!movieObject.getIsFavourite()) {
            favouriteButton.setText("UNSAVE");
            addMovieToFavourites();
        } else {
            favouriteButton.setText("SAVE");
            removeMovieFromFavourites();
        }

    }

    private void addMovieToFavourites() {
        movieObject.setIsFavourite(true);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insertMovie(movieObject);
            }
        });
    }

    private void removeMovieFromFavourites() {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().deleteMovieWithId(movieObject.getId());
            }
        });

        movieObject.setIsFavourite(false);
        Toast.makeText(this, movieObject.getOriginalTitle() + " is removed from favourites", Toast.LENGTH_LONG).show();
    }



}

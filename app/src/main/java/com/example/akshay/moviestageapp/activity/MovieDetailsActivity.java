package com.example.akshay.moviestageapp.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshay.moviestageapp.database.MovieRoomDatabase;
import com.example.akshay.moviestageapp.internetConnection.NetworkChangeReceiver;
import com.example.akshay.moviestageapp.MyBounceInterpolator;
import com.example.akshay.moviestageapp.R;
import com.example.akshay.moviestageapp.recyclerView.ReviewAdapter;
import com.example.akshay.moviestageapp.recyclerView.TrailerRecyclerItemClickListener;
import com.example.akshay.moviestageapp.recyclerView.TrailerRecyclerViewAdapter;
import com.example.akshay.moviestageapp.rest.ApiClient;
import com.example.akshay.moviestageapp.rest.ApiInterface;
import com.example.akshay.moviestageapp.utilities.NetworkUtils;
import com.example.akshay.moviestageapp.model.Movie;
import com.example.akshay.moviestageapp.model.Review;
import com.example.akshay.moviestageapp.model.ReviewResponse;
import com.example.akshay.moviestageapp.model.Trailer;
import com.example.akshay.moviestageapp.model.TrailerResponse;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    ImageButton favouriteButton;

    private MovieRoomDatabase mDb;

    int position;
    boolean isColorChange;
    boolean myBoolean;

    @BindView(R.id.homeactivitycoordinator)
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
        FavouriteActivity.movieDetailsActivityVisited = true;

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

        Call<TrailerResponse> call1 = apiService.getMovieTrailers(""+movieObject.getId(), MainActivity.API_KEY);
        getMovieTrailers(call1);

        Call<ReviewResponse> call2 = apiService.getMovieReviews(""+movieObject.getId(), MainActivity.API_KEY);
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
                        if (intent.resolveActivity(getPackageManager()) != null)
                            startActivity(intent);
                        else{
                            Intent webIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                            if (webIntent.resolveActivity(getPackageManager()) != null)
                            startActivity(webIntent);
                            else{
                                Toast.makeText(MovieDetailsActivity.this,"Sorry, No App available to launch",Toast.LENGTH_LONG).show();
                            }
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
                Snackbar.make(coordinatorLayout.getRootView(), "Failed Loading List", Snackbar.LENGTH_LONG).show();
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

                if(reviews.size() == 0){
                    TextView reviewHeading = findViewById(R.id.reviewHeading);
                    reviewHeading.setText("No Reviews Available");
                }


                reviewRecyclerView.setAdapter(new ReviewAdapter(getApplicationContext(),reviews));
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.e("tag", t.toString());

                Snackbar.make(coordinatorLayout.getRootView(), "Failed Loading List", Snackbar.LENGTH_LONG).show();
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

        if(movieObject.getIsFavourite() == false){
            movieObject.setIsFavourite(true);

            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
            favouriteButton.startAnimation(myAnim);

            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.4, 30);
            myAnim.setInterpolator(interpolator);
            favouriteButton.startAnimation(myAnim);

            favouriteButton.setColorFilter(Color.parseColor("#DD2C00"));
            addMovieToFavourites();
        }

        else {
            movieObject.setIsFavourite(false);
            favouriteButton.setColorFilter(Color.parseColor("#FFEE58"));
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


    @Override
    protected void onResume() {
        super.onResume();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int id = mDb.movieDao().getMovieId(movieObject.getId());

                if (id == movieObject.getId())
                    favouriteButton.setColorFilter(Color.parseColor("#DD2C00"));
            }
        });

    }




    }

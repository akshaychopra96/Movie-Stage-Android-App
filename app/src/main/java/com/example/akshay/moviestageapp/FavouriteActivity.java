package com.example.akshay.moviestageapp;

import android.annotation.TargetApi;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.RoomDatabase;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.akshay.moviestageapp.Database.MovieRoomDatabase;
import com.example.akshay.moviestageapp.InternetConnection.NetworkChangeReceiver;
import com.example.akshay.moviestageapp.RecyclerView.MovieRecyclerItemClickListener;
import com.example.akshay.moviestageapp.RecyclerView.MovieRecyclerViewAdapter;
import com.example.akshay.moviestageapp.Utilities.NetworkUtils;
import com.example.akshay.moviestageapp.ViewModel.MovieViewModel;
import com.example.akshay.moviestageapp.model.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteActivity extends AppCompatActivity {

    @BindView(R.id.favourite_recyclerView)
    RecyclerView favouriteRecyclerView;

    @BindView(R.id.favouriteLinearLayout)
    LinearLayout linearLayout;

    MovieRoomDatabase mDb;
    MovieRecyclerViewAdapter movieAdapter;

    LiveData<List<Movie>> movies;
    int position;
    Movie movieObject;

    public static final String EXTRA_PARCEL = "extra_parcel";
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    RoomDatabase roomDatabase;
    private final Executor executor = Executors.newFixedThreadPool(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        ButterKnife.bind(this);

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

//        movies = intent.getParcelableExtra(EXTRA_PARCEL);

        if (movies == null) {
            // movieObject not found in intent
            closeOnError();
            return;
        }


        favouriteRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        favouriteRecyclerView.setHasFixedSize(true);
        mDb = MovieRoomDatabase.getDatabase(getApplicationContext());

        setupViewModel();

        MovieViewModel movieViewModel = new MovieViewModel(getApplication());
        movies = movieViewModel.getMoviesList();
        Log.d("tag","movies is null or not: "+movies.getValue());


        favouriteRecyclerView.addOnItemTouchListener(
                new MovieRecyclerItemClickListener(this, favouriteRecyclerView, new MovieRecyclerItemClickListener.OnItemClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override

                    public void onItemClick(View view, final int position) {

                        Picasso.get().load(String.valueOf(NetworkUtils.getImageOfMovieDbUrl(movies.getValue().get(position).getPosterPath(),NetworkUtils.IMAGE_SIZE_PATH)))
                                .into(new Target() {

                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        linearLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
                                    }

                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {}

                                    @Override
                                    public void onPrepareLoad(final Drawable placeHolderDrawable) {}
                                });

                        final Rect viewRect = new Rect();
                        view.getGlobalVisibleRect(viewRect);

                        // create Explode transition with epicenter
                        Transition explode = new Explode();
                        explode.setEpicenterCallback(new Transition.EpicenterCallback() {
                            @Override
                            public Rect onGetEpicenter(Transition transition) {
                                return viewRect;
                            }
                        });

                        explode.addListener(new Transition.TransitionListener() {
                            @Override
                            public void onTransitionStart(Transition transition) {}

                            @Override
                            public void onTransitionEnd(Transition transition) {

                                new Handler().postDelayed(new Runnable() {
                                    public void run() {

                                        /*
                                        * EXTRA_POSITION is for passing the position of the item
                                        * EXTRA_CATEGORY is for passing the category of the movies chosen
                                         */

                                        Intent intent = new Intent(FavouriteActivity.this, MovieDetailsActivity.class);
                                        intent.putExtra(MovieDetailsActivity.EXTRA_POSITION, position);
                                        intent.putExtra(MovieDetailsActivity.EXTRA_PARCEL, movies.getValue().get(position));
                                        startActivity(intent);

                     /* For Main Activity exit --> fade out and
                      * For Second Activity entry --> fade in animation transitions.
                      */
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    }
                                }, 200);

                            }

                            @Override
                            public void onTransitionCancel(Transition transition) {}

                            @Override
                            public void onTransitionPause(Transition transition) {}

                            @Override
                            public void onTransitionResume(Transition transition) {}
                        });

                        explode.setDuration(1200);
                        TransitionManager.beginDelayedTransition(favouriteRecyclerView, explode);

                        // remove all views from Recycler View
                        favouriteRecyclerView.setAdapter(null);
                    }

                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

                    @Override
                    public void onLongItemClick(View view, final int position) {

                    }
                })
        );

    }

    private void setupViewModel() {
        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMoviesList().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                movieAdapter = new MovieRecyclerViewAdapter( movies, getApplicationContext());
                favouriteRecyclerView.setAdapter(movieAdapter);
            }
        });
    }


    private void closeOnError() {
        finish();
        Toast.makeText(this, "!X!Error!X!", Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favourite_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.deleteAll){

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().deleteAll();
                }
            });


        }


        return super.onOptionsItemSelected(item);
    }
}

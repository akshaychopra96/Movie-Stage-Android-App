package com.example.akshay.moviestageapp.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.akshay.moviestageapp.BuildConfig;
import com.example.akshay.moviestageapp.internetConnection.NetworkChangeReceiver;
import com.example.akshay.moviestageapp.R;
import com.example.akshay.moviestageapp.recyclerView.MovieRecyclerViewAdapter;
import com.example.akshay.moviestageapp.rest.ApiClient;
import com.example.akshay.moviestageapp.rest.ApiInterface;
import com.example.akshay.moviestageapp.utilities.NetworkUtils;
import com.example.akshay.moviestageapp.model.Movie;
import com.example.akshay.moviestageapp.model.MovieResponse;
import com.example.akshay.moviestageapp.viewModel.MovieViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.ListItemClickListener  {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @BindView(R.id.mainActivityRootLayout) RelativeLayout relativeLayout;

    Spinner spinner;

    @BindView(R.id.progressBar) AVLoadingIndicatorView progressBar;

    static boolean secondActivityVisited = false;

    final static String API_KEY =  BuildConfig.API_KEY;
    ApiInterface apiService;
    List<Movie> movies;

    int MOVIE_CATEGORY = 0;

    Call<MovieResponse> call;

    private static final int POPULAR_MOVIES = 1;
    private static final int TOP_RATED_MOVIES = 2;
    private static final int FAVOURITES = 3;

    GridLayoutManager layoutManager;

    boolean isPopularMovies;
    private MovieViewModel movieViewModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        NetworkChangeReceiver.otherActivityVisited  =true;

        apiService = ApiClient.getClient().create(ApiInterface.class);

/*        SharedPreferences s = getSharedPreferences("myFile", Context.MODE_PRIVATE);
        isPopularMovies = s.getBoolean("isPopularMovies",true);

       if(isPopularMovies) {
            call = apiService.getPopularMovies(API_KEY);
            getPopularMovies(call);
        }
        else{
            call = apiService.getTopRatedMovies(API_KEY);
            getTopRatedMovies(call);
        }


       movieViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MovieViewModel.class);
        movieViewModel.getMoviesList().observe(this, new Observer<List<Movie>>()  {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies != null) {
                    Log.v("Debug App", "Fetched Data: " + movies.toString());
                    recyclerView.setAdapter(new MovieRecyclerViewAdapter(movies,MainActivity.this,MainActivity.this));
                }
            }
        });
*/
        call = apiService.getPopularMovies(API_KEY);
        getPopularMovies(call);

        final int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;

        layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    }


    public void getPopularMovies(Call<MovieResponse> call) {

        progressBarInit();

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                if (movies != null)
                    movies.clear();

                movies = response.body().getResults();
                recyclerView.setAdapter(new MovieRecyclerViewAdapter(movies,MainActivity.this,MainActivity.this));

                progressBarEnd();

                recyclerView.setVisibility(View.VISIBLE);

                MOVIE_CATEGORY = 0;
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("tag", t.toString());

                Snackbar.make(relativeLayout, "Failed Loading List", Snackbar.LENGTH_LONG).show();
            }
        });
    }

        public void getTopRatedMovies(Call<MovieResponse> call) {

            progressBarInit();

            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                    if(movies!=null)
                        movies.clear();

                    movies = response.body().getResults();
                    recyclerView.setAdapter(new MovieRecyclerViewAdapter( movies, MainActivity.this,MainActivity.this));

                    progressBarEnd();

                    recyclerView.setVisibility(View.VISIBLE);
                    MOVIE_CATEGORY = 1;
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.e("tag", t.toString());

                    Snackbar.make(relativeLayout, "Failed Loading List", Snackbar.LENGTH_LONG).show();
                }
            });
        }

    private void progressBarInit() {

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void progressBarEnd() {

        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem item = menu.findItem(R.id.toolbarspinner);
        spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.options_menu_spinner_layout);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences s = getSharedPreferences("myFile", Context.MODE_PRIVATE);

                if(position == 0){
                    // do nothing
                }else {

                    switch (position) {
                        case POPULAR_MOVIES:
                            Call<MovieResponse> call0 = apiService.getPopularMovies(API_KEY);
                            getPopularMovies(call0);
                            SharedPreferences.Editor e = s.edit();
                            e.putBoolean("isPopularMovies", true);
                            e.commit();
                            break;

                        case TOP_RATED_MOVIES:
                            Call<MovieResponse> call1 = apiService.getTopRatedMovies(API_KEY);
                            getTopRatedMovies(call1);
                            SharedPreferences.Editor ee = s.edit();
                            ee.putBoolean("isPopularMovies", false);
                            ee.commit();
                            break;

                        case FAVOURITES:
                            Intent intent = new Intent(getApplicationContext(), FavouriteActivity.class);
                            startActivity(intent);
                            break;

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onListItemClick(final int position, List<Movie> moviesList, View view) {

        Picasso.get().load(String.valueOf(NetworkUtils.getImageOfMovieDbUrl(movies.get(position).getPosterPath(),NetworkUtils.IMAGE_SIZE_PATH)))
                .into(new Target() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        relativeLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
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

                        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                        intent.putExtra(MovieDetailsActivity.EXTRA_POSITION, position);
                        intent.putExtra(MovieDetailsActivity.EXTRA_PARCEL, movies.get(position));
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
        TransitionManager.beginDelayedTransition(recyclerView, explode);

        // remove all views from Recycler View
        recyclerView.setAdapter(null);
    }



    @Override
    protected void onResume() {
        super.onResume();

        if(secondActivityVisited && movies!=null) {
            recyclerView.setAdapter(new MovieRecyclerViewAdapter(movies, MainActivity.this,MainActivity.this));
        }


    }

}





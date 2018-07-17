package com.example.akshay.moviestageapp.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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

import com.example.akshay.moviestageapp.database.MovieRoomDatabase;
import com.example.akshay.moviestageapp.internetConnection.NetworkChangeReceiver;
import com.example.akshay.moviestageapp.R;
import com.example.akshay.moviestageapp.recyclerView.MovieRecyclerViewAdapter;
import com.example.akshay.moviestageapp.utilities.NetworkUtils;
import com.example.akshay.moviestageapp.viewModel.MovieViewModel;
import com.example.akshay.moviestageapp.model.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.ListItemClickListener {

    @BindView(R.id.favourite_recyclerView)
    RecyclerView favouriteRecyclerView;

    LinearLayout linearLayout;

    MovieRoomDatabase mDb;
    MovieRecyclerViewAdapter movieAdapter;

    List<Movie> mm;

    static boolean movieDetailsActivityVisited = false;

    public static final String EXTRA_PARCEL = "extra_parcel";
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        ButterKnife.bind(this);

        linearLayout = findViewById(R.id.favouriteLinearLayout);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        MainActivity.secondActivityVisited = true;
        NetworkChangeReceiver.otherActivityVisited  =true;


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

        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onListItemClick(final int position, final List<Movie> moviesList, View view) {

        mm = moviesList;
        Log.d("tag","mm inside method "+mm);

                        Picasso.get().load(String.valueOf(NetworkUtils.getImageOfMovieDbUrl(moviesList.get(position).getPosterPath(),NetworkUtils.IMAGE_SIZE_PATH)))
                                .into(new Target() {

                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
                                        intent.putExtra(MovieDetailsActivity.EXTRA_PARCEL, moviesList.get(position));
                                        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();
        if(movieDetailsActivityVisited && mm!=null) {
            favouriteRecyclerView.setAdapter(new MovieRecyclerViewAdapter(mm, FavouriteActivity.this,FavouriteActivity.this));
            linearLayout.setBackground(Drawable.createFromPath("ffffff"));
        }
    }
}

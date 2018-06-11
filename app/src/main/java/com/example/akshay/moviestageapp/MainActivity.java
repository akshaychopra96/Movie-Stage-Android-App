package com.example.akshay.moviestageapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.*;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.akshay.moviestageapp.Utilities.JsonUtils;
import com.example.akshay.moviestageapp.Utilities.NetworkUtils;
import com.example.akshay.moviestageapp.model.Movie;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    static Movie movieObject;
    private RecyclerviewAdapter mAdapter;
    private RecyclerView recyclerView;
    RelativeLayout relativeLayout;

    NetworkInfo wifiCheck;
    NetworkInfo mobileDataCheck;

    MovieDbQueryTask task;

    Spinner spinner;

    CircleProgress progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar =  findViewById(R.id.progressBar);

        relativeLayout = findViewById(R.id.mainActivityRootLayout);

        URL movieDBJSONUrl = NetworkUtils.getPopularMovieDbUrl();

        if (movieDBJSONUrl == null) {
//            showError("MovieJSON is null");
            return;
        }

        task = new MovieDbQueryTask();
        task.execute(movieDBJSONUrl);


        recyclerView = findViewById(R.id.recyclerView);

        recyclerViewInit();


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override

                    public void onItemClick(View view, final int position) {

                        //TODO Showing movieobject as null

                        Picasso.get().load(String.valueOf(NetworkUtils.getImageOfMovieDbUrl(movieObject.getImage().get(position))))
                                .into(new Target() {

                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        relativeLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
                                    }

                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                    }


                                    @Override
                                    public void onPrepareLoad(final Drawable placeHolderDrawable) {
                                    }
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
                            public void onTransitionStart(Transition transition) {

                            }

                            @Override
                            public void onTransitionEnd(Transition transition) {

                                new Handler().postDelayed(new Runnable() {
                                    public void run() {

                     /* Create an intent that will start the main activity. */
                                        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                                        intent.putExtra(MovieDetailsActivity.EXTRA_POSITION, position);
                                        //SplashScreen.this.startActivity(mainIntent);
                                        startActivity(intent);

                     /* Apply our splash exit (fade out) and main
                        entry (fade in) animation transitions. */
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    }
                                }, 200);

                                recyclerViewInit();

                            }

                            @Override
                            public void onTransitionCancel(Transition transition) {

                            }

                            @Override
                            public void onTransitionPause(Transition transition) {

                            }

                            @Override
                            public void onTransitionResume(Transition transition) {

                            }
                        });

                        explode.setDuration(1000);
                        TransitionManager.beginDelayedTransition(recyclerView, explode);

                        // remove all views from Recycler View
                        recyclerView.setAdapter(null);


                    }

                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

                    @Override
                    public void onLongItemClick(View view, int position) {

                        //TODO get that specific image using position

                        ImageView imageView = findViewById(R.id.imageView);

                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_animation);
                        imageView.startAnimation(animation);


                    }
                })
        );
    }


    public void recyclerViewInit() {

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        mAdapter = new RecyclerviewAdapter();
        recyclerView.setAdapter(mAdapter);


    }



    public Boolean isInternetActive()
    {


        Boolean internet = true;
        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        mobileDataCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (!(wifiCheck.isConnected()) || !(mobileDataCheck.isConnected())) {
            Toast.makeText(this, "Please connect to the Internet", Toast.LENGTH_LONG).show();

            internet = false;
        }
        return internet;

    }



    public class MovieDbQueryTask extends AsyncTask<URL,Integer,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.refreshDrawableState();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setUnfinishedColor(Color.parseColor("#FFFFFF"));
            progressBar.setFinishedColor(Color.parseColor("#DD2C00"));
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
//            Log.d("tag",""+urls[0]);

            String jsonFile = null;
            try {
                jsonFile = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i=10;i<=100;i += 10)
            {
                try {
                    Thread.sleep(200);
                    publishProgress(i);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return jsonFile;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s == null){
                showError("JSON file from onPostExecute is null");
                return;
            }


            movieObject = JsonUtils.parseMoiveJson(s);


            if(movieObject == null){
                showError("movie Object is null");
                return;
            }

          recyclerView.setVisibility(View.VISIBLE);

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }

    }


    private void showError(String error) {

        Toast.makeText(this,"Something is wrong: "+error,Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem item = menu.findItem(R.id.toolbarspinner);
        spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        URL movieDBJSONUrl = NetworkUtils.getPopularMovieDbUrl();

                        if (movieDBJSONUrl == null) {
                            showError("MovieJSON is null");
                        }

                        task = new MovieDbQueryTask();
                        task.execute(movieDBJSONUrl);

                        recyclerViewInit();

                        break;
                    case 1:

                        URL topRatedMovieDbUrl= NetworkUtils.getTopRatedMovieDbUrl();

                        if (topRatedMovieDbUrl == null) {
                            showError("MovieJSON is null");
                        }

                        task = new MovieDbQueryTask();
                        task.execute(topRatedMovieDbUrl);

                        recyclerViewInit();


                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}

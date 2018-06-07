package com.example.akshay.moviestageapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.akshay.moviestageapp.Utilities.JsonUtils;
import com.example.akshay.moviestageapp.Utilities.NetworkUtils;
import com.example.akshay.moviestageapp.model.Movie;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

     static Movie movieObject;
    private RecyclerviewAdapter mAdapter;
    private RecyclerView recyclerView;

   /* NetworkInfo wifiCheck;
    NetworkInfo mobileDataCheck;

    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobileDataCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(!(wifiCheck.isConnected()) ||!(mobileDataCheck.isConnected())){
            Toast.makeText(this,"Please connect to the Internet",Toast.LENGTH_LONG).show();
        }
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    URL movieDBJSONUrl = NetworkUtils.getPopularMovieDbUrl();

    if(movieDBJSONUrl==null){
        showError("MovieJSON is null");
        return;
    }

    MovieDbQueryTask task = new MovieDbQueryTask();
    task.execute(movieDBJSONUrl);

        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        mAdapter = new RecyclerviewAdapter();
        recyclerView.setAdapter(mAdapter);

       recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override public void onItemClick(View view, int position) {

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
                        explode.setDuration(1000);
                        TransitionManager.beginDelayedTransition(recyclerView, explode);

                        // remove all views from Recycler View
//                        recyclerView.setAdapter(null);

//                        try{
//                            Thread.sleep(2000);
//                            startActivity(new Intent(getApplicationContext(),MovieDetailsActivity.class));
//
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }

                    }

                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

                    @Override public void onLongItemClick(View view, int position) {

                        //TODO get that specific image using position

                        ImageView imageView = findViewById(R.id.imageView);

                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_animation);
                        imageView.startAnimation(animation);



                    }
                })
        );

    }




    public class MovieDbQueryTask extends AsyncTask<URL,Void,String>{

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
            return jsonFile;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s == null){
                showError("JSON file from onPostExecute is null");
                return;
            }

//            Log.d("tag","s is "+s);

            movieObject = JsonUtils.parseMoiveJson(s);
//            Log.d("tag",""+movieObject);

            if(movieObject == null){
                showError("movie Object is null");
                return;
            }

        }

    }


    private void showError(String error) {

        Toast.makeText(this,"Something is wrong: "+error,Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.popular_menu:
                break;

            case R.id.top_rated_menu:

                break;
        }


        return super.onOptionsItemSelected(item);
    }
}

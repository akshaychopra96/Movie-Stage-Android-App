package com.example.akshay.moviestageapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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


}

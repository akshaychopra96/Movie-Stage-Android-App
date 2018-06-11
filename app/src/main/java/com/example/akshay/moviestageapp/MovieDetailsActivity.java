package com.example.akshay.moviestageapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
//        Log.d("tag",""+position);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }





    }

        private void closeOnError() {
            finish();
            Toast.makeText(this, "Error sir", Toast.LENGTH_SHORT).show();

    }

//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        MainActivity.recyclerViewInit();
//
//
//    }

}

package com.example.akshay.moviestageapp;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        ImageView noInternetImageView = findViewById(R.id.noInternetImageView);

        Picasso.get()
                .load(R.drawable.nointernet)
                .into(noInternetImageView);


        NetworkChangeReceiver networkC = new NetworkChangeReceiver();
        registerReceiver(networkC, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }
}

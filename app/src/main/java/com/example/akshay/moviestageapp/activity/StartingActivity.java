package com.example.akshay.moviestageapp.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.akshay.moviestageapp.internetConnection.NetworkChangeReceiver;
import com.example.akshay.moviestageapp.R;
import com.squareup.picasso.Picasso;

public class StartingActivity extends AppCompatActivity {

    final NetworkChangeReceiver networkC = new NetworkChangeReceiver();
    private boolean isRegistered = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        ImageView noInternetImageView = findViewById(R.id.noInternetImageView);

        Picasso.get()
                .load(R.drawable.nointernet)
                .into(noInternetImageView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkC,intentFilter);
        isRegistered = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isRegistered) {
            this.unregisterReceiver(networkC);
            isRegistered = false;
        }
    }
}

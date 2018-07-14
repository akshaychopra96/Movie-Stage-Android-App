package com.example.akshay.moviestageapp.internetConnection;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.akshay.moviestageapp.activity.MainActivity;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static boolean otherActivityVisited = false;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if(checkInternet(context))
        {
            Toast.makeText(context, "Connected to Internet",Toast.LENGTH_LONG).show();
            Intent i = new Intent(context,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        else
        {
            if(otherActivityVisited == true){
                otherActivityVisited = false;
                Toast.makeText(context,"No Internet",Toast.LENGTH_LONG).show();

/*                Intent i = new Intent(context,StartingActivity.class);
                context.startActivity(i);
                context.unregisterReceiver(this);*/
            }
        }
    }

    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        if (serviceManager.isNetworkAvailable()) {
            return true;
        } else {
            return false;
        }
    }

}

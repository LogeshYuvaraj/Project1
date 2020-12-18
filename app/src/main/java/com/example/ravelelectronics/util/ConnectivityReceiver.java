package com.example.ravelelectronics.util;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver {

    public static boolean isConnected(Context context){

        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfos=connectivityManager.getActiveNetworkInfo();

        return (networkInfos!=null && networkInfos.isConnected());
    }
}

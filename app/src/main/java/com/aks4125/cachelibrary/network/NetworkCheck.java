package com.aks4125.cachelibrary.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Aks4125 on 3/31/2019.
 */

public class NetworkCheck {
    private static final int TYPE_WIFI = 1;
    private static final int TYPE_MOBILE = 2;
    private static final int TYPE_NOT_CONNECTED = 0;
    private Context context;

    public NetworkCheck(Context ctx) {
        context = ctx;
    }

    private static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }


    // injected in dagger
    public boolean isConnected() {
        int conn = NetworkCheck.getConnectivityStatus(context);
        boolean status = false;
        if (conn == NetworkCheck.TYPE_WIFI || conn == NetworkCheck.TYPE_MOBILE) {
            status = true;//"Wifi enabled"
        }
        return status;
    }
}

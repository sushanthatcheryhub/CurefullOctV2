package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class CheckNetworkState {

    private Context context;

    public CheckNetworkState(Context context) {
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        boolean isNetworkAvailable = false;
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Network[] networks = connectivityManager.getAllNetworks();
                NetworkInfo networkInfo;
                for (Network mNetwork : networks) {
                    networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        return true;
                    }
                }
            } else {
                if (connectivityManager != null) {
                    //noinspection deprecation
                    NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                    if (info != null) {
                        for (NetworkInfo anInfo : info) {
                            if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return isNetworkAvailable;
    }
}

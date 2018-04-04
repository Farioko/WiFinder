package se.miun.student.faba1500.sakfinnare;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class ConnectToNetworkTask extends AsyncTask<Void, Void, Void> {
    private PostExecuteListener postExecuteListener;
    private WeakReference<Context> context;
    private WifiConfiguration wifiConfiguration;

    public ConnectToNetworkTask(WeakReference<Context> context, WifiConfiguration wifiConfiguration) {
        this.context = context;
        this.wifiConfiguration = wifiConfiguration;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        WifiManager manager = (WifiManager) context.get().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        // Remove all old configurations first, in case the user changed it.
        // Yes, this is safe, because we only have permission to delete configurations
        // that are created by this application.
        for(WifiConfiguration i : Objects.requireNonNull(manager).getConfiguredNetworks()) {
            manager.removeNetwork(i.networkId);
        }

        // Connect to the network.
        manager.enableNetwork(manager.addNetwork(wifiConfiguration), true);

        // Block until connected
        ConnectivityManager cm =
                (ConnectivityManager) context.get().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (postExecuteListener != null) {
            postExecuteListener.postExecute();
        }
    }

    protected void setPostExecuteListener(PostExecuteListener postExecuteListener) {
        this.postExecuteListener = postExecuteListener;
    }

    public interface PostExecuteListener {
        void postExecute();
    }
}

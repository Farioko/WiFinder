package se.miun.student.faba1500.sakfinnare;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Entity
public class WiFinder {
    @PrimaryKey @NonNull
    private String bssid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "key")
    private String key;

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @NonNull
    public String getBssid() {
        return bssid;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if(activeNetwork != null && activeNetwork.isConnected()) {
                Toast connectedToast = Toast.makeText(
                        context,
                        "Connected...",
                        Toast.LENGTH_LONG
                );

                connectedToast.show();

                try {
                    new SendGetRequestTask().execute(new URL("http://10.42.0.1:8080/status/"));
                } catch(MalformedURLException e) {}

                context.unregisterReceiver(this);
            }
        }
    }

    public int setState(boolean lost, WeakReference<Context> context) {
        if(lost) {
            WifiConfiguration configuration = new WifiConfiguration();
            configuration.SSID = "Sakfinnare";
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

            WifiManager manager = (WifiManager) context.get().getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);

            // Remove all old configurations first, in case the user changed it.
            // Yes, this is safe, because we only have permission to delete configurations
            // that are created by this application.
            for(WifiConfiguration i : Objects.requireNonNull(manager).getConfiguredNetworks()) {
                manager.removeNetwork(i.networkId);
            }

            // Enable WiFi in case its disabled.
            manager.setWifiEnabled(true);

            WifiReceiver receiver = new WifiReceiver();
            context.get().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

            // Connect to the network.
            manager.enableNetwork(manager.addNetwork(configuration), true);



            // OLD:

            /*
            ConnectToNetworkTask task = new ConnectToNetworkTask(context, configuration);
            ConnectToNetworkTask.PostExecuteListener postExecuteListener = new ConnectToNetworkTask.PostExecuteListener() {
                @Override
                public void postExecute() {
                    Log.d("ConnectAction", "Connected!");
                }
            };

            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                }
            };

            context.get().registerReceiver(receiver);

            task.setPostExecuteListener(postExecuteListener);
            task.execute();
            */
        }

        return 0;
    }
}

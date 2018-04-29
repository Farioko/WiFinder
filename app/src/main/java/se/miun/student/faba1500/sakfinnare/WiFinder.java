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
        public boolean lost;

        @Override
        public void onReceive(Context context, Intent intent) {
            WifiManager manager = (WifiManager) context.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);

            try {
                if(!manager.getConnectionInfo().getBSSID().equals(WiFinder.this.bssid)) {
                    return;
                }
            } catch(NullPointerException e) {
                return;
            }

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
                    HTTPRequest request = new HTTPRequest();
                    request.type = HTTPRequestType.PUT;
                    request.url = new URL("http://192.168.4.1/status");
                    request.lost = this.lost;

                    new SendHTTPRequestTask().execute(request);
                } catch(MalformedURLException e) {}

                context.unregisterReceiver(this);
            }
        }
    }

    public int setState(boolean lost, WeakReference<Context> context) {
        WifiManager manager = (WifiManager) context.get().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        if(manager.getConnectionInfo().getBSSID().equals(this.bssid)) {
            try {
                HTTPRequest request = new HTTPRequest();
                request.type = HTTPRequestType.PUT;
                request.url = new URL("http://192.168.4.1/status");
                request.lost = lost;

                new SendHTTPRequestTask().execute(request);
            } catch(MalformedURLException e) {
                return -1;
            }

            return 0;
        }

        WifiConfiguration configuration = new WifiConfiguration();

        configuration.BSSID = this.bssid;
        configuration.preSharedKey = "\"" + this.key + "\"";
        configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

        // Remove all old configurations first, in case the user changed it.
        // Yes, this is safe, because we only have permission to delete configurations
        // that are created by this application.
        for(WifiConfiguration i : Objects.requireNonNull(manager).getConfiguredNetworks()) {
            manager.removeNetwork(i.networkId);
        }

        WifiReceiver receiver = new WifiReceiver();
        receiver.lost = lost;
        context.get().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // Connect to the network.
        manager.enableNetwork(manager.addNetwork(configuration), true);

        return 0;
    }
}

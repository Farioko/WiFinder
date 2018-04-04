package se.miun.student.faba1500.sakfinnare;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;

@Entity
public class WiFinder {
    @PrimaryKey
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

    public int setState(boolean lost, WeakReference<Context> context) {
        if(lost) {
            WifiConfiguration configuration = new WifiConfiguration();
            configuration.SSID = "Sakfinnare";
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

            ConnectToNetworkTask task = new ConnectToNetworkTask(context, configuration);
            ConnectToNetworkTask.PostExecuteListener postExecuteListener = new ConnectToNetworkTask.PostExecuteListener() {
                @Override
                public void postExecute() {
                    Log.d("ConnectAction", "Connected!");
                }
            };

            task.setPostExecuteListener(postExecuteListener);
            task.execute();
        }

        return 0;
    }
}

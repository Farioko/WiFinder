package se.miun.student.faba1500.sakfinnare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView noWiFindersFoundLabel = findViewById(R.id.noWiFindersFoundLabel);

        RecyclerView wiFinderList = findViewById(R.id.wiFinderList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        wiFinderList.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(wiFinderList.getContext(),
                layoutManager.getOrientation());
        wiFinderList.addItemDecoration(mDividerItemDecoration);

/*        ArrayList<WiFinder> wiFinders = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            WiFinder wiFinder = new WiFinder();
            wiFinder.setBssid("0A:1B:3C:4D:5E:6" + i);
            wiFinder.setKey("secretPassword");
            wiFinders.add(wiFinder);
        }*/

        // Context route: PopulateWiFinderListTask -> WiFinderAdapter -> WiFinder
        new PopulateWiFinderListTask(
                new WeakReference<Context>(getApplicationContext()),
                new WeakReference<RecyclerView>(wiFinderList),
                new WeakReference<TextView>(noWiFindersFoundLabel)
        ).execute();

        FloatingActionButton addBtn = findViewById(R.id.addWiFinderActivityBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddWiFinderActivity.class));
            }
        });

        /*
        Button connectBtn = findViewById(R.id.connectBtn);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                // Remove all old configurations first, in case the user changed it.
                for(WifiConfiguration i : manager.getConfiguredNetworks()) {
                    if(i.SSID.equals("\"Sakfinnare\""))
                        manager.removeNetwork(i.networkId);
                }

                WifiConfiguration configuration = new WifiConfiguration();
                configuration.SSID = "Sakfinnare";
                configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                manager.enableNetwork(manager.addNetwork(configuration), true);


                try {
                    URL url = new URL("http://10.42.0.1:8080/status/");
                    SendGetRequestTask getRequestTask = new SendGetRequestTask();
                    getRequestTask.execute(url);
                    Log.d("sakfinnare-debug", getRequestTask.toString());
                } catch(MalformedURLException e) {
                    Log.d("sakfinnare-debug", "Malformed URL.");
                }

                TextView connectionStatus = findViewById(R.id.connectionStatusTextView);
                connectionStatus.setVisibility(View.VISIBLE);

            }
        });
        */
    }
}

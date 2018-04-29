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
    }
}

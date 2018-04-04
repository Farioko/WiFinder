package se.miun.student.faba1500.sakfinnare;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

public class PopulateWiFinderListTask extends AsyncTask<Void, Void, Integer> {
    private AppDatabase db;
    private WeakReference<Context> context;
    private WeakReference<TextView> noWiFindersFoundLabel;
    private WeakReference<RecyclerView> wiFinderList;
    private List<WiFinder> wiFinders;

    public PopulateWiFinderListTask(WeakReference<Context> context, WeakReference<RecyclerView> wiFinderList, WeakReference<TextView> noWiFindersFoundLabel) {
        this.context = context;
        this.wiFinderList = wiFinderList;
        this.noWiFindersFoundLabel = noWiFindersFoundLabel;
    }

    @Override
    protected void onPreExecute() {
        if(context.get() != null) {
            db = Room.databaseBuilder(context.get(),
                    AppDatabase.class, "wifinder-database").build();
        }
    }

    @Override
    protected Integer doInBackground(Void... v) {
        try {
            wiFinders = db.wiFinderDao().getAll();
        } catch(Exception e) {
            return -1;
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer i) {
        if(context.get() != null) {
            db.close();

            if(i < 0) {
                Toast errorToast = Toast.makeText(
                        context.get(),
                        "Error: Could not load WiFinders.",
                        Toast.LENGTH_LONG
                );

                errorToast.show();

                return;
            }

            RecyclerView.Adapter adapter = new WiFinderAdapter(wiFinders, context);
            wiFinderList.get().setAdapter(adapter);

            if(wiFinders.size() <= 0)
                noWiFindersFoundLabel.get().setVisibility(View.VISIBLE);
        }
    }
}

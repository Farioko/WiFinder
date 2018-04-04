package se.miun.student.faba1500.sakfinnare;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class AddWiFinderTask extends AsyncTask<WiFinder, Void, Integer> {
    private AppDatabase db;
    private WeakReference<Context> context;
    private WeakReference<Button> saveBtn;
    private Toast savingToast;

    public AddWiFinderTask(WeakReference<Context> context, WeakReference<Button> saveBtn, WeakReference<EditText> bssidInput, WeakReference<EditText> passwordInput) {
        this.context = context;
        this.saveBtn = saveBtn;
    }

    @Override
    protected void onPreExecute() {
        if(context.get() != null) {
            // Disable save button and hide keyboard.
            saveBtn.get().setEnabled(false);

            savingToast = Toast.makeText(
                    context.get(),
                    "Saving...",
                    Toast.LENGTH_LONG
            );

            savingToast.show();

            db = Room.databaseBuilder(context.get(),
                    AppDatabase.class, "wifinder-database").build();
        }
    }

    @Override
    protected Integer doInBackground(WiFinder... wiFinders) {
        for(WiFinder wiFinder : wiFinders) {
            try {
                db.wiFinderDao().insert(wiFinder);
            } catch(Exception e) {
                return -1;
            }
        }

        synchronized (this) {
            try {
                wait(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer i) {
        if(context.get() != null) {
            savingToast.cancel();
            db.close();

            if(i < 0) {
                Toast errorToast = Toast.makeText(
                        context.get(),
                        "Error: Could not add WiFinder.",
                        Toast.LENGTH_LONG
                );

                errorToast.show();
            }

            context.get().startActivity(
                    new Intent(
                            context.get(),
                            MainActivity.class
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
        }
    }
}

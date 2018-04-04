package se.miun.student.faba1500.sakfinnare;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class AddWiFinderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifinder);

        final Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Try to hide the virtual keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(Objects.requireNonNull(
                            getCurrentFocus()).getWindowToken(), 0);
                }

                // TODO: 3-4-18 Implement a constructor and field validation for WiFinder.
                WiFinder wiFinder = new WiFinder();
                EditText bssidInput = findViewById(R.id.bssidInput);
                EditText passwordInput = findViewById(R.id.passwordInput);
                wiFinder.setBssid(bssidInput.getText().toString());
                wiFinder.setKey(passwordInput.getText().toString());

                new AddWiFinderTask(
                        new WeakReference<Context>(getApplicationContext()),
                        new WeakReference<Button>(saveBtn),
                        new WeakReference<EditText>(bssidInput),
                        new WeakReference<EditText>(passwordInput)
                ).execute(wiFinder);
            }
        });
    }
}

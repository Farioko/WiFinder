package se.miun.student.faba1500.sakfinnare;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class SendGetRequestTask extends AsyncTask<URL, Void, String> {
    protected String doInBackground(URL... urls) {
        for (URL url : urls) {
            try {
                url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream())
                );

                String inputLine;
                String result = "";
                while ((inputLine = in.readLine()) != null)
                    result += inputLine;
                in.close();

                return result;
            } catch (IOException e) {
                Log.d("sakfinnare-debug", "Could not open url: " + e.getMessage());
            }
        }

        return "";
    }
}

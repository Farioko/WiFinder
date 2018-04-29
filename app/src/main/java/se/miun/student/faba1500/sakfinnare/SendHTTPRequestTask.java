package se.miun.student.faba1500.sakfinnare;

import android.os.AsyncTask;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class SendHTTPRequestTask extends AsyncTask<HTTPRequest, Void, Void> {
    @Override
    protected Void doInBackground(HTTPRequest... httpRequests) {
        for(HTTPRequest httpRequest : httpRequests) {
            HttpURLConnection httpCon;

            if(httpRequest.type == HTTPRequestType.PUT) {
                try {
                    httpCon = (HttpURLConnection) httpRequest.url.openConnection();
                    httpCon.setDoOutput(true);
                    httpCon.setRequestMethod("PUT");

                    OutputStreamWriter out = new OutputStreamWriter(
                            httpCon.getOutputStream());

                    if(httpRequest.lost) {
                        out.write("{\"lost\":true}");
                    } else {
                        out.write("{\"lost\":false}");
                    }

                    out.close();
                    httpCon.getInputStream();
                } catch(Exception e) {
                    return null;
                }

                return null;
            }
        }

        return null;
    }
}

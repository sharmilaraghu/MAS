package com.example.android.smartalarm;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by joseph on 4/20/15.
 */
public class LightOn extends AsyncTask<String, String, String> {
    private final String USER_AGENT = "Mozilla/5.0";
    @Override
    protected String doInBackground(String... params) {
        String urlString="http://192.168.1.4/apron/group/1/1?set=ON"; //params[0];
        String resultToDisplay = "";

        // HTTP Post
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("X-HTTP-Method-Override", "DELETE");
            urlConnection.setRequestProperty("User-Agent", USER_AGENT);
            urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.flush();
            wr.close();

            int responseCode = urlConnection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            resultToDisplay = response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultToDisplay;
    }

    protected void onPostExecute(String result) {

//            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
//
//            intent.putExtra(EXTRA_MESSAGE, result);
//
//            startActivity(intent);
    }

}
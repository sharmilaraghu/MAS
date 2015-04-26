package com.example.android.smartalarm;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DeviceActivity extends Activity {

    public final static String apiURL = "http://http://192.168.1.4/apron/";
    public final static String turnonURL = "http://192.168.1.4/apron/group/1/1?set=ON";
    public final static String turnoffURL = "http://192.168.1.4/apron/group/1/1?set=OFF";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void turnonLight(View v) {
        new LightOn().execute(turnonURL);
    }

    public void turnoffLight(View v) {
        new LightOff().execute(turnoffURL);
    }

    private class LightOn extends AsyncTask<String, String, String> {
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

    public class LightOff extends AsyncTask<String, String, String> {
        private final String USER_AGENT = "Mozilla/5.0";

        @Override
        protected String doInBackground(String... params) {
            String urlString = turnoffURL; //params[0];
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
}

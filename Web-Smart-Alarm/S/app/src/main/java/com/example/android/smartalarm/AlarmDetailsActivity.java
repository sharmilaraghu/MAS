package com.example.android.smartalarm;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
//import com.example.android.smartalarm.
//import com.example.android.smartalarm.A
//imports
import com.google.gson.stream.JsonReader;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;



public class AlarmDetailsActivity extends Activity {

    private AlarmModel alarmDetails;
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
    public final static String apiURL = "http://dev.m.gatech.edu/developer/gsingh62/api/smartclock/smartclock";
    public final static String makeChangeURL = "http://dev.m.gatech.edu/developer/gsingh62/api/smartclock/smartclock";
    public final static String EXTRA_MESSAGE = "com.example.webapitutorial.MESSAGE";


    private TimePicker timePicker;
    private EditText edtName ;
    private CheckBox chkWeekly ;
    private CheckBox chkSunday;
    private CheckBox chkMonday;
    private CheckBox chkTuesday;
    private CheckBox chkWednesday;
    private CheckBox chkThursday;
    private CheckBox chkFriday;
    private CheckBox chkSaturday;
    private TextView txtToneSelection;
    private ToggleButton smartToggle;

    public NotificationCompat.Builder mBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm_details);

        getActionBar().setTitle("Create New Alarm");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        timePicker = (TimePicker)findViewById(R.id.alarm_details_time_picker);
        edtName = (EditText)findViewById(R.id.alarm_details_name);
        chkWeekly = (CheckBox)findViewById(R.id.alarm_details_repeat_weekly);
        chkSunday= (CheckBox)findViewById(R.id.alarm_details_repeat_sunday);
        chkMonday=(CheckBox)findViewById(R.id.alarm_details_repeat_monday);
        chkTuesday=(CheckBox)findViewById(R.id.alarm_details_repeat_tuesday);
        chkWednesday=(CheckBox)findViewById(R.id.alarm_details_repeat_wednesday);
        chkThursday=(CheckBox)findViewById(R.id.alarm_details_repeat_thursday);
        chkFriday=(CheckBox)findViewById(R.id.alarm_details_repeat_friday);
        chkSaturday=(CheckBox)findViewById(R.id.alarm_details_repeat_saturday);
        txtToneSelection = (TextView)findViewById(R.id.alarm_label_tone_selection);
        smartToggle = (ToggleButton)findViewById(R.id.alarm_smart_toggle);

        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_action_add_to_queue)
                        .setContentTitle("Alarm notification")
                        .setContentText("Alarm Set to 6:00 AM!");

        long id = getIntent().getExtras().getLong("id");

        if (id == -1) {
            alarmDetails = new AlarmModel();
            //alarmDetails.id=-1;
            alarmDetails.id = dbHelper.maxAlarmId()+1;

        } else {
            alarmDetails = dbHelper.getAlarm(id);

            timePicker.setCurrentMinute(alarmDetails.timeMinute);
            timePicker.setCurrentHour(alarmDetails.timeHour);

            edtName.setText(alarmDetails.name);
            chkWeekly.setChecked(alarmDetails.repeatWeekly);
            chkSunday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.SUNDAY));
            chkMonday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.MONDAY));
            chkTuesday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.TUESDAY));
            chkWednesday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.WEDNESDAY));
            chkThursday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.THURSDAY));
            chkFriday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.FRIDAY));
            chkSaturday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.SATURDAY));
            Log.d("ALARMDETAILSACTIVITY", "" + alarmDetails.isSmartEnabled);
            smartToggle.setChecked(alarmDetails.isSmartEnabled);

            txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmDetails.alarmTone).getTitle(this));




        }

        final LinearLayout ringToneContainer = (LinearLayout) findViewById(R.id.alarm_ringtone_container);
        ringToneContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                startActivityForResult(intent , 1);
            }

        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: {
                    alarmDetails.alarmTone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

                    TextView txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
                    txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmDetails.alarmTone).getTitle(this));
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_details, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        long alarmId = getIntent().getExtras().getLong("id");

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }

            case R.id.action_save_alarm_details: {
                updateModelFromLayout();

                AlarmManagerHelper.cancelAlarms(this);


                if (alarmId < 0) {
                    dbHelper.createAlarm(alarmDetails);
                } else {
                    dbHelper.updateAlarm(alarmDetails);
                    //alarmDetails.id = alarmId;
                }




                if(alarmDetails.isSmartEnabled==true)
                {
                    //new CallAPI().execute(apiURL);
                    new MakeChange().execute(makeChangeURL);

                }

              //  updateModelFromLayout();

                AlarmManagerHelper.setAlarms(this);
                setResult(RESULT_OK);
                finish();
            }
        }




        return super.onOptionsItemSelected(item);
    }

    private void updateModelFromLayout() {

        TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_details_time_picker);
        alarmDetails.timeMinute = timePicker.getCurrentMinute().intValue();
        alarmDetails.timeHour = timePicker.getCurrentHour().intValue();

        EditText edtName = (EditText) findViewById(R.id.alarm_details_name);
        alarmDetails.name = edtName.getText().toString();

        CheckBox chkWeekly = (CheckBox) findViewById(R.id.alarm_details_repeat_weekly);
        alarmDetails.repeatWeekly = chkWeekly.isChecked();

        CheckBox chkSunday = (CheckBox) findViewById(R.id.alarm_details_repeat_sunday);
        alarmDetails.setRepeatingDay(AlarmModel.SUNDAY, chkSunday.isChecked());

        CheckBox chkMonday = (CheckBox) findViewById(R.id.alarm_details_repeat_monday);
        alarmDetails.setRepeatingDay(AlarmModel.MONDAY, chkMonday.isChecked());

        CheckBox chkTuesday = (CheckBox) findViewById(R.id.alarm_details_repeat_tuesday);
        alarmDetails.setRepeatingDay(AlarmModel.TUESDAY, chkTuesday.isChecked());

        CheckBox chkWednesday = (CheckBox) findViewById(R.id.alarm_details_repeat_wednesday);
        alarmDetails.setRepeatingDay(AlarmModel.WEDNESDAY, chkWednesday.isChecked());

        CheckBox chkThursday = (CheckBox) findViewById(R.id.alarm_details_repeat_thursday);
        alarmDetails.setRepeatingDay(AlarmModel.THURSDAY, chkThursday.isChecked());

        CheckBox chkFriday = (CheckBox) findViewById(R.id.alarm_details_repeat_friday);
        alarmDetails.setRepeatingDay(AlarmModel.FRIDAY, chkFriday.isChecked());

        CheckBox chkSaturday = (CheckBox) findViewById(R.id.alarm_details_repeat_saturday);
        alarmDetails.setRepeatingDay(AlarmModel.SATURDAY, chkSaturday.isChecked());

        ToggleButton smartToggle = (ToggleButton)findViewById(R.id.alarm_smart_toggle);
        alarmDetails.isSmartEnabled = smartToggle.isChecked();

        alarmDetails.isEnabled = true;
    }


    public static class FragmentOne extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            //Inflate the layout for this fragment

            return inflater.inflate(
                    R.layout.fragment_one, container, false);
        }


    }



    public void saveButtonClick()
    {
        // Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());


    }


    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        Fragment fr;
        fr = new FragmentOne();

        if (on) {
            // Enable vibrate
            //alarmDetails.isSmartEnabled=true;
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.commit();




        } else {
            // Disable vibrate
           //alarmDetails.isSmartEnabled=false;
            Fragment fe= new FragmentEmpty();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place,fe);
            fragmentTransaction.commit();

            //new MakeChange().execute(makeChangeURL);






        }
    }

    private class CallAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String urlString=params[0];
            String resultToDisplay = "";
            JsonReader reader = null;

            // HTTP Get
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                reader = new JsonReader(new BufferedReader(new InputStreamReader(urlConnection.getInputStream())));
                reader.beginArray();

                while (reader.hasNext()) {
                    reader.beginObject();
                    while(reader.hasNext()) {
                        String name = reader.nextName();
                        if (name.equals("id")) {
                            resultToDisplay += "Id: " + reader.nextInt();
                        } else if (name.equals("name")) {
                            resultToDisplay += "\n Name: " + reader.nextString();
                        } else if (name.equals("list_id")) {
                            resultToDisplay += "\n List Id: " + reader.nextInt();
                        } else if (name.equals("time_setting")) {
                            resultToDisplay += "\n Time Setting: " + reader.nextString();
                        } else if (name.equals("repeats")) {
                            resultToDisplay += "\n Repeats: " + reader.nextString();
                        } else if (name.equals("smart_feature_on")) {
                            resultToDisplay += "\n Smart Feature On: " + reader.nextString() + "\n";
                        } else {
                            reader.skipValue(); //avoid some unhandle events
                        }
                    }
                    reader.endObject();
                }

                reader.endArray();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resultToDisplay;
        }
/*
        protected void onPostExecute(String result) {

            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);

            intent.putExtra(EXTRA_MESSAGE, result);

            startActivity(intent);
        }*/

    } // end CallAPI

    private class MakeChange extends AsyncTask<String, String, String> {
        private final String USER_AGENT = "Mozilla/5.0";
        @Override
        protected String doInBackground(String... params) {
            String urlString=params[0];
            String resultToDisplay = "";
            String repeatingDays = "";
            int i;
            for(i=0;i<7;i++)
            {
                repeatingDays += alarmDetails.getRepeatingDay(i);
                if(i!=6) {

                repeatingDays+=",";

                }

            }

            // HTTP Post
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("User-Agent", USER_AGENT);
                    urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                    //urlConnection.setDoOutput(true);
                    //String urlParameters = "name=soccer&list_id=1&time_setting=10:00:00&repeats=weekdays&smart_feature_on=1";
                    String urlParameters = "id=" + alarmDetails.id + "&name=" + alarmDetails.name + "&list_id=1&time_setting=" + alarmDetails.timeHour + ":" + alarmDetails.timeMinute + "&repeats=" + repeatingDays + "&smart_feature_on=" + alarmDetails.isSmartEnabled;


                    urlConnection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    wr.writeBytes(urlParameters);
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

    /*    protected void onPostExecute(String result) {

            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);

            intent.putExtra(EXTRA_MESSAGE, result);

            startActivity(intent);
        }
*/
    } // end MakeChange

}

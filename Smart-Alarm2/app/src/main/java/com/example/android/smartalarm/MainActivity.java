package com.example.android.smartalarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this) ;
    private AlarmListAdapter mAdapter;
    private long alarmId;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mndTitles;
    private Context mContext;
    private AlarmModel alarmDetails;
    private ListView lv;

    public final static String makeChangeURL = "http://dev.m.gatech.edu/developer/gsingh62/api/smartclock/smartclock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mndTitles = getResources().getStringArray(R.array.nd_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        lv = (ListView)findViewById(R.id.list);

        mTitle = mDrawerTitle = getTitle();
        mContext = this;
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mndTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        //getActionBar().setTitle("Smart Alarm");
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter = new AlarmListAdapter(this,dbHelper.getAlarms());
        lv.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
       // menu.findItem(R.id.action_addalarm).setVisible(true);
       // menu.findItem(R.id.action_delalarm).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_addalarm).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_add_new_alarm: {
                //Intent intent = new Intent(this,
                  //      AlarmDetailsActivity.class);

                //startActivity(intent);
                startAlarmDetailsActivity(-1);

                break;
            }

            case R.id.action_delalarm:{
                //Cancel Alarms
                AlarmManagerHelper.cancelAlarms(mContext);
                //Delete alarm from DB by id
                dbHelper.deleteAlarm(alarmId);
                //Refresh the list of the alarms in the adaptor
                mAdapter.setAlarms(dbHelper.getAlarms());
                //Notify the adapter the data has changed
                mAdapter.notifyDataSetChanged();
                //Set the alarms
                AlarmManagerHelper.setAlarms(mContext);
            }


        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //selectItem(position);
            switch(position){
                case 0:
                    break;
                case 1:
                    Intent b = new Intent(MainActivity.this,SetDestination.class);
                    startActivity(b);
                    break;
                case 2:
                    Intent c = new Intent(MainActivity.this,SetDestination.class);
                    startActivity(c);
                    break;
                default:
            }
        }
    }

    //drawer click listener

/*
    private void selectItem(int position) {
        // update the main content by replacing fragments

            Fragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(PlaceholderFragment.ARG_ND_NUMBER, position);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();




            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mndTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);


    }

*/
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
/*
    public static class PlaceholderFragment extends Fragment {

        public static final String ARG_ND_NUMBER = "nd_number";

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            int i = getArguments().getInt(ARG_ND_NUMBER);
            if(i==0) {
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                String nd = getResources().getStringArray(R.array.nd_array)[i];

                //int imageId = getResources().getIdentifier(nd.toLowerCase(Locale.getDefault()),
                //      "drawable", getActivity().getPackageName());
                //((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
                getActivity().setTitle(nd);
                return rootView;

            }
            else
            {
                View rootView = inflater.inflate(R.layout.fragment_else, container, false);
                String nd = getResources().getStringArray(R.array.nd_array)[i];

                //int imageId = getResources().getIdentifier(nd.toLowerCase(Locale.getDefault()),
                //      "drawable", getActivity().getPackageName());
                //((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
                getActivity().setTitle(nd);
                return rootView;

            }

        }
    }

*/
    //on alarm enabled


    public void setAlarmEnabled(long id, boolean isEnabled) {
        AlarmManagerHelper.cancelAlarms(this);
        AlarmModel model = dbHelper.getAlarm(id);
        model.isEnabled = isEnabled;
        dbHelper.updateAlarm(model);
        //new
        mAdapter.setAlarms(dbHelper.getAlarms());
        mAdapter.notifyDataSetChanged();

        AlarmManagerHelper.setAlarms(this);

    }
    public void startAlarmDetailsActivity(long id) {
        Intent intent = new Intent(this, AlarmDetailsActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("Hello","Hello");
        if (resultCode == RESULT_OK) {
//            Log.d("Hi","Hi");
            mAdapter.setAlarms(dbHelper.getAlarms());
            mAdapter.notifyDataSetChanged();
            Log.d("After","After");
        }
    }

    public void deleteAlarm(long id) {
        alarmId = id;
        alarmDetails = dbHelper.getAlarm(alarmId);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please confirm")
                .setTitle("Delete set?")
                .setCancelable(true)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel Alarms
                        AlarmManagerHelper.cancelAlarms(mContext);
                        //Delete alarm from DB by id
                        dbHelper.deleteAlarm(alarmId);
                        //Refresh the list of the alarms in the adaptor
                        mAdapter.setAlarms(dbHelper.getAlarms());
                        //Notify the adapter the data has changed
                        mAdapter.notifyDataSetChanged();
                        //Set the alarms
                        AlarmManagerHelper.setAlarms(mContext);
                    }
                }).show();
        if(alarmDetails.isSmartEnabled == true)
        {
            String deleteUrl = makeChangeURL+"/"+alarmId;
            Log.d("delete", deleteUrl);
            new MakeDelete().execute(deleteUrl);

        }

    }



    private class MakeDelete extends AsyncTask<String, String, String> {
        private final String USER_AGENT = "Mozilla/5.0";
        @Override
        protected String doInBackground(String... params) {
            String urlString=params[0];
            String resultToDisplay = "";
            String repeatingDays = "";

            // HTTP Post
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("X-HTTP-Method-Override", "DELETE");
                urlConnection.setRequestProperty("User-Agent", USER_AGENT);

                urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                //urlConnection.setDoOutput(true);
                //String urlParameters = "name=soccer&list_id=1&time_setting=10:00:00&repeats=weekdays&smart_feature_on=1";
                //String urlParameters = "id=" + alarmDetails.id + "&name=" + alarmDetails.name + "&list_id=1&time_setting=" + alarmDetails.timeHour + ":" + alarmDetails.timeMinute + "&repeats=" + repeatingDays + "&smart_feature_on=" + alarmDetails.isSmartEnabled;


                urlConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                //wr.writeBytes(urlParameters);
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

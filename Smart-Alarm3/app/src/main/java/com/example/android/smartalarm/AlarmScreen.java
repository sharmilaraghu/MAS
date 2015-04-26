package com.example.android.smartalarm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class AlarmScreen extends Activity {

    public final String TAG = this.getClass().getSimpleName();
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;

    private WakeLock mWakeLock;
    private MediaPlayer mPlayer;
    private Button snooze;
    private static final int WAKELOCK_TIMEOUT = 60 * 1000;
    private Context context;
    private Intent mIntent;
    boolean isSnooze = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        //Setup layout
        this.setContentView(R.layout.activity_alarm_screen);

        String name = getIntent().getStringExtra(AlarmManagerHelper.NAME);
        int timeHour = getIntent().getIntExtra(AlarmManagerHelper.TIME_HOUR, 0);
        int timeMinute = getIntent().getIntExtra(AlarmManagerHelper.TIME_MINUTE, 0);
        String tone = getIntent().getStringExtra(AlarmManagerHelper.TONE);

        TextView tvName = (TextView) findViewById(R.id.alarm_screen_name);
        tvName.setText(name);

        TextView tvTime = (TextView) findViewById(R.id.alarm_screen_time);
        tvTime.setText(String.format("%02d : %02d", timeHour, timeMinute));

        Button dismissButton = (Button) findViewById(R.id.alarm_screen_button);

        this.context = this;
        final long alarmId = getIntent().getExtras().getLong("id");
        this.mIntent = getIntent();
        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        isSnooze = prefs.getBoolean("snooze", false); //.getString("text", null);

        dismissButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.stop();

                AlarmModel alarm = dbHelper.getAlarm(alarmId);
                alarm.timeHour = prefs.getInt("h", 0);
                alarm.timeMinute = prefs.getInt("m", 0);
                dbHelper.updateAlarm(alarm);
                AlarmManagerHelper.setAlarms(context);

                editor.putBoolean("snooze", false);
                editor.commit();
                notifyTasks();
                new LightOn().execute("");

                finish();
            }
        });

        snooze = (Button) findViewById(R.id.snooze);
        snooze.setOnClickListener(new OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Log.d("JOSEPH", alarmId + " should be snoozed");

                AlarmModel alarm = dbHelper.getAlarm(alarmId);

                if (!isSnooze) {
                    editor.putInt("h", alarm.timeHour);
                    editor.putInt("m", alarm.timeMinute);
                    editor.commit();
                }

                alarm.setSnooze();
                dbHelper.updateAlarm(alarm);

                editor.putBoolean("snooze", true);
                editor.commit();

//                PendingIntent pIntent = PendingIntent.getService(context, (int) alarmId, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                PendingIntent pIntent = AlarmManagerHelper.createPendingIntent(getApplication().getBaseContext(), )


//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY, alarm.timeHour);
//                calendar.set(Calendar.MINUTE, alarm.timeMinute);
//                calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + 5);

//                AlarmManagerHelper.setAlarm(getApplication().getBaseContext(), calendar, pIntent);
                AlarmManagerHelper.setAlarms(context);

                mPlayer.stop();
                finish();
            }
        });

        //Play alarm tone
        mPlayer = new MediaPlayer();
        try {
            if (tone != null && !tone.equals("")) {
                Uri toneUri = Uri.parse(tone);
                if (toneUri != null) {
                    mPlayer.setDataSource(this, toneUri);
                    mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mPlayer.setLooping(true);
                    mPlayer.prepare();
                    mPlayer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Ensure wakelock release
        Runnable releaseWakelock = new Runnable() {

            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                }
            }
        };

        new Handler().postDelayed(releaseWakelock, WAKELOCK_TIMEOUT);


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void notifyTasks(){
        TaskDBHelper taskdb = new TaskDBHelper(this);


        int i =0;
        for (String s : taskdb.getTasks()){

            Log.d(TAG, s);
            Intent resultIntent = new Intent(this, TaskListActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(TaskListActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            Notification n = new Notification.Builder(this)
                    .setContentTitle("New tasks to do!")
                    .setContentText(s)
                    .setContentIntent(resultPendingIntent).setAutoCancel(true)
                    .setStyle(new Notification.BigTextStyle().bigText(s))
                    .setSmallIcon(R.drawable.earth)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(i++, n);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();

        // Set the window to keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        // Acquire wakelock
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (mWakeLock == null) {
            mWakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        }

        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
            Log.i(TAG, "Wakelock aquired!!");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }
}
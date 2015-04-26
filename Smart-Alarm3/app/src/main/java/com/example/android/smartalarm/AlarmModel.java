package com.example.android.smartalarm;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
/**
 * Created by seema_suresh on 2/21/15.
 */
public class AlarmModel {

    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    //public static final int FRIDAY = 7;

    public long id; //salarm_id
    public int timeHour;
    public int timeMinute;
    private boolean repeatingDays[];
    public boolean repeatWeekly;
    public Uri alarmTone;
    public String name;
    public boolean isEnabled;
    public boolean isSmartEnabled;


    public int mTimeHour;
    public int mTimeMinute;

    public AlarmModel() {
        repeatingDays = new boolean[7];
    }

    public void setRepeatingDay(int dayOfWeek, boolean value) {
        repeatingDays[dayOfWeek] = value;
    }

    public boolean getRepeatingDay(int dayOfWeek) {
        return repeatingDays[dayOfWeek];
    }

    public int getInSec() {
        return (timeHour * 3600) + (timeMinute*60);
    }

    public void setTime(int sec) {
        if (getInSec() > sec) {
            this.timeHour = sec / 3600;
            sec -= (this.timeHour * 3600);
            this.timeMinute = sec / 60;
        }
    }

    public void setSnooze() {
        this.timeMinute++;
        //setTime(getInSec() + 60); // 5 mins
    }

    public AlarmModel copyToSnooze(AlarmModel a) {
        this.id = a.id;
        this.timeHour = a.timeHour;
        this.timeMinute = a.timeMinute;
        this.repeatingDays = a.repeatingDays;
        this.repeatWeekly = a.repeatWeekly;
        this.alarmTone = a.alarmTone;
        this.name = a.name;
        this.isEnabled = a.isEnabled;
        this.isSmartEnabled = a.isSmartEnabled;
        setSnooze();
        return this;
    }

    @Override
    public String toString(){
        return "id="+id +", hour="+timeHour+", min="+timeMinute+", isEnabled="+isEnabled;
    }
}

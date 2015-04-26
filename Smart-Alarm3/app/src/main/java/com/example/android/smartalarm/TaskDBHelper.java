package com.example.android.smartalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class TaskDBHelper extends SQLiteOpenHelper {

    public TaskDBHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%s TEXT)", TaskContract.TABLE,
                        TaskContract.Columns.TASK);

        Log.d("TaskDBHelper","Query to form table: "+sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS "+TaskContract.TABLE);
        onCreate(sqlDB);
    }

    public List<String> getTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM " + TaskContract.TABLE; //.Alarm.TABLE_NAME;
        Cursor c = db.rawQuery(select, null);
        List<String> tasks = new ArrayList<String>();

        while (c.moveToNext()) {
            ContentValues values = new ContentValues();
            String s = c.getString(c.getColumnIndex(TaskContract.Columns.TASK));
            if (s != null)
                tasks.add(s);
        }

        if (!tasks.isEmpty()) {
            return tasks;
        }

        return null;
    }
}
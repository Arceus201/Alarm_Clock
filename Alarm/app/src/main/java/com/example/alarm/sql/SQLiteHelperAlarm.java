package com.example.alarm.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.alarm.Model.Time;
import com.example.alarm.Model.TimeZoneClock;
import com.example.alarm.Model.Setting;
import com.example.alarm.Model.StopWatch;
import com.example.alarm.Model.MTimer;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelperAlarm extends SQLiteOpenHelper {
    private static final  String DATABASE_NAME = "Alarmtest.db";

    private static int DATABASE_VERSION =1;
    public SQLiteHelperAlarm(@Nullable Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table ALarm
        String sql ="CREATE TABLE alarm("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "gio TEXT, phut TEXT, mode INTEGER,repeat TEXT,label TEXT,source TEXT,mode_autosilence INTEGER)";
        db.execSQL(sql);

        String sql2 ="CREATE TABLE timezone("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "idtimezone TEXT)";
        db.execSQL(sql2);

        String sql3 ="CREATE TABLE stopwatch("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "time TEXT,lap TEXT)";
        db.execSQL(sql3);

        String sql4 ="CREATE TABLE timer("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "timer TEXT)";
        db.execSQL(sql4);

        String sql5 ="CREATE TABLE setting("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "auto_silence INTEGER,source  TEXT)";
        db.execSQL(sql5);

        db.execSQL("INSERT INTO " + "setting" + "(" + "auto_silence,source" + ") VALUES " + "(0,'Ring 1') ");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    // CRUD bang Alarm
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
    //get all order by date desc
    public List<Time> getAll(){
        List<Time> list = new ArrayList<>();

        SQLiteDatabase st = getReadableDatabase();
        String order = "gio DESC";
        Cursor rs = st.query("alarm", null, null,
                null, null, null, order);
        while (rs!=null && rs.moveToNext()){
            int id = rs.getInt(0);
            String gio = rs.getString(1);
            String  phut= rs.getString(2);
            int mode = rs.getInt(3);
            String repeat = rs.getString(4);
            String label = rs.getString(5);
            String source = rs.getString(6);
            int mode_autosilence = rs.getInt(7);


            list.add(new Time(id,gio,phut,mode,repeat,label,source,mode_autosilence));

        }

        return list;
    }

    public List<Time> getAll2(){
        List<Time> list = new ArrayList<>();

        SQLiteDatabase st = getReadableDatabase();
        String order = "gio DESC";
        Cursor rs = st.query("alarm", null, null,
                null, null, null, order);
        while (rs!=null && rs.moveToNext()){
            int id = rs.getInt(0);
            String gio = rs.getString(1);
            String  phut= rs.getString(2);
            int mode = rs.getInt(3);
            String repeat = rs.getString(4);
            String label = rs.getString(5);
            String source = rs.getString(6);
            int mode_autosilence = rs.getInt(7);

            if(mode==1) {
                list.add(new Time(id, gio, phut, mode, repeat, label, source,mode_autosilence));
            }

        }

        return list;
    }

    public boolean checkDuplicate(String gio, String phut) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id"};
        String selection = "gio = ? AND phut = ?";
        String[] selectionArgs = {gio, phut};

        Cursor cursor = db.query("alarm", columns, selection, selectionArgs, null, null, null);

        boolean hasDuplicate = cursor.moveToFirst();
        cursor.close();
        db.close();

        return hasDuplicate;
    }

    public Time getTimeById(int id){
        SQLiteDatabase st = getReadableDatabase();
        String selection = "id=?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor rs = st.query("alarm", null, selection,
                selectionArgs, null, null, null);
        if (rs.moveToFirst()) {
            String gio = rs.getString(1);
            String phut = rs.getString(2);
            int mode = rs.getInt(3);
            String repeat = rs.getString(4);
            String label = rs.getString(5);
            String source = rs.getString(6);
            int mode_autosilence = rs.getInt(7);
            rs.close();
            st.close();
            return new Time(id, gio, phut, mode, repeat, label, source,mode_autosilence);
        }
        rs.close();
        st.close();
        return null;
    }

    public Time getTimeByKey(String gio,String phut,String repeat){
        SQLiteDatabase st = getReadableDatabase();
        String selection = "gio = ? AND phut = ? AND repeat = ?";
        String[] selectionArgs = { gio,phut,repeat};
        Cursor rs = st.query("alarm", null, selection,
                selectionArgs, null, null, null);
        if (rs.moveToFirst()) {
            int id = rs.getInt(0);
            int mode = rs.getInt(3);
            String label = rs.getString(5);
            String source = rs.getString(6);
            int mode_autosilence = rs.getInt(7);
            rs.close();
            st.close();
            return new Time(id, gio, phut, mode, repeat, label, source,mode_autosilence);
        }
        rs.close();
        st.close();
        return null;
    }
    public  long addAlarm(Time t){
        ContentValues values = new ContentValues();
        values.put("gio", t.getGio());
        values.put("phut", t.getPhut());
        values.put("mode", t.getMode());
        values.put("repeat", t.getRepeat());
        values.put("label", t.getLabel());
        values.put("source", t.getSource());
        values.put("mode_autosilence", t.getMode_autoSilence());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return  sqLiteDatabase.insert("alarm", null, values);
    }

    public int update(Time t){
        ContentValues values = new ContentValues();
        values.put("gio", t.getGio());
        values.put("phut", t.getPhut());
        values.put("mode", t.getMode());
        values.put("repeat", t.getRepeat());
        values.put("label", t.getLabel());
        values.put("source", t.getSource());
        values.put("mode_autosilence", t.getMode_autoSilence());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = "id= ?";
        String[] whereArgs = {Integer.toString(t.getId())};
        return  sqLiteDatabase.update("alarm",  values,whereClause,whereArgs);
    }
    public int updateMode(int id,int mode){
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = "id= ?";
        String[] whereArgs = {Integer.toString(id)};
        return  sqLiteDatabase.update("alarm",  values,whereClause,whereArgs);
    }

    public  int delete(int id){
        String whereClause = "id= ?";
        String[] whereArgs = {Integer.toString(id)};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("alarm", whereClause, whereArgs);

    }


    // timezone
    public List<TimeZoneClock> getAllTimeZone(){
        List<TimeZoneClock> list = new ArrayList<>();

        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("timezone", null, null,
                null, null, null, null);
        while (rs!=null && rs.moveToNext()){
            int id = rs.getInt(0);
            String idtimezone = rs.getString(1);


            list.add(new TimeZoneClock(id, idtimezone));

        }
        return list;
    }

    public  long addTimeZone(String t){
        ContentValues values = new ContentValues();
        values.put("idtimezone", t);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return  sqLiteDatabase.insert("timezone", null, values);
    }

    public  int deleteTimeZone(int id){
        String whereClause = "id= ?";
        String[] whereArgs = {Integer.toString(id)};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("timezone", whereClause, whereArgs);

    }

    //CRUD bang StopWatch
    public List<StopWatch> getAllStopWatch(){
        List<StopWatch> list = new ArrayList<>();

        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("stopwatch", null, null,
                null, null, null, null);
        while (rs!=null && rs.moveToNext()){
            int id = rs.getInt(0);
            String time = rs.getString(1);
            String lap = rs.getString(2);


            list.add(new StopWatch(id, time,lap));

        }
        return list;
    }

    public void deleteAllStopwatches() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("stopwatch", null, null);
        db.close();
    }

    public void addStopwatches(List<StopWatch> stopwatchList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (StopWatch stopwatch : stopwatchList) {
            values.put("time", stopwatch.getTime());
            values.put("lap", stopwatch.getLap());
            db.insert("stopwatch", null, values);
        }
        db.close();
    }

    public void addOneStopwatch(StopWatch stopwatch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", stopwatch.getTime());
        db.insert("stopwatch", null, values);

        db.close();
    }


    //Timer
    public MTimer getTimer(){
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("timer", null, null,
                null, null, null, null);
        while (rs!=null && rs.moveToNext()){
            int id = rs.getInt(0);
            String timer = rs.getString(1);
            return new MTimer(id, timer);

        }
        return null;
    }

    public void addTimer(MTimer mTimer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("timer", mTimer.getTime());
        db.insert("timer", null, values);

        db.close();
    }

    public void deleteTimer() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("timer", null, null);
        db.close();
    }

    //setting
    public Setting getSetting(){
        Setting s = new Setting();

        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("setting", null, null,
                null, null, null, null);
        while (rs!=null && rs.moveToNext()){
            int id = rs.getInt(0);
            int auto_silence = rs.getInt(1);
            String source = rs.getString(2);
            return new Setting(id,auto_silence,source );
        }
        return null;
    }

    public int updateSetting(Setting t){
        ContentValues values = new ContentValues();
        values.put("auto_silence", t.getAuto_silence());
        values.put("source", t.getSource());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = "id= ?";
        String[] whereArgs = {Integer.toString(1)};
        return  sqLiteDatabase.update("setting",  values,whereClause,whereArgs);
    }

}

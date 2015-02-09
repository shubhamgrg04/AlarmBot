package com.apkbot.alarmbot;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AlarmHelper{
	public static Cursor cursor;
	
	public static SQLiteDatabase db;
	public static Context mcontext;
	public static PendingIntent pendingIntent;
	public static Intent intent;
	
	public static void AlarmDatabaseInitializer(Context context) {
		db = (new DatabaseHelper(context)).getWritableDatabase();
		mcontext = context;
	}
	
	public static void DatabaseClose(){
		db.close();
	}

	
	public static Alarm findalarmbyid(int id){
		cursor = db.query(DatabaseHelper.tablename, DatabaseHelper.Columns.ALARM_QUERY_COLUMNS , DatabaseHelper.Columns._ID + " = " + String.valueOf(id) , null , null, null, null);
		return getAlarmFromCursor(cursor);
	}
	
	public static boolean setAlarm (Alarm alarm){
		intent = new Intent(mcontext , WakeUp.class);
		intent.putExtra("hour", Alarm.getFormattedHour(alarm.hourOfDay));
		intent.putExtra("minute", Alarm.getFormattedMinute(alarm.minute));
		intent.putExtra("ampm", Alarm.getAmPm(alarm.hourOfDay));
		intent.putExtra("alert", alarm.alert);
		intent.putExtra("message", alarm.message);
		intent.putExtra("vibrate", alarm.vibrate);
		intent.putExtra("autocall", alarm.auto_call);
		pendingIntent = PendingIntent.getActivity(AlarmHelper.mcontext, alarm.id , intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager manager = (AlarmManager) mcontext.getSystemService(mcontext.ALARM_SERVICE);
		manager.set(AlarmManager.RTC_WAKEUP, alarm.time_in_millis , pendingIntent);
		return true;
	}
	
	public static boolean unsetAlarm (int id){
		intent = new Intent(mcontext , WakeUp.class);
		pendingIntent = PendingIntent.getActivity(AlarmHelper.mcontext, id , intent, PendingIntent.FLAG_CANCEL_CURRENT);
		pendingIntent.cancel();
		return true;
	}
	
	public static void addnew(Alarm alarm){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.Columns.HOUR, alarm.hourOfDay);
		values.put(DatabaseHelper.Columns.MINUTE , alarm.minute);
		values.put(DatabaseHelper.Columns.TIME_IN_MILLIS,alarm.time_in_millis);
		values.put(DatabaseHelper.Columns.ENABLED, alarm.enabled);
		values.put(DatabaseHelper.Columns.VIBRATE, alarm.vibrate);
		values.put(DatabaseHelper.Columns.MESSAGE, alarm.message);
		values.put(DatabaseHelper.Columns.ALERT_URI, alarm.alert.toString());
		values.put(DatabaseHelper.Columns.AUTO_CALLING, alarm.auto_call);
		db.insert(DatabaseHelper.tablename, null, values);
		
	}
	
	public static void updateAlarm(Alarm alarm, int id){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.Columns.HOUR, alarm.hourOfDay);
		values.put(DatabaseHelper.Columns.MINUTE , alarm.minute);
		values.put(DatabaseHelper.Columns.TIME_IN_MILLIS,alarm.time_in_millis);
		values.put(DatabaseHelper.Columns.ENABLED, alarm.enabled);
		values.put(DatabaseHelper.Columns.VIBRATE, alarm.vibrate);
		values.put(DatabaseHelper.Columns.MESSAGE, alarm.message);
		values.put(DatabaseHelper.Columns.ALERT_URI, alarm.alert.toString());
		values.put(DatabaseHelper.Columns.AUTO_CALLING, alarm.auto_call);
		db.update(DatabaseHelper.tablename, values, DatabaseHelper.Columns._ID + " = " + id , null);
		
	}
	
	
	public static void deletealarm(int id){
		db.delete(DatabaseHelper.tablename, DatabaseHelper.Columns._ID + " = " + id, null);
	}
	
	public static Alarm getAlarmFromCursor(Cursor cursor){
		Alarm alarm = new Alarm();
		if (cursor != null)
	        cursor.moveToFirst();
		alarm.hourOfDay = cursor.getInt(DatabaseHelper.Columns.HOUR_INDEX);
		alarm.minute = cursor.getInt(DatabaseHelper.Columns.MINUTE_INDEX);
		alarm.setTimeInMillis();
		alarm.enabled = cursor.getInt(DatabaseHelper.Columns.ENABLED_INDEX);
		alarm.vibrate = cursor.getInt(DatabaseHelper.Columns.VIBRATE_INDEX);
		alarm.auto_call = cursor.getInt(DatabaseHelper.Columns.AUTO_CALLING_INDEX);
		alarm.id = cursor.getInt(DatabaseHelper.Columns._ID_INDEX);
		alarm.alert = cursor.getString(DatabaseHelper.Columns.ALERT_URI_INDEX);
		alarm.message= cursor.getString(DatabaseHelper.Columns.MESSAGE_INDEX);
		
		cursor.close();
		
		return alarm;
	}
	
	public static void alarmEnabled(int id){
		ContentValues values = new ContentValues();
		
		values.put(DatabaseHelper.Columns.ENABLED, 1);
		db.update(DatabaseHelper.tablename, values, DatabaseHelper.Columns._ID + " = " + id , null);
		
	}
	public static void alarmDisabled(int id){
		ContentValues values = new ContentValues();
		
		values.put(DatabaseHelper.Columns.ENABLED, 0);
		db.update(DatabaseHelper.tablename, values, DatabaseHelper.Columns._ID + " = " + id , null);
	}
}

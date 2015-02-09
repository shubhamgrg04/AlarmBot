package com.apkbot.alarmbot;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
	
	public static class Columns implements BaseColumns {
		public static final String HOUR = "hour";
		public static final String MINUTE = "minute";
		public static final String TIME_IN_MILLIS = "time_in_millis";
		public static final String ENABLED = "enabled";
		public static final String MESSAGE = "message";
		public static final String VIBRATE = "vibrate";
		public static final String ALERT_URI = "alart_uri";
		public static final String AUTO_CALLING = "auto_calling";
		
		public static final String[] ALARM_QUERY_COLUMNS = {_ID, HOUR, MINUTE, TIME_IN_MILLIS, ENABLED, VIBRATE, MESSAGE, ALERT_URI, AUTO_CALLING};
		
		public static final int _ID_INDEX = 0;
		public static final int HOUR_INDEX = 1;
		public static final int MINUTE_INDEX = 2;
		public static final int TIME_IN_MILLIS_INDEX = 3;
		public static final int ENABLED_INDEX = 4;
		public static final int VIBRATE_INDEX = 5;
		public static final int MESSAGE_INDEX = 6;
		public static final int ALERT_URI_INDEX = 7;
		public static final int AUTO_CALLING_INDEX = 8;
	}
	
	
	public static String databasename = "alarms.db";
	public static String tablename = "alarmlist";
	public static final int databaseversion = 1;
	
	private static final String databaseCreate = "create table " + tablename + " (" + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Columns.HOUR + " INTEGER, "
												+ Columns.MINUTE + " INTEGER, " + Columns.TIME_IN_MILLIS + " REAL, " + Columns.ENABLED + " INTEGER, " + Columns.VIBRATE 
												+ " INTEGER, " + Columns.MESSAGE + " TEXT, " + Columns.ALERT_URI + " TEXT, " + Columns.AUTO_CALLING + " INTEGER)" ;
	
	
	public DatabaseHelper(Context context) {
		super(context, databasename , null , databaseversion);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(databaseCreate);
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 Log.w(DatabaseHelper.class.getName(),
			        "Upgrading database from version " + oldVersion + " to "
			            + newVersion + ", which will destroy all old data");
		 db.execSQL("DROP TABLE IF EXISTS " + tablename);
		 onCreate(db);
	}
}

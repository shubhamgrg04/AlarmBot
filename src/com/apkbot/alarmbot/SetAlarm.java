package com.apkbot.alarmbot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class SetAlarm extends Activity {
	
	static int mhourOfDay;
	static int mminute;
	static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getApplicationContext();
		
	}
	
	public static boolean setAlarm (Alarm alarm) {
		
		return true;
	}

}

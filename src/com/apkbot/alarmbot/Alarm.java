package com.apkbot.alarmbot;

import java.util.Calendar;

public class Alarm {
	public int 	id;
	public int hourOfDay;
	public int minute;
	public long time_in_millis;
	public int enabled;
	public int vibrate;
	public String alert;
	public String message;
	public long alarm_time;
	public int auto_call;
	public Calendar calendar;
	
	
	public void setTimeInMillis (){
		
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		if(System.currentTimeMillis() > calendar.getTimeInMillis()){
			calendar.add(Calendar.DATE, 1);
		}	
		time_in_millis = calendar.getTimeInMillis();
	}
	
	public static int get12hour(int hour){
		int rhour;
		if (hour >= 12){
			rhour = hour - 12;
			if (hour == 12)
				rhour = 12;
		}
		else
			rhour = hour;
		return rhour;
	}
	
	public static String getAmPm(int hour){
		String rAmPm;
		if(hour >= 12)
			rAmPm = "pm";
		else
			rAmPm = "am";
		
		return rAmPm;
		
	}
	
	public static String getFormattedHour(int hourofDay){
		String rhour;
		hourofDay = get12hour(hourofDay);
		if(hourofDay<10)
			rhour = "0" + hourofDay;
		else
			rhour = "" + hourofDay;
		
		return rhour;
		
	}
	
	public static String calculateTimeString (Alarm alarm){
			long timedifference = alarm.time_in_millis - System.currentTimeMillis();
			long hour = timedifference / (60*60*1000);
			timedifference = timedifference % (60*60*1000);
			long minute = timedifference / (60*1000);
			return hour + " hours and " + minute + " minutes";
		}
	
	public static String getFormattedMinute(int minute){
		String rminute;
		if(minute<10)
			rminute = "0" + minute;
		else
			rminute = "" + minute;
		
		return rminute;
		
	}
	
	public static int getIntFromBoolean (Boolean bool){
		if(bool == true)
			return 1;
		else
			return 0;
	}
	
	public static boolean getBooleanFromInt (int i){
		if(i == 1)
			return true;
		else
			return false;
	}

}

package com	.apkbot.alarmbot;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.RingtonePreference;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmPreferences extends PreferenceActivity implements TimePickerDialog.OnTimeSetListener {
	
	public Preference timePreference;
	public CheckBoxPreference enabledPreference;
	public CheckBoxPreference vibratePreference;
	public CheckBoxPreference auto_callPreference;
	public EditTextPreference messagePreference;
	public RingtonePreference alertPreference;
	
	private int mhour;
	private int mminute;
	private boolean menabled;
	private boolean mauto_call;
	private boolean mvibrate;
	private String mmessage;
	private String malert;
	public static Toast toast;
	
	public static Context context;
	
	protected int id;
	static Ringtone rt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.alarmpreferences);
		
		timePreference = 	findPreference("time");
		enabledPreference = (CheckBoxPreference) findPreference("enabled");
		vibratePreference = (CheckBoxPreference) findPreference("vibrate");
		auto_callPreference = (CheckBoxPreference) findPreference("auto_call");
		messagePreference = (EditTextPreference) findPreference("message");
		alertPreference =  (RingtonePreference) findPreference("alert");
		alertPreference.setShowDefault(true);
		alertPreference.setShowSilent(false);
		alertPreference.setDefaultValue(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString());
		
		context = this;
		
		id = getIntent().getIntExtra("id", -1);
		
		fillUpTheFields(id);
		updataAlarmPreferences();
		
		
		timePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			public boolean onPreferenceClick(Preference arg0) {
				new TimePickerDialog(AlarmPreferences.this, AlarmPreferences.this , mhour, mminute, false).show();
				return true;
			}
		});
		
		auto_callPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference arg0, Object arg1) {
				mauto_call = (Boolean)arg1;
				return true;
			}
		});
		
		enabledPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if((Boolean)newValue == true){
					menabled = true;
					if(id == -1)
						addalarm(getAlarm());
					else
						updateAlarm(getAlarm(), id);
					
					setAlarm(getAlarm());
					setToast(AlarmPreferences.this, "Alarm set for " + Alarm.calculateTimeString(getAlarm()) + " from now.");
					toast.show();
				}
				else {
					AlarmHelper.unsetAlarm(id);
					menabled = false;
					updateAlarm(getAlarm(), id);
				}
				return true;
			}
		});
		
		messagePreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				messagePreference.setSummary((String)newValue);
				mmessage = (String)newValue;
				return true;
			}
		});
		
		alertPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				
				 malert = (String)newValue;
				 alertPreference.setSummary(getRingtoneTitle(Uri.parse(malert)));
				return true;
			}
		});
		
		vibratePreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				mvibrate = (Boolean)newValue;
				return true;
			}
		});
		
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		mhour = hourOfDay;
		mminute = minute;
		timePreference.setSummary(Alarm.getFormattedHour(mhour) + ":" + Alarm.getFormattedMinute(mminute) + " " + Alarm.getAmPm(mhour));
	}
	
	public void updataAlarmPreferences(){
			timePreference.setSummary(Alarm.getFormattedHour(mhour) + ":" + Alarm.getFormattedMinute(mminute) + " " + Alarm.getAmPm(mhour));
			enabledPreference.setChecked(menabled);
			vibratePreference.setChecked(mvibrate);
			auto_callPreference.setChecked(mauto_call);
			messagePreference.setSummary(mmessage);
			alertPreference.setSummary(getRingtoneTitle(Uri.parse(malert)));
				
		
	}
	
	public void fillUpTheFields (int id){
		if(id == -1){
			Calendar calendar = Calendar.getInstance();
			mhour = calendar.get(calendar.HOUR_OF_DAY);
			mminute = calendar.get(calendar.MINUTE);
			menabled = false;
			mvibrate = false;
			mauto_call = false;
			mmessage = "WakeUp!";
			malert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
		}
		else{
			Alarm alarm = AlarmHelper.findalarmbyid(id);
			mhour = alarm.hourOfDay;
			mminute = alarm.minute;
			menabled = Alarm.getBooleanFromInt(alarm.enabled);
			mvibrate = Alarm.getBooleanFromInt(alarm.vibrate);
			mauto_call =Alarm.getBooleanFromInt(alarm.auto_call);
			mmessage = alarm.message;
			malert = alarm.alert;
		}
		
	}
	
	public Alarm getAlarm(){
		Alarm alarm = new Alarm();
		alarm.hourOfDay = mhour;
		alarm.minute = mminute;
		alarm.setTimeInMillis();
		alarm.auto_call = alarm.getIntFromBoolean(mauto_call);
		alarm.vibrate = alarm.getIntFromBoolean(mvibrate);
		alarm.enabled = alarm.getIntFromBoolean(menabled);
		alarm.alert = malert;
		alarm.message = mmessage;
		return alarm;
	}
	
	public void setAlarm(Alarm alarm){
		AlarmHelper.setAlarm(alarm);
	}
	
	public void addalarm(Alarm alarm){
		AlarmHelper.addnew(alarm);
	}
	
	public void updateAlarm (Alarm alarm, int id){
		AlarmHelper.updateAlarm(alarm, id);
	}
	
	public static void setToast(Context context, String text) {
		toast = Toast.makeText(context , text, Toast.LENGTH_SHORT);
	}
	
	public static String getRingtoneTitle (Uri ringtoneUri){
		rt =RingtoneManager.getRingtone(context, ringtoneUri);
		return rt.getTitle(context);
	}
	
}

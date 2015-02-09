package com.apkbot.alarmbot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TimePicker;

public class AlarmDialog extends Dialog{
	Context context;
	Button done;
	TimePicker picker;
	public int hours ;
	public int minutes ;
	
	
	public AlarmDialog(Activity a, Context con) {
		super (a);
		context = con;
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_dialog);
		ProgressBar pb = (ProgressBar)findViewById (R.id.progressBarToday); 
		pb.setProgress(10);
		
		picker= (TimePicker) findViewById(R.id.timePicker);
		picker.setCurrentHour(5);
		picker.setCurrentMinute(30);
		picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				hours = hourOfDay;
				minutes = minute;
			}
		});
		done = (Button) findViewById(R.id.done);
		done.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return false;
			}
		}); 
	}
	
}

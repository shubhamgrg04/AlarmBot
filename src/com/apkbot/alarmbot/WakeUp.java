package com.apkbot.alarmbot;

import android.app.Activity;
import android.app.Notification;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class WakeUp extends Activity {
	
	protected String hour;
	protected String minute;
	protected String ampm;
	protected String message;
	protected Ringtone rt;
	protected String alert;
	protected Thread thread;
	protected int vibrate;
	Vibrator vibrator;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wakeup);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		hour = getIntent().getStringExtra("hour");
		alert = getIntent().getStringExtra("alert");
		minute = getIntent().getStringExtra("minute");
		ampm = getIntent().getStringExtra("ampm");
		message = getIntent().getStringExtra("message");
		vibrate = getIntent().getIntExtra("vibrate", 0);
		if(vibrate == 1){
			vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			long[] pattern = {0, 1000, 3000};
			vibrator.vibrate(pattern, 0);
		}
				
		((TextView)findViewById(R.id.alarm_time)).setText(hour + ":" + minute );
		((TextView)findViewById(R.id.ampm)).setText(ampm);
		((TextView)findViewById(R.id.message)).setText(message);
		
		if(alert != null){
		rt = RingtoneManager.getRingtone(this, Uri.parse(alert));
		rt.play();
		}
		
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				Notification notification = new Notification(R.drawable.addnew, "Alarm at 06:30 pm was expired", Notification.FLAG_FOREGROUND_SERVICE);
				vibrator.cancel();
				finish();
				
			}
		}, 10000);
	}
	
	public void stop (View view){
		vibrator.cancel();
		finish();
	}
	
	@Override
	protected void onDestroy() {
		//rt.stop();
		vibrator.cancel();
		super.onDestroy();
	}

}

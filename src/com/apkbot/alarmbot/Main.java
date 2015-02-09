package com.apkbot.alarmbot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class Main extends Activity {
	
	public static Context context;
	
	protected SQLiteDatabase db;
	protected Cursor cursor;
	protected ListView alarm_list;
	protected AlarmDialog dialog;
	protected AlarmListAdapter adapter;
	protected OnCheckedChangeListener changelistener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this.getApplicationContext();
		AlarmHelper.AlarmDatabaseInitializer(this.getApplicationContext());
		db = (new DatabaseHelper(this.getApplicationContext())).getWritableDatabase();
		alarm_list = (ListView) findViewById(R.id.list);
		alarm_list.setLongClickable(true);
		alarm_list.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
				Cursor cursor = (Cursor)adapter.getItem(pos);
				AlarmHelper.deletealarm(cursor.getInt(DatabaseHelper.Columns._ID_INDEX));
				return true;
			}
		});
		alarm_list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(Main.this.getApplicationContext(), AlarmPreferences.class);
				Cursor cursor = (Cursor)adapter.getItem(arg2);
				intent.putExtra("id", cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Columns._ID)));
				startActivity(intent);
			}
		});
	}
	
	public void setting(View view) {
		Intent intent = new Intent(Main.this.getApplicationContext(), Settings.class);
		startActivity(intent);
	}
	
	public void addalarm(View view) {
		Intent intent = new Intent(Main.this.getApplicationContext(),AlarmPreferences.class);
		startActivity(intent);
	}	
		
	public void refreshlist(){		
		cursor = db.query(DatabaseHelper.tablename ,null, null, null, null , null , null);
		adapter = new AlarmListAdapter(Main.this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		alarm_list.setAdapter(adapter);
		
	}
	
	@Override
	protected void onResume() {
		refreshlist();
		super.onResume();
	}
	
	public class AlarmListAdapter extends CursorAdapter {
		
		LayoutInflater inflater;
		int id;
		Context mcontext;
		ViewHolder mholder;
		View v;

		public AlarmListAdapter(Context context, Cursor cursor, int flags) {
			super(context, cursor, flags);
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.mcontext = context;
			
		}
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			
			id = cursor.getInt(DatabaseHelper.Columns._ID_INDEX);
			int hourOfDay = cursor.getInt(DatabaseHelper.Columns.HOUR_INDEX);
			String timeofday;
			if(hourOfDay > 12)
				timeofday = "pm";
			else
				timeofday = "am";
			
			mholder = (ViewHolder) view.getTag();
			if(cursor.getInt(DatabaseHelper.Columns.ENABLED_INDEX) == 1)
				mholder.on_off.setChecked(true);
			else
				mholder.on_off.setChecked(false);
			
			//mholder.on_off.setOnCheckedChangeListener(changelistener);
			mholder.hour.setText(Alarm.getFormattedHour(hourOfDay));
			mholder.minute.setText(Alarm.getFormattedMinute(cursor.getInt(DatabaseHelper.Columns.MINUTE_INDEX)));
			mholder.am_pm.setText(timeofday);
			mholder.id = id;
			
			
				}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			v = inflater.inflate(R.layout.listxml, parent, false);
			final ViewHolder holder = new ViewHolder();
			holder.hour = (TextView) v.findViewById(R.id.hour);
			holder.minute = (TextView) v.findViewById(R.id.minute);
			holder.am_pm = (TextView) v.findViewById(R.id.am_pm);
			holder.on_off = (CheckBox) v.findViewById(R.id.checkbox);
			holder.on_off.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					ListView lv = (ListView) Main.this.findViewById(R.id.list);
					int position = lv.getPositionForView(v);
					Cursor ncursor = (Cursor) getItem(position);
					int id = ncursor.getInt(DatabaseHelper.Columns._ID_INDEX);
					if (holder.on_off.isChecked() == true){
						AlarmHelper.alarmEnabled(id);
						Log.w("hello", position + " enabled " + id);
					}
					else if (holder.on_off.isChecked() == false){
						AlarmHelper.alarmDisabled(id);
						Log.w("hello", position + " disabled " + id);
					}
					
				}
			});
			v.setTag(holder);
			return v;
		}
		
		public class ViewHolder {
			TextView hour;
			TextView minute;
			TextView am_pm;
			CheckBox on_off;
			int id;
		}

	}
}

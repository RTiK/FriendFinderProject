package com.summerschool.friendfinderapplication.activities;

import java.util.Calendar;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.models.Event;
import com.summerschool.friendfinderapplication.models.Group;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class NewEventActivity extends Activity {
	
	private final static String LOGTAG = "NEW_EVENT";
	
	public static final String EXTRA_MARKER_LATITUDE = "LATITUDE";
	public static final String EXTRA_MARKER_LONGITUDE = "LONGITUDE";
	public static final String EXTRA_GROUPNAME = "GROUPNAME";

	private LatLng mLocation;
	private String mGroupName;
	private Group mCurrentGroup;
	private EditText mOutputDate;
	private EditText mOutputTime;
	private Calendar mCalendar;
    
	private final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_event);
		
		Intent i = getIntent();
		
		if (i.hasExtra(EXTRA_MARKER_LATITUDE) && i.hasExtra(EXTRA_MARKER_LONGITUDE) && i.hasExtra(EXTRA_GROUPNAME)) {
			mGroupName = i.getStringExtra(EXTRA_GROUPNAME);
			mLocation = new LatLng(i.getDoubleExtra(EXTRA_MARKER_LATITUDE, 0.0), i.getDoubleExtra(EXTRA_MARKER_LONGITUDE, 0.0));
		} else {
			Toast.makeText(getApplicationContext(), "Invalid data provided", Toast.LENGTH_LONG).show();
			Log.e(LOGTAG, "Invalid coordinates or groupname");
			finish();
		}
		
		mOutputDate = (EditText) findViewById(R.id.eventDate);
		mOutputTime = (EditText) findViewById(R.id.eventTime);		
	 
        // Get current date by calender
        mCalendar = Calendar.getInstance();
        mOutputDate.setText(mCalendar.get(Calendar.DAY_OF_MONTH) + " " + MONTHS[mCalendar.get(Calendar.MONTH)] + " " + mCalendar.get(Calendar.YEAR));
        String minutes = "" + mCalendar.get(Calendar.MINUTE);
        String hours = "" + mCalendar.get(Calendar.HOUR_OF_DAY);
        if (mCalendar.get(Calendar.MINUTE) < 10)
        	minutes = "0" + mCalendar.get(Calendar.MINUTE);
        if (mCalendar.get(Calendar.HOUR_OF_DAY) < 10)
        	hours = "0" + mCalendar.get(Calendar.HOUR_OF_DAY);
        mOutputTime.setText(hours + ":" + minutes);
				
	}
	
	public void onClickDate(final View v) {
		OnDateSetListener dateParser = new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				mCalendar.set(Calendar.YEAR, year);
				mCalendar.set(Calendar.MONTH, monthOfYear);
				mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				mOutputDate.setText(dayOfMonth + " " + MONTHS[monthOfYear] + " " + year);
			}
		};
		DatePickerDialog datePicker = new DatePickerDialog(NewEventActivity.this, dateParser, 
				mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
		datePicker.show();
	}
	
	public void onClickTime(final View v)
	{
		OnTimeSetListener timeParser = new OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				mCalendar.set(Calendar.MINUTE, minute);
				String formattedMinutes = "" + mCalendar.get(Calendar.MINUTE);
		        String formattedHours = "" + mCalendar.get(Calendar.HOUR_OF_DAY);
		        if (mCalendar.get(Calendar.MINUTE) < 10)
		        	formattedMinutes = "0" + mCalendar.get(Calendar.MINUTE);
		        if (mCalendar.get(Calendar.HOUR_OF_DAY) < 10)
		        	formattedHours = "0" + mCalendar.get(Calendar.HOUR_OF_DAY);
				mOutputTime.setText(formattedHours + ":" + formattedMinutes);
			}
		};
		TimePickerDialog timePicker = new TimePickerDialog(NewEventActivity.this, timeParser,
				mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
		timePicker.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_event, menu);
		return true;
	}

	public void onClickCreate(final View v) {
		
		//Get data information from the view
		EditText markerName = (EditText) findViewById(R.id.markerName);
		EditText markerDescription = (EditText) findViewById(R.id.markerDescription);
		
		final String eventName = markerName.getText().toString().trim();
		final String eventDescription = markerDescription.getText().toString().trim();		

		//Check whether all necessary information has been added
		if(eventName.length() < 1) {
			Log.e(LOGTAG, "Event name is empty");
			Toast.makeText(getApplicationContext(), "Please enter event name", Toast.LENGTH_LONG).show();
		} else if (eventDescription.length() < 1) {
			Log.e(LOGTAG, "Event description is empty");
			Toast.makeText(getApplicationContext(), "Please enter event decription", Toast.LENGTH_LONG).show();
		} else if (mCalendar.before(Calendar.getInstance())) {
			Log.e(LOGTAG, "Event date is invalid");
			Toast.makeText(getApplicationContext(), "Please enter a date in the future", Toast.LENGTH_LONG).show();
		} else {
		
			Log.i(LOGTAG, "" + mCalendar.getTime());
				
			//Look for the group
			ParseQuery<Group> findGroupName = ParseQuery.getQuery(Group.class);
			findGroupName.whereEqualTo("name", mGroupName);
			try {
				List<Group> g = findGroupName.find();
				mCurrentGroup = g.get(0);	
			} catch(ParseException e) {
				Log.e(LOGTAG, "Group not found");
			}
			
			if (mCurrentGroup != null) {
				ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
				eventQuery.whereEqualTo(Event.TITLE, eventName);
				eventQuery.countInBackground(new CountCallback() {
					
					@Override
					public void done(int c, ParseException err) {
						// If the marker doesn't already exist
						if(c == 0) {
							//Create the element
							com.summerschool.friendfinderapplication.models.Event e = new com.summerschool.friendfinderapplication.models.Event();
							e.setTitle(eventName);
							e.setDescription(eventDescription);
							e.setOwner(ParseUser.getCurrentUser());
							e.setDate(mCalendar.getTime());
							e.setLocation(new ParseGeoPoint(mLocation.latitude, mLocation.longitude));
							e.setGroup(mCurrentGroup);
							
							//Try to save data in database
							try {
								e.save();
								Toast.makeText(getApplicationContext(), "Event created", Toast.LENGTH_LONG).show();		
							} catch(ParseException ex) {
								Log.e(LOGTAG, "Event could not be created");
							}
							
							finish();
						}
					}
				});
			}	
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}

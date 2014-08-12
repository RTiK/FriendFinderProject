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
import com.summerschool.friendfinderapplication.models.Group;
import com.summerschool.friendfinderapplication.models.POI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class NewEventActivity extends Activity {
	
	private final static String LOGTAG = "NEW_EVENT";
	public static final String EXTRA_MARKER_LATITUDE = "LATITUDE";
	public static final String EXTRA_MARKER_LONGITUDE = "LONGITUDE";

	private LatLng mLocation;
	private String mGroupName;
	
	public static final String EXTRA_GROUPNAME = "GROUPNAME";

	private Group selectedGroup;
	
	private EditText OutputDate;
	private EditText OutputTime;
	 
	private int year;
    private int month;
    private int day;
    
    private int hour;
    private int minute;
    private boolean morning;
 
    static final int DATE_PICKER_ID = 1111; 
    static final int TIME_PICKER_ID = 2222; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_event);	// TODO change to activity_new_event as soon as the file is created
		
		Intent i = getIntent();
		mGroupName = i.getStringExtra(EXTRA_GROUPNAME);
		mLocation = new LatLng(i.getDoubleExtra(EXTRA_MARKER_LATITUDE, 0.0), i.getDoubleExtra(EXTRA_MARKER_LONGITUDE, 0.0));
		
		OutputDate = (EditText) findViewById(R.id.eventDate);
		OutputTime = (EditText) findViewById(R.id.eventTime);
	 
	        // Get current date by calender
	        final Calendar c = Calendar.getInstance();
	        year  = c.get(Calendar.YEAR);
	        month = c.get(Calendar.MONTH);
	        day   = c.get(Calendar.DAY_OF_MONTH);
	        hour  = c.get(Calendar.HOUR_OF_DAY);
	        minute= c.get(Calendar.MINUTE);
	 
	        // Show current date
	         
	        OutputDate.setText(new StringBuilder()
	                // Month is 0 based, just add 1
	                .append(month + 1).append("-").append(day).append("-")
	                .append(year).append(" "));
	        
	        OutputTime.setText(new StringBuilder()
	            .append(hour).append(":").append(minute));
				
	}
	
	public void onClickDate(final View v)
	{
		showDialog(DATE_PICKER_ID);
	}
	
	public void onClickTime(final View v)
	{
		showDialog(TIME_PICKER_ID);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_poi, menu);
		return true;
	}

	public void onClickCreate(final View v) {
		
		//Get data information from the view
		EditText markerName = (EditText) findViewById(R.id.markerName);
		EditText markerDescription = (EditText) findViewById(R.id.markerDescription);
		
		final String mName = markerName.getText().toString().trim();
		final String mDescription = markerDescription.getText().toString().trim();
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		//user has to be connected to create a new marker
		if(currentUser == null) {
			Intent intent = new Intent(this, ConnectionActivity.class);
			startActivity(intent);
		} else {
			//marker must have a name
			if(markerName == null || markerName.length() < 1) {
				Toast.makeText(getApplicationContext(), "Marker name can't be empty", Toast.LENGTH_LONG).show();
			} else {
				//Look for the group
				ParseQuery<Group> findGroupName = ParseQuery.getQuery(Group.class);
				findGroupName.whereEqualTo("name", mGroupName);
				try {
					List<Group> g = findGroupName.find();

					if(g == null) {
						Toast.makeText(getApplicationContext(), "We can't find this group", Toast.LENGTH_LONG).show();
					} else {
						//We find a group with this name
						selectedGroup = g.get(0);	
					}
				} catch(ParseException e) {
					Toast.makeText(getApplicationContext(), "Was unable to execue : " + e, Toast.LENGTH_LONG).show();
				}
				
				ParseQuery<POI> poiQuery = ParseQuery.getQuery(POI.class);
				poiQuery.whereEqualTo(POI.NAME, mName);
				poiQuery.whereEqualTo(POI.GROUP, selectedGroup.getObjectId());
				poiQuery.countInBackground(new CountCallback() {
					
					@Override
					public void done(int c, ParseException err) {
						// If the marker doesn't already exist
						if(c == 0) {
							//Create the element
							POI p = new POI();
							p.setName(mName);
							p.setDescription(mDescription);
							p.setCreator(ParseUser.getCurrentUser());
							p.setGPSLocation(new ParseGeoPoint(mLocation.latitude, mLocation.longitude));
							p.setGroup(selectedGroup);
							
							//Try to save data in database
							try {
								p.save();
								Toast.makeText(getApplicationContext(), "Event created", Toast.LENGTH_LONG).show();		
							} catch(ParseException e) {
								Toast.makeText(getApplicationContext(), "Event could not be created", Toast.LENGTH_LONG).show();
							}
							finish();
						}
					}
				});
				
				if(selectedGroup != null) {
					ParseQuery<com.summerschool.friendfinderapplication.models.Event> eventQuery = ParseQuery.getQuery(com.summerschool.friendfinderapplication.models.Event.class);
					eventQuery.whereEqualTo(com.summerschool.friendfinderapplication.models.Event.TITLE, mName);
					eventQuery.countInBackground(new CountCallback() {
						
						@Override
						public void done(int c, ParseException err) {
							// If the marker doesn't already exist
							if(c == 0) {
								//Create the element
								com.summerschool.friendfinderapplication.models.Event e = new com.summerschool.friendfinderapplication.models.Event();
								e.setTitle(mName);
								e.setDescription(mDescription);
								e.setOwner(ParseUser.getCurrentUser());
								e.setLocation(new ParseGeoPoint(mLocation.latitude, mLocation.longitude));
								e.setGroup(selectedGroup);
								
								//Try to save data in database
								try {
									e.save();
									Toast.makeText(getApplicationContext(), "Every thing looks like fine to create an event !", Toast.LENGTH_LONG).show();		
								} catch(ParseException ex) {
									Toast.makeText(getApplicationContext(), "Was unable to save", Toast.LENGTH_LONG).show();
								}
							} else {
								Toast.makeText(getApplicationContext(), "Was unable to save", Toast.LENGTH_LONG).show();
							}
							
						}
					});
				}
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
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_PICKER_ID:
             
            // open datepicker dialog. 
            // set date picker for current date 
            // add pickerListener listner to date picker
            return new DatePickerDialog(this, pickerListener, year, month,day);
        case TIME_PICKER_ID:
        	return new TimePickerDialog(this, timePickerListener, hour, minute, morning);
        }
        return null;
    }
	
	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		 
		// when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                int selectedMonth, int selectedDay) {
             
            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
 
            // Show selected date 
        OutputDate.setText(new StringBuilder().append(month + 1)
                .append("-").append(day).append("-").append(year)
                .append(" "));
 
       }
    };
    
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int selectedMinute) {
			// TODO Auto-generated method stub
			hour = hourOfDay;
			minute = selectedMinute;
			
			OutputTime.setText(hour + ":" + minute);
			
		}
    };
}

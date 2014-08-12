package com.summerschool.friendfinderapplication.activities;

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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewEventActivity extends Activity {
	
	private final static String LOGTAG = "NEW_EVENT";
	
	public static final String EXTRA_MARKER_LATITUDE = "LATITUDE";
	public static final String EXTRA_MARKER_LONGITUDE = "LONGITUDE";
	public static final String EXTRA_GROUPNAME = "GROUPNAME";

	private LatLng mLocation;
	private String mGroupName;
	private Group mCurrentGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_poi);	// TODO change to activity_new_event as soon as the file is created
		
		Intent i = getIntent();
		if (i.hasExtra(EXTRA_MARKER_LATITUDE) && i.hasExtra(EXTRA_MARKER_LONGITUDE) && i.hasExtra(EXTRA_GROUPNAME)) {
			mGroupName = i.getStringExtra(EXTRA_GROUPNAME);
			mLocation = new LatLng(i.getDoubleExtra(EXTRA_MARKER_LATITUDE, 0.0), i.getDoubleExtra(EXTRA_MARKER_LONGITUDE, 0.0));
		} else {
			Toast.makeText(getApplicationContext(), "Invalid data provided", Toast.LENGTH_LONG).show();
			Log.e(LOGTAG, "Invalid coordinates or groupname");
			finish();
		}
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
		
		final String mName = markerName.getText().toString().trim();
		final String mDescription = markerDescription.getText().toString().trim();

		//Check whether all necessary information has been added
		if(mName.length() < 1) {
			Log.e(LOGTAG, "Event name is empty");
			Toast.makeText(getApplicationContext(), "Please enter event name", Toast.LENGTH_LONG).show();
		} else if (mDescription.length() < 1) {
			Log.e(LOGTAG, "Event description is empty");
			Toast.makeText(getApplicationContext(), "Please enter event decription", Toast.LENGTH_LONG).show();
		} else {
			
			//Look for the group
			ParseQuery<Group> findGroupName = ParseQuery.getQuery(Group.class);
			findGroupName.whereEqualTo("name", mGroupName);
			try {
				List<Group> g = findGroupName.find();
				mCurrentGroup = g.get(0);	
			} catch(ParseException e) {
				Log.e(LOGTAG, "Group not found");
			}
			
//				if (mCurrentGroup != null) {
//				
//				ParseQuery<POI> poiQuery = ParseQuery.getQuery(POI.class);
//				poiQuery.whereEqualTo(POI.NAME, mName);
//				poiQuery.whereEqualTo(POI.GROUP, mCurrentGroup.getObjectId());
//				poiQuery.countInBackground(new CountCallback() {
//					
//					@Override
//					public void done(int c, ParseException err) {
//						// If the marker doesn't already exist
//						if(c == 0) {
//							//Create the element
//							POI p = new POI();
//							p.setName(mName);
//							p.setDescription(mDescription);
//							p.setCreator(ParseUser.getCurrentUser());
//							p.setGPSLocation(new ParseGeoPoint(mLocation.latitude, mLocation.longitude));
//							p.setGroup(mCurrentGroup);
//							
//							//Try to save data in database
//							try {
//								p.save();
//								Toast.makeText(getApplicationContext(), "Event created", Toast.LENGTH_LONG).show();		
//							} catch(ParseException e) {
//								Toast.makeText(getApplicationContext(), "Event could not be created", Toast.LENGTH_LONG).show();
//							}
//							finish();
//						}
//					}
//				});
			
			if(mCurrentGroup != null) {
				ParseQuery<com.summerschool.friendfinderapplication.models.Event> eventQuery = ParseQuery.getQuery(com.summerschool.friendfinderapplication.models.Event.class);
				eventQuery.whereEqualTo(Event.TITLE, mName);
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

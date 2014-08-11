package com.summerschool.friendfinderapplication.activities;

import java.util.List;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.models.Group;
import com.summerschool.friendfinderapplication.models.POI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewMarkerActivity extends Activity {
	
	public static final String EXTRA_MARKER_LATITUDE = "LATITUDE";
	public static final String EXTRA_MARKER_LONGITUDE = "LONGITUDE";

	private ParseGeoPoint gpsLocation;
=======
	public static final String EXTRA_GROUPNAME = "GROUPNAME";

	private Group selectedGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_marker);
		
		Intent i = getIntent();
		gpsLocation = new ParseGeoPoint(new Double(i.getDoubleExtra(EXTRA_MARKER_LATITUDE, 0.0)), new Double(i.getDoubleExtra(EXTRA_MARKER_LONGITUDE, 0.0)));
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_marker, menu);
		return true;
	}

	public void onClickCreate(final View v)
	{
		//Get data information from the view
		EditText markerName = (EditText) findViewById(R.id.markerName);
		EditText markerDescription = (EditText) findViewById(R.id.markerDescription);
		EditText markerGroupName = (EditText) findViewById(R.id.groupName);
		markerGroupName.setText(getIntent().getStringExtra(EXTRA_GROUPNAME));
		
		final String mName = markerName.getText().toString().trim();
		final String mDescription = markerDescription.getText().toString().trim();
		final String mGroupName = markerGroupName.getText().toString().trim();
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		//user has to be connected to create a new marker
		if(currentUser == null)
		{
			Intent intent = new Intent(this, ConnectionActivity.class);
			startActivity(intent);
		}
		else
		{
			//marker must have a name
			if(markerName == null || markerName.length() < 1)
			{
				Toast.makeText(getApplicationContext(), "Marker name can't be empty", Toast.LENGTH_LONG).show();
			}
			else
			{
				//Look for the group
				ParseQuery<Group> findGroupName = ParseQuery.getQuery(Group.class);
				findGroupName.whereEqualTo("name", mGroupName);
				try
				{
					List<Group> g = findGroupName.find();

					if(g == null)
					{
						Toast.makeText(getApplicationContext(), "We can't find this group", Toast.LENGTH_LONG).show();
					}
					else
					{
						//We find a group with this name
						selectedGroup = g.get(0);	
					}
				}
				catch(ParseException e)
				{
					Toast.makeText(getApplicationContext(), "Was unable to execue : " + e, Toast.LENGTH_LONG).show();
				}
				
				if(selectedGroup != null)
				{
				
					ParseQuery<POI> poiQuery = ParseQuery.getQuery(POI.class);
					poiQuery.whereEqualTo(POI.NAME, mName);
					poiQuery.whereEqualTo(POI.GROUP, selectedGroup.getObjectId());
					poiQuery.countInBackground(new CountCallback() {
						
						@Override
						public void done(int c, ParseException err) {
							// If the marker doesn't already exist
							if(c == 0)
							{
								//Create the element
								POI p = new POI();
								p.setName(mName);
								p.setDescription(mDescription);
								p.setCreator(ParseUser.getCurrentUser());
								p.setGPSLocation(gpsLocation);
								p.setGroup(selectedGroup);
								
								//Try to save data in database
								try
								{
									p.save();
									Toast.makeText(getApplicationContext(), "Every thing looks like fine !", Toast.LENGTH_LONG).show();		
								}
								catch(ParseException e)
								{
									Toast.makeText(getApplicationContext(), "Was unable to save", Toast.LENGTH_LONG).show();
								}
							}
							else
							{
								
							}
						}
					});
				}
				else
				{
					Toast.makeText(getApplicationContext(), "We can't find the group", Toast.LENGTH_LONG).show();
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
}

package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.models.GroupMember;

public class MainActivity extends Activity {

	protected Button mMyEventButton;
	protected Button mMyPOIButton;
	protected Button mGroupsButton;
	protected Button mParameterButton;
	protected Switch mLocationSwitch;
	
	public void onClickGroupsButton(final View v) {
		//TODO
	}
	public void onClickMyEventButton(final View v) {
		//TODO
	}
	public void onClickMyPOIButton(final View v) {
		//TODO
	}
	public void onClickLocationSwitch(final View v) {
		//TODO
	}
	public void onClickParameterButton(final View v) {
		//TODO
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.i("LOCATION", "map loaded");
		GoogleMap mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		
//		Intent call = getIntent();
//		int numberOfUsers = call.getIntExtra("NUMBER_OF_USERS", 0);
//		ArrayList<String> users = new ArrayList<String>();
//		for (int i = 0; i < numberOfUsers; i++) {
//			users.add(call.getStringExtra("USER_IN_GROUP" + i));
//			Log.i("DISPLAY", call.getStringExtra("USER_IN_GROUP" + i));
//		}
		
		Intent i = getIntent();
		String groupName = i.getStringExtra("GROUPNAME");
		
		Log.i("LOCATION", groupName);
		
		ParseQuery<GroupMember> members =
				ParseQuery.getQuery(GroupMember.class).whereEqualTo("Group", groupName);
		members.include("Member");
	
//		ArrayList<ParseUser> usersInGroup = new ArrayList<ParseUser>();
		
		try {
			List<GroupMember> users = members.find();
			Log.i("LOCATION", "found " + users.size() + " members");
			for (GroupMember u : users) {
				ParseUser user = u.getParseUser("Member");
				if (user.getParseGeoPoint("location") != null) {
//					usersInGroup.add(user);
					ParseGeoPoint pos = user.getParseGeoPoint("location");
					
					mMap.addMarker(new MarkerOptions()
						.position(new LatLng(pos.getLatitude(), pos.getLatitude()))
						.title(user.getUsername()));

					Log.i("LOCATION", user.getString("username") + " is at " +
						user.getParseGeoPoint("location").getLatitude() + " " +
						user.getParseGeoPoint("location").getLongitude());
					
				} else {
					Log.i("LOCATION", user.getString("username") + " has no location");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

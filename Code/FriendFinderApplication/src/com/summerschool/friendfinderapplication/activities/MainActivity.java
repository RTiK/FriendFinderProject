package com.summerschool.friendfinderapplication.activities;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.models.Group;
import com.summerschool.friendfinderapplication.models.GroupMember;

public class MainActivity extends Activity {

	protected Button mMyEventButton;
	protected Button mMyPOIButton;
	protected Button mGroupsButton;
	protected Button mParameterButton;
	protected Switch mLocationSwitch;

	GoogleMap mMap;

	public void onClickGroupsButton(final View v) {
		// intent to main activity
		Intent intent = new Intent(MainActivity.this, GroupListActivity.class);
		startActivity(intent);
		finish();
	}

	public void onClickMyEventButton(final View v) {
		// TODO
	}

	public void onClickMyPOIButton(final View v) {
		// TODO
	}

	public void onClickLocationSwitch(final View v) {
		// TODO
	}

	public void onClickParameterButton(final View v) {
		// TODO
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.i("LOCATION", "map loaded");
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		// Intent call = getIntent();
		// int numberOfUsers = call.getIntExtra("NUMBER_OF_USERS", 0);
		// ArrayList<String> users = new ArrayList<String>();
		// for (int i = 0; i < numberOfUsers; i++) {
		// users.add(call.getStringExtra("USER_IN_GROUP" + i));
		// Log.i("DISPLAY", call.getStringExtra("USER_IN_GROUP" + i));
		// }

		Intent i = getIntent();
		String groupName = i.getStringExtra("GROUPNAME");
		
		showMembersOnMap(getUsersOfGroup(groupName));
		
	}

	private List<ParseUser> getUsersOfGroup(String groupName) {
		final List<ParseUser> groupMembers = new LinkedList<ParseUser>();
		if (groupName != null && !groupName.equals("")) {
			Log.i("LOCATION", groupName);
			Toast.makeText(MainActivity.this, "Map for Group " + groupName, Toast.LENGTH_SHORT).show();
			
			ParseQuery<Group> groupQuery = ParseQuery.getQuery(Group.class);
			groupQuery.whereEqualTo("name", groupName);
			groupQuery.findInBackground(new FindCallback<Group>() {
				@Override
				public void done(List<Group> group, ParseException error) {
					if(group != null && group.size() == 1) {
						Group thisGroup = group.get(0);
						Log.i("LOCATION","Found group " + thisGroup.getName());
						
						ParseQuery<GroupMember> members = ParseQuery.getQuery(GroupMember.class);
						members.whereEqualTo("Group", thisGroup);
						members.include("Member");
						members.findInBackground(new FindCallback<GroupMember>() {
							@Override
							public void done(List<GroupMember> users, ParseException error) {
								Log.i("LOCATION", "found " + users.size() + " members");
								for (GroupMember u : users) {
									ParseUser user = u.getParseUser("Member");
									groupMembers.add(user);
								}
							}
						});
					} else {
						Log.i("Error","Group didn't exist or is double");
					}
				}
			});
		}
		return groupMembers;
	}

	//Only 1 Location
	private void showMembersOnMap(List<ParseUser> groupMembers) {
		for (ParseUser user : groupMembers) {
			if (user.getParseGeoPoint("location") != null) {
				ParseGeoPoint pos = user.getParseGeoPoint("location");
				
				mMap.addMarker(new MarkerOptions().position(new LatLng(pos.getLatitude(), pos.getLatitude())).title(
						user.getUsername()));
				
				Log.i("LOCATION", user.getString("username") + " is at "
						+ user.getParseGeoPoint("location").getLatitude() + " "
						+ user.getParseGeoPoint("location").getLongitude());
			} else {
				Log.i("LOCATION", user.getString("username") + " has no location");
			}
		}
	}
	
	
}

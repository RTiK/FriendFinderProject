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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.controller.MyMarker;
import com.summerschool.friendfinderapplication.models.Group;
import com.summerschool.friendfinderapplication.models.GroupMember;

public class MapActivity extends Activity {

	protected Button mMyEventButton;
	protected Button mMyPOIButton;
	protected Button mGroupsButton;
	protected Button mParameterButton;
	protected Switch mLocationSwitch;

	GoogleMap mMap;

	public void onClickGroupsButton(final View v) {
		// intent to main activity
		Intent intent = new Intent(MapActivity.this, GroupListActivity.class);
		startActivity(intent);
		finish();
	}

	public void onClickMyEventButton(final View v) {
		// TODO
		Log.i("button","Clicked");
		Intent intent = new Intent(MapActivity.this, MyEventListActivity.class);
		Log.i("button","instantiation of Intent");
		startActivity(intent);
		finish();
	}

	public void onClickMyPOIButton(final View v) {
		// TODO
		Log.i("button","Clicked");
		Intent intent = new Intent(MapActivity.this, MyPOIListActivity.class);
		Log.i("button","instantiation of Intent");
		startActivity(intent);
		Log.i("button","start !");
		finish();
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
		setContentView(R.layout.activity_map);

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

		getUsersOfGroup(groupName);
	}

	private List<ParseUser> getUsersOfGroup(String groupName) {
		final List<ParseUser> groupMembers = new LinkedList<ParseUser>();
		if (groupName != null && !groupName.equals("")) {
			Log.i("LOCATION", groupName);
			Toast.makeText(MapActivity.this, "Map for Group " + groupName, Toast.LENGTH_SHORT).show();

			ParseQuery<Group> groupQuery = ParseQuery.getQuery(Group.class);
			groupQuery.whereEqualTo("name", groupName);
			groupQuery.findInBackground(new FindCallback<Group>() {
				@Override
				public void done(List<Group> group, ParseException error) {
					if (group != null && group.size() == 1) {
						Group thisGroup = group.get(0);
						Log.i("LOCATION", "Found group " + thisGroup.getName());

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
							
								//show on map
								showMembersOnMap(groupMembers);
							}
						});
					} else {
						Log.i("Error", "Group didn't exist or is double");
					}
				}
			});
		}
		return groupMembers;
	}

	// Only 1 Location
	private void showMembersOnMap(List<ParseUser> groupMembers) {
		Log.i("LOCATION", "call showMembersOnMap groupMembers size: " + groupMembers.size());
		for (ParseUser user : groupMembers) {
			if (user.getParseGeoPoint("location") != null) {
				ParseGeoPoint pos = user.getParseGeoPoint("location");
				MarkerOptions mo = new MarkerOptions();
				mo.position(new LatLng(pos.getLatitude(), pos.getLongitude()));
				mo.title(user.getUsername());
				mo.visible(true);
				if (user.getUsername().equals(ParseUser.getCurrentUser().getUsername())) {
					mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
					
					//set Camera to my own position
					Log.i("LOCATION","move to position " + pos.getLatitude() + " " + pos.getLongitude());
					CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(pos.getLatitude(), pos.getLongitude()),10);
					mMap.moveCamera(cu);
					
				} else {
					mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
				}
				mMap.addMarker(mo).setVisible(true);
				Log.i("LOCATION", user.getString("username") + " is at "
						+ user.getParseGeoPoint("location").getLatitude() + " "
						+ user.getParseGeoPoint("location").getLongitude());
			} else {
				Log.i("LOCATION", user.getString("username") + " has no location");
			}
		}
	}

	public List<MyMarker> createMarkerFromMemberData(List<ParseUser> members) {
		List<MyMarker> markers = new LinkedList<MyMarker>();
		for (ParseUser m : members) {
			markers.add(new MyMarker(m.getUsername(), "icon1", m.getParseGeoPoint("location").getLatitude(), m
					.getParseGeoPoint("location").getLongitude()));
		}
		return markers;
	}
}

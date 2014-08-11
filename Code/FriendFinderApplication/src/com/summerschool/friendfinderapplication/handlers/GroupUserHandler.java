package com.summerschool.friendfinderapplication.handlers;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.models.Group;
import com.summerschool.friendfinderapplication.models.GroupMember;

public class GroupUserHandler {
	
	private GoogleMap mMap;
	private String mGroupName;
	private LinkedList<Marker> mMarkers = new LinkedList<Marker>();

	public GroupUserHandler(GoogleMap map, String groupName) {
		mMap = map;
		mGroupName = groupName;
		getUsersOfGroup();
	}
	
	public void showActiveUsers() {
		Log.i(GroupUserHandler.class.getName(), "Displaying " + mMarkers.size() + " users");
		for (Marker marker : mMarkers)
			marker.setVisible(true);
	}
	
	public void removeActiveUsers() {
		Log.i(GroupUserHandler.class.getName(), "Removing " + mMarkers.size() + " users");
		for (Marker marker : mMarkers)
			marker.setVisible(false);
	}
	
	private void getUsersOfGroup() {
		if (mGroupName != null && !mGroupName.equals("")) {
			Log.i("LOCATION", mGroupName);
			
			ParseQuery<Group> groupQuery = ParseQuery.getQuery(Group.class);
			groupQuery.whereEqualTo("name", mGroupName);
			groupQuery.findInBackground(new FindCallback<Group>() {
				
				@Override
				public void done(List<Group> groups, ParseException error) {
					Log.i("LOCATION", "groups found " + groups.size());
					if (groups != null && groups.size() == 1) {
						Group thisGroup = groups.get(0);
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
									
									if (user.getParseGeoPoint("location") != null) {
										ParseGeoPoint pos = user.getParseGeoPoint("location");
										MarkerOptions mo = new MarkerOptions()
											.position(new LatLng(pos.getLatitude(), pos.getLongitude()))
											.title(user.getUsername()).visible(true);
										mMarkers.add(mMap.addMarker(mo));
									}

								}
							}
						});
					} else {
						Log.i("Error", "Group didn't exist or is double");
					}
				}
			});
		}
	}
}

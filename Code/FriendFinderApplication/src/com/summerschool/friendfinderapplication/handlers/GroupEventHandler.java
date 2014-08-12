package com.summerschool.friendfinderapplication.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.summerschool.friendfinderapplication.models.Event;
import com.summerschool.friendfinderapplication.models.Group;

public class GroupEventHandler {
	
	private final static String LOGTAG = "GROUP_POI_HANDLER";
	
	private GoogleMap mMap;
	private String mGroupName;
	private HashMap<Marker, String> mMarkers = new HashMap<Marker, String>();

	public GroupEventHandler(GoogleMap map, String groupName) {
		mMap = map;
		mGroupName = groupName;
		getEventsOfGroup();
	}
	
	public HashMap<Marker, String> getMarkers() {
		return mMarkers;
	}
	
	public void showEvents() {
		Log.i(GroupEventHandler.class.getName(), "Displaying " + mMarkers.size() + " Events");
		for (Map.Entry<Marker, String> marker : mMarkers.entrySet())
			marker.getKey().setVisible(true);
	}
	
	public void removeEvents() {
		Log.i(GroupEventHandler.class.getName(), "Removing " + mMarkers.size() + " Events");
		for (Map.Entry<Marker, String> marker : mMarkers.entrySet())
			marker.getKey().setVisible(false);
	}
	
	private void getEventsOfGroup() {
		if (mGroupName != null && !mGroupName.equals("")) {
			Log.i(LOGTAG, mGroupName);
			
			ParseQuery<Group> groupQuery = ParseQuery.getQuery(Group.class);
			groupQuery.whereEqualTo("name", mGroupName);
			groupQuery.findInBackground(new FindCallback<Group>() {
				@Override
				public void done(List<Group> groups, ParseException error) {
					Log.i(LOGTAG, "Groups found " + groups.size());
					if (groups != null && groups.size() == 1) {
						Group thisGroup = groups.get(0);
						Log.i(LOGTAG, "Found group " + thisGroup.getName());
						ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
						query.whereEqualTo("group", thisGroup);
						query.findInBackground(new FindCallback<Event>() {
							
							@Override
							public void done(List<Event> events, ParseException error) {
								Log.i(LOGTAG, "Found " + events.size() + " Events");
								for (Event e : events) {
									LatLng location = new LatLng(e.getLocation().getLatitude(), e.getLocation().getLongitude());
									if (location != null) {
										MarkerOptions mo = new MarkerOptions().
												position(location).
												title(e.getTitle()).
												icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
										mMarkers.put(mMap.addMarker(mo), e.getObjectId());
									}

								}
							}
						});
					} else {
						Log.i(LOGTAG, "Group didn't exist or is double");
					}
				}
			});
		}
	}
}

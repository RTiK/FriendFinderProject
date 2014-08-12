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
import com.summerschool.friendfinderapplication.models.Group;
import com.summerschool.friendfinderapplication.models.POI;

public class GroupPOIHandler {
	
	private final static String LOGTAG = "GROUP_POI_HANDLER";
	
	private GoogleMap mMap;
	private String mGroupName;
	private HashMap<Marker, String> mMarkers = new HashMap<Marker, String>();

	public GroupPOIHandler(GoogleMap map, String groupName) {
		mMap = map;
		mGroupName = groupName;
		getPOIsOfGroup();
	}
	
	public HashMap<Marker, String> getMarkers() {
		return mMarkers;
	}
	
	public void showPOIs() {
		Log.i(GroupPOIHandler.class.getName(), "Displaying " + mMarkers.size() + " POIs");
		for (Map.Entry<Marker, String> marker : mMarkers.entrySet())
			marker.getKey().setVisible(true);
	}
	
	public void removePOIs() {
		Log.i(GroupPOIHandler.class.getName(), "Removing " + mMarkers.size() + " POIs");
		for (Map.Entry<Marker, String> marker : mMarkers.entrySet())
			marker.getKey().setVisible(false);
	}
	
	private void getPOIsOfGroup() {
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
						ParseQuery<POI> query = ParseQuery.getQuery(POI.class);
						query.whereEqualTo("group", thisGroup);
						query.findInBackground(new FindCallback<POI>() {
							
							@Override
							public void done(List<POI> pois, ParseException error) {
								Log.i(LOGTAG, "Found " + pois.size() + " POIs");
								for (POI p : pois) {
									LatLng location = new LatLng(p.getLocation().getLatitude(), p.getLocation().getLongitude());
									if (location != null) {
										MarkerOptions mo = new MarkerOptions().
												position(location).
												title(p.getName()).
												icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
										mMarkers.put(mMap.addMarker(mo), p.getObjectId());
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

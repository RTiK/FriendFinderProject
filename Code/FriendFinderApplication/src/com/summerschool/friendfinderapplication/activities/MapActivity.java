package com.summerschool.friendfinderapplication.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.handlers.GroupUserHandler;


public class MapActivity extends Activity {
	
	private static final String LOGTAG = "MAP_ACTIVITY";
	
	public static final String EXTRA_EVENTS = "EVENTS_ENABLED";
	public static final String EXTRA_USERS = "USERS_ENABLED";
	public static final String EXTRA_POIS = "POIS_ENABLED";
	public static final String EXTRA_GROUPNAME = "GROUPNAME";
	public static final String EXTRA_FOCUS_LATITUDE = "FOCUS_LATITUDE";
	public static final String EXTRA_FOCUS_LONGITUDE = "FOCUS_LONGITUDE";
	public static final String EXTRA_FOCUS_ZOOM = "FOCUS_ZOOM";

	protected Button mMyEventButton;
	protected Button mMyPOIButton;
	protected Button mGroupsButton;
	protected Button mParameterButton;
	protected Switch mLocationSwitch;

	private GoogleMap mMap;
	private ToggleButton mToggleUsers;
	private ToggleButton mToggleEvents;
	private ToggleButton mTogglePOIs;
	private GroupUserHandler mGroupUserHandler;
	
	private static Marker mNewMarker;
	
	private void initToggles() {
		mToggleEvents = (ToggleButton) findViewById(R.id.mapToggleEvents);
		mToggleEvents.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.i("MAP", "Events button pressed");
				// TODO Auto-generated method stub
				
			}
		});
		mToggleUsers = (ToggleButton) findViewById(R.id.mapToggleUsers);
		mToggleUsers.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.i("MAP", "Users button pressed");
				if (isChecked)
					mGroupUserHandler.showActiveUsers();
				else
					mGroupUserHandler.removeActiveUsers();
			}
		});
		mTogglePOIs = (ToggleButton) findViewById(R.id.mapTogglePOIs);
		mTogglePOIs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.i("MAP", "POI button pressed");
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		initToggles();

		Log.i(LOGTAG, "map loaded");
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mNewMarker = mMap.addMarker(new MarkerOptions()
			.position(new LatLng(0.0, 0.0))
			.title("Tap to create a new POI")
			.draggable(true)
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
			.visible(false));
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				if (marker.equals(mNewMarker)) {
					Log.i(LOGTAG, "new marker info window tapped");
					Intent createNewMarker = new Intent(getApplicationContext(), NewMarkerActivity.class);
					createNewMarker.putExtra(NewMarkerActivity.EXTRA_MARKER_LATITUDE, marker.getPosition().latitude);
					createNewMarker.putExtra(NewMarkerActivity.EXTRA_MARKER_LONGITUDE, marker.getPosition().longitude);
					startActivity(createNewMarker);
					finish();
				}
			}
		});
		mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng point) {
				mNewMarker.setPosition(point);
				mNewMarker.setVisible(true);
				mNewMarker.showInfoWindow();
				Log.i(LOGTAG, mNewMarker + " created");
			}
		});
		
		Intent i = getIntent();
		mGroupUserHandler = new GroupUserHandler(mMap, i.getStringExtra(EXTRA_GROUPNAME));
		
		if (i.getBooleanExtra(EXTRA_USERS, true)) {
			Log.i("MAP", "Users enbled");
			mToggleUsers.setChecked(true);
		} else {
			Log.i("MAP", "Users disabled");
			mToggleUsers.setChecked(false);
		}
		
		if (i.getBooleanExtra(EXTRA_EVENTS, true)) {
			Log.i("MAP", "Events enbled");
			mToggleEvents.setChecked(true);
			Toast.makeText(getApplicationContext(), "Function not yet implemented", Toast.LENGTH_LONG).show();
			// TODO
		} else {
			Log.i("MAP", "Events disabled");
			mToggleEvents.setChecked(false);
		}
		
		if (i.getBooleanExtra(EXTRA_POIS, true)) {
			Log.i("MAP", "POIs enbled");
			mTogglePOIs.setChecked(true);
			Toast.makeText(getApplicationContext(), "Function not yet implemented", Toast.LENGTH_LONG).show();
			// TODO
		} else {
			Log.i("MAP", "POIs disabled");
			mTogglePOIs.setChecked(false);
		}
		
		// Determine zoom location
		LatLng focus = null;
		if (i.hasExtra(EXTRA_FOCUS_LATITUDE) && i.hasExtra(EXTRA_FOCUS_LONGITUDE)) {
			Log.i(LOGTAG, "Zoom position provided by intent");
			focus = new LatLng(
					i.getFloatExtra(EXTRA_FOCUS_LATITUDE, 0),
					i.getFloatExtra(EXTRA_FOCUS_LONGITUDE, 0));
		} else {
			Log.i(LOGTAG, "Zoom position not provided");
			ParseUser user = ParseUser.getCurrentUser();
			if (user.getParseGeoPoint("location") != null) {
				Log.i(LOGTAG, "Zoom in on user's position");
				focus = new LatLng(
						user.getParseGeoPoint("location").getLatitude(),
						user.getParseGeoPoint("location").getLongitude());
			} else {
				Log.i(LOGTAG, "User has no position, zoom in on Aalborg");
				focus = new LatLng(57.05, 9.916667);
			}
		}
		
		float zoom = i.getFloatExtra(EXTRA_FOCUS_ZOOM, 10);
		
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(focus, zoom));

	}
	
	// TODO we may not need these functions
	public void onClick_mapToggleUsers(View v) {
		Log.i("MAP", "Users toggle pressed");
	}
	
	public void onClick_mapToggleEvents(View v) {
		Log.i("MAP", "Events toggle pressed");
	}
	
	public void onClick_mapTogglePOIs(View v) {
		Log.i("MAP", "POIs toggle pressed");
	}
}

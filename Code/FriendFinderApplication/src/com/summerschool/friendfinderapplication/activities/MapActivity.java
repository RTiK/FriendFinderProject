package com.summerschool.friendfinderapplication.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.handlers.GroupEventHandler;
import com.summerschool.friendfinderapplication.handlers.GroupPOIHandler;
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
	private GroupPOIHandler mGroupPOIHandler;
	private GroupEventHandler mGroupEventHandler;
	private String mGroupName;
	private static Marker mNewMarker;
	
	private boolean mPrevPOIState;
	private boolean mPrevEventState;
	private boolean mPrevUsersState;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		Log.i(LOGTAG, "Create");

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		mNewMarker = mMap.addMarker(new MarkerOptions()
			.position(new LatLng(0.0, 0.0))
			.title("Tap to create a new marker")
			.draggable(true)
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
			.visible(false));

		initMap();
		
		//Disassemble intent
		Intent i = getIntent();
		mGroupName = i.getStringExtra(EXTRA_GROUPNAME);
		mGroupUserHandler = new GroupUserHandler(mMap, mGroupName);
		mGroupPOIHandler = new GroupPOIHandler(mMap, mGroupName);
		mGroupEventHandler = new GroupEventHandler(mMap, mGroupName);
		
		initToggles();
		setToggleButtons(i);
		setToggleListeners();
		setMapCamera(i);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mNewMarker.setVisible(false);
		
		// reload markers on activity resume
		mGroupEventHandler.removeEvents();
		mGroupPOIHandler.removePOIs();
		mGroupUserHandler.removeActiveUsers();
		
		mGroupEventHandler.getEventsOfGroup(mPrevEventState);
		mGroupPOIHandler.getPOIsOfGroup(mPrevPOIState);
		mGroupUserHandler.getUsersOfGroup(mPrevUsersState);
		
		Log.i(LOGTAG, "Markers reloaded");

	}
	
	private void initToggles() {
		mToggleEvents = (ToggleButton) findViewById(R.id.mapToggleEvents);
		mToggleUsers = (ToggleButton) findViewById(R.id.mapToggleUsers);
		mTogglePOIs = (ToggleButton) findViewById(R.id.mapTogglePOIs);
	}
	
	private void setToggleListeners() {
		mToggleEvents.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.i(LOGTAG, "Events button pressed");
				mPrevEventState = isChecked;
				if (isChecked)
					mGroupEventHandler.showEvents();
				else
					mGroupEventHandler.removeEvents();
			}
		});
		mToggleUsers.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.i(LOGTAG, "Users button pressed");
				mPrevUsersState = isChecked;
				if (isChecked)
					mGroupUserHandler.showActiveUsers();
				else
					mGroupUserHandler.removeActiveUsers();
			}
		});
		mTogglePOIs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.i(LOGTAG, "POI button pressed");
				mPrevPOIState = isChecked;
				if (isChecked)
					mGroupPOIHandler.showPOIs();
				else
					mGroupPOIHandler.removePOIs();
			}
		});
	}
	
	private void initMap() {
		mMap.setMyLocationEnabled(true);
		
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(final Marker marker) {
				if (marker.equals(mNewMarker)) {
					Log.i(LOGTAG, "new marker info window tapped");

					AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
					builder.setTitle("Select marker type");
					builder.setPositiveButton("POI", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent createNewMarker = new Intent(getApplicationContext(), NewPOIActivity.class);
							createNewMarker.putExtra(NewPOIActivity.EXTRA_GROUPNAME, mGroupName);
							createNewMarker.putExtra(NewPOIActivity.EXTRA_MARKER_LATITUDE, marker.getPosition().latitude);
							createNewMarker.putExtra(NewPOIActivity.EXTRA_MARKER_LONGITUDE, marker.getPosition().longitude);
							startActivity(createNewMarker);
						}
					});
					builder.setNegativeButton("Event", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent createNewMarker = new Intent(getApplicationContext(), NewEventActivity.class);
							createNewMarker.putExtra(NewPOIActivity.EXTRA_GROUPNAME, mGroupName);
							createNewMarker.putExtra(NewPOIActivity.EXTRA_MARKER_LATITUDE, marker.getPosition().latitude);
							createNewMarker.putExtra(NewPOIActivity.EXTRA_MARKER_LONGITUDE, marker.getPosition().longitude);
							startActivity(createNewMarker);
						}
					});
					AlertDialog dialog = builder.create();
					dialog.show();
					
				} else {
					if (mGroupPOIHandler.getMarkers().containsKey(marker)) {
						Log.i(LOGTAG, "POI tapped, display info");
						Intent goToPoiInfo = new Intent(getApplicationContext(), POIInfoActivity.class);
						goToPoiInfo.putExtra(POIInfoActivity.EXTRAS_GROUPNAME, mGroupName);
						goToPoiInfo.putExtra(POIInfoActivity.EXTRAS_MARKER_ID, mGroupPOIHandler.getMarkers().get(marker));
						startActivity(goToPoiInfo);
					} else if (mGroupEventHandler.getMarkers().containsKey(marker)) {
						Log.i(LOGTAG, "Event tapped, display info");
						Intent goToEventInfo = new Intent(getApplicationContext(), EventInfoActivity.class);
						goToEventInfo.putExtra(EventInfoActivity.EXTRAS_GROUPNAME, mGroupName);
						goToEventInfo.putExtra(EventInfoActivity.EXTRAS_MARKER_ID, mGroupEventHandler.getMarkers().get(marker));
						startActivity(goToEventInfo);
					}
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
		
		mMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng point) {
				mNewMarker.setVisible(false);
			}
		});
	}
	
	private void setToggleButtons(Intent intent) {
		if (intent.getBooleanExtra(EXTRA_USERS, true)) {
			Log.i(LOGTAG, "Users enbled");
			mToggleUsers.setChecked(true);
			mPrevUsersState = true;
		} else {
			Log.i(LOGTAG, "Users disabled");
			mToggleUsers.setChecked(false);
			mPrevUsersState = false;
		}
		
		if (intent.getBooleanExtra(EXTRA_EVENTS, true)) {
			Log.i(LOGTAG, "Events enbled");
			mToggleEvents.setChecked(true);
			mPrevEventState = true;
		} else {
			Log.i(LOGTAG, "Events disabled");
			mToggleEvents.setChecked(false);
			mPrevEventState = false;
		}
		
		if (intent.getBooleanExtra(EXTRA_POIS, true)) {
			Log.i(LOGTAG, "POIs enbled");
			mTogglePOIs.setChecked(true);
			mPrevPOIState = true;
		} else {
			Log.i(LOGTAG, "POIs disabled");
			mTogglePOIs.setChecked(false);
			mPrevPOIState = false;
		}
	}
	
	private void setMapCamera(Intent intent) {
		LatLng focus = null;
		if (intent.hasExtra(EXTRA_FOCUS_LATITUDE) && intent.hasExtra(EXTRA_FOCUS_LONGITUDE)) {
			Log.i(LOGTAG, "Zoom position provided by intent");
			focus = new LatLng(
					intent.getDoubleExtra(EXTRA_FOCUS_LATITUDE, 0),
					intent.getDoubleExtra(EXTRA_FOCUS_LONGITUDE, 0));
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
		
		float zoom = intent.getFloatExtra(EXTRA_FOCUS_ZOOM, 12);
		Log.i(LOGTAG,"zoom position is: " + focus.latitude + ", " + focus.longitude);
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(focus, zoom));
	}
}

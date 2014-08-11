package com.summerschool.friendfinderapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.controller.UserLocationListener;
import com.summerschool.friendfinderapplication.models.Event;
import com.summerschool.friendfinderapplication.models.EventMember;
import com.summerschool.friendfinderapplication.models.Group;
import com.summerschool.friendfinderapplication.models.GroupMember;
import com.summerschool.friendfinderapplication.models.POI;
import com.summerschool.friendfinderapplication.models.UserLikesPOI;

public class ConnectionActivity extends Activity {

	// Parse application keys
	private static final String PARSE_APPLICATION_ID = "rU3OkVyuuIgA17MsCPBgspurzhM00QOSxIaXvzsI";
	private static final String PARSE_CLIENT_KEY = "Vw4U1lSXshwY9Nia14KV1MpGxJht8S3Q9H1N7TVP";
	private final String PREF_NAME = "MySettings";
	private static final String ACTIVE_USER_ACCOUNT = "activeUserAccount";
	private static final String FIRST_RUN = "firstRun";
		
	protected ImageButton mConnButton;
	protected EditText mConnInput;	
	protected TextView mErrorField;

	public void onClickConnButton(final View v) {		
		TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		String password = "12345";
		final String simno = tMgr.getSimSerialNumber(); //password
		if(simno != null && !simno.equals("")) 
			password = simno;
		final String mUsername = mConnInput.getText().toString(); //Username
		ParseUser.logInInBackground(
				mUsername, 
				password, 
				new LogInCallback() {
					@Override
					public void done(ParseUser user, ParseException e) {
						if(user != null) {
							saveUser(mUsername);
							Intent intent = new Intent(ConnectionActivity.this, GroupListActivity.class);
							startActivity(intent);
							finish();
						} else {
							//Login failed
							switch(e.getCode()) {
							case ParseException.USERNAME_TAKEN:
								//mErrorField.setText("Sorry, this username has already been taken.");
								Toast.makeText(ConnectionActivity.this, "username taken", Toast.LENGTH_SHORT).show();
								break;
							default:
								if(mUsername.length() == 0) {
									Toast.makeText(ConnectionActivity.this, "username needed", Toast.LENGTH_SHORT).show();
									break;
								}
								Log.i("Login Failed", e.getLocalizedMessage().toString());								
								//create new user
								String password = "12345";
								if(simno != null && simno != "") password = simno;
								ParseUser newUser = new ParseUser();
								newUser.setUsername(mUsername);
								newUser.setPassword(password);
								newUser.signUpInBackground(new SignUpCallback() {
									@Override
									public void done(ParseException e) {
										if(e == null) {
											//make username persistance
											saveUser(mUsername);
											updateLocation(ParseUser.getCurrentUser());
											Intent intent = new Intent(ConnectionActivity.this, GroupListActivity.class);
											startActivity(intent);
											finish();
										} else {
											//sign-up didn't succeed ?!
											Log.i("Create Failed", e.getLocalizedMessage().toString());
											Toast.makeText(ConnectionActivity.this, "Epic fail: ", Toast.LENGTH_SHORT).show();
										}
									}
								});
							}	
							v.setEnabled(true);
						}
					}
				});
	}
	
	private void saveUser(String username) {
		SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		Editor editor = sp.edit();	
		editor.putString(ACTIVE_USER_ACCOUNT, username);
		editor.putBoolean(FIRST_RUN, false);
		editor.commit();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		
		ParseObject.registerSubclass(Group.class);
		ParseObject.registerSubclass(GroupMember.class);
		ParseObject.registerSubclass(Event.class);
		ParseObject.registerSubclass(EventMember.class);
		ParseObject.registerSubclass(POI.class);
		ParseObject.registerSubclass(UserLikesPOI.class);
		
		//Initialize Parse
		Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
		ParseAnalytics.trackAppOpened(getIntent());
		
		//Init fields
		mConnButton = (ImageButton) findViewById(R.id.connButton);
		mConnInput = (EditText) findViewById(R.id.connInput);
		mErrorField = (TextView) findViewById(R.id.errorField);
	}
	
	@Override
	protected void onStart() {
		super.onStart();		
		SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);		
		Boolean isFirstRun = sp.getBoolean(FIRST_RUN,true);
		if(!isFirstRun) {
			Log.i("UserName","/"+sp.getString(ACTIVE_USER_ACCOUNT, "")+"/");
			mConnInput.setText(sp.getString(ACTIVE_USER_ACCOUNT, "")); //set Text into box
			this.onClickConnButton(null); //call login method
		}
	}
	
	private void updateLocation(ParseUser user) {
		
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		UserLocationListener locationListener = new UserLocationListener(user);
		
		long timeRefresh = 5000; // refresh every 20 seconds
		
		Log.i("TEST", "location");
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeRefresh, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timeRefresh, 0, locationListener);

	}

}

package com.summerschool.friendfinderapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.summerschool.friendfinderapplication.R;

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
		final String simno = tMgr.getSimSerialNumber(); //password
		final String mUsername = mConnInput.getText().toString(); //Username
		ParseUser.logInInBackground(
				mUsername, 
				simno, 
				new LogInCallback() {					
					@Override
					public void done(ParseUser user, ParseException e) {
						if(user != null) {
							Intent intent = new Intent(ConnectionActivity.this, GroupListActivity.class);
							startActivity(intent);
							finish();
						} else {
							//Login failed
							switch(e.getCode()){
							case ParseException.USERNAME_TAKEN:
								//mErrorField.setText("Sorry, this username has already been taken.");
								Toast.makeText(ConnectionActivity.this, "username taken", Toast.LENGTH_SHORT).show();
								break;
							default:
								if(mUsername.length() == 0) {
									Toast.makeText(ConnectionActivity.this, "username needed", Toast.LENGTH_SHORT).show();
									break;
								}
								//create new user
								ParseUser newUser = new ParseUser();
								newUser.setUsername(mUsername);
								newUser.setPassword(simno);
								newUser.signUpInBackground(new SignUpCallback() {
									@Override
									public void done(ParseException e) {
										if(e == null) {
											//make username persistance
											saveUser(mUsername);											
											Intent intent = new Intent(ConnectionActivity.this, GroupListActivity.class);
											startActivity(intent);
											finish();
										} else {
											//sign-up didn't succeed ?!
											Toast.makeText(ConnectionActivity.this, "Epic fail", Toast.LENGTH_SHORT).show();
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
		SharedPreferences sp = 
				 getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		Editor editor = sp.edit();	
		editor.putString(ACTIVE_USER_ACCOUNT, username);
		editor.putBoolean(FIRST_RUN, false);
		editor.commit();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		
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
		SharedPreferences sp = 
				 getSharedPreferences(PREF_NAME, MODE_PRIVATE);		
		Boolean isFirstRun = sp.getBoolean(FIRST_RUN,true);
		if(!isFirstRun) {
			mConnInput.setText(sp.getString(ACTIVE_USER_ACCOUNT, "")); //set Text into box
			this.onClickConnButton(null); //call login method
		}
	}

}

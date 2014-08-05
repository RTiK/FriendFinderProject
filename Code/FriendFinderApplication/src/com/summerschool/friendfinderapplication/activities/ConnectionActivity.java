package com.summerschool.friendfinderapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;

public class ConnectionActivity extends Activity {

	// Parse application keys
	private static final String PARSE_APPLICATION_ID = "rU3OkVyuuIgA17MsCPBgspurzhM00QOSxIaXvzsI";
	private static final String PARSE_CLIENT_KEY = "Vw4U1lSXshwY9Nia14KV1MpGxJht8S3Q9H1N7TVP";
	
	protected Button mConnButton;
	protected TextView mConnInput;	
	protected TextView mErrorField;
	
	/**
	 * Tries to establish a connection with a valid username.
	 * @param v
	 */
	public void onClickConnButton(final View v) {
		v.setEnabled(false);
		
		ParseUser.logInInBackground(mConnInput.getText().toString(), "asdf", 
				new LogInCallback() {
					
					@Override
					public void done(ParseUser user, ParseException e) {
						if(user != null) {
							Intent intent = new Intent(ConnectionActivity.this, MainActivity.class);
							startActivity(intent);
							finish();
						} else {
							//Login failed
							switch(e.getCode()){
							case ParseException.USERNAME_TAKEN:
								mErrorField.setText("Sorry, this username has already been taken.");
								break;
							case ParseException.USERNAME_MISSING:
								mErrorField.setText("Sorry, you must supply a username to register.");
								break;
							default:
								mErrorField.setText(e.getLocalizedMessage());
								break;
							}	
							v.setEnabled(true);
						}
					}
				});
		
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		
		//Initialize Parse
		Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
		ParseAnalytics.trackAppOpened(getIntent());
		
		//Init fields
		//mConnButton = (Button) findViewById(R.id.connButton);
		//mConnInput = (Button) findViewById(R.id.connInput);
		//mErrorField = (TextView) findViewById(R.id.errorField);
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connection, menu);
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

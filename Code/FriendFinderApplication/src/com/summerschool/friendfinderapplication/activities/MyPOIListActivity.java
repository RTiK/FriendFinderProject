package com.summerschool.friendfinderapplication.activities;

import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.R.id;
import com.summerschool.friendfinderapplication.R.layout;
import com.summerschool.friendfinderapplication.R.menu;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyPOIListActivity extends Activity {

	protected Button mHomeButton;
	protected Button mMyEventButton;
	protected Button mMyPOIButton;
	protected Button mGroupsButton;
	
	public void onClickHome(final View v) {
		//intent to new Group activty		
		Intent intent = new Intent(MyPOIListActivity.this, MapActivity.class);
		startActivity(intent);
		finish();		
	}
	
	public void onClickGroupsButton(final View v) {
		// intent to main activity
		Intent intent = new Intent(MyPOIListActivity.this, GroupListActivity.class);
		startActivity(intent);
		finish();
	}

	public void onClickMyEventButton(final View v) {
		// TODO
		Log.i("button","Clicked");
		Intent intent = new Intent(MyPOIListActivity.this, MyEventListActivity.class);
		Log.i("button","instantiation of Intent");
		startActivity(intent);
		finish();
	}

	public void onClickMyPOIButton(final View v) {
		// TODO
		Log.i("button","Clicked");
		Intent intent = new Intent(MyPOIListActivity.this, MyPOIListActivity.class);
		Log.i("button","instantiation of Intent");
		startActivity(intent);
		Log.i("button","start !");
		finish();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_poi);
		
        // get action bar   
        ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
		//TextView Tv = (TextView)findViewById(R.id.helloView);
		//Tv.setText("MyPOI Avtivity");
		Log.i("here","Successfully opennned");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_poi, menu);
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

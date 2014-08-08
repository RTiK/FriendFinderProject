package com.summerschool.friendfinderapplication.activities;

import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.R.id;
import com.summerschool.friendfinderapplication.R.layout;
import com.summerschool.friendfinderapplication.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MyEventActivity extends Activity {

	protected Button mHomeButton;
	protected Button mMyEventButton;
	protected Button mMyPOIButton;
	protected Button mGroupsButton;
	
	public void onClickHome(final View v) {
		//intent to new Group activty		
		Intent intent = new Intent(MyEventActivity.this, MainActivity.class);
		startActivity(intent);
		finish();		
	}
	public void onClickGroupsButton(final View v) {
		// intent to main activity
		Intent intent = new Intent(MyEventActivity.this, GroupListActivity.class);
		startActivity(intent);
		finish();
	}

	public void onClickMyEventButton(final View v) {
		// TODO
		Log.i("button","Clicked");
		Intent intent = new Intent(MyEventActivity.this, MyEventActivity.class);
		Log.i("button","instantiation of Intent");
		startActivity(intent);
		finish();
	}

	public void onClickMyPOIButton(final View v) {
		// TODO
		Log.i("button","Clicked");
		Intent intent = new Intent(MyEventActivity.this, MyPOIActivity.class);
		Log.i("button","instantiation of Intent");
		startActivity(intent);
		Log.i("button","start !");
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_event, menu);
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

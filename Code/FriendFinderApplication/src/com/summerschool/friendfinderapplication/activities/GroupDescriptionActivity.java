package com.summerschool.friendfinderapplication.activities;

import com.summerschool.friendfinderapplication.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;

public class GroupDescriptionActivity extends Activity {


	protected Button mHomeButton;
	protected Button mMyEventButton;
	protected Button mMyPOIButton;
	protected Button mMemberListButton;
	protected Button mEventsButton;
	protected Button mMapButton;
	
	public void onClickHomeButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MainActivity.class);
		startActivity(intent);
	}
	public void onClickMyEventButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MyEventActivity.class);
		startActivity(intent);
	}
	public void onClickMyPOIButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MyPOIActivity.class);
		startActivity(intent);
	}
	public void onClickMemberListButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, GroupListActivity.class);
		startActivity(intent);
	}
	public void onClickEventsButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, EventActivity.class);
		startActivity(intent);
	}
	public void onClickMapButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MainActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_description);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_description, menu);
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

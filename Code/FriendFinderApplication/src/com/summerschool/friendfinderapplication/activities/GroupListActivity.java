package com.summerschool.friendfinderapplication.activities;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.summerschool.friendfinderapplication.R;

public class GroupListActivity extends Activity {

	protected Button mHomeButton;
	protected Button mMyEventButton;
	protected Button mMyPOIButton;
	protected Button mEditButton;
	protected Button mSearchButton;
	protected Button mAddButton;
	
	protected LinkedList<Switch> LocationSwitches;
	protected LinkedList<Button> MapButtons;
	protected LinkedList<Button> InfoButtons;
	
	public void onClickHomeButton(final View v) {
		//TODO
	}
	public void onClickMyEventButton(final View v) {
		//TODO
	}
	public void onClickMyPOIButton(final View v) {
		//TODO
	}
	public void onClickEditButton(final View v) {
		//TODO
	}
	public void onClickSearchButton(final View v) {
		//TODO
	}
	public void onClickAddButton(final View v) {
		//TODO
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_list, menu);
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

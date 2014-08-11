package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.controller.MyPOIListAdapter;
import com.summerschool.friendfinderapplication.models.POI;
import com.summerschool.friendfinderapplication.models.UserLikesPOI;

public class MyPOIListActivity extends Activity {

	protected Button mHomeButton;
	protected Button mMyEventButton;
	protected Button mMyPOIButton;
	protected Button mGroupsButton;
	
	private MyPOIListAdapter adapter;
	
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
		
		adapter = new MyPOIListAdapter(MyPOIListActivity.this, new ArrayList<POI>());
		updateEventList();
		populateEventList();
	}
	
	private void updateEventList() {
		Log.i("MyPOIList","updating my poi list");
		
		ParseQuery<UserLikesPOI> innerQuery = ParseQuery.getQuery(UserLikesPOI.class);
		innerQuery.whereEqualTo(UserLikesPOI.USER, ParseUser.getCurrentUser());
		
		ParseQuery<POI> query = ParseQuery.getQuery(POI.class);
		query.whereMatchesQuery(POI.USER_LIKES_POI, innerQuery);
		
		query.findInBackground(new FindCallback<POI>() {
			@Override
			public void done(List<POI> pois, ParseException error) {
				Log.i("MyEventList","found " + pois.size() + " events I attend.");
				adapter.clear();
				adapter.addAll(pois);
			}
		});
	}
	
	private void populateEventList() {
		ListView list = (ListView) findViewById(R.id.poiListView);
		list.setAdapter(adapter);
	}
	
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
	
	

}

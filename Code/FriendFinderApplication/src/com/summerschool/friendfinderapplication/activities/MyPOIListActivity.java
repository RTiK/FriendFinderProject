package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
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
	private static final String LOGTAG = "MyPOIList";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_poi);
		
        // get action bar   
        ActionBar actionBar = getActionBar();
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);

        adapter = new MyPOIListAdapter(MyPOIListActivity.this, new ArrayList<POI>());
        
		updateEventList();
		populateEventList();
	}
	
	private void updateEventList() {
		Log.i(LOGTAG,"updating my poi list");
		
		ParseQuery<UserLikesPOI> q = ParseQuery.getQuery(UserLikesPOI.class);
		q.whereEqualTo(UserLikesPOI.USER, ParseUser.getCurrentUser());
		q.include(UserLikesPOI.POI);
		q.include(POI.GROUP);
		List<POI> poiList= new ArrayList<POI>();
		try {
			for(UserLikesPOI poi : q.find()) {
				poiList.add(poi.getPOI());
			}
			Log.i(LOGTAG,"found " + poiList.size() + " pois I liked or created");
			adapter.clear();
			adapter.addAll(poiList);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		
//		ParseQuery<POI> query = ParseQuery.getQuery(POI.class);
//		query.whereMatchesQuery(POI.USER_LIKES_POI, innerQuery);
//		
//		query.include(POI.GROUP);
//		List<POI> pois;
//		try {
//			pois = query.find();
//			Log.i(LOGTAG,"found " + pois.size() + " pois I liked or created");
//			adapter.clear();
//			adapter.addAll(pois);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
	}
	
	private void populateEventList() {
		ListView list = (ListView) findViewById(R.id.poiListView);
		list.setAdapter(adapter);
	}
	
	public void onClickGroupsButton(final View v) {
		Intent intent = new Intent(MyPOIListActivity.this, GroupListActivity.class);
		startActivity(intent);
	}	
}

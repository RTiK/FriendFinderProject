package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.controller.FanListAdapter;
import com.summerschool.friendfinderapplication.controller.GroupEventAdapter;
import com.summerschool.friendfinderapplication.models.Event;
import com.summerschool.friendfinderapplication.models.POI;
import com.summerschool.friendfinderapplication.models.UserLikesPOI;

public class POIInfoActivity extends Activity {
	
	private final static String LOGTAG = "POIInfoActivity";
	public final static String EXTRAS_GROUPNAME = "GROUPNAME";
	public final static String EXTRAS_MARKER_ID = "MARKER_ID";
	
	private POI currPOI;
	private List<ParseUser> poiFans;
	private FanListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_info);
		Log.i(LOGTAG, "started");

		poiFans = new ArrayList<ParseUser>();
		//instantiation of adapter
		adapter = new FanListAdapter(POIInfoActivity.this,new ArrayList<ParseUser>());
		
		
		
		Intent i = getIntent();
		getCurrentPOI(i.getStringExtra(POIInfoActivity.EXTRAS_MARKER_ID));
		if(currPOI != null) {
			getCurrentFans();
			//update of the list of fans
			ListView fanlist = (ListView) findViewById(R.id.poiFansListView);
			fanlist.setAdapter(adapter);
			
			
			getActionBar().setTitle(currPOI.getName());
			
			TextView title = (TextView) findViewById(R.id.poi_title);
			TextView desc = (TextView) findViewById(R.id.poi_description);
			
			title.setText(currPOI.getName());
			desc.setText(currPOI.getDescription());
		} else {
			Toast.makeText(POIInfoActivity.this, "currPOI is null", Toast.LENGTH_SHORT).show();
			Log.i(LOGTAG,"currPOI is null");
		}
		
		//Buttons enabling/disabling in fonction of the user
		Button blike = (Button) findViewById(R.id.poiLike);
		Button bdislike = (Button) findViewById(R.id.poiDislike);
		Button bdelete = (Button) findViewById(R.id.poiDelete);
		
		if(isCreator()){
			Log.i(LOGTAG,"current user is the creator");
			blike.setEnabled(false);
			bdislike.setEnabled(false);
			bdelete.setEnabled(true);
			
			blike.setVisibility(View.GONE);
			bdislike.setVisibility(View.GONE);
			bdelete.setVisibility(View.VISIBLE);
			
		} else if(isFan()) {
			Log.i(LOGTAG,"current user is a fan");
			blike.setEnabled(false);
			bdislike.setEnabled(true);
			bdelete.setEnabled(false);
			
			blike.setVisibility(View.GONE);
			bdislike.setVisibility(View.VISIBLE);
			bdelete.setVisibility(View.GONE);			
		} else {
			Log.i(LOGTAG,"current user is not a fan and not the creator");
			blike.setEnabled(true);
			bdislike.setEnabled(false);
			bdelete.setEnabled(false);
			
			blike.setVisibility(View.VISIBLE);
			bdislike.setVisibility(View.GONE);
			bdelete.setVisibility(View.GONE);	        
		}  
	}
	
	private boolean isCreator() {
		if(currPOI != null)
			return currPOI.getCreator().getObjectId().equals(ParseUser.getCurrentUser().getObjectId());
		return false;
	}
	
	private boolean isFan() {
		if(poiFans != null) {
			Log.i(LOGTAG, "found current user in list");
			for(ParseUser u : poiFans) {
				if(u.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) return true;
			}
		}
		return false;
	}
	
	private void getCurrentFans() {
		ParseQuery<UserLikesPOI> q = ParseQuery.getQuery(UserLikesPOI.class);
		q.whereEqualTo(UserLikesPOI.POI, currPOI);
		q.include(UserLikesPOI.USER);
		try {
			poiFans.clear();
			for(UserLikesPOI ulp : q.find()) {
				poiFans.add(ulp.getUser());				
			}
			adapter.clear();
			adapter.addAll(poiFans);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	private void getCurrentPOI(final String poiObjID) {
		ParseQuery<POI> q1 = ParseQuery.getQuery(POI.class);
		q1.whereEqualTo("objectId", poiObjID);
		q1.include(POI.CREATOR);
		List<POI> pois;
		try {
			pois = q1.find();
			Log.i(LOGTAG, "found " + pois.size() + " POI's with the id " + poiObjID);
			if(pois.size() > 1) 
				Toast.makeText(POIInfoActivity.this, "multiple POI's with that name found??", Toast.LENGTH_SHORT).show();
			if(pois.size() > 0) 
				currPOI = pois.get(0);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
		
	public void likeCurrPOI(final View v) {
		UserLikesPOI elp = new UserLikesPOI();
		elp.put(UserLikesPOI.USER, ParseUser.getCurrentUser());
		elp.put(UserLikesPOI.POI, currPOI);
		try {
			elp.save();
			finish();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void dislikeCurrPOI(final View v) {
		ParseQuery<UserLikesPOI> query = ParseQuery.getQuery(UserLikesPOI.class);
		query.whereEqualTo(UserLikesPOI.POI, currPOI);
		query.whereEqualTo(UserLikesPOI.USER, ParseUser.getCurrentUser());
		try {
			UserLikesPOI.deleteAll((List) query.find());
			finish();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteCurrPOI(final View v) {
		try {
			Log.i(LOGTAG, "deleting poi " + currPOI.getName());
			currPOI.delete();
			//Intent i = new Intent(POIInfoActivity.this, MapActivity.class);
			//startActivity(i);		
//			finish();
			Toast.makeText(getApplicationContext(), currPOI.getName() + " deleted", Toast.LENGTH_LONG).show();
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		finish();
	}
	
}

package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.models.POI;
import com.summerschool.friendfinderapplication.models.UserLikesPOI;

public class POIInfoActivity extends Activity {
	
	private final static String LOGTAG = "POIInfoActivity";
	public final static String EXTRAS_GROUPNAME = "GROUPNAME";
	public final static String EXTRAS_MARKER_ID = "MARKER_ID";
	
	private POI currPOI;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_info);
		Log.i(LOGTAG, "started");

		Intent i = getIntent();
		getCurrentPOI(i.getStringExtra(POIInfoActivity.EXTRAS_MARKER_ID));
		
		
		if(currPOI != null) {
			getActionBar().setTitle(currPOI.getName());
			
			TextView title = (TextView) findViewById(R.id.poi_title);
			TextView desc = (TextView) findViewById(R.id.poi_description);
			
			title.setText(currPOI.getName());
			desc.setText(currPOI.getDescription());
		}
		
	}
	private void getCurrentPOI(final String poiObjID) {
		ParseQuery<POI> q1 = ParseQuery.getQuery(POI.class);
		q1.whereEqualTo("objectId", poiObjID);
		q1.findInBackground(new FindCallback<POI>() {
			@Override
			public void done(List<POI> pois, ParseException error) {
				Log.i(LOGTAG, "found " + pois.size() + " POI's with the id " + poiObjID);
				if(pois.size() > 1) {
					Toast.makeText(POIInfoActivity.this, "multiple POI's with that name found??", Toast.LENGTH_SHORT).show();
				}
				if(pois.size() > 0) {
					currPOI = pois.get(0);
				}
			}
		});
	}
	
	private List<ParseUser> getUsersWithPOIlikes(POI e) {
		List<ParseUser> users = new ArrayList<ParseUser>();
		if(e!=null) {
			ParseQuery<UserLikesPOI> query = ParseQuery.getQuery(UserLikesPOI.class);
			query.whereEqualTo(UserLikesPOI.POI, e);
			try {
				List<UserLikesPOI> poiMembers = query.find();
				for(UserLikesPOI em : poiMembers) {
					users.add(em.getUser());
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		return users;
	}
	
	public void likeCurrPOI(final View v) {
		//TODO
	}
	
	public void dislikeCurrPOI(final View v) {
		//TODO
	}
	
	public void deleteCurrPOI(final View v) {
		//TODO
	}
	
}

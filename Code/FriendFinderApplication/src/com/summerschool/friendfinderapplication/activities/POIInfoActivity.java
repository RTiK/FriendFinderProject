package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_info);
		
		//TODO getExtra from Intent about what POI this is
		//TODO need group and event identifier
		
		final String currentPOIName = "";
		
		ParseQuery<POI> q1 = ParseQuery.getQuery(POI.class);		
		q1.whereEqualTo(POI.NAME, currentPOIName);
		q1.findInBackground(new FindCallback<POI>() {
			@Override
			public void done(List<POI> pois, ParseException error) {
				Log.i(LOGTAG, "found " + pois.size() + " POI's with the name " + currentPOIName);
				if(pois.size() > 1) {
					Toast.makeText(POIInfoActivity.this, "multiple POI's with that name found??", Toast.LENGTH_SHORT).show();
				}
				
				POI currPOI = pois.get(0);
				
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
	
}

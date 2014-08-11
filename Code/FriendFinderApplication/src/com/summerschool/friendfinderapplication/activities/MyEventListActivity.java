package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;	

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.controller.MyEventListAdapter;
import com.summerschool.friendfinderapplication.models.Event;
import com.summerschool.friendfinderapplication.models.EventMember;

public class MyEventListActivity extends Activity {

	private MyEventListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_event_list);

		adapter = new MyEventListAdapter(MyEventListActivity.this, new ArrayList<Event>());
		
		updateEventList();
		populateEventList();

        // get action bar      
        ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);

	}

	private void updateEventList() {
		Log.i("MyEventList","updating my event list");
		
		//final List<Event> myEvents = new ArrayList<Event>();
		
		//ParseQuery<EventMember> innerQuery = ParseQuery.getQuery(EventMember.class);
		//innerQuery.whereEqualTo(EventMember.MEMBER, ParseUser.getCurrentUser());
		
		ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
		//query.whereMatchesQuery(Event.EVENTMEMBER, innerQuery);
		query.whereEqualTo(Event.OWNER, ParseUser.getCurrentUser());
		query.include(Event.GROUP);
		query.findInBackground(new FindCallback<Event>() {
			@Override
			public void done(List<Event> events, ParseException error) {
				Log.i("MyEventList","found " + events.size() + " events I attend.");
				adapter.clear();
				adapter.addAll(events);
			}
		});
	}
	
	private void populateEventList() {
		ListView list = (ListView) findViewById(R.id.eventListView);
		list.setAdapter(adapter);
	}

	//TODO onClickGotoEventOnMap with intent to MapActivity
	
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}

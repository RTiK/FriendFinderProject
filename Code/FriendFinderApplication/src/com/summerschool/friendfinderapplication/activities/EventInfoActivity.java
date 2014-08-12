package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.models.Event;
import com.summerschool.friendfinderapplication.models.EventMember;

public class EventInfoActivity extends Activity {

	private final static String LOGTAG = "EventInfoActivity";
	public final static String EXTRAS_GROUPNAME = "GROUPNAME";
	public final static String EXTRAS_MARKER_ID = "MARKER_ID";
	
	private Event currEvent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_info);
		Log.i(LOGTAG, "started");
		
		Intent i = getIntent();
		final String eventObjID = i.getStringExtra(POIInfoActivity.EXTRAS_MARKER_ID);
		
		ParseQuery<Event> q1 = ParseQuery.getQuery(Event.class);		
		q1.whereEqualTo("objectId", eventObjID);
		q1.findInBackground(new FindCallback<Event>() {
			@Override
			public void done(List<Event> events, ParseException error) {
				Log.i(LOGTAG, "found " + events.size() + " Events with the id " + eventObjID);
				if(events.size() > 1) {
					Toast.makeText(EventInfoActivity.this, "multiple Events with that name found??", Toast.LENGTH_SHORT).show();
				}
				if(events.size() > 0) {
					currEvent = events.get(0);
					getActionBar().setTitle(currEvent.getTitle());
					
					TextView title = (TextView) findViewById(R.id.event_title);
					TextView desc = (TextView) findViewById(R.id.event_description);
					TextView date = (TextView) findViewById(R.id.event_date);
					
					title.setText(currEvent.getTitle());
					desc.setText(currEvent.getDescription());
					date.setText(currEvent.getDate().toString());
					
					//TODO Memberliste anzeigen
					
				}
				
				
			}
		});
	}
	
	private List<ParseUser> getEventMembers(Event e) {
		List<ParseUser> members = new ArrayList<ParseUser>();
		if(e!=null) {
			ParseQuery<EventMember> query = ParseQuery.getQuery(EventMember.class);
			query.whereEqualTo(EventMember.EVENT, e);
			try {
				List<EventMember> eventMembers = query.find();
				for(EventMember em : eventMembers) {
					members.add(em.getMember());
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		return members;
	}
	
	public void addUserToEvent(final View v) {
		EventMember newEm =  new EventMember();
		newEm.addMember(ParseUser.getCurrentUser());
		newEm.addEvent(currEvent);
		try {
			newEm.save();
			Toast.makeText(EventInfoActivity.this, "saved", Toast.LENGTH_SHORT).show();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void removeUserFromEvent(final View v) {
		ParseQuery<EventMember> query = ParseQuery.getQuery(EventMember.class);
		query.whereEqualTo(EventMember.EVENT, currEvent);
		query.whereEqualTo(EventMember.MEMBER, ParseUser.getCurrentUser());
		try {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<ParseObject> emList2 = (List) query.find();
			ParseObject.deleteAll(emList2);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}	
	}
	
	public void onEventSubscriptionClicked(View view){
		
		Button add = (Button) findViewById(R.id.add);
		Button remove = (Button) findViewById(R.id.unsubscribe);
		
		ParseQuery<EventMember> query = ParseQuery.getQuery(EventMember.class);
		query.whereEqualTo(EventMember.EVENT, currEvent);
		query.whereEqualTo(EventMember.MEMBER, ParseUser.getCurrentUser());
		
		// participant
		try {
			if(query.count() >= 1){
				add.setVisibility(View.INVISIBLE);
				remove.setVisibility(View.VISIBLE);
			}
			// declined the event
			else{
				add.setVisibility(View.VISIBLE);
				remove.setVisibility(View.INVISIBLE);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	}
}

package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.Currency;
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

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.controller.EventParticipantAdapter;
import com.summerschool.friendfinderapplication.controller.MyPOIListAdapter;
import com.summerschool.friendfinderapplication.models.Event;
import com.summerschool.friendfinderapplication.models.EventMember;
import com.summerschool.friendfinderapplication.models.POI;

public class EventInfoActivity extends Activity {

	private static String LOGTAG = "EventInfoActivity";
	public static String EXTRAS_GROUPNAME = "GROUPNAME";
	public static String EXTRAS_MARKER_ID = "MARKER_ID";
	
	private Event currEvent;
	private EventParticipantAdapter adapter; 
	private List<Event> events;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_info);
		Log.i(LOGTAG, "started");
		//ListView listview = (ListView) findViewById(R.id.listView1);
		
		adapter = new EventParticipantAdapter(EventInfoActivity.this, new ArrayList<ParseUser>());
        
		Intent i = getIntent();
		final String eventObjID = i.getStringExtra(EventInfoActivity.EXTRAS_MARKER_ID);
		Log.i("objID",eventObjID);
		//request for the event info
		ParseQuery<Event> q1 = ParseQuery.getQuery(Event.class);		
		q1.whereEqualTo("objectId", eventObjID);
				
		try {
			events = q1.find();
			if(events.size() > 1) {
				Toast.makeText(EventInfoActivity.this, "multiple Events with that name found??", Toast.LENGTH_SHORT).show();
			}
			if(events.size() > 0) { //only one event found
				currEvent = events.get(0); //stored in currEvent
				Log.i("currEvent Name",currEvent.getTitle());
				getActionBar().setTitle(currEvent.getTitle());
				
				TextView title = (TextView) findViewById(R.id.event_title);
				TextView desc = (TextView) findViewById(R.id.event_description);
				TextView date = (TextView) findViewById(R.id.event_date);
				
				title.setText(currEvent.getTitle());
				desc.setText(currEvent.getDescription());
				//date.setText(currEvent.getDate().toString());
				
				//set the adapter
				getEventMembers(currEvent);
				Log.i("qdqpter",adapter.getItem(0).getClassName());
				ListView list = (ListView) findViewById(R.id.eventMemberListView);
				list.setAdapter(adapter);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
		
	
	private void getEventMembers(Event e) {	
		//parseUser Members
		List<ParseUser> members = new ArrayList<ParseUser>();
		
		if(e!=null) {
			//request for members that participate to the event
			ParseQuery<EventMember> query = ParseQuery.getQuery(EventMember.class);
			query.whereEqualTo(EventMember.EVENT, e);
			query.include(EventMember.MEMBER);
			try {
				List<EventMember> eventMembers = query.find();
				System.out.println("eventMembers" + eventMembers);
				for(EventMember em : eventMembers) {
					members.add(em.getMember());
				}
				adapter.clear();
				adapter.addAll(members);
				
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		
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
		
		//Button add = (Button) findViewById(R.id.add);
		//Button remove = (Button) findViewById(R.id.unsubscribe);
		
		ParseQuery<EventMember> query = ParseQuery.getQuery(EventMember.class);
		query.whereEqualTo(EventMember.EVENT, currEvent);
		query.whereEqualTo(EventMember.MEMBER, ParseUser.getCurrentUser());
		
		// participant
		try {
			if(query.count() >= 1){
			//	add.setVisibility(View.INVISIBLE);
			//	remove.setVisibility(View.VISIBLE);
			}
			// declined the event
			else{
				//add.setVisibility(View.VISIBLE);
				//remove.setVisibility(View.INVISIBLE);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
	}
	
	public void deleteEvent(final View v)
	{
		if(ParseUser.getCurrentUser().getObjectId().toString().trim().equals(currEvent.getOwner().getObjectId().toString().trim()))
		{	
			ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
			eventQuery.whereEqualTo(Event.TITLE, currEvent.getTitle());
			eventQuery.whereEqualTo(Event.OWNER, ParseUser.getCurrentUser());
			eventQuery.findInBackground(new FindCallback<Event>() {
				public void done(List<Event> e, ParseException err)
				{
					if(e != null)
					{
						if(e.size() == 1)
						{
							e.get(0).deleteInBackground(new DeleteCallback() {
								
								@Override
								public void done(ParseException arg0) {
									// TODO Auto-generated method stub
									if(arg0 != null)
									{
										Toast.makeText(getApplicationContext(), "We can't delete the association", Toast.LENGTH_LONG).show();
									}
									
								}
							});
						}
					}
				}
			});
			
			ParseQuery<EventMember> query = ParseQuery.getQuery(EventMember.class);
			query.whereEqualTo(EventMember.EVENT, currEvent);
			try {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				List<ParseObject> emList2 = (List) query.find();
				ParseObject.deleteAll(emList2);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(), "You can't delete a group that you don't own", Toast.LENGTH_SHORT).show();
		}
	}
	
	
}


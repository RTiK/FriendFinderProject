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
	
	private List<ParseUser> eventMembers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_info);
		
		adapter = new EventParticipantAdapter(EventInfoActivity.this, new ArrayList<ParseUser>());
		eventMembers = new ArrayList<ParseUser>();
		
		updateEventInfoData(getIntent().getStringExtra(EventInfoActivity.EXTRAS_MARKER_ID));
		updateLayoutInformation();
		populateEventMemberList();
		
	}
	
	public void updateEventInfoData(String eventObjID) {
		Log.i("objID",eventObjID);
		
		//Current Event Object
		ParseQuery<Event> query = ParseQuery.getQuery(Event.class);		
		query.whereEqualTo("objectId", eventObjID);
		try {
			List<Event> events = new ArrayList<Event>();
			events = query.find();
			if(events != null && events.size() > 1) {
				Log.w(LOGTAG,"multiple Events with that name found??");
			}
			if(events != null) currEvent = events.get(0);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//EventMembers Objects
		Log.i(LOGTAG,"get eventmembers");
		if(currEvent != null) {
			ParseQuery<EventMember> query2 = ParseQuery.getQuery(EventMember.class);
			query2.whereEqualTo(EventMember.EVENT, currEvent);
			query2.include(EventMember.MEMBER);
			try {
				List<EventMember> members = query2.find();
				for(EventMember em : members) {
					ParseUser t = em.getMember();
					if(t!=null) eventMembers.add(t);
				}
				Log.i(LOGTAG,"found " + eventMembers.size() + " EventMembers");
				adapter.clear();
				adapter.addAll(eventMembers);
				
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void updateLayoutInformation() {
		if(currEvent != null) {
			Log.i("currEvent Name",currEvent.getTitle());
			getActionBar().setTitle(currEvent.getTitle());
			
			TextView title = (TextView) findViewById(R.id.event_title);
			TextView desc = (TextView) findViewById(R.id.event_description);
			TextView date = (TextView) findViewById(R.id.event_date);
			
			if(currEvent.getTitle() != null) title.setText(currEvent.getTitle());
			if(currEvent.getDescription() != null) desc.setText(currEvent.getDescription());
			if(currEvent.getDate() != null) date.setText(currEvent.getDate().toString());
			
			//Buttons enabling/disabling in fonction of the user
			Button blike = (Button) findViewById(R.id.eventJoin);
			Button bdislike = (Button) findViewById(R.id.eventLeave);
			Button bdelete = (Button) findViewById(R.id.eventDelete);
			
			if(isOwner()){
				Log.i(LOGTAG,"current user is the owner");
				blike.setEnabled(false);
				bdislike.setEnabled(false);
				bdelete.setEnabled(true);
				
				blike.setVisibility(View.GONE);
				bdislike.setVisibility(View.GONE);
				bdelete.setVisibility(View.VISIBLE);
				
			} else if(isFan()) {
				Log.i(LOGTAG,"current user already joined");
				blike.setEnabled(false);
				bdislike.setEnabled(true);
				bdelete.setEnabled(false);
				
				blike.setVisibility(View.GONE);
				bdislike.setVisibility(View.VISIBLE);
				bdelete.setVisibility(View.GONE);			
			} else {
				Log.i(LOGTAG,"current user is not a fan and not the owner");
				blike.setEnabled(true);
				bdislike.setEnabled(false);
				bdelete.setEnabled(false);
				
				blike.setVisibility(View.VISIBLE);
				bdislike.setVisibility(View.GONE);
				bdelete.setVisibility(View.GONE);	        
			}  
		}
	}
	
	public void populateEventMemberList() {
		ListView list = (ListView) findViewById(R.id.eventMemberListView);
		list.setAdapter(adapter);
	}
	
	public void joinCurrEvent(final View v) {
		EventMember newEm =  new EventMember();
		newEm.addMember(ParseUser.getCurrentUser());
		newEm.addEvent(currEvent);
		try {
			newEm.save();
			finish();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void leaveCurrEvent(final View v) {
		ParseQuery<EventMember> query = ParseQuery.getQuery(EventMember.class);
		query.whereEqualTo(EventMember.EVENT, currEvent);
		query.whereEqualTo(EventMember.MEMBER, ParseUser.getCurrentUser());
		try {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<ParseObject> emList2 = (List) query.find();
			ParseObject.deleteAll(emList2);
			finish();
		} catch (ParseException e) {
			e.printStackTrace();
		}	
	}

	private boolean isOwner() {
		if(currEvent != null)
			return currEvent.getOwner().getObjectId().equals(ParseUser.getCurrentUser().getObjectId());
		return false;
	}
	
	private boolean isFan() {
		if(eventMembers != null) {
			for(ParseUser u : eventMembers) {
				if(u.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteCurrEvent(final View v)
	{
		if(currEvent != null) {
			ParseQuery<EventMember> query = ParseQuery.getQuery(EventMember.class);
			query.whereEqualTo(EventMember.EVENT, currEvent);
			try {
				ParseObject.deleteAll((List) query.find());
				currEvent.delete();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		finish();
	}
	
	
}


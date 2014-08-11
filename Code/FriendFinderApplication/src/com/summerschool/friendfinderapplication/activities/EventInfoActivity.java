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
import com.summerschool.friendfinderapplication.models.Event;
import com.summerschool.friendfinderapplication.models.EventMember;

public class EventInfoActivity extends Activity {

	private final static String LOGTAG = "EventInfoActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_info);
		
		//TODO getExtra from Intent about what POI this is
		//TODO need group and event identifier
		
		final String currentEventName = "";
		
		ParseQuery<Event> q1 = ParseQuery.getQuery(Event.class);		
		q1.whereEqualTo(Event.TITLE, currentEventName);
		q1.findInBackground(new FindCallback<Event>() {
			@Override
			public void done(List<Event> events, ParseException error) {
				Log.i(LOGTAG, "found " + events.size() + " Events with the name " + currentEventName);
				if(events.size() > 1) {
					Toast.makeText(EventInfoActivity.this, "multiple Events with that name found??", Toast.LENGTH_SHORT).show();
				}
				
				Event currEvent = events.get(0);
				//TODO show object info
				
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
}

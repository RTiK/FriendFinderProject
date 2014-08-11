package com.summerschool.friendfinderapplication.controller;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.activities.MyEventListActivity;
import com.summerschool.friendfinderapplication.models.Event;

public class MyEventListAdapter extends ArrayAdapter<Event>{

	private Context mContext;
	private static List<Event> mEvents;
	
	public MyEventListAdapter(Context context, List<Event> events) {
		super(context, R.layout.event_item_view,events);
		this.mContext = context;
		this.mEvents = events;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		
		if(itemView == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
			itemView = mLayoutInflater.inflate(R.layout.event_item_view, parent, false);
		}
		
		final Event currentEvent = mEvents.get(position);
		
		TextView tv = (TextView) itemView.findViewById(R.id.item_event_name);
		tv.setText(currentEvent.getTitle());
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO Make intent to MapActivity to show position of this event
				Toast.makeText(mContext, "Event for " + currentEvent.getDate(), Toast.LENGTH_SHORT).show();
			}
		});
		
		return itemView;
	}

}

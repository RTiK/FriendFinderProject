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

import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.activities.EventInfoActivity;
import com.summerschool.friendfinderapplication.activities.MyEventListActivity;
import com.summerschool.friendfinderapplication.models.Event;
import com.summerschool.friendfinderapplication.models.EventMember;

public class EventParticipantAdapter extends ArrayAdapter<ParseUser> {

	private Context mContext;
	private static List<ParseUser> mParticipants;
	
	public EventParticipantAdapter(Context context, List<ParseUser> participants) {
		super(context, R.layout.activity_event_info);
		this.mContext = context;
		this.mParticipants = participants;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		
		if(itemView == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
			itemView = mLayoutInflater.inflate(R.layout.group_item_view, parent, false);
		}
		
		final ParseUser currentParticipant = mParticipants.get(position);
		
		TextView tv = (TextView) itemView.findViewById(R.layout.activity_event_info);
		tv.setText(currentParticipant.getUsername());
		/*
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO Make intent to MapActivity to show position of this event
				Toast.makeText(mContext, "Username: " + currentEventParticipant.getUsername(), Toast.LENGTH_SHORT).show();
			}
		}); */
		
		return itemView;
	}
	
	
	
}
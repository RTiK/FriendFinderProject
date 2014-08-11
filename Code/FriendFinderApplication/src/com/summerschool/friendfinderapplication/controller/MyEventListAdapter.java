package com.summerschool.friendfinderapplication.controller;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseGeoPoint;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.activities.MapActivity;
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
		final ParseGeoPoint pgp = currentEvent.getLocation();
		final String currentGroupName = currentEvent.getGroup().getName();
		
		
		
		TextView tv = (TextView) itemView.findViewById(R.id.item_event_name);
		tv.setText(currentEvent.getTitle());
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("MyEventListAdapter", "go to map for group " + currentGroupName);
				Intent goToMap = new Intent(mContext, MapActivity.class);
				goToMap.putExtra("GroupName", currentGroupName);
				goToMap.putExtra(MapActivity.EXTRA_FOCUS_LATITUDE, pgp.getLatitude());
				goToMap.putExtra(MapActivity.EXTRA_FOCUS_LONGITUDE, pgp.getLongitude());
				goToMap.putExtra(MapActivity.EXTRA_FOCUS_ZOOM, 9);
				
				mContext.startActivity(goToMap);
			}
		});
		
		return itemView;
	}

}

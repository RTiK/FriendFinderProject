package com.summerschool.friendfinderapplication.controller;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.activities.MapActivity;
import com.summerschool.friendfinderapplication.models.Event;
import com.summerschool.friendfinderapplication.models.POI;

public class GroupEventAdapter extends ArrayAdapter<Event>{

	private Context mContext;
	private List<Event> mEvent;
	
	public GroupEventAdapter(Context context, List<Event> event) {
		super(context, R.id.eventListView, event);
		this.mContext = context;
		this.mEvent = event;
		Log.i("creation of GroupEventAdapter","OK");
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		//make sure we have a view (could be null)
		if(itemView == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
			itemView = mLayoutInflater.inflate(R.layout.member_item_view, parent, false);
		}
		Log.i("get(position)",mEvent.size()+"/"+position);
		final Event currentEvent = mEvent.get(position);
		
		//Set the text of the TextField to the right name and its onclicklistener
				TextView textView = (TextView) itemView.findViewById(R.id.item_member_name);
				textView.setText(currentEvent.getTitle());
				textView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ParseGeoPoint p = currentEvent.getLocation();
						Intent goToMap = new Intent(mContext.getApplicationContext(), MapActivity.class);
						goToMap.putExtra(MapActivity.EXTRA_FOCUS_LATITUDE, p.getLatitude());
						goToMap.putExtra(MapActivity.EXTRA_FOCUS_LONGITUDE, p.getLongitude());
						goToMap.putExtra(MapActivity.EXTRA_GROUPNAME, currentEvent.getGroup().getName());
						mContext.startActivity(goToMap);
					}
				});					
					
		return itemView;
	}



	public void onClickMapButton(final View v) {
		//TODO
		Intent intent = new Intent(mContext, MapActivity.class);
		//startActivity(intent);
	}
}

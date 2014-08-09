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
import com.summerschool.friendfinderapplication.models.POI;

public class MyPOIListAdapter extends ArrayAdapter<POI> {

	private Context mContext;
	private static List<POI> mPOIs;
	
	public MyPOIListAdapter(Context context, List<POI> pois) {
		super(context, R.layout.poi_item_view);
		mContext = context;
		mPOIs = pois;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		
		if(itemView == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
			itemView = mLayoutInflater.inflate(R.layout.group_item_view, parent, false);
		}
		
		final POI currentPOI = mPOIs.get(position);
		
		TextView tv = (TextView) itemView.findViewById(R.id.item_event_name);
		tv.setText(currentPOI.getName());
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO Make intent to MapActivity to show position of this event
				Toast.makeText(mContext, "Event at " + currentPOI.getLocation(), Toast.LENGTH_SHORT).show();
			}
		});
		
		return itemView;
	}
}

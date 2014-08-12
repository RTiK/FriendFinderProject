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
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.activities.MapActivity;
import com.summerschool.friendfinderapplication.models.POI;

public class MyPOIListAdapter extends ArrayAdapter<POI> {

	private Context mContext;
	private List<POI> mPOIs;
	private static final String LOGTAG = "MyPOIListAdapter";

	public MyPOIListAdapter(Context context, List<POI> pois) {
		super(context, R.layout.poi_item_view, pois);
		mContext = context;
		this.mPOIs = pois;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		
		if(itemView == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
			itemView = mLayoutInflater.inflate(R.layout.poi_item_view, parent, false);
		}
		
		Log.i(LOGTAG, "mPois size: " + mPOIs.size());
		
		if(mPOIs.size() > 0) {
			final POI currentPOI = mPOIs.get(position);
			TextView tv = (TextView) itemView.findViewById(R.id.item_poi_name);
			tv.setText(currentPOI.getName());
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ParseGeoPoint p = currentPOI.getLocation();
					Intent goToMap = new Intent(mContext.getApplicationContext(), MapActivity.class);
					goToMap.putExtra(MapActivity.EXTRA_FOCUS_LATITUDE, p.getLatitude());
					goToMap.putExtra(MapActivity.EXTRA_FOCUS_LONGITUDE, p.getLongitude());
					mContext.startActivity(goToMap);
				}
			});
		}
		
		return itemView;
	}
}

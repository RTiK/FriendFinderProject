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

import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.activities.MapActivity;
import com.summerschool.friendfinderapplication.models.POI;

public class POIListAdapter extends ArrayAdapter<POI> {
	private Context mContext;
	private List<POI> mPOI;
	
	public POIListAdapter(Context context, List<POI> poi) {
		super(context, R.id.poiListView, poi);
		this.mContext = context;
		this.mPOI = poi;
		Log.i("creation of POIListAdapter","OK");
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		//make sure we have a view (could be null)
		if(itemView == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
			itemView = mLayoutInflater.inflate(R.layout.member_item_view, parent, false);
		}
		//Log.i("test poisito,","on");
		//Log.i("get(position)",mPOI.size()+"/"+position+"/"+mPOI.get(position));
		final POI currentPOI = mPOI.get(position);
		
		//Set the text of the TextField to the right name and its onclicklistener
				TextView textView = (TextView) itemView.findViewById(R.id.item_member_name);
				textView.setText(currentPOI.getName());
				textView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(mContext, "This is " + currentPOI.getName(), Toast.LENGTH_SHORT).show();
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

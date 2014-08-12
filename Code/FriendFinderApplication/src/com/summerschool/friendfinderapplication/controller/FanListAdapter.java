package com.summerschool.friendfinderapplication.controller;

import java.util.List;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.activities.MapActivity;
import com.summerschool.friendfinderapplication.models.POI;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class FanListAdapter extends ArrayAdapter<ParseUser>{

	private Context mContext;
	private List<ParseUser> mFan;
	
	public FanListAdapter(Context context, List<ParseUser> fan) {
		super(context, R.id.poiFansListView, fan);
		this.mContext = context;
		this.mFan = fan;
		//Log.i("creation of POIListAdapter","OK");
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
		final ParseUser currentFan = mFan.get(position);
		
		//Set the text of the TextField to the right name and its onclicklistener
		TextView textView = (TextView) itemView.findViewById(R.id.item_member_name);
		textView.setText(currentFan.getUsername());

		return itemView;
	}



	public void onClickMapButton(final View v) {
		//TODO
		Intent intent = new Intent(mContext, MapActivity.class);
		//startActivity(intent);
	}
	
	
}

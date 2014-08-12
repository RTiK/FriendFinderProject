package com.summerschool.friendfinderapplication.controller;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.activities.GroupDescriptionActivity;
import com.summerschool.friendfinderapplication.activities.MapActivity;
import com.summerschool.friendfinderapplication.models.Group;

public class MyGroupAdapter extends ArrayAdapter<Group> {

		private Context mContext;
		private List<Group> mGroups;
		
		public MyGroupAdapter(Context context, List<Group> groups) {
			super(context, R.layout.group_item_view, groups);
			this.mContext = context;
			this.mGroups = groups;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			//make sure we have a view (could be null)
			if(itemView == null) {
				LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
				itemView = mLayoutInflater.inflate(R.layout.group_item_view, parent, false);
			}
			
			//find the group to work with
			final Group currentGroup = mGroups.get(position);
			
			//Set the text of the TextField to the right name and its onclicklistener
			TextView textView = (TextView) itemView.findViewById(R.id.item_poi_name);
			textView.setText(currentGroup.getName());
			
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					Toast.makeText(mContext, "This is " + currentGroup.getName(), Toast.LENGTH_SHORT).show();
					if(currentGroup.isGPSActive()) {
						Intent goToMap = new Intent(mContext.getApplicationContext(), MapActivity.class);
						goToMap.putExtra("GROUPNAME", currentGroup.getString("name"));
						mContext.startActivity(goToMap);
						Log.i("TEST", "activity started");
					}
				}
			});
						
			ImageButton infButton = (ImageButton) itemView.findViewById(R.id.item_group_info);
			//Set onclicklistener for ImageButton
			infButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(currentGroup.isGPSActive()) {
						Intent intent = new Intent(mContext, GroupDescriptionActivity.class);
						intent.putExtra("GroupName", currentGroup.getName());
						mContext.startActivity(intent);					
						//Toast.makeText(mContext, "This is " + currentGroup.getName(), Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			//Set onclicklistener for GPS Switch
			final Switch gpsSwitch = (Switch) itemView.findViewById(R.id.item_group_switch);
			if(currentGroup.isGPSActive()) {
				gpsSwitch.setChecked(true);	
			} else {
				gpsSwitch.setChecked(false);	
			}
			gpsSwitch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					currentGroup.setGPSActive(!currentGroup.isGPSActive());
					currentGroup.saveEventually();
					try {
						currentGroup.fetch();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			});
						
			return itemView;
		}
		
		
	}
package com.summerschool.friendfinderapplication.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.activities.GroupListActivity;
import com.summerschool.friendfinderapplication.activities.MainActivity;
import com.summerschool.friendfinderapplication.activities.GroupDescriptionActivity;
import com.summerschool.friendfinderapplication.models.Group;
import com.summerschool.friendfinderapplication.models.GroupMember;
import com.summerschool.friendfinderapplication.models.User;

public class MyGroupAdapter extends ArrayAdapter<Group> {

		private Context mContext;
		private static List<Group> mGroups;
		
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
			
			//final Group currentGroup = new Group();
			//Set the text of the TextField to the right name and its onclicklistener
			TextView textView = (TextView) itemView.findViewById(R.id.item_group_name);
			textView.setText(currentGroup.getName());
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					Toast.makeText(mContext, "This is " + currentGroup.getName(), Toast.LENGTH_SHORT).show();
					
					Intent goToMap = new Intent(mContext.getApplicationContext(), MainActivity.class);
					goToMap.putExtra("GROUPNAME", currentGroup.getString("name"));
					mContext.startActivity(goToMap);
					Log.i("TEST", "activity started");
					
				}
			});
			
			//Set onclicklistener for ImageButton
			ImageButton infButton = (ImageButton) itemView.findViewById(R.id.item_group_info);
			infButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, GroupDescriptionActivity.class);
					intent.putExtra("GroupName", currentGroup.getName());
					mContext.startActivity(intent);					
					//Toast.makeText(mContext, "This is " + currentGroup.getName(), Toast.LENGTH_SHORT).show();
				}
			});
			
			//Set onclicklistener for GPS Switch
			Switch gpsSwitch = (Switch) itemView.findViewById(R.id.item_group_switch);
			gpsSwitch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(mContext, "This is " + currentGroup.getName(), Toast.LENGTH_SHORT).show();
				}
			});
						
			return itemView;
		}
		
	}
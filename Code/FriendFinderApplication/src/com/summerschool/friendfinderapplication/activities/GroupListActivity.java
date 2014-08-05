package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.models.Group;

public class GroupListActivity extends Activity {

	protected Button mHomeButton;
	protected Button mMyEventButton;
	protected Button mMyPOIButton;
	protected Button mEditButton;
	protected Button mSearchButton;
	protected Button mAddButton;
	
	private List<Group> myGroups = new ArrayList<Group>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);
		
		populateGroupList();
		populateListView();
	}
	
	private void populateGroupList() {
		//Testing data
		myGroups.add(new Group("Group 1", "This is group number one", true));
		myGroups.add(new Group("Group 2", "This is group number two", true));
		myGroups.add(new Group("Group 3", "This is group number tree", true));
		myGroups.add(new Group("Group 4", "This is group number four", true));
		myGroups.add(new Group("Group 5", "This is group number five", true));
		myGroups.add(new Group("Group 6", "This is group number six", true));
		myGroups.add(new Group("Group 7", "This is group number seven", true));
		myGroups.add(new Group("Group 8", "This is group number eight", true));
	}
	
	private void populateListView() {
		ArrayAdapter<Group> adapter = new MyGroupAdapter();
		ListView list = (ListView) findViewById(R.id.groupListView);
		list.setAdapter(adapter);
	}	
	
	private class MyGroupAdapter extends ArrayAdapter<Group> {

		public MyGroupAdapter() {
			super(GroupListActivity.this, R.layout.group_item_view, myGroups);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			//make sure we have a view (could be null)
			if(itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.group_item_view, parent, false);
			}
			
			//find the group to work with
			final Group currentGroup = myGroups.get(position);
			
			//Set the text of the TextField to the right name and its onclicklistener
			TextView textView = (TextView) itemView.findViewById(R.id.item_group_name);
			textView.setText(currentGroup.getName());
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(GroupListActivity.this, "This is " + currentGroup.getName(), Toast.LENGTH_SHORT).show();
				}
			});
			
			//Set onclicklistener for ImageButton
			ImageButton infButton = (ImageButton) itemView.findViewById(R.id.item_group_info);
			infButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(GroupListActivity.this, "This is " + currentGroup.getName(), Toast.LENGTH_SHORT).show();
				}
			});
			
			//Set onclicklistener for ImageButton
			Switch gpsSwitch = (Switch) itemView.findViewById(R.id.item_group_switch);
			gpsSwitch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(GroupListActivity.this, "This is " + currentGroup.getName(), Toast.LENGTH_SHORT).show();
				}
			});
						
			return itemView;
		}
	}
}

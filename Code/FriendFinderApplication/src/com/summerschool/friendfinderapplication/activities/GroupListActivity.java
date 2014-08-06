package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.models.Group;
import com.summerschool.friendfinderapplication.models.GroupMember;

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
		
		TextView tv = (TextView) findViewById(R.id.helloView);
		tv.setText("Hello " + ParseUser.getCurrentUser().getUsername());
		
		updateGroupList();
		populateListView();
	}
	
	private void updateGroupList() {

		myGroups.clear();
		
		//Add your own groups
		ParseQuery<Group> query = ParseQuery.getQuery(Group.class);
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
		query.findInBackground(new FindCallback<Group>() {
			@Override
			public void done(List<Group> groups, ParseException error) {
				if(groups != null) {
					myGroups.addAll(groups);
				}
			}
		});
		
		//add groups you have joined
		ParseQuery<GroupMember> query2 = ParseQuery.getQuery(GroupMember.class);
		query2.whereEqualTo("Member", ParseUser.getCurrentUser());
		query2.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
		query2.findInBackground(new FindCallback<GroupMember>() {
			@Override
			public void done(List<GroupMember> groups, ParseException error) {
				if(groups != null) {
					for(GroupMember gm : groups) {
						myGroups.add(gm.getGroupName());
					}
				}
			}
		});
		
	}
	
	private void populateListView() {
		ArrayAdapter<Group> adapter = new MyGroupAdapter();
		ListView list = (ListView) findViewById(R.id.groupListView);
		list.setAdapter(adapter);
	}	
	
	public void onClickAddGroup(final View v) {
		//intent to new Group activty		
		Intent intent = new Intent(GroupListActivity.this, NewGroupActivity.class);
		startActivity(intent);
		finish();		
	}
	
	public void onClickReloadButton(final View v) {
		updateGroupList();
		populateListView();
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

package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.controller.MyGroupAdapter;
import com.summerschool.friendfinderapplication.models.Group;
import com.summerschool.friendfinderapplication.models.GroupMember;

public class GroupListActivity extends Activity {

	protected Button mHomeButton;
	protected Button mMyEventButton;
	protected Button mMyPOIButton;
	protected Button mEditButton;
	protected Button mSearchButton;
	protected Button mAddButton;
	
	private MyGroupAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);
		
		TextView tv = (TextView) findViewById(R.id.helloView);
		tv.setText(ParseUser.getCurrentUser().getUsername() + "'s groups");
		
		adapter = new MyGroupAdapter(GroupListActivity.this,new ArrayList<Group>());
		
		updateGroupList();
		populateListView();
		
	}
	
	private void updateGroupList() {
		Log.i("userinfo:",""+ParseUser.getCurrentUser() + " ___ " + ParseUser.getCurrentUser().getObjectId());
		
		//Add your own groups
		/*final List<Group> myCreatedGroups = new ArrayList<Group>();
		ParseQuery<Group> query = ParseQuery.getQuery(Group.class);
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		//query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
		List<Group> groups;
		try {
			groups = query.find();
			if(groups != null) {
				Log.i("created groups", "" + groups.size());
				myCreatedGroups.clear();
				myCreatedGroups.addAll(groups);
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		
//		query.findInBackground(new FindCallback<Group>() {
//			@Override
//			public void done(List<Group> groups, ParseException error) {
//				if(error != null) {
//					Log.e("FUCK THIS ParseObject", error.getLocalizedMessage());
//				}
//				if(groups != null) {
//					Log.i("created groups", "" + groups.size());
//					myCreatedGroups.clear();
//					myCreatedGroups.addAll(groups);
//				}
//			}
//		});
		
		
		//add groups you have joined
		final List<Group> myJoinedGroups = new ArrayList<Group>();
		ParseQuery<GroupMember> query2 = ParseQuery.getQuery(GroupMember.class);
		query2.whereEqualTo("Member", ParseUser.getCurrentUser());
		query2.include("Group");
		//query2.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
		try {
			List<GroupMember> groups2 = query2.find();
			Log.i("joined not unique groups", "" + groups2.size());
			HashSet<GroupMember> hs = new HashSet<GroupMember>(groups2);
			groups2.clear();			
			groups2.addAll(hs);
			Log.i("joined unique groups", "" + groups2.size());
			myJoinedGroups.clear();
			for(GroupMember gm : groups2) {
				Group jg = gm.getGroup();
				Log.i("Found joined group",jg.getName());				
				myJoinedGroups.add(jg);
			}
		} catch (ParseException e) {
			Log.e("error", e.getLocalizedMessage());
		}
//		query2.findInBackground(new FindCallback<GroupMember>() {
//			@Override
//			public void done(List<GroupMember> groups, ParseException error) {
//				if(groups != null) {
//					Log.i("joined not unique groups", "" + groups.size());
//					HashSet<GroupMember> hs = new HashSet<GroupMember>();
//					hs.addAll(groups);
//					myJoinedGroups.clear();
//					groups.addAll(hs);
//					Log.i("joined unique groups", "" + groups.size());
//					for(GroupMember gm : groups) {
//						myJoinedGroups.add(gm.getGroup());
//					}
//				}
//			}
//		});
		
		Collections.sort(myJoinedGroups,new Comparator<Group>() {
			@Override
			public int compare(Group lhs, Group rhs) {
				if(lhs.getName().equals(rhs.getName()))
					return 0;
				else 
					return 1;
			}
		});
		
		adapter.clear();
		adapter.addAll(myJoinedGroups);
	}
	
	private void populateListView() {
		ListView list = (ListView) findViewById(R.id.groupListView);
		list.setAdapter(adapter);
	}	
	
	public void onClickAddGroup(final View v) {
		//intent to new Group activty		
		Intent intent = new Intent(GroupListActivity.this, NewGroupActivity.class);
		startActivity(intent);		
	}
	
	public void onClickReloadButton(final View v) {
		updateGroupList();
		//populateListView();
	}
	
	public void onClickGroupMap(View v) {
		Log.i("TEST", "map button clicked");
		Intent switchToMainView = new Intent(getApplicationContext(), MapActivity.class);
		startActivity(switchToMainView);
	}
	
	public void onClickHome(final View v) {
		//intent to new Group activty		
		Intent intent = new Intent(GroupListActivity.this, MapActivity.class);
		startActivity(intent);	
	}
	
	public void onClickMyEvent(final View v) {
		//intent to new Group activty		
		Intent intent = new Intent(GroupListActivity.this, MyEventListActivity.class);
		startActivity(intent);
	}
	
	public void onClickMyPoi(final View v) {
		//intent to new Group activty		
		Intent intent = new Intent(GroupListActivity.this, MyPOIListActivity.class);
		startActivity(intent);		
	}
	
	
}

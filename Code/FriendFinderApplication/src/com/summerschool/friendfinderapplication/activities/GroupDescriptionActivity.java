package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.maps.MapActivity;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseQuery.CachePolicy;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.controller.MemberListAdapter;
import com.summerschool.friendfinderapplication.models.Group;
import com.summerschool.friendfinderapplication.models.GroupMember;

public class GroupDescriptionActivity extends Activity {


	protected Button mHomeButton;
	protected Button mMyEventButton;
	protected Button mMyPOIButton;
	protected Button mMemberListButton;
	protected Button mEventsButton;
	protected Button mMapButton;
	
	private Group currentGroup;
	private MemberListAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_description);
		
		Log.i("Creation of the view","creation OK");
        // get action bar   
        ActionBar actionBar = getActionBar();
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        Log.i("ActionBar","OK");
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        Log.i("ActionBar displayed","OK");
		String groupName = getIntent().getStringExtra("GroupName");
		Log.i("groupName imported","OK");
		final TextView tv = (TextView) findViewById(R.id.testTextView);
		Log.i("textView found","OK");
		tv.setText("this is group: " + groupName);
		Log.i("groupName ecrit","OK");
		
		adapter = new MemberListAdapter(GroupDescriptionActivity.this,new ArrayList<ParseUser>());
		
		Log.i("adapter correctly instantiated","OK");
		//looking for the group 
		ParseQuery<Group> getGroupInfo = ParseQuery.getQuery(Group.class);
		Log.i("groupName",groupName);
		getGroupInfo.whereEqualTo("name", groupName);
		getGroupInfo.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
		getGroupInfo.findInBackground(new FindCallback<Group>() {
			@Override
			public void done(List<Group> groups, ParseException error) {
				Log.i("request done","OK");
				if(groups != null) {
					if(groups.size() > 1) {
						Log.i("Error", "Multiple Groups with the same name found");
					} else if(groups.size() == 0) {
						Log.i("Warning", "No Group found like that?!");
						tv.setText("WTF?");
					} else {
						currentGroup = groups.get(0);
						Log.i("grooupName =",currentGroup.getName());
						String txt = currentGroup.getName() + "\n" + currentGroup.getDescription() +
								"\n";
						tv.setText(txt);
					}
					Log.i("grooupName2 =",currentGroup.getName());
				} else {
					//groups was null
					Log.i("ERROR", "Group List returned was null ??");
				}
				
			}
		});
		
		//Log.i("grooupName3 =",currentGroup.getName());
		updateMemberList(groupName);
		populateListView();
		
	}
	
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	MenuInflater inflater= getMenuInflater();
    	inflater.inflate(R.menu.activity_main_actions, menu);
    	
    	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	
    	// Take appropriate action for each action item click
        switch (item.getItemId()) {
        case R.id.action_leaveGroup:
            // search action
        	ActionLeaveGroup(getIntent().getStringExtra("groupName"));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
 
	/**
     * Launching new activity
     * */	
    private void ActionLeaveGroup(String groupName) {
    	
    	 //TODO Leave group when here !
    	Log.i("Enter into button","OK");
    	final boolean alone = false;
		//The group is known
		Log.i("groupName",currentGroup.getName());
		//the userName too
		Log.i("username", ParseUser.getCurrentUser().toString());
		
		
		//Find the user entity in the Parse database
		ParseQuery<GroupMember> userInfo = ParseQuery.getQuery(GroupMember.class);
		//find specific user
		userInfo.whereEqualTo("Member",ParseUser.getCurrentUser());
		//in a specific group
		Log.i("groupname =",""+ currentGroup);
		userInfo.whereEqualTo("Group",currentGroup);
		userInfo.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
		//find the group member list that matches the request
		userInfo.findInBackground(new FindCallback<GroupMember>() {
			public void done(List<GroupMember> users, ParseException error) {
				if(users != null) { //you are at least in the chosen group
					if(users.size() > 1) { //Your are registered twice in the group
						Log.i("Error","too much members : " + users.size());
					} 
					else if (users.size() ==1){ //There is only one line that matches
						Log.i("Only  one line matches","OK");
						Log.i("users.size()= ",""+users.size());
						users.get(0).deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                // TODO Auto-generated method stub
                            	
                                if(e==null)
                                {
                                    Toast.makeText(getBaseContext(),"Deleted Successfully!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getBaseContext(),"Cant Delete Tickle!"+e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
						}); 
					}
					else{
						Log.i("ERROR","No group found");
					}
				} else {
					//you are not in the group
					Toast.makeText(getApplicationContext(), "You are not in the group !", Toast.LENGTH_SHORT);
				}
			}
		});
		
        Intent i = new Intent(GroupDescriptionActivity.this, GroupListActivity.class);
        startActivity(i);
       
 	}
   
	private void updateMemberList(final String groupName) {
		
		Log.i("Info","Find group " + groupName);
		
		ParseQuery<Group> query0 = ParseQuery.getQuery(Group.class);
		query0.whereEqualTo("name", groupName);
		query0.findInBackground(new FindCallback<Group>() {
			@Override
			public void done(List<Group> groups, ParseException error) {
				if(groups != null && groups.size() == 1) {
					//group found 
					Group g = groups.get(0);
					//Log.i("Info","!!!!Get Memberlist of group " + g.getName());
					ParseQuery<GroupMember> query1 = ParseQuery.getQuery(GroupMember.class);
					query1.whereEqualTo("Group", g);
					query1.include("Member");
					query1.findInBackground(new FindCallback<GroupMember>() {
						@Override
						public void done(List<GroupMember> groupMembers, ParseException error) {
							if(groupMembers != null) {
								Log.i("Info","Size of the group is = " + groupMembers.size());
								List<ParseUser> members = new LinkedList<ParseUser>();
								//Log.i("Info","Toto is part of group ? " + groupMembers.get(0).toString());
								for(GroupMember gm : groupMembers) {
									
									//Log.i("Info","Toto is part of group ? " + gm.containsKey("toto"));
									members.add(gm.getMember());
									//Log.i("Member = ",gm.getMember().getUsername());
								}
								
								adapter.clear();
								adapter.addAll(members);
								
							} else {
								Log.i("Error","GroupMember returned null");
							}
						}
					});
					
				} else {
					Log.i("Error", "No group found or mutliple groups found");
				}
				
				
			}
		});		
	}


	private void populateListView() {
		ListView list = (ListView) findViewById(R.id.memberListView);
		list.setAdapter(adapter);
	}	

	

	public void onClickMapButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MapActivity.class);
		startActivity(intent);
	}
}

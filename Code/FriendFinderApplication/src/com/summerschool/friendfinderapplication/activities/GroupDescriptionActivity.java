package com.summerschool.friendfinderapplication.activities;

import java.util.LinkedList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseQuery.CachePolicy;
import com.summerschool.friendfinderapplication.R;
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
	
	public void onClickHomeButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MapActivity.class);
		startActivity(intent);
	}
	public void onClickMyEventButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MyEventListActivity.class);
		startActivity(intent);
	}
	public void onClickMyPOIButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MyPOIListActivity.class);
		startActivity(intent);
	}

	public void onClickEventsButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MyEventListActivity.class);
		startActivity(intent);
	}
	public void onClickMapButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MapActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_description);
		
        // get action bar   
        ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);

		String groupName = getIntent().getStringExtra("GroupName");
		final TextView tv = (TextView) findViewById(R.id.testTextView);
		tv.setText("this is group: " + groupName);
		
		ParseQuery<Group> getGroupInfo = ParseQuery.getQuery(Group.class);
		getGroupInfo.whereEqualTo("name", groupName);
		getGroupInfo.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
		getGroupInfo.findInBackground(new FindCallback<Group>() {
			@Override
			public void done(List<Group> groups, ParseException error) {
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
				Log.i("grooupName3 =",currentGroup.getName());
				updateMemberList(currentGroup.getName());
			}
		});
		
		//Log.i("Avant Update, groupName",currentGroup.getName());
		//updateMemberList();
		
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
								
								//adapter.clear();
								//adapter.addAll(members);
								
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








private class MemberListAdapter extends ArrayAdapter<ParseUser> {

	private Context mContext;
	private List<ParseUser> mUsers;
	
	public MemberListAdapter(Context context, List<ParseUser> users) {
		super(GroupDescriptionActivity.this, R.id.memberListView, users);
		this.mContext = context;
		this.mUsers = users;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		//make sure we have a view (could be null)
		if(itemView == null) {
			itemView = getLayoutInflater().inflate(R.layout.member_item_view, parent, false);
		}
		
		final ParseUser currentUser = mUsers.get(position);
		
		//Set the text of the TextField to the right name and its onclicklistener
		TextView textView = (TextView) itemView.findViewById(R.id.item_member_name);
		textView.setText(currentUser.getUsername());
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(GroupDescriptionActivity.this, "This is " + currentUser.getUsername(), Toast.LENGTH_SHORT).show();
			}
		});			
					
		return itemView;
	}
}
}

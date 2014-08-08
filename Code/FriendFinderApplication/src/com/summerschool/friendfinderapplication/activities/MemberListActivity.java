
package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class MemberListActivity extends Activity {
	
	private MemberListAdapter adapter;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_list);
		
		adapter = new MemberListAdapter(this, new ArrayList<ParseUser>());
		
		updateMemberList(getIntent().getStringExtra("GroupName"));
		populateListView();
	}
	
	
	private void updateMemberList(String groupName) {
		
		Log.i("Info","Get Memberlist of group " + groupName);
		
		ParseQuery<Group> query0 = ParseQuery.getQuery(Group.class);
		query0.whereEqualTo("name", groupName);
		query0.findInBackground(new FindCallback<Group>() {
			@Override
			public void done(List<Group> groups, ParseException error) {
				if(groups != null && groups.size() == 1) {
					//group found 
					Group g = groups.get(0);
					
					ParseQuery<GroupMember> query1 = ParseQuery.getQuery(GroupMember.class);
					query1.whereEqualTo("Group", g);
					query1.include("Member");
					query1.findInBackground(new FindCallback<GroupMember>() {
						@Override
						public void done(List<GroupMember> groupMembers, ParseException error) {
							if(groupMembers != null) {
								List<ParseUser> members = new LinkedList<ParseUser>();
								for(GroupMember gm : groupMembers) {
									members.add(gm.getMember());
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
	
	private class MemberListAdapter extends ArrayAdapter<ParseUser> {

		private Context mContext;
		private List<ParseUser> mUsers;
		
		public MemberListAdapter(Context context, List<ParseUser> users) {
			super(MemberListActivity.this, R.id.memberListView, users);
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
					Toast.makeText(MemberListActivity.this, "This is " + currentUser.getUsername(), Toast.LENGTH_SHORT).show();
				}
			});			
						
			return itemView;
		}
	}
	
	public void onClickHomeButton(final View v) {
		//TODO
	}
	public void onClickMyEventButton(final View v) {
		//TODO
	}
	public void onClickMyPOIButton(final View v) {
		//TODO
	}
}
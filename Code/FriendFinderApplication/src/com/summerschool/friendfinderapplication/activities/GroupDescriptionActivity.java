package com.summerschool.friendfinderapplication.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.models.Group;

public class GroupDescriptionActivity extends Activity {


	protected Button mHomeButton;
	protected Button mMyEventButton;
	protected Button mMyPOIButton;
	protected Button mMemberListButton;
	protected Button mEventsButton;
	protected Button mMapButton;
	
	private Group currentGroup;
	
	public void onClickHomeButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MainActivity.class);
		startActivity(intent);
	}
	public void onClickMyEventButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MyEventActivity.class);
		startActivity(intent);
	}
	public void onClickMyPOIButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MyPOIActivity.class);
		startActivity(intent);
	}
	public void onClickMemberListButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, GroupListActivity.class);
		startActivity(intent);
	}
	public void onClickEventsButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, EventActivity.class);
		startActivity(intent);
	}
	public void onClickMapButton(final View v) {
		//TODO
		Intent intent = new Intent(GroupDescriptionActivity.this, MainActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_description);

		String groupName = getIntent().getStringExtra("GroupName");
		final TextView tv = (TextView) findViewById(R.id.testTextView);
		tv.setText("this is group: " + groupName);
		
		ParseQuery<Group> getGroupInfo = ParseQuery.getQuery(Group.class);
		getGroupInfo.whereEqualTo("name", groupName);
		getGroupInfo.findInBackground(new FindCallback<Group>() {
			@Override
			public void done(List<Group> groups, ParseException error) {
				if(groups.size() > 1) {
					Log.i("Error", "Multiple Groups with the same name found");
				} else if(groups.size() == 0) {
					Log.i("Warning", "No Group found like that?!");
					tv.setText("WTF?");
				} else {
					currentGroup = groups.get(0);
					
					String txt = currentGroup.getName() + "\n" + currentGroup.getDescription() +
							"\n";
					tv.setText(txt);
					
				}
			}
		});
	}
}

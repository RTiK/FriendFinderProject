package com.summerschool.friendfinderapplication.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			Group currentGroup = myGroups.get(position);
			
			//Set the text of the TextField to the right name
			TextView textView = (TextView) itemView.findViewById(R.id.item_group_name);
			textView.setText(currentGroup.getName());
			
			return itemView;
		}

		
	}
}

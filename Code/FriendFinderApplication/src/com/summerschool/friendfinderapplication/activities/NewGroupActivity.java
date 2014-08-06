package com.summerschool.friendfinderapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.models.Group;

public class NewGroupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_group);
	}
	
	public void onClickCreateGroupButton(final View v) {
		
		//create new group
		EditText newNameTextField = (EditText) findViewById(R.id.group_name);		
		String newGroupName = newNameTextField.getText().toString().trim();
		
		if(newGroupName == null || newGroupName.length() < 1) {
			Toast.makeText(this, "Group can't be empty", Toast.LENGTH_SHORT).show();
		} else {
			Log.i("new group","create group " + newGroupName);
			//save new group in parse				
			ParseUser currentUser = ParseUser.getCurrentUser();
			if(currentUser == null){
				Intent intent = new Intent(this, ConnectionActivity.class);
				startActivity(intent);
				finish();
			}		
			Group g = new Group();
			g.setACL(new ParseACL(ParseUser.getCurrentUser()));
			g.setOwner(ParseUser.getCurrentUser());
			g.setName(newGroupName);
			g.setDescription("Dummy Description");
			g.setGPS(false);
			try {
				g.save();
				//intent back to group list view
				Intent i = new Intent(NewGroupActivity.this,GroupListActivity.class);
				startActivity(i);
				finish();
			} catch (ParseException e) {
				Toast.makeText(NewGroupActivity.this, "Was not able to save", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void onClickJoinGroupButton() {
		
		EditText newNameTextField = (EditText) findViewById(R.id.group_name);		
		String newGroupName = newNameTextField.getText().toString().trim();
		
		if(newGroupName == null || newGroupName.length() < 1) {
			Toast.makeText(this, "Group can't be empty", Toast.LENGTH_SHORT).show();
		} else {
			
			//find out if group exists
			
			//attempt to join group 
			
			//go back to group view
			
		}
		
	}
}

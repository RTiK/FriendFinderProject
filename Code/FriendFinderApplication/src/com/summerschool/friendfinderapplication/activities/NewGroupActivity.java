package com.summerschool.friendfinderapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
		String newGroupName = newNameTextField.getText().toString();
		
		//save new group in parse
				
		ParseUser currentUser = ParseUser.getCurrentUser();
		if(currentUser == null){
			Intent intent = new Intent(this, ConnectionActivity.class);
			startActivity(intent);
			finish();
		}
		
		
		
		//intent back to group list view
		
	}
}

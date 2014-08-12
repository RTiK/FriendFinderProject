package com.summerschool.friendfinderapplication.activities;

import java.util.List;

import android.R.drawable;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.controller.MyGroupAdapter;
import com.summerschool.friendfinderapplication.models.Group;
import com.summerschool.friendfinderapplication.models.GroupMember;

public class NewGroupActivity extends Activity {
	
	
    private EditText editText1;
    private EditText editText2;


    //TextWatcher
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
       {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            checkFieldsForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        	
        }
    
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_group);
        // get action bar      
        ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        editText1 = (EditText) findViewById(R.id.group_name);
        editText2 = (EditText) findViewById(R.id.group_desc);
        //set listeners
        editText1.addTextChangedListener(textWatcher);
        editText2.addTextChangedListener(textWatcher);

        // run once to disable if empty
        checkFieldsForEmptyValues();
	}
	
	public void onClickCreateGroupButton(final View v) {
		Log.i("JoinGroup", "Create new group ... ");
		//create new group
		EditText newNameTextField = (EditText) findViewById(R.id.group_name);		
		final String newGroupName = newNameTextField.getText().toString().trim();
		
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
			ParseQuery<Group> validGroupQuery = ParseQuery.getQuery(Group.class);
			validGroupQuery.whereEqualTo("name", newGroupName);
			validGroupQuery.countInBackground(new CountCallback() {
				@Override
				public void done(int c, ParseException err) {
					if(c == 0) { //no other group has the same name
						//instantiation of a new group
						Group g = new Group();
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
						//Instantiation of a new user
						GroupMember gm = new GroupMember();					
						gm.addGroup(g);
						gm.addMember(ParseUser.getCurrentUser());
						
						try {
							gm.save();
						} catch (ParseException err2) {
							err2.printStackTrace();
						}
					} else {
						
						Toast.makeText(NewGroupActivity.this, "This group already exists!", Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			
		}
	}
	
	public void onClickJoinGroupButton(final View v) {
		Log.i("JoinGroup", "Join new group ... ");
		EditText newNameTextField = (EditText) findViewById(R.id.group_name);		
		final String groupName = newNameTextField.getText().toString().trim();
		
		if(groupName == null || groupName.length() < 1) {
			Toast.makeText(this, "Group can't be empty", Toast.LENGTH_SHORT).show();
		} else {			
			//find out if group exists
			ParseQuery<Group> groupQuery = ParseQuery.getQuery(Group.class);
			groupQuery.whereEqualTo("name", groupName);
			groupQuery.findInBackground(new FindCallback<Group>() {
				@Override
				public void done(final List<Group> groups, ParseException error) {
					Log.i("JoinGroup","Found " + groups.size() + " groups matching " + groupName);
					//group name is ok -> check if you are already in group -> create group -> go back
					if(groups.size() == 0) {
						//no group found
						Toast.makeText(NewGroupActivity.this, "This group doesn\'t exists", Toast.LENGTH_SHORT).show();
					} else if(groups.size() == 1) {
						//found 1 group, lets try to join
						ParseQuery<GroupMember> groupQuery = ParseQuery.getQuery(GroupMember.class);
						groupQuery.whereEqualTo("Group", groups.get(0));
						groupQuery.whereEqualTo("Member", ParseUser.getCurrentUser());
						groupQuery.countInBackground(new CountCallback() {
							@Override
							public void done(int c, ParseException error) {
								if(c==0) {
									Log.i("CreateJoinGroup","try to join group ...");
									// not joined yet, join group now
									GroupMember gm = new GroupMember();					
									gm.addGroup(groups.get(0));
									gm.addMember(ParseUser.getCurrentUser());
									
									try {
										gm.save();
									} catch (ParseException err) {
										err.printStackTrace();
									}
									
									Log.i("CreateJoinGroup","... group joined");
									
									//jump back
									Intent i = new Intent(NewGroupActivity.this,GroupListActivity.class);
									startActivity(i);
									finish();
									
								} else if(c==1) {
									//group already joined
									Log.i("CreateJoinGroup","already joined");
									Toast.makeText(NewGroupActivity.this, "You already joined this group", Toast.LENGTH_SHORT).show();
								} else {
									//fail, same group joined several times
									Log.i("CreateJoinGroup","failure, fix this");
									Toast.makeText(NewGroupActivity.this, "Fail! Group was joined several times. impossible.", Toast.LENGTH_SHORT).show();
								}
							}
						});
					} else {
						//found several groups -> his should not happen
						Toast.makeText(NewGroupActivity.this, "Fail! Group was joined several times. impossible.", Toast.LENGTH_SHORT).show();
					}
					
				}
			});
//			try {
//				int c = groupQuery.count();
//				if(c > 0) {
//					Log.i("JoinGroup", "Found a group to join: " + c);
//					//group name is ok -> create group -> go back
//					GroupMember gm = new GroupMember();
//					gm.addGroup(groupQuery.getFirst());
//					gm.addMember(ParseUser.getCurrentUser());
//					gm.save();
//					
//					//jump back
//					Intent i = new Intent(NewGroupActivity.this,GroupListActivity.class);
//					startActivity(i);
//					finish();
//				} else {
//					Log.i("JoinGroup", "There is no such group to join");
//					Toast.makeText(this, "This group does not exists...", Toast.LENGTH_SHORT).show();
//				}
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
		}
		
	}
	
	public void onClickMyEvent(final View v) {
		//intent to new Group activty		
		Intent intent = new Intent(NewGroupActivity.this, MyEventListActivity.class);
		startActivity(intent);
		finish();		
	}
	
	public void onClickMyPoi(final View v) {
		//intent to new Group activty		
		Intent intent = new Intent(NewGroupActivity.this, MyPOIListActivity.class);
		startActivity(intent);
		finish();		
	}
	   
	private  void checkFieldsForEmptyValues(){
	        Button bjoin = (Button) findViewById(R.id.join_grou_button);
	        Button bcreate = (Button) findViewById(R.id.create_group_button);
	        

	        String s1 = editText1.getText().toString();
	        String s2 = editText2.getText().toString();
	        if(s2.equals("") )
	        {
	            bjoin.setEnabled(true);
	            bjoin.setBackground(this.getResources().getDrawable(R.drawable.buttonsaction));
	        }
	        	
	        else
	        {
	            bjoin.setEnabled(false);
	            bjoin.setBackgroundColor(Color.GRAY);
	        }


	        
	        
	        
	        if(s1.equals("") && s2.equals(""))
	        {
	            bjoin.setEnabled(false);
	            bjoin.setBackgroundColor(Color.GRAY);
	            bcreate.setEnabled(false);
	            bcreate.setBackgroundColor(Color.GRAY);
	        }

	        else if(!s1.equals("")&&s2.equals("")){
	            bcreate.setEnabled(false);
	            bcreate.setBackgroundColor(Color.GRAY);
	        	bjoin.setEnabled(true);
	            bjoin.setBackground(this.getResources().getDrawable(R.drawable.buttonsaction));
	        }

	        else if(s1.equals("")&&!s2.equals(""))
	        {
	            bjoin.setEnabled(false);
	            bjoin.setBackgroundColor(Color.GRAY);
	            bcreate.setEnabled(false);
	            bcreate.setBackgroundColor(Color.GRAY);
	        }

	        else
	        {
	            bjoin.setEnabled(false);
	            bjoin.setBackgroundColor(Color.GRAY);
	            bcreate.setEnabled(true);
	            bcreate.setBackground(this.getResources().getDrawable(R.drawable.buttonsaction));
	        }
	    }
}

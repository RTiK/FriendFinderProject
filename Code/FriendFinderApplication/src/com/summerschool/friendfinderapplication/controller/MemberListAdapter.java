package com.summerschool.friendfinderapplication.controller;

import java.util.List;

import com.parse.ParseUser;
import com.summerschool.friendfinderapplication.R;
import com.summerschool.friendfinderapplication.activities.GroupDescriptionActivity;
import com.summerschool.friendfinderapplication.activities.MapActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MemberListAdapter extends ArrayAdapter<ParseUser> {
	private Context mContext;
	private List<ParseUser> mUsers;
	
	public MemberListAdapter(Context context, List<ParseUser> users) {
		super(context, R.id.memberListView, users);
		this.mContext = context;
		this.mUsers = users;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		//make sure we have a view (could be null)
		if(itemView == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
			itemView = mLayoutInflater.inflate(R.layout.member_item_view, parent, false);
		}
		
		final ParseUser currentUser = mUsers.get(position);
		//Set the text of the TextField to the right name and its onclicklistener
		TextView textView = (TextView) itemView.findViewById(R.id.item_member_name);
		textView.setText(currentUser.getUsername());
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "This is " + currentUser.getUsername(), Toast.LENGTH_SHORT).show();
			}
		});			
					
		return itemView;
	}



	public void onClickMapButton(final View v) {
		//TODO
		Intent intent = new Intent(mContext, MapActivity.class);
		//startActivity(intent);
	}
}

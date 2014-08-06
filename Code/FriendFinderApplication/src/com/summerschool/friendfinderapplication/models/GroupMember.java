package com.summerschool.friendfinderapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("GroupMemebers")
public class GroupMember extends ParseObject {

	public GroupMember() {
		
	}
	
	//Getter
	public String getMemberName() {
		return getString("Member");
	}
	public String getGroupName() {
		return getString("Group");
	}
	
	//Setter
	public void addMember(String memberName) {
		put("Member", memberName);
	}
	public void addGroup(String groupName) {
		put("Group",groupName);
	}
}

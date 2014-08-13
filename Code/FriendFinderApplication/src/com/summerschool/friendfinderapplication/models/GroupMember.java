package com.summerschool.friendfinderapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("GroupMemebers")
public class GroupMember extends ParseObject {
	
	//Getter
	public ParseUser getMember() {
		return (ParseUser) get("Member");
	}
	public Group getGroup() {
		return (Group) get("Group");
	}
	public boolean isGPSActive(){
		return getBoolean("GPSActive");
	}
	
	//Setter
	public void addMember(ParseUser member) {
		put("Member", member);
	}
	public void addGroup(Group group) {
		put("Group",group);
	}
	public void setGPSActive(boolean gpsActive){
		put("GPSActive",gpsActive);
	}
}

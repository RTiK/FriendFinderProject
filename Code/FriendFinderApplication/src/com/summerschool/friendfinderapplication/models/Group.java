package com.summerschool.friendfinderapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Group")
public class Group extends ParseObject {

	public Group() {

	}
	
	//Getters
	public String getName() {
		return getString("name");
	}	
	public String getDescription() {
		return getString("description");
	}
	
	//Setters	
	public void setName(String name) {
		put("name",name);
	}
	public void setDescription(String description) {
		put("description",description);
	}
	public void setOwner(ParseUser currentUser) {
		put("user", currentUser);
	}
	
}
package com.summerschool.friendfinderapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {

	public User() {
		
	}
	
	//Getters
	public String getName() {
		return getString("name");
	}	
	public boolean getGPSActive() {
		return this.getBoolean("GPSActive");
	}	
	public double getLocation() {
		return this.getLocation();
	}
	
	
	//Setters
	public void setName(String name) {
		put("name",name);
	}
	public void setGPSActive(boolean value) {
		this.put("GPSActive", value);
	}
	public void setLocation() {
		this.setLocation();
	}
}
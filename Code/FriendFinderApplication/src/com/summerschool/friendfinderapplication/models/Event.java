package com.summerschool.friendfinderapplication.models;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Event")
public class Event extends ParseObject {
	
	//Columns
	public final static String TITLE = "title";
	public final static String DATE = "date";
	public final static String LOCATION = "location";
	public final static String DESCRIPTION = "description";
	public final static String OWNER = "owner";
	public final static String GROUP = "group";
	public final static String EVENTMEMBER = "eventmember";
	
	//getters
	public String getTitle() {
		return getString(TITLE);
	}
	public Date getDate() {
		return getDate(DATE);
	}
	public ParseGeoPoint getLocation() {
		return getParseGeoPoint(LOCATION);
	}
	public String getDescription() {
		return getString(DESCRIPTION);
	}
	public ParseUser getOwner() {
		return getParseUser(OWNER);
	}
	public Group getGroup() {
		return (Group) get(GROUP);
	}
	
	//setters
	public void setTitle(String title) {
		put(TITLE, title);
	}
	public void setLocation(ParseGeoPoint location) {
		put(LOCATION, location);
	}
	public void setDescription(String description) {
		put(DESCRIPTION, description);
	}
	public void setDate(Date date) {
		put(DATE,date);
	}
	public void setOwner(ParseUser owner) {
		put(OWNER, owner);
	}
	public void setGroup(Group group) {
		put(GROUP, group);
	}
}
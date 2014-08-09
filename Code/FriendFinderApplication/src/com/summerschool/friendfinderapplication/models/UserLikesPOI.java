package com.summerschool.friendfinderapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("UserLikesPOI")
public class UserLikesPOI extends ParseObject {

	public final static String USER = "user";
	public final static String POI	= "poi";
	
	//getters
	public ParseUser getUser() {
		return getParseUser(USER);
	}
	public POI getPOI() {
		return (POI) get(POI);
	}
	
	//setters
	public void setUser(ParseUser user) {
		put(USER, user);
	}
	public void setPOI(POI poi) {
		put(POI,poi);
	}
}

package com.summerschool.friendfinderapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("POI")
public class POI extends ParseObject {

	public final static String NAME = "name";
	public final static String LOCATION = "location";
	public final static String DESCRIPTION = "description";
	public final static String CREATOR = "creator";
	public final static String GROUP = "group";
	public final static String USER_LIKES_POI = "user_poi_likes";
	
	//getters
	public String getName() {
		return getString(NAME);
	}
	public ParseGeoPoint getLocation() {
		return getParseGeoPoint(LOCATION);
	}
	public String getDescription() {
		return getString(DESCRIPTION);
	}
	public String getCreator() {
		return getString(CREATOR);
	}
	public Group getGroup() {
		return (Group) get(GROUP);
	}
	public UserLikesPOI getUserLikesPOI() {
		return (UserLikesPOI) get(USER_LIKES_POI);
	}
	
	//setters
	public void setName(String name)
	{
		put("name", name);
	}
	public void setDescription(String description)
	{
		put("description", description);
	}
	public void setCreator(ParseUser creatorPK)
	{
		put("creator", creatorPK);
	}
	public void setGPSLocation(ParseGeoPoint gps)
	{
		put("location", gps);
	}
	public void setGroup(Group g)
	{
		put("group", g);
	}
	
}
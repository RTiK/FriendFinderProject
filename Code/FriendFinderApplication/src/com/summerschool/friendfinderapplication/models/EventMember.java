package com.summerschool.friendfinderapplication.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("EventMember")
public class EventMember extends ParseObject {

	public final static String MEMBER = "member";
	public final static String EVENT = "event";

	//Getter
	public ParseUser getMember() {
		return (ParseUser) get(MEMBER);
	}
	public Event getEvent() {
		return (Event) get(EVENT);
	}
	
	//Setter
	public void addMember(ParseUser member) {
		put(MEMBER,member);
	}
	public void addEvent(Event event) {
		put(EVENT,event);
	}
}

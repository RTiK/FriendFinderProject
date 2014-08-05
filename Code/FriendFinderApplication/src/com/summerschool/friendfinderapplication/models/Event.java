package com.summerschool.friendfinderapplication.models;

public class Event {

	private String ID_Event;
	private String title;
	private double location;
	private String description;
	private String time;
	private String date;

	public Event(){
		
	}
	
	public String getTitle() {
		return this.title;
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public double getLocation() {
		return this.location;
	}

	/**
	 * 
	 * @param location
	 */
	public void setLocation(double location) {
		this.location = location;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public String getTime() {
		return this.time;
	}

	/**
	 * 
	 * @param time
	 */
	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return this.date;
	}

	/**
	 * 
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}

}
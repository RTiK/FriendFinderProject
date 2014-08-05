package com.summerschool.friendfinderapplication.models;

public class Group {

	private String ID_Group;
	private String name;
	private String description;
	private boolean gps;

	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
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

	public boolean getGPS(){
		return this.gps; 
	}
	
	/**
	 * 
	 * @param gps
	 */
	public void setGPS(boolean gps){
		this.gps = gps;
	}
	
	
}
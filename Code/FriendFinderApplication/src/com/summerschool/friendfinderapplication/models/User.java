package com.summerschool.friendfinderapplication.models;

public class User {

	private String ID_User;
	private String name;
	private double cLocation;
	private boolean GPSActivated;

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

	public double getCLocation() {
		return this.cLocation;
	}

	/**
	 * 
	 * @param cLocation
	 */
	public void setCLocation(double cLocation) {
		this.cLocation = cLocation;
	}

	public boolean getGPSActivated() {
		// TODO - implement User.getGPSActivated
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param GPSActivated
	 */
	public void setGPSActivated(boolean GPSActivated) {
		// TODO - implement User.setGPSActivated
		throw new UnsupportedOperationException();
	}

}
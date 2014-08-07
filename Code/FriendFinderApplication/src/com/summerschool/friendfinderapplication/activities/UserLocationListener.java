package com.summerschool.friendfinderapplication.activities;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class UserLocationListener implements LocationListener {
	
	private ParseUser mUser;
	
	public UserLocationListener(ParseUser user) {
		if (user != null)
			mUser = user;
		else
			System.err.println("ParseUser is null!");
	}

	@Override
	public void onLocationChanged(Location location) {
		mUser.put("location", new ParseGeoPoint(
				location.getLatitude(),
				location.getLongitude()
				));
		mUser.saveEventually();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}

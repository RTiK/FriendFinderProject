package com.summerschool.friendfinderapplication.activities;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

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
		if(location != null) {
			mUser.put("location", new ParseGeoPoint(
					location.getLatitude(),
					location.getLongitude()
					));
			mUser.saveInBackground();
			Log.i("UPDATE", "location updated: " + location.getLatitude() + ", " + location.getLongitude());
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i("TEST", "status changed");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.i("TEST", "provider enabled");
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.i("TEST", "provider diasabled");
	}

}

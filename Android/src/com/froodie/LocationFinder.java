package com.froodie;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class LocationFinder {
	private static LocationFinder finder = null;
	LocationManager locationManager = null;
	
	protected LocationFinder() {
		//to prevent external isntantiation
	}
	
	public static LocationFinder getInstance() {
		if (finder == null) {
			finder = new LocationFinder();
		}
		return finder;
	}
	
	public Location getCurrentLocation(Context appContext) {
		if (locationManager == null) {
			locationManager = (LocationManager)appContext.getSystemService(Context.LOCATION_SERVICE);
		}
		List<String> providers = locationManager.getProviders(true);
	    Location bestLocation = null;
	    for (String provider : providers) {
	        Location l = locationManager.getLastKnownLocation(provider);
	        if (l == null) {
	            continue;
	        }
	        if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
	            // Found best last known location: %s", l);
	            bestLocation = l;
	        }
	    }
	    return bestLocation;
		
	}	
}

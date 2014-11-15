package com.froodie;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class LocationUpdateService extends Service implements LocationListener
{	

	private int notificationId = 0;
	private final int TIME_SENSITIVITY = 30000; // milliseconds
	private final int DISTANCE_SENSITIVITY = -1;  // meters. if negative, then distance is not used, only the time.
	private final double RADIUS = 0.005;
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
	
	@Override
	public void onLocationChanged(Location currentLoc)
	{			
		System.out.println("TOOTSIE! locaParseObjectd");
		System.out.println("TOOTSIE! CURR LAT:" + currentLoc.getLatitude() + " LONG:" + currentLoc.getLongitude());
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
		query.whereLessThan("latitude", currentLoc.getLatitude() + RADIUS);
		query.whereGreaterThan("latitude", currentLoc.getLatitude() - RADIUS);
		query.whereLessThan("longitude", currentLoc.getLongitude() + RADIUS);
		query.whereGreaterThan("longitude", currentLoc.getLongitude() - RADIUS);
		query.findInBackground(new FindCallback<ParseObject>() {
			
	        @Override
	        public void done(List<ParseObject> objects, com.parse.ParseException e) {
	            if (e == null) {
	            	for(int i = 0; i < objects.size(); i++) {
		            	System.out.println("TOOTSIE RETRIEVED OBJ LAT: " + objects.get(i).get("latitude") + " LONG: " + objects.get(i).get("longitude"));
	            	}

	            } else {
	                Log.d("App", "Error: " + e.getMessage());
	            }
	        }

	    });
	}
	
	// Creates a notification and displays it on System tray 
	@SuppressLint("NewApi")
	void makeNotification() {
		System.out.println("TOOTSIE! making notif nbd");

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("ALERT!")
		.setContentText("Frood Nearby")
		.setAutoCancel(true);
	Intent baseIntent = new Intent(this, MainActivity.class);
	TaskStackBuilder stack_builder = TaskStackBuilder.create(this);
	stack_builder.addParentStack(MainActivity.class);
	stack_builder.addNextIntent(baseIntent);
	PendingIntent pending_intent = stack_builder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
	builder.setContentIntent(pending_intent);
	NotificationManager notification_manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	notification_manager.notify(notificationId,builder.build());	
	}
	
	@Override
	// Sets up the providers from which to get location information
    public void onCreate()
	{
		System.out.println("TOOTSIE! in service");
		LocationManager location_manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_SENSITIVITY, DISTANCE_SENSITIVITY, (android.location.LocationListener) this);
		location_manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_SENSITIVITY, DISTANCE_SENSITIVITY, (android.location.LocationListener) this);
    }
	
	@Override
	public void onProviderDisabled(String provider) {}
	
	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}
package com.froodie;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class LocationUpdateService extends Service implements LocationListener
{	

	private int notificationId = 0;
	private final int TIME_SENSITIVITY = 30000; // milliseconds
	private final int DISTANCE_SENSITIVITY = -1;  // meters. if negative, then distance is not used, only the time.
	private final float RADIUS = 0.005;
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
	
	@Override
	public void onLocationChanged(Location currentLoc)
	{			System.out.println("TOOTSIE! locaParseObjectd");
	
	ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
	query.whereLessThan("latitude", currentLoc.getLatitude() + RADIUS);
	query.whereGreaterThan("latitude", currentLoc.getLatitude() - RADIUS);
	query.whereLessThan("longitude", currentLoc.getLatitude() + RADIUS);
	query.whereGreaterThan("longitude", currentLoc.getLatitude() - RADIUS);
	query.findInBackground(new FindCallback<ParseObject>() {
	    public void done(List<ParseObject> scoreList, ParseException e) {
	        if (e == null) {
	            Log.d("score", "Retrieved " + scoreList.size() + " scores");
	        } else {
	            Log.d("score", "Error: " + e.getMessage());
	        }
	    }
	});
	
	// Creates a notification and displays it on System tray 
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
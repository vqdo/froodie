package com.froodie;


import com.froodie.db.ParseEvent;
import com.parse.Parse;
import com.parse.ParseObject;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends Activity {

	Context localContext;
	LocationFinder finder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.err.println("TOOTSIE");
		setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.report_food_button);
		button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                promptForFoodInfo();
            }
        });
		localContext = this;
		ParseManager pm = new ParseManager();
		pm.initialize(this);
		initializeWebview();
		Intent intent = new Intent(this, LocationUpdateService.class);
		startService(intent);
	}
	
	private void initializeWebview() {
		WebView webView = (WebView) findViewById(R.id.events_webview);
		webView.setWebViewClient(new WebViewClient());		

		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setGeolocationEnabled(true);
		
		webView.loadUrl("http://victoriado.com/froodie");
						
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void promptForFoodInfo() {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			// Add the buttons
			LayoutInflater inflater = this.getLayoutInflater();

		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
			final View dialogLayout = inflater.inflate(R.layout.frood_info, null);
		    builder.setView(dialogLayout);
			builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			           }
			       });
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   ParseEvent event = generateParseEvent(dialogLayout);
			        	   ParseObject o = event.toParseObject();
			        	   o.saveInBackground();
			           }
			       });

			AlertDialog dialog = builder.create();

			    // Inflate and set the layout for the dialog
			    // Pass null as the parent view because its going in the dialog layout
			    builder.setView(inflater.inflate(R.layout.frood_info, null));
			dialog.show();
	}
	
	private ParseEvent generateParseEvent(View containingView) {
		String eventName = ((EditText) containingView.findViewById(R.id.event_name)).getText().toString();
		String location= ((EditText) containingView.findViewById(R.id.event_location)).getText().toString();
		String description= ((EditText) containingView.findViewById(R.id.event_description)).getText().toString();
		String food= ((EditText) containingView.findViewById(R.id.event_food)).getText().toString();
		finder = LocationFinder.getInstance();
		Location loc = finder.getCurrentLocation(this);
		ParseEvent event = new ParseEvent(eventName);
		event.setLocation(location);
		event.setDescription(description);
		event.setFood(food);
		event.setLatitude(loc.getLatitude());
		event.setLongitude(loc.getLongitude());	
		return event;
		//Toast toast = Toast.makeText(this, "Name: " + event.getName() + "\nLocation " + event.getLocation() + "\nDescription: " + event.getDescription() + "\nFood: " + event.getFood() + "\nLAT: " + event.getLatitude() + "\nLONG " + event.getLongitude(), Toast.LENGTH_SHORT);
		//toast.show();
	}
}

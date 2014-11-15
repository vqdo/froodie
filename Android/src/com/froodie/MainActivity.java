package com.froodie;


import com.froodie.db.ParseEvent;
import com.parse.Parse;
import com.parse.ParseObject;

import android.location.Location;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.GeolocationPermissions;


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
	
	@SuppressLint("NewApi")
	private void initializeWebview() {
		WebView webView = (WebView) findViewById(R.id.events_webview);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
			    super.onPageFinished(view, url);
			    view.clearCache(true);
			}				
		});		
		webView.setWebChromeClient(new WebChromeClient() {
			
			@Override
			public void onConsoleMessage(String message, int lineNumber, String sourceID) {
			    Log.d("MyApplication", message + " -- From line "
			                         + lineNumber + " of "
			                         + sourceID);
			  }
			
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
				super.onGeolocationPermissionsShowPrompt(origin, callback);
				callback.invoke(origin, true, false);
			}		
		});

		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setGeolocationEnabled(true);
		settings.setAllowUniversalAccessFromFileURLs(true);
		settings.setAllowFileAccessFromFileURLs(true);
		settings.setAllowContentAccess(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setDomStorageEnabled(true);		
		
		webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);		
		webView.addJavascriptInterface(new FroodieWebInterface(this), "Froodie");		
		
		//webView.loadUrl("file:///android_asset/index.html");
		webView.loadUrl("http://victoriado.com/froodie/index.html");
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
//		Toast toast = Toast.makeText(this, "Name: " + event.getName() + "\nLocation " + event.getLocation() + "\nDescription: " + event.getDescription() + "\nFood: " + event.getFood() + "\nLAT: " + event.getLatitude() + "\nLONG " + event.getLongitude(), Toast.LENGTH_SHORT);
//		toast.show();
		return event;		
	}
	
	
	public class FroodieWebInterface {
	Context mContext;
	
	/** Instantiate the interface and set the context */
	FroodieWebInterface(Context c) {
	    mContext = c;
	}
	
	/** Show a toast from the web page */
	@JavascriptInterface
	public String getPosition() {
		Location loc = LocationFinder.getInstance().getCurrentLocation(localContext);
		System.out.println(loc);
		return "{ latitude: " + loc.getLatitude() 
				+ ", longitude: " + loc.getLongitude() + "}";
	}
}		
}

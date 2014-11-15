package com.froodie;


import com.parse.Parse;
import com.parse.ParseObject;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

	Context localContext;
	LocationFinder finder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		    builder.setView(inflater.inflate(R.layout.frood_info, null));
			builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			           }
			       });
			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   finder = LocationFinder.getInstance();
			        	   Location loc = finder.getCurrentLocation(localContext);
			        	   Toast toast = Toast.makeText(localContext, "LATLONG:" + loc.getLatitude() + "," +loc.getLongitude(), Toast.LENGTH_SHORT);
			        	   toast.show();
			           }
			       });

			AlertDialog dialog = builder.create();

			    // Inflate and set the layout for the dialog
			    // Pass null as the parent view because its going in the dialog layout
			    builder.setView(inflater.inflate(R.layout.frood_info, null));
			dialog.show();
	}
}
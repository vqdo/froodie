package com.froodie;

import com.parse.Parse;
import com.parse.ParseObject;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ParseManager pm = new ParseManager();
		pm.initialize(this);
		
		initializeWebview();
		
		
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
	
	

}

package com.froodie;

import android.content.Context;

import com.parse.Parse;

public class ParseManager {
	private final static String appId = "tsLX6wJX2RBrRKjnmIrMxe5BG3gbevOpg2mnK6Po";
	private final static String clientId = "4Ww6Vbn3pLgVhzkYYBGWcFtD5KkBfqZ9XBpzWSsC";
	
	public void initialize(Context context) {
		Parse.initialize(context, appId, clientId);		
	}
	
}

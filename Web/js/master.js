var froodie = (function() {
	var APP_ID = "tsLX6wJX2RBrRKjnmIrMxe5BG3gbevOpg2mnK6Po";
	var JS_KEY = "rFQvJJ1WQn5ZBo7CLpJWIR95fWhriXY3P9oHEe3S";
	var DEFAULT_RADIUS_DEG = 0.05;

	var instance = {};
	var DB_EVENTS = "EventList";
	var sampleData = {
		latitude: '0',
		longitude: '0', 
		name: "Hackathon",
		location: 'Jacobs Hall Room 1000',
		food: "Pizza",
		description: "Participants get food! YUM!",
		numLikes: 0,
		numDislikes: 0
	}

	var lastLocation = null;

	instance.init = function() {
		if ("geolocation" in navigator) {
			navigator.geolocation.getCurrentPosition(function(position) {
			  	lastLocation = {
			  		latitude: position.coords.latitude, 
			  		longitude: position.coords.longitude
			  	};
				instance.initializeParse();			  
			});			
		} else {
			$('body').append("Location detection is not enabled for your device.")
		}		
	}

	instance.initializeParse = function() {
		Parse.initialize(APP_ID, JS_KEY);
		var p_events = Parse.Object.extend(DB_EVENTS);
		var p_eventsList = Parse.Query(p_events);

		instance.getListItems();
		// p_events.save(sampleData).then(function(object) {
		//   console.log(object);
		// });	
	}

	instance.getListItems = function() {
		console.log(instance.getCoordinateRange());
		//var query = new Parse.Query(DB_EVENTS);
	}

	instance.getCoordinateRange = function(radius) {
		// ignore radius for now
		return {
			latitude: [lastLocation.latitude - DEFAULT_RADIUS_DEG,
				lastLocation.latitude + DEFAULT_RADIUS_DEG],
			longitude: [lastLocation.longitude - DEFAULT_RADIUS_DEG,
				lastLocation.longitude + DEFAULT_RADIUS_DEG]
		}
	}

	return instance;

})();

$(document).ready(function() {
	froodie.init();
});

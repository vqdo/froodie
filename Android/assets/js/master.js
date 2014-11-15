var froodie = (function() {
	var APP_ID = "tsLX6wJX2RBrRKjnmIrMxe5BG3gbevOpg2mnK6Po";
	var JS_KEY = "rFQvJJ1WQn5ZBo7CLpJWIR95fWhriXY3P9oHEe3S";
	var DEFAULT_RADIUS_DEG = 0.05;

	var instance = {};
	var DB_EVENTS = "Event";
	var sampleData = {
		latitude: '32.8817181',
		longitude: '-117.235544', 
		name: "Hackathon",
		location: 'Jacobs Hall Room 1000',
		food: "Pizza",
		description: "Participants get food! YUM!",
		numLikes: 0,
		numDislikes: 0
	}

	var lastLocation = null;

	instance.p_event = null;

	instance.init = function() {
		// if(instance.hasLocalStorageSupport()) {
		// 	var cachedData = localStorage['events'];
		// 	if(cachedData) {
		// 		cachedData = JSON.parse(cachedData);
		// 		//instance.displayListItems(cachedData);
		// 	}
		// }
		
		instance.initializeParse();	
		instance.watchPosition();
		if ("geolocation" in navigator) {
			$('.status').html("Loading...");			
			
		} else {
			$('.status').append("Location detection is not enabled for your device.")
		}		
	}

	instance.watchPosition = function() {
		if(window.Froodie) {
			lastLocation = JSON.parse(Froodie.getPosition()); 
			console.log(lastLocation);
		  	//$('.status').html("Latitude: " + lastLocation.latitude);
		  	instance.getListItems();			

		} else {
			navigator.geolocation.watchPosition(
				function(position) {
				  	lastLocation = {
				  		latitude: position.coords.latitude, 
				  		longitude: position.coords.longitude
				  	};
				  	$('.status').html(lastLocation.latitude);
				  	instance.getListItems();
				}, 
				function(err) {
					$('.status').html("Could not get position.");
				}

			);			
		}	
	}

	instance.initializeParse = function() {
		Parse.initialize(APP_ID, JS_KEY);
	
		instance.p_event = Parse.Object.extend(DB_EVENTS);

		//instance.doSample();
		// p_events.save(sampleData).then(function(object) {
		//   console.log(object);
		// });	
	}

	instance.doSample = function() {
		var evt = new instance.p_event();
		//evt.set('name', sampleData.name);
		evt.set('longitude', sampleData.longitude);
		//evt.set('longitude', String(sampleData.longitude));
		evt.save(null, {
			success: instance.getListItems
		});
	}

	instance.getListItems = function() {
		var range = instance.getCoordinateRange();

		var query = new Parse.Query(DB_EVENTS);
		query
			.lessThan('latitude', range.latitude[1])
			 .greaterThan('latitude', range.latitude[0])
			 .lessThan('longitude', range.longitude[1])
			 .greaterThan('latitude', range.latitude[0])
			//.startsWith('name', "huh")
			.find({
				success: function(result) {
					if(instance.hasLocalStorageSupport()) {	
						localStorage['events'] = JSON.stringify(result);
					}	
					instance.displayListItems(result);
				}
			});
	}

	instance.displayListItems = function(result) {
		var ul = $('.events-list');
		ul.empty();

		$.each(result, function(i, evt) {
			var item = $('<li />').addClass('event-list-item');

			var name = $('<div />').addClass('event-name cf').html(
				'<strong class="event-label">Name</strong> <span class="event-value">' 
				+ (evt.name || (evt.attributes && evt.attributes.name) || "") + "</span>");
			item.append(name);

			var loc = $('<div />').addClass('event-location cf').html(
				'<strong class="event-label">Location</strong> <span class="event-value">' 
				+ (evt.location || (evt.attributes && evt.attributes.location) || "") + "</span>");
			item.append(loc);			

			var food = $('<div />').addClass('event-food cf').html(
				'<strong class="event-label">On the Menu</strong> <span class="event-value">' 
				+ (evt.food || (evt.attributes && evt.attributes.food) || "?") + "</span>");
			item.append(food);	

			var description = $('<div />').addClass('event-food cf').html(
				'<strong class="event-label">Description</strong> <span class="event-value">' 
				+ (evt.description || (evt.attributes && evt.attributes.description) || "") + "</span>");
			item.append(description);	

			ul.append(item);
		});
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

	instance.hasLocalStorageSupport = function() {
		try {
		    return 'localStorage' in window && window['localStorage'] !== null;
		  } catch (e) {
		    return false;
		  }		
	}

	return instance;

})();

$(document).ready(function() {
	froodie.init();
});

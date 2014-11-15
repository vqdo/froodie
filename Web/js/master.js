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
	var lastCount = 0;

	instance.p_event = null;

	instance.init = function() {
		// if(instance.hasLocalStorageSupport()) {
		// 	var cachedData = localStorage['events'];
		// 	if(cachedData) {
		// 		cachedData = JSON.parse(cachedData);
		// 		//instance.displayListItems(cachedData);
		// 	}
		// }

		if ("geolocation" in navigator) {
			instance.initializeParse();			
			navigator.geolocation.watchPosition(
				function(position) {
				  	lastLocation = {
				  		latitude: position.coords.latitude, 
				  		longitude: position.coords.longitude
				  	};
				  	$('.status').empty();
				  	instance.getListItems();
				}, 
				function(err) {
					$('.status').html("Could not get position.");
				}

			);			
		} else {
			$('.status').append("Location detection is not enabled for your device.")
		}		

		setInterval(function() {
			instance.getListItems();
		}, 3000);
	}

	instance.handleEventClick = function() {
		$('.event-list-item').click(function(e) {
			//$('body').css('background-color', 'white');
			//console.log($(this).data('latitude'));
			//var info = $(this).data('info');
			if(window.Froodie) {				
				Froodie.onEventClick($(this).data('latitude'), $(this).data('longitude'));
			}
		});
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
			 .descending('createdAt')
			//.startsWith('name', "huh")
			.find({
				success: function(result) {
					// if(instance.hasLocalStorageSupport()) {	
					// 	localStorage['events'] = JSON.stringify(result);
					// }	
					console.log("Got result!");
					instance.displayListItems(result);
				}
			});
	}

	instance.displayListItems = function(result) {
		console.log(result);
		var ul = $('.events-list');
		ul.empty();

		$.each(result, function(i, evt) {
			var item = $('<li />').addClass('event-list-item');
			$(item).data("latitude", evt.attributes.latitude);
			$(item).data("longitude", evt.attributes.longitude);

			var lastSpotted = instance.getLastSpotted(evt.createdAt || (evt.attributes && evt.createdAt));

			var name = $('<div />').addClass('event-name cf').html(
				'<strong class="event-label">' 
				+ (evt.name || (evt.attributes && evt.attributes.name) || '&nbsp;') + '</strong>');
			name.append($('<span/>').addClass('event-timestamp').html(lastSpotted + " hour(s) ago"));
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

			item.appendTo(ul);
			
		});

		instance.handleEventClick();
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

	instance.getLastSpotted = function(time) {
		var now = new Date();
		var diff = new Date(Date.parse(time));
		var hours =  now.getHours() - diff.getHours();
		return hours;
	}

	return instance;

})();

$(document).ready(function() {
	froodie.init();
});

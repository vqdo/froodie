(function() {
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

	Parse.initialize("tsLX6wJX2RBrRKjnmIrMxe5BG3gbevOpg2mnK6Po", "rFQvJJ1WQn5ZBo7CLpJWIR95fWhriXY3P9oHEe3S");
	var TestObject = Parse.Object.extend("TestObject");
	var testObject = new TestObject();
	testObject.save(sampleData).then(function(object) {
	  console.log(object);
	});	
})();
package com.froodie.db;

public class ParseEvent {
	double latitude;
	double longitude; 
	String name;
	String location;
	String food;
	String description;
	int numLikes;
	int numDislikes;
	
	public ParseEvent(String name) {
		this.name = name;
	}
	
	public ParseEvent(ParseEvent event) {
		this.latitude = event.latitude;
		this.longitude = event.longitude;
		this.name = event.name;
		this.location = event.location;
		this.food = event.food;
		this.description = event.description;
		this.numLikes = event.numLikes;
		this.numDislikes = event.numDislikes;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumLikes() {
		return numLikes;
	}

	public void setNumLikes(int numLikes) {
		this.numLikes = numLikes;
	}

	public int getNumDislikes() {
		return numDislikes;
	}

	public void setNumDislikes(int numDislikes) {
		this.numDislikes = numDislikes;
	}
	
	

}
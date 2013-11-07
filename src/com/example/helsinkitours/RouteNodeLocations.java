package com.example.helsinkitours;

public class RouteNodeLocations {
	private String streetName;
	private String time;
	private double latitude;
	private double longitude;
	
	public RouteNodeLocations( String name, String time, double latitude, double longitude ) {
		this.streetName = name;
		this.time = time;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public RouteNodeLocations( ) {
	}
	
	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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
	
	
	
}

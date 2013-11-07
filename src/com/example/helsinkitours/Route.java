package com.example.helsinkitours;

import java.util.ArrayList;

public class Route {
	private double routeLength;
	private double routeDuration;
	private ArrayList<RouteNode> routeNodes;
	
	public Route( double routeLength, double routeDuration, ArrayList<RouteNode> routeNodes ) {
		this.routeLength = routeLength;
		this.routeDuration = routeDuration;
		this.routeNodes = routeNodes;
	}
	
	public Route( ) {
	}

	public double getRouteLength() {
		return routeLength;
	}

	public void setRouteLength(double routeLength) {
		this.routeLength = routeLength;
	}

	public double getRouteDuration() {
		return routeDuration;
	}

	public void setRouteDuration(double routeDuration) {
		this.routeDuration = routeDuration;
	}

	public ArrayList<RouteNode> getRouteNodes() {
		return routeNodes;
	}

	public void setRouteNodes(ArrayList<RouteNode> routeNodes) {
		this.routeNodes = routeNodes;
	}
	
}

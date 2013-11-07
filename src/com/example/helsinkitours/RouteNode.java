package com.example.helsinkitours;

import java.util.ArrayList;

public class RouteNode {
	private String nodetype;
	private String nodeTransportCode;
	private ArrayList<RouteNodeLocations> nodeLocations;
	private ArrayList<Double []> shapeNodes;

	public RouteNode( String type, ArrayList<RouteNodeLocations> nodeLocations, ArrayList<Double []> shapeNodes ) {
		this.nodetype = type;
		this.nodeLocations = nodeLocations;
		this.shapeNodes = shapeNodes;
	}
	
	public RouteNode( ) {
	}
	
	public String getNodetype() {
		return nodetype;
	}

	public void setNodetype(String nodetype) {
		this.nodetype = nodetype;
	}

	public ArrayList<RouteNodeLocations> getNodeLocations() {
		return nodeLocations;
	}

	public void setNodeLocations(ArrayList<RouteNodeLocations> nodeLocations) {
		this.nodeLocations = nodeLocations;
	}

	public ArrayList<Double[]> getShapeNodes() {
		return shapeNodes;
	}

	public void setShapeNodes(ArrayList<Double[]> shapeNodes) {
		this.shapeNodes = shapeNodes;
	}

	public String getNodeTransportCode() {
		return nodeTransportCode;
	}

	public void setNodeTransportCode(String nodeTransportCode) {
		this.nodeTransportCode = nodeTransportCode;
	}
	
}

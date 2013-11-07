package com.example.helsinkitours;


import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;

public class ToursXMLParser {

	public ArrayList<Attraction> parseAttractionXML( ArrayList<String> attractionCategories, int xmlFile, MainActivity main_activity ) throws IOException, XmlPullParserException, NotFoundException {
		ArrayList<Attraction> attractionList = new ArrayList<Attraction>();
		XmlResourceParser xpp = main_activity.getResources().getXml(xmlFile);
		xpp.next();
        int eventType = xpp.getEventType();
    	String name = "";
    	double latitude = 0;
    	double longitude = 0;
    	String category = "";
    	String description = "";
    	int parserState = 0;
        while (eventType != XmlPullParser.END_DOCUMENT) {
        	if (eventType == XmlPullParser.START_TAG) {
        		if ( xpp.getName().equals("title") ) {
        			parserState = 1;
        		}
        		if ( xpp.getName().equals("type1") ) {
        			parserState = 2;
        		}
        		if ( xpp.getName().equals("longitude") ) {
        			parserState = 3;
        		}
        		if ( xpp.getName().equals("latitude") ) {
        			parserState = 4;
        		}
        		if ( xpp.getName().equals("description") ) {
        			parserState = 5;
        		}
        	}
        	if (eventType == XmlPullParser.TEXT) {
        		if ( parserState == 1 && xpp.getText() != null ) {
        			name = xpp.getText();
        			parserState = 0;
        		}
        		if ( parserState == 2 && xpp.getText() != null ) {
        			if ( attractionCategories.size() > 0 ) {
	        			for ( String cat : attractionCategories ) {
		        			if ( xpp.getText().indexOf(cat) != -1 ) {
		        				category = xpp.getText();
		        				parserState = 0;
		        			}
	        			}
        			} else {
        				category = xpp.getText();
        				parserState = 0;
        			}
        		}
        		if ( parserState == 3 && xpp.getText() != null ) {
        			longitude = Double.parseDouble(xpp.getText());
        			parserState = 0;
        		}
        		if ( parserState == 4 && xpp.getText() != null ) {
        			latitude = Double.parseDouble(xpp.getText());
        			parserState = 0;
        		}
        		if ( parserState == 5 && xpp.getText() != null ) {
        			description = xpp.getText();
        			parserState = 0;
        		}
        	}
        	if (eventType == XmlPullParser.END_TAG) {
        		if (xpp.getName().equals("item")) {
        			if ( !category.isEmpty() && !name.isEmpty() ) {
	        			attractionList.add(new Attraction(name, latitude, longitude, category, description));
        			}
        	    	name = "";
        	    	latitude = 0;
        	    	longitude = 0;
        	    	category = "";
        		}
        	}
        	eventType = xpp.next();
        }
        return attractionList;
	}
	
	
	
	public Route parseRouteXML( String xmlData ) throws IOException, XmlPullParserException, NotFoundException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(new StringReader(xmlData));

		boolean parserFinished = false;
		double routeLength = 0.0;
		double routeDuration = 0.0;
		Double [] shapeCoords = null;
		ArrayList<Double []> shapeNodes = null;
		ArrayList<RouteNode> routeNodes = null;
		ArrayList<RouteNodeLocations> nodeLocations = null;
		Route route = null;
		RouteNode routeNode = null;
		RouteNodeLocations routeNodeLocsNode = null;
		
		String parserState = "response";
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT || parserFinished == false ) {
        	if (eventType == XmlPullParser.START_TAG) {
        		if ( xpp.getName().equals("node") && parserState == "response" ) {
        			parserState = "node1";
        		}
        		if ( xpp.getName().equals("node") && parserState == "node1" ) {
        			parserState = "node2";
        			route = new Route();
        		}
        		if ( xpp.getName().equals("length") && parserState == "node2" ) {
        			parserState = "length";
        		}
        		if ( xpp.getName().equals("duration") && parserState == "node2" ) {
        			parserState = "duration";
        		}
        		if ( xpp.getName().equals("legs") && parserState == "node2" ) {
        			parserState = "legs";
        			routeNodes = new ArrayList<RouteNode>();
        		}
        		if ( xpp.getName().equals("node") && parserState == "legs" ) {
        			parserState = "legsNode";
        			routeNode = new RouteNode();
        		}
        		if ( xpp.getName().equals("length") && parserState == "legsNode" ) {
        			parserState = "legsNodeLength";
        		}
        		if ( xpp.getName().equals("duration") && parserState == "legsNode" ) {
        			parserState = "legsNodeDuration";
        		}
        		if ( xpp.getName().equals("type") && parserState == "legsNode" ) {
        			parserState = "legsNodeType";
        		}
        		if ( xpp.getName().equals("code") && parserState == "legsNode" ) {
        			parserState = "legsNodeCode";
        		}
        		if ( xpp.getName().equals("locs") && parserState == "legsNode" ) {
        			parserState = "legsNodeLocs";
        			nodeLocations = new ArrayList<RouteNodeLocations>();
        		}
        		if ( xpp.getName().equals("node") && parserState == "legsNodeLocs" ) {
        			parserState = "legsNodeLocsNode";
        			routeNodeLocsNode = new RouteNodeLocations();
        		}
        		if ( xpp.getName().equals("coord") && parserState == "legsNodeLocsNode" ) {
        			parserState = "legsNodeLocsNodeCoord";
        		}
        		if ( xpp.getName().equals("x") && parserState == "legsNodeLocsNodeCoord" ) {
        			parserState = "legsNodeLocsNodeCoordX";
        		}
        		if ( xpp.getName().equals("y") && parserState == "legsNodeLocsNodeCoord" ) {
        			parserState = "legsNodeLocsNodeCoordY";
        		}
        		if ( xpp.getName().equals("depTime") && parserState == "legsNodeLocsNode" ) {
        			parserState = "legsNodeLocsNodeDepTime";
        		}
        		if ( xpp.getName().equals("name") && parserState == "legsNodeLocsNode" ) {
        			parserState = "legsNodeLocsNodeStreetName";
        		}
        		if ( xpp.getName().equals("shape") && parserState == "legsNode" ) {
        			parserState = "legsNodeShape";
        			shapeNodes = new ArrayList<Double[]>();
        		}
        		if ( xpp.getName().equals("node") && parserState == "legsNodeShape" ) {
        			parserState = "legsNodeShapeNode";
        			shapeCoords = new Double[2];
        		}
        		if ( xpp.getName().equals("x") && parserState == "legsNodeShapeNode" ) {
        			parserState = "legsNodeShapeNodeX";
        		}
        		if ( xpp.getName().equals("y") && parserState == "legsNodeShapeNode" ) {
        			parserState = "legsNodeShapeNodeY";
        		}
        	}
        	if (eventType == XmlPullParser.TEXT) {
        		if ( parserState == "length" && xpp.getText() != null ) {
        			routeLength = Double.parseDouble(xpp.getText());
        			route.setRouteLength(routeLength);
        		}
        		if ( parserState == "duration" && xpp.getText() != null ) {
        			routeDuration = Double.parseDouble(xpp.getText());
        			route.setRouteDuration(routeDuration);
        		}
        		if ( parserState == "legsNodeType" && xpp.getText() != null ) {
        			routeNode.setNodetype(xpp.getText());
        		}
        		if ( parserState == "legsNodeCode" && xpp.getText() != null ) {
        			routeNode.setNodeTransportCode(xpp.getText());
        		}
        		if ( parserState == "legsNodeLocsNodeCoordX" && xpp.getText() != null ) {
        			routeNodeLocsNode.setLongitude(Double.parseDouble(xpp.getText()));
        		}
        		if ( parserState == "legsNodeLocsNodeCoordY" && xpp.getText() != null ) {
        			routeNodeLocsNode.setLatitude(Double.parseDouble(xpp.getText()));
        		}
        		if ( parserState == "legsNodeLocsNodeDepTime" && xpp.getText() != null ) {
        			routeNodeLocsNode.setTime(xpp.getText());
        		}
        		if ( parserState == "legsNodeLocsNodeStreetName" && xpp.getText() != null ) {
        			routeNodeLocsNode.setStreetName(xpp.getText());
        		}
        		if ( parserState == "legsNodeShapeNodeX" && xpp.getText() != null ) {
        			shapeCoords[0] = Double.parseDouble(xpp.getText());
        		}
        		if ( parserState == "legsNodeShapeNodeY" && xpp.getText() != null ) {
        			shapeCoords[1] = Double.parseDouble(xpp.getText());
        		}
        	}
        	if (eventType == XmlPullParser.END_TAG) {
        		if ( xpp.getName().equals("node") && parserState == "node1" ) {
        			parserState = "response";
        		}
        		if ( xpp.getName().equals("node") && parserState == "node2" ) {
        			parserState = "node1";
        			parserFinished = true;
        		}
        		if ( xpp.getName().equals("length") && parserState == "length" ) {
        			parserState = "node2";
        		}
        		if ( xpp.getName().equals("duration") && parserState == "duration" ) {
        			parserState = "node2";
        		}
        		if ( xpp.getName().equals("legs") && parserState == "legs" ) {
        			parserState = "node2";
        			route.setRouteNodes(routeNodes);
        		}
        		if ( xpp.getName().equals("node") && parserState == "legsNode" ) {
        			parserState = "legs";
        			routeNodes.add(routeNode);
        		}
        		if ( xpp.getName().equals("length") && parserState == "legsNodeLength" ) {
        			parserState = "legsNode";
        		}
        		if ( xpp.getName().equals("duration") && parserState == "legsNodeDuration" ) {
        			parserState = "legsNode";
        		}
        		if ( xpp.getName().equals("type") && parserState == "legsNodeType" ) {
        			parserState = "legsNode";
        		}
        		if ( xpp.getName().equals("code") && parserState == "legsNodeCode" ) {
        			parserState = "legsNode";
        		}
        		if ( xpp.getName().equals("locs") && parserState == "legsNodeLocs" ) {
        			parserState = "legsNode";
        			routeNode.setNodeLocations(nodeLocations);
        		}
        		if ( xpp.getName().equals("node") && parserState == "legsNodeLocsNode" ) {
        			parserState = "legsNodeLocs";
        			nodeLocations.add(routeNodeLocsNode);
        		}
        		if ( xpp.getName().equals("coord") && parserState == "legsNodeLocsNodeCoord" ) {
        			parserState = "legsNodeLocsNode";
        		}
        		if ( xpp.getName().equals("x") && parserState == "legsNodeLocsNodeCoordX" ) {
        			parserState = "legsNodeLocsNodeCoord";
        		}
        		if ( xpp.getName().equals("y") && parserState == "legsNodeLocsNodeCoordY" ) {
        			parserState = "legsNodeLocsNodeCoord";
        		}
        		if ( xpp.getName().equals("depTime") && parserState == "legsNodeLocsNodeDepTime" ) {
        			parserState = "legsNodeLocsNode";
        		}
        		if ( xpp.getName().equals("name") && parserState == "legsNodeLocsNodeStreetName" ) {
        			parserState = "legsNodeLocsNode";
        		}
        		if ( xpp.getName().equals("shape") && parserState == "legsNodeShape" ) {
        			parserState = "legsNode";
        			routeNode.setShapeNodes(shapeNodes);
        		}
        		if ( xpp.getName().equals("node") && parserState == "legsNodeShapeNode" ) {
        			parserState = "legsNodeShape";
        			shapeNodes.add(shapeCoords);
        		}
        		if ( xpp.getName().equals("x") && parserState == "legsNodeShapeNodeX" ) {
        			parserState = "legsNodeShapeNode";
        		}
        		if ( xpp.getName().equals("y") && parserState == "legsNodeShapeNodeY" ) {
        			parserState = "legsNodeShapeNode";
        		}
        	}
        	eventType = xpp.next();
        }
        return route;
	}
	
}









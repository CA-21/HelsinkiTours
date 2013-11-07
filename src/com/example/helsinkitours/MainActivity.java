package com.example.helsinkitours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends Activity implements LocationListener {
    private LocationManager locationManager;
    private String provider;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
    }
    
    public void onClick ( View view ) {
    	if ( view.getId() == R.id.button1 ) {
    		ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar1);
    		bar.setVisibility(View.VISIBLE);
    		if ( !locationManager.isProviderEnabled(provider) ) {
    			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    			builder.setMessage("In order to continue, you have to enable GPS").setTitle("Enable GPS");
    			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar1);
		        	   bar.setVisibility(View.INVISIBLE);
		        	   Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
		        	   startActivity(gpsOptionsIntent);
		           }
		        });
    			AlertDialog dialog = builder.create();
    			dialog.show();
    		} else {
    			createTourAutomatically();
    		}
    	} else if ( view.getId() == R.id.button2 ) {
    		ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar1);
    		bar.setVisibility(View.VISIBLE);
    		if ( !locationManager.isProviderEnabled(provider) ) {
    			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    			builder.setMessage("In order to continue, you have to enable GPS").setTitle("Enable GPS");
    			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar1);
		        	   bar.setVisibility(View.INVISIBLE);
		        	   Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
		        	   startActivity(gpsOptionsIntent);
		           }
		        });
    			AlertDialog dialog = builder.create();
    			dialog.show();
    		} else {
    			createTourManually();
    		}
    	}
	}

    @SuppressWarnings("unchecked")
	public void createTourAutomatically() {
        locationManager.requestSingleUpdate(provider, this, getMainLooper());
        Location location = locationManager.getLastKnownLocation(provider);
        
		ToursXMLParser parser = new ToursXMLParser();
		ArrayList<Attraction> attractionList_full = new ArrayList<Attraction>();
		ArrayList<Attraction> attractionList_selected = new ArrayList<Attraction>();
		ArrayList<String> attractionCoordinates = new ArrayList<String>();
		ArrayList<String> attractionCategories = new ArrayList<String>();
		try {
			attractionList_full = parser.parseAttractionXML(attractionCategories, R.xml.attractions, this);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			System.out.println("xml not found");
		}
		
		String usrPointLat = "60.171126";
		String usrPointLong = "24.939798";
		double user_gps_lat = 60.171126;
		double user_gps_lon = 24.939798;
		double distance = 0.0;
		
		if ( location != null ) {
			user_gps_lat = location.getLatitude();
			user_gps_lon = location.getLongitude();
			distance = Math.sqrt( ((location.getLongitude() - user_gps_lon)*(location.getLongitude() - user_gps_lon)) + ((location.getLatitude() - user_gps_lat)*(location.getLatitude() - user_gps_lat)) );
		}
		if ( distance > 0.0756 ) {
			user_gps_lat = 60.171126;
			user_gps_lon = 24.939798;
		}

		attractionList_selected.add(new Attraction("Tour start location", user_gps_lat, user_gps_lon, "User", "Location you started the tour from."));
		usrPointLat = Double.valueOf(user_gps_lat).toString();
		usrPointLong = Double.valueOf(user_gps_lon).toString();
		
		ArrayList<String[]> pointList = new ArrayList<String[]>();
		String [] usr_points = new String[2];
		usr_points[0] = usrPointLat;
		usr_points[1] = usrPointLong;
		
		int random_att_id = 0;
		ArrayList<Integer> used_attraction_ids = new ArrayList<Integer>();
		while ( used_attraction_ids.size() < 5 ) {
			random_att_id = (int)(Math.random() * attractionList_full.size());
			if ( !used_attraction_ids.contains(random_att_id) ) {
				used_attraction_ids.add(random_att_id);
			}
		}
		
		pointList.add(usr_points);
		for ( int i = 0; i < 5; i++ ) {
			Attraction att = attractionList_full.get( used_attraction_ids.get(i) );
			String [] points = new String[2];
			points[0] = Double.valueOf(att.getLatitude()).toString();
			points[1] = Double.valueOf(att.getLongitude()).toString();
			pointList.add(points);
			attractionList_selected.add(att);
		}
		pointList.add(usr_points);
		
		// YYYYMMDD
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		month++;
		String timestr = "";
		String datestr = "";
		
		
		for ( int i = 0; i < pointList.size()-1; i++ ) {
			timestr = Integer.toString(hour) + Integer.toString(minute);
			datestr = Integer.toString(year) + Integer.toString(month) + Integer.toString(day);
			attractionCoordinates.add("http://api.reittiopas.fi/hsl/prod/?user=<username>&pass=<password>&format=xml&epsg_in=wgs84&epsg_out=wgs84&request=route&show=1&date="+datestr+"&time="+timestr+"&detail=full&from="+pointList.get(i)[1]+","+pointList.get(i)[0]+"&to="+pointList.get(i+1)[1]+","+pointList.get(i+1)[0]);
			if ( (hour+1) > 23 ) {
				hour = 0;
				day++;
			} else {
				hour++;
			}
		}
		
		ToursDataDownloader downloader = new ToursDataDownloader(this, attractionList_selected);
		downloader.execute(attractionCoordinates);
    }
    
    
    @SuppressWarnings("unchecked")
	public void createTourManually() {
    	TextView distance_tv = (TextView)findViewById(R.id.editText1);
    	double max_distance = 3000;
    	try {
    		max_distance = Double.valueOf(distance_tv.getText().toString());
    	} catch ( NumberFormatException e ) {
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Give distance in meters. Eg. 1500").setTitle("Invalid distance value");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   TextView distance_tv = (TextView)findViewById(R.id.editText1);
	        	   distance_tv.setText("");
	           }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
			ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar1);
     	   	bar.setVisibility(View.INVISIBLE);
			return;
    	}
    	max_distance = max_distance*0.0000108;
    	
        locationManager.requestSingleUpdate(provider, this, getMainLooper());
        Location location = locationManager.getLastKnownLocation(provider);
        
		ToursXMLParser parser = new ToursXMLParser();
		ArrayList<Attraction> attractionList = new ArrayList<Attraction>();
		ArrayList<String> attractionCoordinates = new ArrayList<String>();
		ArrayList<String> attractionCategories = new ArrayList<String>();
		attractionCategories.add("MUSEO");
		attractionCategories.add("NÄHTÄVYYS");
		try {
			attractionList = parser.parseAttractionXML(attractionCategories, R.xml.helsinki_tourism_poi, this);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			System.out.println("xml not found");
		}
		
		Map<Double, Attraction> attractionPointsSorted = new HashMap<Double, Attraction>();
		double latitude = 60.168415;
		double longitude = 24.935017;
		for ( Attraction att : attractionList ) {
			double distance = Math.sqrt( ((att.getLongitude() - longitude)*(att.getLongitude() - longitude)) + ((att.getLatitude() - latitude)*(att.getLatitude() - latitude)) );
			if ( distance < max_distance ) {
				attractionPointsSorted.put(distance, att);
			}
		}
		
		attractionList.clear();
		int selectCounter = 0;
		String usrPointLat = Double.valueOf(location.getLatitude()).toString();
		String usrPointLong = Double.valueOf(location.getLongitude()).toString();
		
		ArrayList<String[]> pointList = new ArrayList<String[]>();
		String [] usr_points = new String[2];
		usr_points[0] = usrPointLat;
		usr_points[1] = usrPointLong;
		pointList.add(usr_points);
		
		attractionList.add(new Attraction("Tour start location", location.getLatitude(), location.getLongitude(), "User", "Location you started the tour from."));
		
		for(Map.Entry<Double, Attraction> entry : attractionPointsSorted.entrySet()) {
			if ( (int)(Math.random() * 2) == 1 ) {
				if ( selectCounter < 5 ) {
					attractionList.add(entry.getValue());
					String [] points = new String[2];
					points[0] = Double.valueOf(entry.getValue().getLatitude()).toString();
					points[1] = Double.valueOf(entry.getValue().getLongitude()).toString();
					pointList.add(points);
					selectCounter++;
				}
			}
		}
		pointList.add(usr_points);
		
		// YYYYMMDD
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		month++;
		String timestr = "";
		String datestr = "";
		
		for ( int i = 0; i < pointList.size()-1; i++ ) {
			timestr = Integer.toString(hour) + Integer.toString(minute);
			datestr = Integer.toString(year) + Integer.toString(month) + Integer.toString(day);
			attractionCoordinates.add("http://api.reittiopas.fi/hsl/prod/?user=<username>&pass=<password>&format=xml&epsg_in=wgs84&epsg_out=wgs84&request=route&date="+datestr+"&time="+timestr+"&detail=full&from="+pointList.get(i)[1]+","+pointList.get(i)[0]+"&to="+pointList.get(i+1)[1]+","+pointList.get(i+1)[0]);
			if ( (hour+1) > 23 ) {
				hour = 0;
				day++;
			} else {
				hour++;
			}
		}
		
		ToursDataDownloader downloader = new ToursDataDownloader(this, attractionList);
		downloader.execute(attractionCoordinates);
    }

    
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
    
}





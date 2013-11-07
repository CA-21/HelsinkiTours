package com.example.helsinkitours;

import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParserException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MapActivity extends Activity implements LocationListener {
	
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private String provider;
    private Marker user_marker;
    private ArrayList<Attraction> attractionList;
    private DrawerLayout mDrawerLayout;
    private ArrayList<Marker> markerList;
    private ArrayAdapter<Attraction> leftDrawerItemList;
    private ArrayAdapter<RouteNode> rightDrawerItemList;
    private ListView mDrawerList_left;
    private ListView mDrawerList_right;
    private TextView previewmode_tv;
    private ArrayList<Polyline> routeLines;
    private int currentlySelectedAtt = 0;
    private ArrayList<Route> routes;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        previewmode_tv = (TextView)findViewById(R.id.tour_preview);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); 
        mDrawerList_left = (ListView) findViewById(R.id.left_drawer);
        mDrawerList_right = (ListView) findViewById(R.id.right_drawer);
        markerList = new ArrayList<Marker>();
        routeLines = new ArrayList<Polyline>();
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(provider, 15000, 0, this);
        //locationManager.requestSingleUpdate(provider, this, getMainLooper());
        Location location = locationManager.getLastKnownLocation(provider);
        
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setZoomGesturesEnabled(false);
            
            Bundle extras = getIntent().getExtras();
            attractionList = extras.getParcelableArrayList("attractionList");
            ArrayList<String> routeDataList = extras.getStringArrayList("routeXmls");
            
            ToursXMLParser parser = new ToursXMLParser();
            routes = new ArrayList<Route>();
            for ( String xmlData : routeDataList ) {
            	try {
					routes.add( parser.parseRouteXML(xmlData) );
				} catch (NotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
            }
            
            for ( Route rt : routes ) {
            	PolylineOptions line = new PolylineOptions();
            	line.color(Color.GREEN);
            	line.geodesic(true);
            	for ( RouteNode rtn : rt.getRouteNodes() ) {
            		for ( Double[] shape : rtn.getShapeNodes() ) {
            			line.add(new LatLng(shape[1], shape[0]));
            		}
            	}
            	Polyline currentRouteLine = googleMap.addPolyline(line);
            	currentRouteLine.setVisible(false);
            	routeLines.add(currentRouteLine);
            }
            
            if ( routeLines.size() > 0 ) {
            	routeLines.get(0).setVisible(true);
            }
            
            googleMap.setInfoWindowAdapter(new MapInfoWindowAdapter(this, attractionList));
            user_marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Your location").icon(BitmapDescriptorFactory.fromResource(R.drawable.user_icon)).snippet("Location you started from."));
            for ( Attraction att : attractionList ) {
            	markerList.add(googleMap.addMarker(new MarkerOptions().position(new LatLng(att.getLatitude(), att.getLongitude())).title(att.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.attraction_icon)).snippet(att.getDescription())));
            }
            
            if ( markerList.size() > 0 ) {
            	markerList.get(0).showInfoWindow();            	
            }
            
            leftDrawerItemList = new AttractionAdapter(this, R.layout.row, attractionList);
            mDrawerList_left.setAdapter(leftDrawerItemList);
            mDrawerList_left.setOnItemClickListener(new LeftDrawerItemClickListener(this));
            
            if ( routes.size() > 0 ) {
            	rightDrawerItemList = new RouteAdapter(this, R.layout.row, routes.get(0).getRouteNodes());
            	mDrawerList_right.setAdapter(rightDrawerItemList);
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(60.168415, 24.935017), 15));
	        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
        }
        
        if (location != null) {
        	onLocationChanged(location);
        }
    }
    
    public void onClick ( View view ) {
    	if ( view.getId() == R.id.button_accept_tour ) {
    		Button btn = (Button)findViewById(R.id.button_another_tour);
    		btn.setVisibility(View.INVISIBLE);
    		Button btn2 = (Button)findViewById(R.id.button_accept_tour);
    		btn2.setVisibility(View.INVISIBLE);
    		Button btn3 = (Button)findViewById(R.id.att_left_btn);
    		btn3.setVisibility(View.VISIBLE);
    		Button btn4 = (Button)findViewById(R.id.att_right_btn);
    		btn4.setVisibility(View.VISIBLE);
    		previewmode_tv.setVisibility(View.INVISIBLE);
    		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerList.get(0).getPosition(), 15));
    		googleMap.getUiSettings().setZoomGesturesEnabled(true);
    	}
    	if ( view.getId() == R.id.button_another_tour ) {
    		ProgressBar bar = (ProgressBar)findViewById(R.id.new_tour_progress);
    		bar.setVisibility(View.VISIBLE);
    		Button btn = (Button)findViewById(R.id.button_another_tour);
    		btn.setVisibility(View.INVISIBLE);
    		Button btn2 = (Button)findViewById(R.id.button_accept_tour);
    		btn2.setVisibility(View.INVISIBLE);
    		this.finish();
    	}
    	if ( view.getId() == R.id.att_right_btn ) {
    		if ( currentlySelectedAtt < markerList.size()-1 ) {
    			currentlySelectedAtt++;
    		} else {
    			currentlySelectedAtt = 0;
    		}
    		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerList.get(currentlySelectedAtt).getPosition(), 15), 500, null);
    		markerList.get(currentlySelectedAtt).showInfoWindow();
    		for ( Polyline line : routeLines ) {
    			line.setVisible(false);
    		}
    		rightDrawerItemList.clear();
    		rightDrawerItemList.addAll(routes.get(currentlySelectedAtt).getRouteNodes());
        	routeLines.get(currentlySelectedAtt).setVisible(true);
    	}
    	if ( view.getId() == R.id.att_left_btn ) {
    		if ( currentlySelectedAtt > 0 ) {
    			currentlySelectedAtt--;
    		} else {
    			currentlySelectedAtt = markerList.size()-1;
    		}
    		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerList.get(currentlySelectedAtt).getPosition(), 15), 500, null);
    		markerList.get(currentlySelectedAtt).showInfoWindow();
    		for ( Polyline line : routeLines ) {
    			line.setVisible(false);
    		}
    		rightDrawerItemList.clear();
    		rightDrawerItemList.addAll(routes.get(currentlySelectedAtt).getRouteNodes());
        	routeLines.get(currentlySelectedAtt).setVisible(true);
    	}
    	if ( view.getId() == R.id.drawer_left_btn ) { 
    		mDrawerLayout.openDrawer(mDrawerList_left);
    	}
    	if ( view.getId() == R.id.drawer_right_btn ) {
    		mDrawerLayout.openDrawer(mDrawerList_right);
    	}
    }
    
    public void updateSelectedAttraction() {
    	googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerList.get(currentlySelectedAtt).getPosition(), 15), 500, null);
		markerList.get(currentlySelectedAtt).showInfoWindow();
		for ( Polyline line : routeLines ) {
			line.setVisible(false);
		}
		rightDrawerItemList.clear();
		rightDrawerItemList.addAll(routes.get(currentlySelectedAtt).getRouteNodes());
    	routeLines.get(currentlySelectedAtt).setVisible(true);
    }
    
    public int getCurrentlySelectedAtt() {
		return currentlySelectedAtt;
	}

	public void setCurrentlySelectedAtt(int currentlySelectedAtt) {
		this.currentlySelectedAtt = currentlySelectedAtt;
	}

	
	@Override
    protected void onResume() {
      super.onResume();
      locationManager.requestLocationUpdates(provider, 15000, 0, this);
    }
    
    
    @Override
    protected void onPause() {
      super.onPause();
      locationManager.removeUpdates(this);
    }

    
	@Override
	public void onLocationChanged(Location location) {
		user_marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
	}


	@Override
	public void onProviderDisabled(String provider) {
	}


	@Override
	public void onProviderEnabled(String provider) {
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}






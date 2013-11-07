package com.example.helsinkitours;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class RouteAdapter extends ArrayAdapter<RouteNode> {
	private ArrayList<RouteNode> nodeLocations;

	public RouteAdapter(Context context, int textViewResourceId, ArrayList<RouteNode> arrayList) {
		super(context, textViewResourceId, arrayList);
		this.nodeLocations = arrayList;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View targetView = convertView;
		if(targetView == null) {
			LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			targetView = li.inflate(R.layout.routes_row, null);
		}
		
		RouteNode rtn = nodeLocations.get(position);
		String type = rtn.getNodetype();
		String transportType = "-";
		String transportCode = "";
		if ( rtn.getNodeTransportCode() != null ) {
			if ( rtn.getNodeTransportCode().length() > 1 ) {
				transportCode = rtn.getNodeTransportCode();
				transportCode = transportCode.substring(1, 4);
			}
		}
		
		ImageView icon = (ImageView)targetView.findViewById(R.id.transportation_icon);
		if ( type.equals("walk") ) {
			icon.setImageResource(R.drawable.walking);
			transportType = "Walk";
		}
		if ( (type.equals("1")) || (type.equals("3")) || (type.equals("4")) || (type.equals("5")) ) {
			icon.setImageResource(R.drawable.bus_large);
			transportType = "Bus";
		}
		if ( type.equals("2") ) {
			icon.setImageResource(R.drawable.tram_large);
			transportType = "Tram";
		}
		if ( type.equals("6") ) {
			icon.setImageResource(R.drawable.metro_large);
			transportType = "Metro";
		}
		if ( type.equals("7") ) {
			icon.setImageResource(R.drawable.ferry_large);
			transportType = "Ferry";
		}
		if ( type.equals("12") ) {
			icon.setImageResource(R.drawable.train);
			transportType = "Train";
		}
		
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyyMMddHHmm", Locale.ENGLISH).parse(rtn.getNodeLocations().get(0).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		TextView time_tv = (TextView)targetView.findViewById(R.id.time_field);
		time_tv.setText( date.getHours() + ":" + date.getMinutes() + " - " + transportType + " " + transportCode );
		
		TextView street_tv = (TextView)targetView.findViewById(R.id.street_field);
		if ( (rtn.getNodeLocations().size() > 0) && (rtn.getNodeLocations().get(0).getStreetName() != null) ) {
			street_tv.setText( "Street: " + rtn.getNodeLocations().get(0).getStreetName() );
		} else {
			street_tv.setText( "Street: -" );
		}
		
		return targetView;
	}
		
}




package com.example.helsinkitours;


import java.util.ArrayList;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class MapInfoWindowAdapter implements InfoWindowAdapter {
	private View infoContentView;
	private ArrayList<Attraction> attractionList;
	
	public MapInfoWindowAdapter( MapActivity map_activity, ArrayList<Attraction> attractionList ) {		
		this.attractionList = attractionList;
		infoContentView = map_activity.getLayoutInflater().inflate(R.layout.info_window, null);
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}
	
	@Override
	public View getInfoWindow(Marker marker) {
		TextView title_tv = ((TextView)infoContentView.findViewById(R.id.info_title));
		title_tv.setText(marker.getTitle());
		TextView desc_tv = ((TextView)infoContentView.findViewById(R.id.info_desc));
		for ( Attraction att : attractionList ) {
			if ( att.getName().equals(marker.getTitle()) ) {
				desc_tv.setText(att.getDescription());
			}
		}
		return infoContentView;
	}

}
 

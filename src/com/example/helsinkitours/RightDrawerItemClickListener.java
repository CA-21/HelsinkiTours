package com.example.helsinkitours;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class RightDrawerItemClickListener implements ListView.OnItemClickListener {
	private MapActivity map_activity;
	
	public RightDrawerItemClickListener( MapActivity activity ) {
		this.map_activity = activity;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		map_activity.setCurrentlySelectedAtt(position);
		map_activity.updateSelectedAttraction();
	}
	
}

package com.example.helsinkitours;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AttractionAdapter extends ArrayAdapter<Attraction> {
	private ArrayList<Attraction> attractions;

	public AttractionAdapter(Context context, int textViewResourceId, ArrayList<Attraction> objects) {
		super(context, textViewResourceId, objects);
		this.attractions = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View targetView = convertView;
		if(targetView == null) {
			LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			targetView = li.inflate(R.layout.row, null);
		}
		
		Attraction attraction = attractions.get(position);
		if ( attractions != null ) {
			TextView att_name = (TextView)targetView.findViewById(R.id.name_field);
			att_name.setText( attraction.getName() );
			TextView att_desc = (TextView)targetView.findViewById(R.id.desc_field);
			att_desc.setText( attraction.getDescription() );
		}
		
		return targetView;
	}
	
}





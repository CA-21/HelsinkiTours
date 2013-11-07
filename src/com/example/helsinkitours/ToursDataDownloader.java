package com.example.helsinkitours;

import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class ToursDataDownloader extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
	private MainActivity main_activity;
	private ArrayList<Attraction> attractionList;
	
	public ToursDataDownloader( MainActivity activity, ArrayList<Attraction> attractionList ) {
		this.main_activity = activity;
		this.attractionList = attractionList;
	}
	
	protected ArrayList<String> doInBackground(ArrayList<String>... params) {
		try {
			return new ToursDownloader().downloadAttractions(params[0]);
		} catch(IOException e) {
			return null;
		}
	}
	
	@Override
    protected void onPostExecute(ArrayList<String> result) {
		if ( (result != null) && (result.size() > 0) ) {
			ProgressBar bar = (ProgressBar)this.main_activity.findViewById(R.id.progressBar1);
    		bar.setVisibility(View.INVISIBLE);
			Intent intent = new Intent( this.main_activity, MapActivity.class );
			Bundle extra_bundle = new Bundle();
			extra_bundle.putParcelableArrayList("attractionList", attractionList);
			extra_bundle.putStringArrayList("routeXmls", result);
			intent.putExtras(extra_bundle);
			this.main_activity.startActivity(intent);
		} else {
			ProgressBar bar = (ProgressBar)this.main_activity.findViewById(R.id.progressBar1);
    		bar.setVisibility(View.INVISIBLE);
    		AlertDialog.Builder builder = new AlertDialog.Builder(this.main_activity);
			builder.setMessage("Unable to download route information, make sure you have access to internet.").setTitle("Download failed");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	           }
	        });
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}
}

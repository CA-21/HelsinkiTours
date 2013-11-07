package com.example.helsinkitours;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ToursDownloader {
	
	public ArrayList<String> downloadAttractions(ArrayList<String> coords) throws IOException {
		String inputString = "";
		String xmlString = "";
		ArrayList<String> resultXmls = new ArrayList<String>();
		for ( String url_string : coords ) {
			inputString = "";
			xmlString = "";
			try {
				URL url = new URL(url_string);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setReadTimeout(10000);
				urlConnection.setConnectTimeout(15000);
				urlConnection.setRequestMethod("GET");
				urlConnection.connect();
				try {
					BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					while ( (inputString = input.readLine()) != null ) {
						xmlString += inputString;
					}
					input.close();
					urlConnection.disconnect();
					if ( (xmlString != null) && (xmlString.length() > 10) ) {
						resultXmls.add(xmlString);
					}
				} catch(UnsupportedEncodingException e) {
					return null;
				}
			} catch (IOException e1) {
				return null;
			}
		}
		return resultXmls;
	}
	
}

package com.example.helsinkitours;

import android.os.Parcel;
import android.os.Parcelable;

public class Attraction implements Parcelable {
	private String name;
	private double latitude;
	private double longitude;
	private String category;
	private String description;
	
	public Attraction( String name, double latitude, double longitude, String category, String description ) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.category = category;
		this.description = description;
	}
	
	public Attraction(Parcel in) {
		this.name = in.readString();
		this.latitude = in.readDouble();
		this.longitude = in.readDouble();
		this.category = in.readString();
		this.description = in.readString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static final Parcelable.Creator<Attraction> CREATOR = new Parcelable.Creator<Attraction>() {
		public Attraction createFromParcel(Parcel in) {
			return new Attraction(in);
		}

		public Attraction[] newArray (int size) {
			return new Attraction[size];
		}
    };

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeString(category);
		dest.writeString(description);
	}
	
}

package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;

public class Intersection {
	float latitude,longitude;
	int i;
	
	List<Road> incoming = new ArrayList<Road>();
	List<Road> outgoing = new ArrayList<Road>();

	public Intersection(float latitude, float longitude, int i) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.i = i;
	}
}

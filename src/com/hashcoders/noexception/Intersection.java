package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;

public class Intersection {
	double latitude,longitude;
	int i;
	
	List<Road> incoming = new ArrayList<Road>();
	List<Road> outgoing = new ArrayList<Road>();

	public Intersection(double latitude, double longitude, int i) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.i = i;
	}
	
	public Road findRoadTo(Intersection intersection) {
		for (Road r : outgoing) {
			if (r.to == intersection)
				return r;
		}
		return null;
	}
}

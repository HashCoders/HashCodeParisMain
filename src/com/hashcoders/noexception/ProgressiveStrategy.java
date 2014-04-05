package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;

public class ProgressiveStrategy implements Strategy {
	
	class Vehicle {
		float t;
		Intersection intersection;
		
		Vehicle(float t, Intersection intersection) {
			this.t = t;
			this.intersection = intersection;
		}
	}
	
	
	@Override
	public Solution process(Data data) {
		Solution solution = new Solution(data.C);
		
		List<Vehicle> costs = new ArrayList<Vehicle>();
		for (int i = 0; i < data.C; i++)
			costs.add(new Vehicle(0, data.startingIntersection));
		
		return solution;
	}

}

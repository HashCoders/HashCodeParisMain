package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;

public class ProgressiveStrategy implements Strategy {
	
	class Vehicle {
		int i;
		float t;
		Intersection intersection;
		
		Vehicle(int i, float t, Intersection intersection) {
			this.t = t;
			this.i = i;
			this.intersection = intersection;
		}
		
		void takeRoad(Road road) {
			assert(intersection == road.from);
			t += road.cost;
			intersection = road.to;
		}
	}
	
	Vehicle selectEarliest(List<Vehicle> candidates) {
		Vehicle best = candidates.get(0);
		for (Vehicle vehicle : candidates) {
			if (vehicle.t < best.t)
				best = vehicle;
		}
		return best;
	}
	
	Road selectBestRoad(Intersection from) {
		Road best = from.outgoing.get(0);
		
		for (Road road : from.outgoing) {
			
		}
		
		
		return best;
	}
	
	@Override
	public Solution process(Data data) {
		Solution solution = new Solution(data.C);
		
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		for (int i = 0; i < data.C; i++)
			vehicles.add(new Vehicle(i, 0, data.startingIntersection));
		
		while(true) {
			// Pick earliest vehicle
			Vehicle vehicle = selectEarliest(vehicles);
			// Select best road
			
			break;
		}
		
		return solution;
	}

}

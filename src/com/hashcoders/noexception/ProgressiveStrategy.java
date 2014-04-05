package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;

public class ProgressiveStrategy implements Strategy {
	
	List<Boolean> passedRoads = new ArrayList<Boolean>();
	
	class Vehicle {
		int i;
		float t;
		Intersection intersection;
		boolean over = false;
		
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
		
		boolean isOver(float maxT) {
			if (intersection.outgoing.isEmpty())
				System.out.println("Empty intersection");
			for (Road road : intersection.outgoing) {
				System.out.println("Current time: " + t + " ; candidate: " + road.cost);
				if (t + road.cost <= maxT)
					return false;
			}
			return true;
		}
	}
	
	Vehicle selectEarliest(List<Vehicle> candidates) {
		Vehicle best = null;
		for (Vehicle vehicle : candidates) {
			if (!vehicle.over)
				if (best == null || vehicle.t < best.t)
					best = vehicle;
		}
		return best;
	}
	
	Road selectBestRoad(Intersection from, float t, float maxT) {
		
		for (Road road : from.outgoing) {
			if (road.cost + t > maxT)
				continue;
			if (passedRoads.get(road.i))
				continue;
			if (road.to.outgoing.isEmpty())
				continue;
			return road;
		}

		for (Road road : from.outgoing) {
			if (road.cost + t > maxT)
				continue;
			if (road.to.outgoing.isEmpty())
				continue;
			return road;
		}

		for (Road road : from.outgoing) {
			if (road.cost + t > maxT)
				continue;
			return road;
		}

		return null;
	}
	
	@Override
	public Solution process(Data data) {
		Solution solution = new Solution(data.C);
		for (Road r : data.roads)
			passedRoads.add(new Boolean(false));
		System.out.println("maxT: " + data.maxT);
		
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		for (int i = 0; i < data.C; i++)
		{
			vehicles.add(new Vehicle(i, 0, data.startingIntersection));
			solution.paths.get(i).intersections.add(data.startingIntersection);
		}
			
		while(true) {
			// Pick earliest vehicle
			Vehicle vehicle = selectEarliest(vehicles);
			if (vehicle == null)
				break;
			if (vehicle.isOver(data.maxT))
			{
				System.out.println("Vehicle is over...");
				vehicle.over = true;
				continue;
			}
			
			// Select best road
			Road road = selectBestRoad(vehicle.intersection, vehicle.t, data.maxT);
			System.out.println("Vehicle " + vehicle.i + " takes a road costing " + road.cost);
			vehicle.takeRoad(road);
			solution.paths.get(vehicle.i).intersections.add(road.to);
			passedRoads.set(road.i, true);
		}
		
		return solution;
	}

}

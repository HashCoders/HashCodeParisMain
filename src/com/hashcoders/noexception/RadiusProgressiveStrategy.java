package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RadiusProgressiveStrategy implements Strategy {
	
	double lat0 = 0.0;
	double longi0 = 0.0;
	
	public RadiusProgressiveStrategy(Data d) {
		lat0 = d.startingIntersection.latitude;
		longi0 = d.startingIntersection.longitude;
	}
	
	List<Boolean> passedRoads = new ArrayList<Boolean>();
	
	public int radius(double la, double lo) {
		return (int) (Math.floor(((la-lat0)*(la-lat0)+(lo-longi0)*(lo-longi0))*1000000000.0));
	}
	
	class Vehicle {
		int i;
		float t;
		Intersection intersection;
		boolean over = false;

		double cX,cY;
		
		Vehicle(int i, float t, Intersection intersection) {
			this.t = t;
			this.i = i;
			this.intersection = intersection;
			// cX = intersection.longitude;
			// cY = intersection.latitude;
			cX = 2.351828;
			cY = 48.856578;
		}
		
		void takeRoad(Road road) {
			assert(intersection == road.from);
			t += road.cost;
			intersection = road.to;
		}
		
		boolean isOver(int maxT) {
			if (intersection.outgoing.isEmpty())
				System.out.println("Empty intersection");
			for (Road road : intersection.outgoing) {
				// System.out.println("Current time: " + t + " ; candidate: " + road.cost);
				if (t + road.cost <= maxT)
					return false;
			}
			return true;
		}
	

		int getRoadScore(Road r, List<Boolean> passedRoads, int maxR) {
			int rad = radius(r.to.latitude,r.to.longitude);
			double t = Math.abs((double) rad- ((double) i*maxR)/8.0);
			return ((int) (1/(Math.sqrt(Math.sqrt(t)))*100000.0));
		}
		
		Road selectBestRoad(float maxT, List<Boolean> passedRoads, int maxR) {
			List<Road> candidates = new ArrayList<Road>();
			List<Integer> scores = new ArrayList<Integer>();
			int sum = 0;
			for (Road r : intersection.outgoing)
			{
				if (r.cost + t > maxT)
					continue;

				int score = getRoadScore(r, passedRoads, maxR);
				System.out.println(score);
				sum += score;
				candidates.add(r);
				scores.add(new Integer(score));				
			}
			// System.out.println("BestScore: " + bestScore);
			
			if (!candidates.isEmpty()) {
				java.util.Random random = new Random();
				int target = random.nextInt(sum);
				for (int i = 0; i < candidates.size(); i++) {
					target -= scores.get(i);
					if (target < 0)
						return candidates.get(i);
				}
			}
			
			return null;
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
	
	
	@Override
	public Solution process(Data data) {
		int maxR = 0;
		for (Intersection s : data.intersections) {
			int potR = radius(s.latitude,s.longitude);
			if (maxR < potR) { maxR = potR;}
		}
		System.out.println(maxR);
		Solution solution = new Solution(data.C);
		
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		for (int i = 0; i < data.C; i++)
		{
			vehicles.add(new Vehicle(i, 0, data.startingIntersection));
			solution.paths.get(i).intersections.add(data.startingIntersection);
		}
		
		List<Boolean> passedRoads = new ArrayList<Boolean>();
		for (Loot l : data.loots)
			passedRoads.add(new Boolean(false));
		
		while(true) {
			// Pick earliest vehicle
			Vehicle vehicle = selectEarliest(vehicles);
			if (vehicle == null)
				break;
			if (vehicle.isOver(data.maxT))
			{
				// System.out.println("Vehicle " + vehicle.i + " is over...");
				vehicle.over = true;
				continue;
			}
			
			// Select best road
			Road road = vehicle.selectBestRoad(data.maxT, passedRoads, maxR);
			
			// System.out.println("Vehicle " + vehicle.i + " takes a road costing " + road.cost);
			vehicle.takeRoad(road);
			solution.paths.get(vehicle.i).intersections.add(road.to);
			passedRoads.set(road.loot.i, true);
		}
		
		return solution;
	}
}

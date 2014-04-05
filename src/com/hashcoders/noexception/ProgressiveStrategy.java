package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProgressiveStrategy implements Strategy {
	
	List<Boolean> passedRoads = new ArrayList<Boolean>();
	
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
			cX = intersection.longitude;
			cY = intersection.latitude;
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
		
		class Direction {
			boolean goTop;
			boolean goLeft;
			boolean goVertical;
			
			public Direction(int id) {
				goTop = (i%2 == 0);
				goLeft = (i/8 == 0);
				goVertical = (i%4 == 0) || (i%4 == 1);
			}
						
			public Direction(double alpha) {
				goLeft = Math.abs(alpha) > Math.PI/2;
				goTop = alpha > 0;
				goVertical = (Math.abs(alpha) > Math.PI/4) && (Math.abs(alpha) < 3*Math.PI/4);
			}
			
			public int compare(Direction ideal, int idealScore, Direction planB, int planBScore) {
				int score = 0;

				if (goTop == ideal.goTop)
					score += idealScore;
				else if (goTop == planB.goTop)
					score += planBScore;

				if (goLeft == ideal.goLeft)
					score += idealScore;
				else if (goLeft == planB.goLeft)
					score += planBScore;

				if (goVertical == ideal.goVertical)
					score += idealScore;
				else if (goVertical == planB.goVertical)
					score += planBScore;
				
				return score;
			}
		}
		

		int getRoadScore(Road r) {
			double tx,ty, rx,ry;
			tx = r.to.longitude -cX;
			ty = r.to.latitude - cY;
			rx = r.to.longitude -r.from.longitude;
			ry = r.to.latitude - r.from.latitude;

			
			double talpha = Math.atan2(ty, tx);
			double ralpha = Math.atan2(ry, rx);
			System.out.println("Talpha: " + talpha);
			System.out.println("Ralpha: " + ralpha);
			Direction total = new Direction(talpha);
			Direction relative = new Direction(ralpha);
			Direction orders = new Direction(i);

			
			

			return orders.compare(total, 2, relative, 1);
		}
		
		Road selectBestRoad(float maxT) {
			System.out.println("Test");

			for (int i = 6; i >= 0; i--) {
				List<Road> candidates = new ArrayList<Road>();
				
				System.out.println("Test");
				
				for (Road r : intersection.outgoing)
					if (r.cost + t <= maxT && getRoadScore(r) >= i)
						candidates.add(r);
				
				if (!candidates.isEmpty()) {
					java.util.Random random = new Random();
					return candidates.get(random.nextInt(candidates.size()));
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
		Solution solution = new Solution(data.C);
		
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		for (int i = 0; i < data.C; i++)
		{
			vehicles.add(new Vehicle(i, 0, data.startingIntersection));
			solution.paths.get(i).intersections.add(data.startingIntersection);
		}
			
		while(true) {
			System.out.println("Test");
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
			System.out.println("Test");
			Road road = vehicle.selectBestRoad(data.maxT);
			
			// System.out.println("Vehicle " + vehicle.i + " takes a road costing " + road.cost);
			vehicle.takeRoad(road);
			solution.paths.get(vehicle.i).intersections.add(road.to);
			// passedRoads.set(road.loot.i, true);
		}
		
		return solution;
	}
}

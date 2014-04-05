package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProgressiveStrategy implements Strategy {
	
	int idealScore, planBScore, newScore;
	
	public ProgressiveStrategy(int idealScore, int planBScore, int newScore) {
		this.idealScore = idealScore;
		this.planBScore = planBScore;
		this.newScore = newScore;
	}
	
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
		
		class Direction {
			boolean goTop;
			boolean goLeft;
			boolean goVertical;

			public boolean comply(Direction order) {
				return goTop == order.goTop && goLeft == order.goLeft && goVertical == order.goVertical;
			}
			
			public Direction(int id) {
				goTop = (id%2 == 0);
				goLeft = (id/4 == 0);
				goVertical = (id%4 == 0) || (id%4 == 1);
			}
						
			public Direction(double alpha) {
				goLeft = Math.abs(alpha) > Math.PI/2;
				goTop = alpha > 0;
				goVertical = (Math.abs(alpha) > Math.PI/4) && (Math.abs(alpha) < 3*Math.PI/4);
			}
			
			public int compare(Direction ideal, int idealScore, Direction planB, int planBScore) {
				int score = 1;

				if (goTop == ideal.goTop)
					score += idealScore;
				if (goTop == planB.goTop)
					score += planBScore;

				if (goLeft == ideal.goLeft)
					score += idealScore;
				if (goLeft == planB.goLeft)
					score += planBScore;

				if (goVertical == ideal.goVertical)
					score += idealScore;
				
				return score;
			}
		}
		

		int getRoadScore(Road r, List<Boolean> passedRoads) {
			double tx,ty, rx,ry;
			tx = r.to.longitude -cX;
			ty = r.to.latitude - cY;
			rx = r.to.longitude -r.from.longitude;
			ry = r.to.latitude - r.from.latitude;

			
			double talpha = Math.atan2(ty, tx);
			double ralpha = Math.atan2(ry, rx);
			// System.out.println("Talpha: " + talpha);
			// System.out.println("Ralpha: " + ralpha);
			Direction total = new Direction(talpha);
			Direction relative = new Direction(ralpha);
			Direction orders = new Direction(i);

			int score = orders.compare(total, idealScore, relative, planBScore);
			
			// System.out.println("Score: " + score);
			if (orders.comply(total) && !passedRoads.get(r.loot.i)) {
				score += newScore;
				// System.out.println("BisScore: " + score + " ; " + r.loot.i);
			}
			return score;
		}
		
		Road selectBestRoad(float maxT, List<Boolean> passedRoads) {
			List<Road> candidates = new ArrayList<Road>();
			List<Integer> scores = new ArrayList<Integer>();
			int sum = 0;
			for (Road r : intersection.outgoing)
			{
				if (r.cost + t > maxT)
					continue;

				int score = getRoadScore(r, passedRoads);
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
			Road road = vehicle.selectBestRoad(data.maxT, passedRoads);
			
			// System.out.println("Vehicle " + vehicle.i + " takes a road costing " + road.cost);
			vehicle.takeRoad(road);
			solution.paths.get(vehicle.i).intersections.add(road.to);
			passedRoads.set(road.loot.i, true);
		}
		
		return solution;
	}
}

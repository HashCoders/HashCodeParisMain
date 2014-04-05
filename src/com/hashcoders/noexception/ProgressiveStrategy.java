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
		int role;
		float t;
		Intersection intersection;
		boolean over = false;

		double cX,cY;
		
		Vehicle(int i, float t, Intersection intersection) {
			this.t = t;
			this.i = i;
			role = i;
			this.intersection = intersection;
			//cX = intersection.longitude;
			//cY = intersection.latitude;
			cX = 2.351828;
			cY = 48.856578;
		}
		
		void takeRoad(Road road) {
			assert(intersection == road.from);
			t += road.cost;
			intersection = road.to;
		}
		
		boolean isInGoodQuadrant() {
			Direction dir = new Direction(role);
			Direction dir2 = new Direction(intersection.longitude - cX, intersection.latitude - cY);
			return dir.comply(dir2);
		}
		
		public int getIdealRole() {
			Direction dir = new Direction(intersection.longitude - cX, intersection.latitude - cY);			
			for (int i = 0; i < 8; i++)
				if (dir.comply(new Direction(i)))
					return i;
			return 0;
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

			public Direction(double longitude, double latitude) {
				this(Math.atan2(longitude, latitude));
			}

			public int compare(Direction ideal, int idealScore, Direction planB, int planBScore) {
				int score = 1;

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
				
				return score;
			}
		}
		

		int getRoadScore(Road r, List<Boolean> passedRoads) {
			double tx,ty, rx,ry;
			tx = r.to.longitude -cX;
			ty = r.to.latitude - cY;
			rx = r.to.longitude -r.from.longitude;
			ry = r.to.latitude - r.from.latitude;
			
			// System.out.println("Talpha: " + talpha);
			// System.out.println("Ralpha: " + ralpha);
			Direction total = new Direction(ty, tx);
			Direction relative = new Direction(ry, rx);
			Direction orders = new Direction(role);
			int score = 1;
			if (isInGoodQuadrant()) {
				if (!passedRoads.get(r.loot.i)) {
					score += newScore;
				}
			}
			else {
				score = orders.compare(total, idealScore, relative, planBScore);
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
		
		int step = 0;
		while(true) {
			step++;
			
			if (step%100 == 0) {
				//Reorganize
				List< List<Vehicle> > candidates = new ArrayList<List<Vehicle>>();
				for (Vehicle v : vehicles)
					candidates.add(new ArrayList<Vehicle>());
				
				int[] idealRoles = new int[vehicles.size()];
				for (Vehicle v : vehicles) {
					//System.out.println("Ideal for " + v.i + " : " + v.getIdealRole());
					idealRoles[v.i] = v.getIdealRole();
					candidates.get(v.getIdealRole()).add(v);
				}
				List<Integer> ingrateRoles = new ArrayList<Integer>();
				List<Vehicle> leftOvers = new ArrayList<Vehicle>();
				Random random = new Random();
				for (int i = 0; i < candidates.size(); ++i) {
					if (candidates.get(i).isEmpty())
						ingrateRoles.add(i);
					else {
						int selected = random.nextInt(candidates.get(i).size());
						candidates.get(i).get(selected).role = i;
						for (int j = 0; j < candidates.get(i).size(); j++) {
							if (j == selected)
								continue;
							leftOvers.add(candidates.get(i).get(j));
						}
					}
				}
				
				//System.out.println("Step: " + step);
				for (Integer role : ingrateRoles) {
					//System.out.println("Trying for role " + role);
					int selected = random.nextInt(leftOvers.size());
					leftOvers.get(selected).role = role;
					leftOvers.remove(selected);
				}
			}
			
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

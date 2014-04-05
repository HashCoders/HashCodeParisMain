package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;

public class Random_Vehicules implements Strategy {
	Data data;
	List<Intersection> solution;
	int time;
	List<Road> used = new ArrayList<Road>();
	int miniByPath;

	Random_Vehicules(Data d, int m) {
		data = d;
		miniByPath = m;
	}

	int randomInt(int min, int max) {
		return (int) (min + Math.random() * (max - min + 1));
	}

	public List<Road> notUsed(List<Road> av) {
		List<Road> res = new ArrayList<Road>();
		for (Road r : av) {
			if (!used.contains(r))
				res.add(r);
		}

		return res;
	}

	public Intersection nextIntersection(Intersection from,
			List<Intersection> path, List<Road> usedHere) {
		List<Road> available = from.outgoing;
		List<Road> availableNotUsed = notUsed(available);

		if (!available.isEmpty()) {
			if (!availableNotUsed.isEmpty()) {
				int i = 0;
				while (time != 0 && i < 100) {
					i++;
					int temp = randomInt(0, availableNotUsed.size() - 1);
					// System.out.println(temp);
					Road next = availableNotUsed.get(temp);
					int cost = next.cost;
					if (cost <= time) {
						usedHere.add(next);
						time -= cost;
						Intersection to = next.to;
						path.add(to);
						return to;
					}
				}
			}
			else {
				int i = 0;
				while (time != 0 && i < 100) {
					i++;
					int temp = randomInt(0, available.size() - 1);
					// System.out.println(temp);
					Road next = available.get(temp);
					int cost = next.cost;
					if (cost <= time) {
						usedHere.add(next);
						time -= cost;
						Intersection to = next.to;
						path.add(to);
						return to;
					}
				}
			}
		}
		time = 0;
		return from;
	}

	public List<Road> createPath(Intersection start, List<Intersection> path) {
		List<Road> usedHere = new ArrayList<Road>();
		Intersection temp = start;
		path.add(start);
		while (time != 0) {
			temp = nextIntersection(temp, path, usedHere);
			if (temp.equals(start))
				return usedHere;
		}
		return usedHere;
	}

	public Solution process(Data d) {
		int numVeh = data.C;
		Solution s = new Solution(numVeh);
		for (int i = 0; i < numVeh; i++) {
			time = d.maxT;
			List<Road> usedHere = createPath(data.startingIntersection, s.paths.get(i).intersections);
			while (s.paths.get(i).getScore(data) < miniByPath) {
				s.paths.get(i).intersections = new ArrayList<Intersection>();
				usedHere = createPath(data.startingIntersection, s.paths.get(i).intersections);	
			}
			used.addAll(usedHere);
			System.out.println("Vehicule "+i);
		}
		return s;
	}
}

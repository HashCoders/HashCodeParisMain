package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;

public class Random_Better implements Strategy {
	Data data;
	List<Intersection> solution;
	int time;
	List<Road> used;

	Random_Better(Data d) {
		data = d;
		used = new ArrayList<Road>();
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
			List<Intersection> path) {
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
						used.add(next);
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
						used.add(next);
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
		Intersection temp2 = temp;
		path.add(start);
		while (time != 0) {
			temp2 = temp;
			temp = nextIntersection(temp, path);
			if (temp.equals(temp2))
				return new ArrayList<Road>();
		}
		return usedHere;
	}

	public Solution process(Data d) {
		int numVeh = data.C;
		Solution s = new Solution(numVeh);
		for (int i = 0; i < numVeh; i++) {
			time = d.maxT;
			createPath(data.startingIntersection, s.paths.get(i).intersections);
		}
		return s;
	}
}

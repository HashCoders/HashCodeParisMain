package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;

public class Grid implements Strategy {
	Data data;
	List<Intersection> solution;
	int time;
	List<Road> used;
	final double lat0 = 48.856578;
	final double longi0 = 2.351828;

	Grid(Data d) {
		data = d;
		used = new ArrayList<Road>();
	}

	public double angle(double lat, double longi) {
		return Math.atan2(lat - lat0, longi - longi0) + Math.PI;
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
			List<Intersection> path, int v) {
		List<Road> available = from.outgoing;
		List<Road> availableNotUsed = notUsed(available);

		if (!available.isEmpty()) {
			if (!availableNotUsed.isEmpty()) {
				Road bestRoad = null;
				for (Road next : availableNotUsed) {
					int cost = next.cost;
					double min = 1000000.0;
					if (cost <= time) {
						double theta = angle(next.to.latitude,next.to.longitude);
						if (theta >= v*Math.PI/4 && theta <= (v+1)*Math.PI/4) {
							used.add(next);
							time -= cost;
							Intersection to = next.to;
							path.add(to);
							return to;
						}
						else {
							double newMin = Math.abs(theta - (2*v+1)*Math.PI/8);
							if (min > newMin) {
								min = newMin;
								bestRoad = next;
							}
						}
					}
					if (bestRoad != null) {
						used.add(bestRoad);
						time -= bestRoad.cost;
						Intersection to = bestRoad.to;
						path.add(to);
						return to;
					}
							
				}
			}
			else {
				Road bestRoad = null;
				for (Road next : available) {
					int cost = next.cost;
					double min = 1000000.0;
					if (cost <= time) {
						double theta = angle(next.to.latitude,next.to.longitude);
						double newMin = Math.abs(theta - (2 * v + 1) * Math.PI
								/ 8);
						if (min > newMin) {
							min = newMin;
							bestRoad = next;
						}
					}
					if (bestRoad != null) {
						used.add(bestRoad);
						time -= bestRoad.cost;
						Intersection to = bestRoad.to;
						path.add(to);
						return to;
					}
							
				}
			}
		}
		time = 0;
		return from;
	}

	public List<Road> createPath(Intersection start, List<Intersection> path, int v) {
		List<Road> usedHere = new ArrayList<Road>();
		Intersection temp = start;
		Intersection temp2 = temp;
		path.add(start);
		while (time != 0) {
			temp2 = temp;
			temp = nextIntersection(temp, path, v);
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
			createPath(data.startingIntersection, s.paths.get(i).intersections, i);
		}
		return s;
	}
}

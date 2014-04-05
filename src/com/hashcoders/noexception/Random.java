package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;

public class Random implements Strategy {
	Data data;
	List<Intersection> solution;
	int time;
	
	Random(Data d) {
		data = d;
	}
	
	public void nextIntersection(Intersection from, List<Intersection> path) {
		for (Road next : from.outgoing) {
			int cost = next.cost;
			if (cost <= time) {
				time -= cost;
				Intersection to = next.to;
				path.add(to);
				from = to;
				return;
			}
		}
		time = 0;
	}
	
	public void createPath(Intersection start, List<Intersection> path) {
		Intersection temp = start;
		while (time != 0) {
			nextIntersection(temp, solution);
		}
	}

	public Solution process(Data d) {
		int numVeh = data.C;
		Solution s = new Solution(numVeh);
		for (int i=0 ; i<numVeh; i++) {
			List<Intersection> path = new ArrayList<Intersection>();
			createPath(data.startingIntersection,path);
			s.paths.get(i).intersections = path;
		}
		return s;
	}
}

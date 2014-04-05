package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;

public class Random implements Strategy {
	Data data;
	List<Intersection> solution;
	int time;
	
	Random(Data d) {
		data = d;
		time = d.maxT;
	}
	
	public Intersection nextIntersection(Intersection from, List<Intersection> path) {
		for (Road next : from.outgoing) {
			int cost = next.cost;
			if (cost <= time) {
				time -= cost;
				Intersection to = next.to;
				path.add(to);
				return to;
			}
		}
		time = 0;
		return from;
	}
	
	public void createPath(Intersection start, List<Intersection> path) {
		Intersection temp = start;
		while (time != 0) {
			temp = nextIntersection(temp, path);
			if (temp.equals(start)) return;
		}
	}

	public Solution process(Data d) {
		int numVeh = data.C;
		Solution s = new Solution(numVeh);
		for (int i=0 ; i<numVeh; i++) {
			createPath(data.startingIntersection,s.paths.get(i).intersections);
		}
		return s;
	}
}

package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;

public class Random {
	Data data;
	List<Intersection> solution;
	int time;
	
	Random(Data d) {
		data = d;
	}
	
	public void nextIntersection(Intersection from) {
		for (Road next : from.outgoing) {
			int cost = next.cost;
			if (cost <= time) {
				time -= cost;
				Intersection to = next.to;
				solution.add(to);
				from = to;
				return;
			}
		}
		time = 0;
	}
	
	public void createPath(Intersection start) {
		Intersection temp = start;
		while (time != 0) {
			nextIntersection(temp);
		}
	}
}

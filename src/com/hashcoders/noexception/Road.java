package com.hashcoders.noexception;

public class Road {
	Intersection from;
	Intersection to;
	int cost;
	int length;
	
	public Road(Intersection from, Intersection to, int cost, int length) {
		this.from = from;
		this.to = to;
		this.cost = cost;
		this.length = length;
	}
}

package com.hashcoders.noexception;

public class Road {
	Intersection from;
	Intersection to;
	boolean oneway;
	int cost;
	int length;
	
	public Road(Intersection from, Intersection to, boolean oneway, int cost, int length) {
		this.from = from;
		this.to = to;
		this.oneway = oneway;
		this.cost = cost;
		this.length = length;
	}
}

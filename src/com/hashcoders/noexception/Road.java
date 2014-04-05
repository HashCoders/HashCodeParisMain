package com.hashcoders.noexception;

public class Road {
	Intersection from;
	Intersection to;
	int cost;
	int length;
	int i;
	
	public Road(Intersection from, Intersection to, int cost, int length, int i) {
		this.from = from;
		this.to = to;
		this.cost = cost;
		this.length = length;
	}
}

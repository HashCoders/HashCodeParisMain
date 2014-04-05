package com.hashcoders.noexception;

public class Road {
	Intersection from;
	Intersection to;
	Loot loot;
	int cost;
	int i;
	
	public Road(Intersection from, Intersection to, int cost, Loot loot, int i) {
		this.from = from;
		this.to = to;
		this.cost = cost;
		this.loot= loot;
	}
}

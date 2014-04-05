package com.hashcoders.noexception;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Solution {
	
	
	public static class Path {
		List<Intersection> intersections = new ArrayList<Intersection>();
	}
	
	int C;
	List<Path> paths = new ArrayList<Path>();
	
	public Solution(int C) {
		this.C = C;
		for (int i = 0; i < C; i++)
			paths.add(new Path());
	}
	
	public int getScore(Data data) {
		List<Boolean> passedRoads = new ArrayList<Boolean>();
		for (Loot l : data.loots)
			passedRoads.add(new Boolean(false));
		
		int score = 0;
		for (Path p : paths) {
			for (int i = 0; i < p.intersections.size()-1; i++) {
				Road r = p.intersections.get(i).findRoadTo(p.intersections.get(i+1));
				if (r == null)
					System.out.println("Cannot find road from " + p.intersections.get(i).i + " to " + p.intersections.get(i+1).i);
				if (!passedRoads.get(r.loot.i)) {
					passedRoads.set(r.loot.i, true);
					score += r.loot.score;
				}
			}
		}
		
		return score;
	}
	
	public void toFile(String filename) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filename, "UTF-8");

		writer.println(C);
		for (int i = 0; i < C; i++)
		{
			writer.println(paths.get(i).intersections.size());
			for (int j = 0; j < paths.get(i).intersections.size(); j++)
				writer.println(paths.get(i).intersections.get(j).i);
		}
		
		writer.close();
	}
}

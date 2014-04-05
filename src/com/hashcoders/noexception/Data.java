package com.hashcoders.noexception;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Data {
	
	List<Road> roads;
	List<Intersection> intersections;
	
	int maxT;
	int C;
	int startingIntersection;
	
	public static Data fromFile(String filename) throws IOException {

		Data data = new Data();
		
		//TODO : make it work
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String line;
		line = br.readLine();
		String[] init = line.split(" ");
		int N = Integer.parseInt(init[0]);
		int M = Integer.parseInt(init[1]);
		data.maxT = Integer.parseInt(init[2]);
		data.C = Integer.parseInt(init[3]);
		data.startingIntersection = Integer.parseInt(init[4]);
		
		for (int i = 0; i < M; i++)
		{
			line = br.readLine();
			String[] pos = line.split(" ");
			data.intersections.add(new Intersection(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]), i));
		}
		
		for (int i = 0; i < N; i++)
		{
			line = br.readLine();
			String[] road = line.split(" ");
			data.roads.add(new Road(
					data.intersections.get(Integer.parseInt(road[0])), 
					data.intersections.get(Integer.parseInt(road[1])),
					(Integer.parseInt(road[3]) == 1),
					Integer.parseInt(road[4]),
					Integer.parseInt(road[5])));
		}
		
		br.close();
		
		return data;
	}
}

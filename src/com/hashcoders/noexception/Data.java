package com.hashcoders.noexception;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Data {
	
	List<Road> roads = new ArrayList<Road>();
	List<Intersection> intersections = new ArrayList<Intersection>();
	List<Loot> loots = new ArrayList<Loot>();
	
	int maxT;
	int C;
	Intersection startingIntersection;
	
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
		
		for (int i = 0; i < N; i++)
		{
			line = br.readLine();
			String[] pos = line.split(" ");
			data.intersections.add(new Intersection(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), i));
		}
		
		for (int i = 0; i < M; i++)
		{
			line = br.readLine();
			String[] road = line.split(" ");
			Intersection from = data.intersections.get(Integer.parseInt(road[0]));
			Intersection to = data.intersections.get(Integer.parseInt(road[1]));
						
			int cost = Integer.parseInt(road[3]);
			int length = Integer.parseInt(road[4]);
			Loot loot = new Loot(data.loots.size(), length);
			data.loots.add(loot);
			
			Road direct = new Road(
					from, 
					to,
					cost,
					loot, 
					data.roads.size());

			data.roads.add(direct);
			from.outgoing.add(direct);
			to.incoming.add(direct);
			
			if (Integer.parseInt(road[2]) == 2) {
				Road indirect = new Road(
						to,
						from, 
						cost,
						loot, 
						data.roads.size());
				
				data.roads.add(indirect);

				to.outgoing.add(indirect);
				from.incoming.add(indirect);
			}
		}
		
		data.startingIntersection = data.intersections.get(Integer.parseInt(init[4]));

		br.close();
		
		return data;
	}
}

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

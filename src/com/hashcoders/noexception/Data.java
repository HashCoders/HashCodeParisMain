package com.hashcoders.noexception;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Data {
	public static Data fromFile(String filename) throws IOException {

		Data data = new Data();
		
		//TODO : make it work
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String line;
		while ((line = br.readLine()) != null) {
		}
		br.close();
		
		return data;
	}
}

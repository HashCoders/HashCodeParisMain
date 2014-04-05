package com.hashcoders.noexception;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			Data data = Data.fromFile("input.txt");
			
			Strategy strategy = new BestPickerStrategy(new Random_Better(data),100000);
			Solution solution = strategy.process(data);
			solution.toFile("output.txt");
			System.out.println("Score: " + solution.getScore(data));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
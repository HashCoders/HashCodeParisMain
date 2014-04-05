package com.hashcoders.noexception;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			Data data = Data.fromFile("input.txt");
			Strategy strategy = new Random(data);
			Solution solution = strategy.process(data);
			solution.toFile("output.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

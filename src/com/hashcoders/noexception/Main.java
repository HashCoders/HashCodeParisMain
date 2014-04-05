package com.hashcoders.noexception;

public class Main {

	public static void main(String[] args) {
		Data data = Data.fromFile("input.txt");
		Strategy strategy = null;
		Solution solution = strategy.process(data);
		solution.toFile("output.txt");
	}

}

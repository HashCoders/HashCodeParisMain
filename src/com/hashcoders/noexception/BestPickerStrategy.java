package com.hashcoders.noexception;

public class BestPickerStrategy implements Strategy {
	int n;
	Strategy strategy;
	
	public BestPickerStrategy(int n, Strategy strategy) {
		this.n = n;
		this.strategy = strategy;
	}

	@Override
	public Solution process(Data data) {
		Solution best = null;
		int bestScore = 0;
		for (int i = 0; i < n; i++) {
			Solution solution = strategy.process(data);
			int score = solution.getScore(data);
			if (best == null || score > bestScore) {
				best = solution;
				bestScore = score;
			}
		}
		
		return best;
	}
}

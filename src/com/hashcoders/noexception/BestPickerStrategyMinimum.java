package com.hashcoders.noexception;

public class BestPickerStrategyMinimum implements Strategy {
	Strategy strategy;
	int mini;
	
	public BestPickerStrategyMinimum(Strategy strategy, int m) {
		this.strategy = strategy;
		this.mini = m;
	}

	@Override
	public Solution process(Data data) {
		Solution best = null;
		int bestScore = 0;
		while (bestScore < mini) {
			Strategy strategy = new Random_Better(data);
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

import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

public class MyMutation implements EvolutionaryOperator<double[]> {

	private int generation = 0;
	private double rate = 1;

	public List<double[]> apply(List<double[]> population, Random random) {

		// initial population
		// need to change individuals, but not their number!
		// your implementation:
		for (double[] individual : population) {
			if (random.nextDouble() < rate) {
				// swap
				int index1 = random.nextInt(population.size());
				int index2 = random.nextInt(population.size());
				double tmp = individual[index1];
				individual[index1] = individual[index2];
				individual[index2] = tmp;
			}
		}
		rate = Math.min(1, Math.max(0.1, 1 / Math.log((generation / 1) + 1)));
		System.out.println("rate : " + rate);
		// result population
		return population;
	}
}

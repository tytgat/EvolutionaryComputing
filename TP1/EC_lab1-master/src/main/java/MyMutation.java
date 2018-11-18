import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

public class MyMutation implements EvolutionaryOperator<double[]> {

	private int generation = 0;
	private double rate = 1;

	/*
	 * double conv = 50; double init_rate = 2.0; double end_rate = 0.5; double steps
	 * = (init_rate - end_rate) / conv; double rate = init_rate;
	 */
	public List<double[]> apply(List<double[]> population, Random random) {

		// initial population
		// need to change individuals, but not their number!

		// your implementation:
		for (double[] individual : population) {
			if (random.nextDouble() < rate) {

				int indexToChange = random.nextInt(individual.length);
				individual[indexToChange] += (random.nextGaussian() * rate);

				if (individual[indexToChange] > 5) {
					individual[indexToChange] = 5;
				}
				if (individual[indexToChange] < -5) {
					individual[indexToChange] = -5;
				}
				/*
				 * for(int i = 0 ; i < individual.length; i++) { if(random.nextDouble() < 0.05)
				 * { individual[i] += (random.nextGaussian() * 0.5); } }
				 */
			}
		}
		generation++;
		rate = Math.min(1.5, Math.max(0.1, 1 / Math.log((generation / 100) + 1)));
		// System.out.println(rate);
		// result population
		return population;
	}
}

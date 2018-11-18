import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

public class MyMutation implements EvolutionaryOperator<double[]> {

	// private int generation = 0;
	// private double rate = 1;

	public List<double[]> apply(List<double[]> population, Random random) {
		int swap = 0;
		int pasSwap = 0;
		double[][] distanceMat = TSPTytgatKarel.distanceMat;
		// initial population
		// need to change individuals, but not their number!
		// your implementation:
		for (double[] individual : population) {
			for (int sw = 0; sw < individual.length; sw++) {
				// swap
				int indexCity1 = random.nextInt(individual.length);
				int indexCity2 = random.nextInt(individual.length);

				int indexCity13 = getRightIndex(indexCity1 + 1, individual.length);
				int indexCity14 = getRightIndex(indexCity1 - 1, individual.length);

				int indexCity25 = getRightIndex(indexCity2 + 1, individual.length);
				int indexCity26 = getRightIndex(indexCity2 - 1, individual.length);

				int city1 = (int) individual[indexCity1];
				int city2 = (int) individual[indexCity2];

				int city13 = (int) individual[indexCity13];
				int city14 = (int) individual[indexCity14];

				int city25 = (int) individual[indexCity25];
				int city26 = (int) individual[indexCity26];

				double dist13 = city1 == city13 ? 0 : distanceMat[Math.max(city1, city13)][Math.min(city1, city13)];
				double dist14 = city1 == city14 ? 0 : distanceMat[Math.max(city1, city14)][Math.min(city1, city14)];

				double dist25 = city2 == city25 ? 0 : distanceMat[Math.max(city2, city25)][Math.min(city2, city25)];
				double dist26 = city2 == city26 ? 0 : distanceMat[Math.max(city2, city26)][Math.min(city2, city26)];

				double dist15 = city1 == city25 ? 0 : distanceMat[Math.max(city1, city25)][Math.min(city1, city25)];
				double dist16 = city1 == city26 ? 0 : distanceMat[Math.max(city1, city26)][Math.min(city1, city26)];

				double dist23 = city2 == city13 ? 0 : distanceMat[Math.max(city2, city13)][Math.min(city2, city13)];
				double dist24 = city2 == city14 ? 0 : distanceMat[Math.max(city2, city14)][Math.min(city2, city14)];

				double distWSwitch1 = dist15 + dist16;
				double distWSwitch2 = dist23 + dist24;

				double distWOSwitch1 = dist13 + dist14;
				double distWOSwitch2 = dist25 + dist26;

				if (distWSwitch1 + distWSwitch2 < distWOSwitch1 + distWOSwitch2) {
					swap++;
					double tmp = individual[indexCity1];
					individual[indexCity1] = individual[indexCity2];
					individual[indexCity2] = tmp;
				} else {
					pasSwap++;
				}
			}
			// }
		}
		System.out.println(swap + "/" + pasSwap);
		// generation++;
		// rate = Math.min(1, Math.max(0.1, 1 / Math.log((generation / 100) + 1)));
		// System.out.println("rate : " + rate);
		// result population
		return population;
	}

	private int getRightIndex(int index, int size) {
		if (index == -1) {
			return size - 1;
		}
		if (index == size) {
			return 0;
		}
		return index;
	}
}

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

public class MyCrossover extends AbstractCrossover<double[]> {
	protected MyCrossover() {
		super(1);
	}

	protected List<double[]> mate(double[] p1, double[] p2, int i, Random random) {
		ArrayList<double[]> children = new ArrayList<double[]>();

		// your implementation:
		int min = Math.min(p1.length, p2.length);
		int max = Math.max(p1.length, p2.length);
		int myRandom1 = random.nextInt(min);
		int myRandom2 = random.nextInt(min);
		int randMin = Math.min(myRandom2, myRandom1);
		int randMax = Math.max(myRandom2, myRandom1);
		if (randMin == randMax) {
			randMax = max;
		}
		double[] child1 = new double[max];
		double[] child2 = new double[max];
		for (int index = 0; index < randMin; index++) {
			child1[index] = p1[index];
			child2[index] = p2[index];
		}
		for (int index = randMin; index < randMax; index++) {
			child1[index] = p2[index];
			child2[index] = p1[index];
		}
		for (int index = randMax; index < max; index++) {
			child1[index] = p1[index];
			child2[index] = p2[index];
		}

		children.add(child1);
		children.add(child2);
		return children;
	}
}

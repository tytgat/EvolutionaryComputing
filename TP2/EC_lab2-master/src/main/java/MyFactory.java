import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class MyFactory extends AbstractCandidateFactory<double[]> {

	private int dimension;

	public MyFactory(int dimension) {
		this.dimension = dimension;
	}

	public double[] generateRandomCandidate(Random random) {
		List<Double> solutionLst = new ArrayList<Double>();
		for (int i = 0; i < dimension; i++) {
			solutionLst.add((double) i);
		}
		Collections.shuffle(solutionLst);

		double[] solution = new double[dimension];
		for (int i = 0; i < dimension; i++) {
			solution[i] = solutionLst.get(i);
		}

		return solution;
	}
}

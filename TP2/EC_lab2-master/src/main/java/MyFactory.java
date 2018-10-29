import java.util.Random;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class MyFactory extends AbstractCandidateFactory<double[]> {

	private int dimension;

	public MyFactory(int dimension) {
		this.dimension = dimension;
	}

	public double[] generateRandomCandidate(Random random) {
		double[] solution = new double[dimension];
		// x from -5.0 to 5.0

		// your implementation:

		return solution;
	}
}

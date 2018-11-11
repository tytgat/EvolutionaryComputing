import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class FitnessFunction implements FitnessEvaluator<double[]> {

	public FitnessFunction(int dimension, double[][] distanceMat) {
		this.dimension = dimension;
		this.distances = distanceMat;
	}

	private int dimension;
	private double[][] distances;

	public double getFitness(double[] solution, List<? extends double[]> list) {
		/*
		int n = dimension;
		double pi = Math.PI;
		double dn = 1.0 / n;
		double a = 10;
		double b = 0.2;
		double c = 2 * pi;
		double s1 = 0.0;
		double s2 = 0.0;
		Random noise = new Random(1);
		for (int i = 0; i < dimension; i++) {
			double val = solution[i] + noise.nextDouble();
			s1 += val * val;
			s2 += Math.cos(c * val);
		}
		s1 = -a * Math.exp(-b * Math.sqrt(dn * s1));
		s2 = -Math.exp(dn * s2);
		double result = s1 + s2 + a + Math.exp(1);
		result = -result;
		result = result + a;
		result = Math.abs(result);*/
		double distance = computeLength(solution);

		// System.out.println("dist : " + distance + "(" + 1 / distance + ")");
		return 1 / distance;
	}

	public double computeLength(double[] solution) {
		double distance = 0;
		for (int i = 0; i < dimension - 1; i++) {
			int city1 = (int) Math.max(solution[i], solution[i + 1]);
			int city2 = (int) Math.min(solution[i], solution[i + 1]);
			distance += distances[city1][city2];
		}

		return distance;
	}

	public boolean isNatural() {
		return true;
	}
}

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

		if (p1.length != p2.length) {
			System.out.println("ERROR : Arrays do not have the same length");
			return null;
		}

		int dimention = p1.length;
		int myRandom1 = random.nextInt(dimention);
		int myRandom2 = random.nextInt(dimention);
		int randMin = Math.min(myRandom2, myRandom1);
		int randMax = Math.max(myRandom2, myRandom1);
		if (randMin == randMax) {
			randMax = dimention;
		}

		List<Integer> lstP1 = new ArrayList<Integer>();
		List<Integer> lstP2 = new ArrayList<Integer>();

		List<Integer> lstC1 = new ArrayList<Integer>();
		List<Integer> lstC2 = new ArrayList<Integer>();
		for (int index = 0; index < p1.length; index++) {
			lstP1.add((int) p1[index]);
			lstP2.add((int) p2[index]);
			lstC1.add(null);
			lstC2.add(null);
		}

		System.out.println("p :" + lstP1.toString() + " - " + lstP2.toString());

		double[] child1 = new double[dimention];
		double[] child2 = new double[dimention];

		for (int index = randMin; index < randMax; index++) {// middle part
			lstC1.set(index, (int) p2[index]);
			lstC2.set(index, (int) p1[index]);
		}

		// get missing elements
		lstP1.removeAll(lstC1);
		lstP2.removeAll(lstC2);
		for (int value : lstP1) {
			if (!lstC1.contains(value)) {
				int index2 = 0;
				while (lstC1.get(index2) != null) {
					index2++;
				}
				lstC1.set(index2, value);
			}
		}
		for (int value : lstP2) {
			if (!lstC2.contains(value)) {
				int index2 = 0;
				while (lstC2.get(index2) != null) {
					index2++;
				}
				lstC2.set(index2, value);
			}
		}

		System.out.println("c :" + lstC1.toString() + " - " + lstC2.toString());

		// addList
		for (int index = 0; index < p1.length; index++) {
			child1[index] = lstC1.get(index);
			child2[index] = lstC2.get(index);
		}

		children.add(child1);
		children.add(child2);
		return children;
	}
}

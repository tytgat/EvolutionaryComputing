import java.util.LinkedList;
import java.util.List;

public class Map {
	private double[][]		distanceMat;
	private int				nbCities;
	private double[][]		pheromonesTable;
	public static double	evaporationRate	= 0.1;
	public static double	constanteQ		= 1;

	public Map(double[][] distanceMat, int nbCities, double[][] pheromonesTable) {
		super();
		this.distanceMat = distanceMat;
		this.nbCities = nbCities;
		this.pheromonesTable = pheromonesTable;
	}

	public void updatePheromones(List<Ant> lstAnt) {
		List<Ant> computedAnt = new LinkedList<Ant>();
		computedAnt.addAll(lstAnt);
		for (int i = 0; i < nbCities; i++) {
			for (int j = 0; j < i; j++) {
				setPheromone(i, j, computedAnt);
			}
		}
	}

	private void setPheromone(int city1, int city2, List<Ant> lstAnt) {
		if (city1 == city2) {
			return;
		}
		double sumAllAnt = sumPheromonesAnt(city1, city2, lstAnt);
		double constant = (1 - evaporationRate);
		double currentPheromone = getPheromone(city1, city2);
		int c1 = Math.max(city1, city2);
		int c2 = Math.min(city1, city2);

		pheromonesTable[c1][c2] = (constant * currentPheromone) + sumAllAnt;
	}

	private double sumPheromonesAnt(int city1, int city2, List<Ant> lstAnt) {
		double sum = 0;
		for (Ant k : lstAnt) {
			if (k.wasThere(city1, city2)) {
				sum += constanteQ / k.lengthPath;
			}
		}
		return sum;
	}

	public double getPheromone(int city1, int city2) {
		if (city1 == city2) {
			return 0;
		}
		int c1 = Math.max(city1, city2);
		int c2 = Math.min(city1, city2);

		return pheromonesTable[c1][c2];
	}

	public double getDistance(int city1, int city2) {
		if (city1 == city2) {
			return 0;
		}
		int c1 = Math.max(city1, city2);
		int c2 = Math.min(city1, city2);

		return distanceMat[c1][c2];
	}

	public void setDistanceMat(double[][] distanceMat) {
		this.distanceMat = distanceMat;
	}

	public int getNbCities() {
		return nbCities;
	}

	public void setNbCities(int nbCities) {
		this.nbCities = nbCities;
	}

}

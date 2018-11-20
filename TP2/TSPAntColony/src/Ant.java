import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;

public class Ant {
	int						position;
	int						destination;
	List<Integer>			visitedNode;
	double[][]				pathMemory;
	public static double	pheromonePower	= 1;
	public static double	distancePower	= 1;

	public double			lengthPath		= 0;

	public Ant(int position, int nbCity) {
		super();
		this.position = position;
		this.visitedNode = new ArrayList<Integer>();
		visitedNode.add(position);
		this.pathMemory = new double[nbCity][];
		for (int i = 0; i < nbCity; i++) {
			pathMemory[i] = new double[nbCity];
		}
	}

	public Ant(Ant a) {
		super();
		this.position = a.position;
		this.destination = a.destination;
		this.pathMemory = a.pathMemory;
		this.visitedNode = new ArrayList<Integer>();
		visitedNode.addAll(a.visitedNode);
		visitedNode.add(position);
	}

	public void prepareMove(Map map) {
		this.destination = whereTomove(map);
		move(map);

	}

	private int whereTomove(Map map) {
		int[] cities = new int[map.getNbCities()];
		double[] probaList = new double[map.getNbCities()];
		double sumAll = 0;
		for (int possibleDestination = 0; possibleDestination < map.getNbCities(); possibleDestination++) {
			if (!visitedNode.contains(possibleDestination) && possibleDestination != destination) {
				double pheromoneSum = Math.pow(map.getPheromone(position, possibleDestination), pheromonePower);
				double distanceSum = Math.pow(1 / map.getDistance(position, possibleDestination), distancePower);
				sumAll += pheromoneSum * distanceSum;
			}
		}
		for (int j = 0; j < map.getNbCities(); j++) {
			cities[j] = j;
			int destination = j;
			if (!visitedNode.contains(destination)) {
				double prob = 0;
				double pheromone = Math.pow(map.getPheromone(position, destination), pheromonePower);
				double distance = Math.pow(1 / map.getDistance(position, destination), distancePower);
				prob = (pheromone * distance) / sumAll;
				if (Double.isNaN(prob)) {
					probaList[j] = 0;
				} else if (Double.isInfinite(prob)) {
					probaList[j] = 10000000;
				} else {
					probaList[j] = prob;
				}
			} else {
				probaList[j] = 0;
			}
		}

		/*System.out.println("---------------");
		double pr = 0;
		for (int i = 0; i < probaList.length; i++) {
			pr += probaList[i];
			System.out.println(i + " : " + probaList[i] + ",");
		}*/
		try {
			EnumeratedIntegerDistribution distribution = new EnumeratedIntegerDistribution(cities, probaList);

			int numSamples = 1;
			int[] samples = distribution.sample(numSamples);

			return samples[0];
		} catch (MathArithmeticException e) {
			if (e.getMessage().equals("array sums to zero")) {
				System.out.println("ERROR : No path founded, the evaporation rate may be to high");
				System.out.println(e.getMessage());
			}

			System.exit(1);
		}
		return -1;
	}

	public void move(Map map) {
		addLength(map);
		updatePathMemory();
		visitedNode.add(destination);
		position = destination;
	}

	private void updatePathMemory() {
		int c1 = Math.max(position, destination);
		int c2 = Math.min(position, destination);

		pathMemory[c1][c2] = 1;
	}

	private void addLength(Map map) {
		lengthPath += map.getDistance(position, destination);
	}

	public Boolean wasThere(int city1, int city2) {
		if (city1 == city2) {
			return false;
		}
		int c1 = Math.max(city1, city2);
		int c2 = Math.min(city1, city2);

		return pathMemory[c1][c2] == 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + destination;
		long temp;
		temp = Double.doubleToLongBits(lengthPath);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + position;
		result = prime * result + ((visitedNode == null) ? 0 : visitedNode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ant other = (Ant) obj;
		if (destination != other.destination)
			return false;
		if (Double.doubleToLongBits(lengthPath) != Double.doubleToLongBits(other.lengthPath))
			return false;
		if (position != other.position)
			return false;
		if (visitedNode == null) {
			if (other.visitedNode != null)
				return false;
		} else if (!visitedNode.equals(other.visitedNode))
			return false;
		return true;
	}

}

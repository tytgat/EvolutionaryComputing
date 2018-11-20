import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/*
 * Algorithm based on this article :
 * https://courses.cs.ut.ee/all/MTAT.03.238/2011K/uploads/Main/04129846.pdf
 */

public class TSPAntColonyTytgatKarel {
	public static int		population		= 300;	// number of ants
	public static int		generations		= 3;	// generations (do not put a high number)
	public static int		pheromonePower	= 1;	// power of the pheromones compare to the distance
	public static int		distancePower	= 1;	// power of the distance compare to the pheromones
	public static double	evaporationRate	= 0.1;	// evaporation rate of the pheromones

	public static void main(String[] args) {
		if (args.length <= 0) {
			System.out.println("ERROR : Need the file path as argument of the program");
			return;
		}
		Map map = parseFile(args[0]);

		List<Integer> lstCities = new ArrayList<Integer>();
		for (int i = 0; i < map.getNbCities(); i++) {
			lstCities.add(i);
		}
		int numberOfAnts = population;
		Ant.pheromonePower = pheromonePower;
		Ant.distancePower = distancePower;
		Map.evaporationRate = evaporationRate;
		double bestOverallDist = -1;

		System.out.println("*** Traveler Salesman Problem - Ant colony solution ***");
		System.out.println("Number of cities              : " + map.getNbCities());
		System.out.println("Number of ants                : " + numberOfAnts);
		System.out.println("Number of generations         : " + generations);
		System.out.println("Importance Pheromone/Distance : " + pheromonePower + ":" + distancePower);
		System.out.println("Pheromone evaporation rate    : " + evaporationRate);
		System.out.println("----- ----- ----- ----- ----- -----");

		long startTimeOverall = System.currentTimeMillis();
		for (int nbGeneration = 0; nbGeneration < generations; nbGeneration++) {
			/* Set List of ants */
			List<Ant> listAnts = new ArrayList<Ant>();
			{
				Random rnd = new Random();
				for (int i = 0; i < numberOfAnts; i++) {
					listAnts.add(new Ant(rnd.nextInt(map.getNbCities()), map.getNbCities()));
				}
			}

			long startTime = System.currentTimeMillis();
			/* Move the ants */
			for (int i = 0; i < map.getNbCities() - 1; i++) {
				// System.out.println("City explored : " + i);
				for (Ant k : listAnts) {
					k.prepareMove(map);
				}
				// System.out.println("Update Ph");
				map.updatePheromones(listAnts);
			}

			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;

			/* get best distance */
			double bestDistance = -1;
			for (int i = 0; i < listAnts.size(); i++) {
				Ant k = listAnts.get(i);
				if (bestDistance == -1 || bestDistance > k.lengthPath) {
					bestDistance = k.lengthPath;
				}
			}

			if (bestOverallDist == -1 || bestDistance < bestOverallDist) {
				bestOverallDist = bestDistance;
			}

			System.out.println("Best distance generation " + (nbGeneration + 1) + "     : " + bestDistance);
			System.out.println("Execution time of generation " + (nbGeneration + 1) + " : " + elapsedTime * 0.001 + "s");
			System.out.println("----- ----- ----- ----- ----- -----");
		}
		long stopTimeOverall = System.currentTimeMillis();
		long elapsedTimeOverall = stopTimeOverall - startTimeOverall;
		System.out.println("Best distance founded : " + bestOverallDist);
		System.out.println("Global execution time : " + elapsedTimeOverall * 0.001 + "s");

	}

	public static Map parseFile(String arg0) {
		HashMap<Integer, int[]> mapCitiesCoords = new HashMap<Integer, int[]>();

		List<int[]> lstCities = readFile(arg0);
		int nbCities = 0;
		for (int[] val : lstCities) {
			mapCitiesCoords.put(nbCities, val);
			nbCities++;
		}

		if (mapCitiesCoords.size() <= 0) {
			System.out.println("ERROR : No point found in the file");
			return null;
		}

		double[][] distanceMat = new double[nbCities][];
		double[][] pheromonesTable = new double[nbCities][];

		for (int i = 0; i < nbCities; i++) {
			distanceMat[i] = new double[i];
			pheromonesTable[i] = new double[i];
			for (int j = 0; j < i; j++) {
				int[] p1 = mapCitiesCoords.get(i);
				int[] p2 = mapCitiesCoords.get(j);
				distanceMat[i][j] = Math.sqrt(Math.pow(p1[0] - p2[0], 2) + Math.pow(p1[1] - p2[1], 2));
				pheromonesTable[i][j] = 1;
			}
		}

		return new Map(distanceMat, nbCities, pheromonesTable);
	}

	public static List<int[]> readFile(String path) {
		List<int[]> cities = new ArrayList<int[]>();
		File file = new File(path);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;

			while ((text = reader.readLine()) != null) {
				if (text.matches("[0-9 ]+")) {
					String[] valueTable = text.split(" ");
					if (valueTable.length == 3) {
						int[] coords = new int[2];
						try {
							coords[0] = Integer.parseInt(valueTable[1]);
							coords[1] = Integer.parseInt(valueTable[2]);
						} catch (NumberFormatException nfe) {
							nfe.printStackTrace();
						}
						cities.add(coords);
					}
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}

		return cities;
	}

	public static double computeLength(double[] solution, double[][] distanceMat) {
		double distance = 0;
		for (int i = 0; i < solution.length - 1; i++) {
			int city1 = (int) Math.max(solution[i], solution[i + 1]);
			int city2 = (int) Math.min(solution[i], solution[i + 1]);
			distance += distanceMat[city1][city2];
		}

		return distance;
	}

}

class AntThread extends Thread {
	Ant	ant;
	Map	map;

	public AntThread(Ant ant, Map map) {
		this.ant = ant;
		this.map = map;
	}

	public void run() {
		ant.prepareMove(map);
	}
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.SteadyStateEvolutionEngine;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

public class TSPTytgatKarel {

	public static double[][] distanceMat;

	public static void main(String[] args) {
		HashMap<Integer, int[]> mapCitiesCoords = new HashMap<Integer, int[]>();

		{
			if (args.length <= 0) {
				System.out.println("ERROR : Need the file path as argument of the program");
				return;
			}
			int nbCities = 0;
			List<int[]> lstCities = readFile(args[0]);
			for (int[] val : lstCities) {
				mapCitiesCoords.put(nbCities, val);
				nbCities++;
			}
			if (mapCitiesCoords.size() <= 0) {
				System.out.println("ERROR : No point found in the file");
				return;
			}

			distanceMat = new double[nbCities][];

			for (int i = 0; i < nbCities; i++) {
				distanceMat[i] = new double[i];
				for (int j = 0; j < i; j++) {
					int[] p1 = mapCitiesCoords.get(i);
					int[] p2 = mapCitiesCoords.get(j);
					distanceMat[i][j] = Math.sqrt(Math.pow(p1[0] - p2[0], 2) + Math.pow(p1[1] - p2[1], 2));
				}
			}

			// TODO print
			/*
			for (int coords : mapCitiesCoords.keySet()) {
				System.out.println(
						coords + ": [" + mapCitiesCoords.get(coords)[0] + "," + mapCitiesCoords.get(coords)[1] + "]");
			}
			for (int i = 00; i < nbCities; i++) {
				for (int j = 0; j < i; j++) {
					System.out.print((int) distanceMat[i][j] + ",");
				}
				System.out.println();
			}*/
		}

		int dimension = mapCitiesCoords.size(); // number of cities
		int populationSize = 200; // size of population
		int generations = 10000; // number of generations
		Random random = new Random(); // random

		CandidateFactory<double[]> factory = new MyFactory(dimension); // generation of solutions

		ArrayList<EvolutionaryOperator<double[]>> operators = new ArrayList<EvolutionaryOperator<double[]>>();
		operators.add(new MyCrossover()); // Crossover
		operators.add(new MyMutation()); // Mutation
		EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<double[]>(operators);

		SelectionStrategy<Object> selection = new RouletteWheelSelection(); // Selection operator

		FitnessEvaluator<double[]> evaluator = new FitnessFunction(dimension, distanceMat); // Fitness function

		EvolutionEngine<double[]> algorithm = new SteadyStateEvolutionEngine<double[]>(factory, pipeline, evaluator,
				selection, populationSize, false, random);

		algorithm.addEvolutionObserver(new EvolutionObserver<Object>() {
			public void populationUpdate(PopulationData<?> populationData) {
				double bestFit = populationData.getBestCandidateFitness();
				System.out.println("Generation " + populationData.getGenerationNumber() + ": " + bestFit);
				System.out
						.println("\tBest solution = " + Arrays.toString((double[]) populationData.getBestCandidate()));
				System.out.println("\tBest distance = "
						+ computeLength((double[]) populationData.getBestCandidate(), distanceMat));
				System.out.println("\tPop size = " + populationData.getPopulationSize());
			}
		});

		TerminationCondition terminate = new GenerationCount(generations);
		algorithm.evolve(populationSize, 1, terminate);

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

	// TODO DELETE !
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

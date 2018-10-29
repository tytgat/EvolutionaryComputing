import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

public class MyAlg {

	public static void main(String[] args) {
		if (args.length <= 0) {
			System.out.println("ERROR : Need the file path as argument of the program");
			return;
		}
		List<int[]> lstCities = readFile(args[0]);
		for (int[] val : lstCities) {
			System.out.println("[" + val[0] + "," + val[1] + "]");
		}
		if (lstCities.size() <= 0) {
			System.out.println("ERROR : No point found in the file");
			return;
		}

		int dimension = lstCities.size(); // number of cities
		int populationSize = 10; // size of population
		int generations = 10; // number of generations
		Random random = new Random(); // random

		CandidateFactory<double[]> factory = new MyFactory(dimension); // generation of solutions

		ArrayList<EvolutionaryOperator<double[]>> operators = new ArrayList<EvolutionaryOperator<double[]>>();
		operators.add(new MyCrossover()); // Crossover operators.add(new MyMutation()); // Mutation
		EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<double[]>(operators);

		SelectionStrategy<Object> selection = new RouletteWheelSelection(); // Selection operator

		FitnessEvaluator<double[]> evaluator = new FitnessFunction(dimension); // Fitness function

		EvolutionEngine<double[]> algorithm = new SteadyStateEvolutionEngine<double[]>(factory, pipeline, evaluator,
				selection, populationSize, false, random);

		algorithm.addEvolutionObserver(new EvolutionObserver<Object>() {
			public void populationUpdate(PopulationData<?> populationData) {
				double bestFit = populationData.getBestCandidateFitness();
				System.out.println("Generation " + populationData.getGenerationNumber() + ": " + bestFit);
				System.out
						.println("\tBest solution = " + Arrays.toString((double[]) populationData.getBestCandidate()));
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

}

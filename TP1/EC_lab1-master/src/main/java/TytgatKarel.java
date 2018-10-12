import java.util.ArrayList;
import java.util.Arrays;
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

public class TytgatKarel {

	public static void main(String[] args) {
		int dimension = 100; // dimension of problem
		int populationSize = 100; // size of population
		int generations = 10000; // number of generations

		Random random = new Random(); // random

		CandidateFactory<double[]> factory = new MyFactory(dimension); // generation of solutions

		ArrayList<EvolutionaryOperator<double[]>> operators = new ArrayList<EvolutionaryOperator<double[]>>();
		operators.add(new MyCrossover()); // Crossover
		operators.add(new MyMutation()); // Mutation
		EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<double[]>(operators);

		SelectionStrategy<Object> selection = new RouletteWheelSelection(); // Selection operator

		FitnessEvaluator<double[]> evaluator = new FitnessFunction(dimension); // Fitness function

		EvolutionEngine<double[]> algorithm = new SteadyStateEvolutionEngine<double[]>(factory, pipeline, evaluator,
				selection, populationSize, false, random);

		algorithm.addEvolutionObserver(new EvolutionObserver() {
			public void populationUpdate(PopulationData populationData) {
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
}

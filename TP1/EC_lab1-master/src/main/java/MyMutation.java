import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.List;
import java.util.Random;

public class MyMutation implements EvolutionaryOperator<double[]> {
    public List<double[]> apply(List<double[]> population, Random random) {
        // initial population
        // need to change individuals, but not their number!

        // your implementation:
    	for(double[] individual : population) {
    		for(int i = 0 ; i < individual.length; i++) {
    			if(random.nextInt(individual.length) < 0) {
    				individual[i] = (random.nextDouble() * 10) - 5;
    			}
			}
    	}
        //result population
        return population;
    }
}

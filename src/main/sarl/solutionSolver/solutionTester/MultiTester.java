package solutionSolver.solutionTester;

import java.util.List;

import map.SimulationMap;

public abstract class MultiTester {

	protected SimulationMap map;
	protected int nbStep;
	protected int nbTestByStep;
	
	protected List<Double> energyCostResults;
	protected List<Double> timeCostResults;
	
	protected int i = 0; //valeur variant entre 0 et nbStep
	protected int j = 0; //valeur variant entre 0 et nbTestByStep
	
	public MultiTester(SimulationMap map, int nbStep) {
		this.map = map;
	}
	
	public void runMultiTest() {
		while(!runMultiTestStep());
	}
	
	public abstract boolean runMultiTestStep();

	public int getNbStep() {
		return nbStep;
	}

	public List<Double> getEnergyCostResults() {
		return energyCostResults;
	}

	public List<Double> getTimeCostResults() {
		return timeCostResults;
	}
	
	
}

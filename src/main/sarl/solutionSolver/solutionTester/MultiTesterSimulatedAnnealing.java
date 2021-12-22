package solutionSolver.solutionTester;

import map.MapPoint;
import map.SimulationMap;
import solutionSolver.SolutionSolver;
import solutionSolver.simulatedAnnealing.SimulatedAnnealing;
import java.util.List;

public class MultiTesterSimulatedAnnealing extends MultiTester {
	
	SimulatedAnnealing sim;
	double minTempInit, maxTempInit;
	double minTreshold, maxTreshold;
	double minAlpha, maxAlpha;
	int minNbItPalier, maxNbItPalier;

	public MultiTesterSimulatedAnnealing(SimulationMap map, int nbStep,
			double minTempInit, double maxTempInit,
			double minTreshold, double maxTreshold,
			double minAlpha, double maxAlpha,
			int minNbItPalier, int maxNbItPalier){
		super(map, nbStep);
		sim = new SimulatedAnnealing(map);
	}
	
	@Override
	public boolean runMultiTestStep() {

		/*double currentTempInit = minTempInit + (  i * (maxTempInit - minTempInit) / this.nbStep);
		double currentTreshold = minTreshold + (  i * (maxTreshold - minTreshold) / this.nbStep);
		double currentAlpha = minAlpha + (  i * (maxAlpha - minAlpha) / this.nbStep);
		int currentNbItPalier = minNbItPalier + (  i * (maxNbItPalier - minNbItPalier) / this.nbStep);
		List<List<MapPoint>> currentSolution =
				sim.Solve(currentTempInit, currentTreshold, currentAlpha, currentNbItPalier);
		double Energycost = SolutionSolver.solutionCost(currentSolution);
		double timeCost = SolutionSolver.solutionTimeCost(solution, nDrone)
		return i * nbTestByStep + j >= nbStep * nbTestByStep;*/
		return true;
	}

}

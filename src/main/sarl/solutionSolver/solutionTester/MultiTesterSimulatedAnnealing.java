package solutionSolver.solutionTester;

import map.MapPoint;
import map.SimulationMap;
import solutionSolver.SolutionSolver;
import solutionSolver.simulatedAnnealing.SimulatedAnnealing;

import java.util.ArrayList;
import java.util.List;

public class MultiTesterSimulatedAnnealing extends MultiTester {
	
	SimulatedAnnealing sim;
	double minTempInit, maxTempInit;
	double minTreshold, maxTreshold;
	double minAlpha, maxAlpha;
	double minNbItPalier, maxNbItPalier;

	List<Double> currentEnergyCosts;
	List<Double> currentTimeCosts;
	List<List<List<MapPoint>>> currentSolutions;

	public MultiTesterSimulatedAnnealing(SimulationMap map, int nbStep, int nbTestByStep, int nbDrones,
			double minTempInit, double maxTempInit,
			double minTreshold, double maxTreshold,
			double minAlpha, double maxAlpha,
			int minNbItPalier, int maxNbItPalier){
		
		super(map, nbStep, nbTestByStep, nbDrones);
		sim = new SimulatedAnnealing(map);
		this.minTempInit = minTempInit;
		this.maxTempInit = maxTempInit;
		this.minTreshold = minTreshold;
		this.maxTreshold = maxTreshold;
		this.minAlpha = minAlpha;
		this.maxAlpha = maxAlpha;
		this.minNbItPalier = minNbItPalier;
		this.maxNbItPalier = maxNbItPalier;

		this.currentEnergyCosts = new ArrayList<>();
		this.currentTimeCosts = new ArrayList<>();
		this.currentSolutions = new ArrayList<>();
		
	}
	
	@Override
	public boolean runMultiTestStep() {
		
		//calcul des paramètres pour l'étape actuelle
		double currentTempInit = minTempInit + (  i * (maxTempInit - minTempInit) / this.nbStep);
		double currentTreshold = minTreshold + (  i * (maxTreshold - minTreshold) / this.nbStep);
		double currentAlpha = minAlpha + (  i * (maxAlpha - minAlpha) / this.nbStep);
		double currentNbItPalier = minNbItPalier + (  i * (maxNbItPalier - minNbItPalier) / this.nbStep);
		
		//calcul de la solution
		List<List<MapPoint>> sol =
				sim.Solve(currentTempInit, currentTreshold, currentAlpha, (int)currentNbItPalier);
		
		//récupération des résulats
		double energycost = SolutionSolver.solutionCost(sol);
		double timeCost = SolutionSolver.solutionTimeCost(sol, nbDrones);
		this.currentEnergyCosts.add(energycost);
		this.currentTimeCosts.add(timeCost);
		this.currentSolutions.add(sol);
		
		j++;
		if(j >= nbTestByStep) { //si tout les tests d'une étapes ont été fait
			
			//calcul des moyennes
			this.energyCostResults.add(meanOfList(currentEnergyCosts));
			this.timeCostResults.add(meanOfList(currentTimeCosts));
			
			//récupération de la meilleur solution de l'étape
			int idxBestSolution = bestSolution(currentEnergyCosts);
			this.solutionsHistory.add(currentSolutions.get(idxBestSolution));
			
			//nettoyage des listes
			this.currentEnergyCosts.clear();
			this.currentTimeCosts.clear();
			this.currentSolutions.clear();
			j = 0;
			i++;
		}
		
		return getAvancement() >= 1;
	}
	
	

}

package solutionSolver.solutionTester;

import map.MapPoint;
import map.SimulationMap;
import solutionSolver.SolutionSolver;
import solutionSolver.qLearning.QLearning;

import java.util.ArrayList;
import java.util.List;

public class MultiTesterQLearning extends MultiTester {
	
	QLearning sim;
	double minAlpha, maxAlpha;
	double minGamma, maxGamma;
	double minEpsilon, maxEpsilon;
	int minNbEpisode, maxNbEpisode;

	List<Double> currentEnergyCosts;
	List<Double> currentTimeCosts;
	List<List<List<MapPoint>>> currentSolutions;

	public MultiTesterQLearning(SimulationMap map, int nbStep, int nbTestByStep, int nbDrones,
			double minAlpha, double maxAlpha,
			double minGamma, double maxGamma,
			double minEpsilon, double maxEpsilon,
			int minNbEpisode, int maxNbEpisode){
		
		super(map, nbStep, nbTestByStep, nbDrones);
		sim = new QLearning(map);

		this.minAlpha = minAlpha;
		this.maxAlpha = maxAlpha;
		this.minAlpha = minGamma;
		this.maxAlpha = maxGamma;
		this.minAlpha = minEpsilon;
		this.maxAlpha = maxEpsilon;
		this.minNbEpisode = minNbEpisode;
		this.maxNbEpisode = maxNbEpisode;

		this.currentEnergyCosts = new ArrayList<>();
		this.currentTimeCosts = new ArrayList<>();
		this.currentSolutions = new ArrayList<>();
		
	}
	
	@Override
	public boolean runMultiTestStep() {


		//calcul des paramètres pour l'étape actuelle
		double currentAlpha = minAlpha + (  i * (maxAlpha - minAlpha) / this.nbStep);
		double currentGamma = minGamma + (  i * (maxGamma - minGamma) / this.nbStep);
		double currentEpsilon = minEpsilon + (  i * (maxEpsilon - minEpsilon) / this.nbStep);
		double currentNbEpisode = minNbEpisode + (  i * (maxNbEpisode - minNbEpisode) / this.nbStep);
		
		//calcul de la solution
		List<List<MapPoint>> sol =
				sim.Solve((int)currentNbEpisode, currentAlpha, currentGamma, currentEpsilon, -100);


		//récupération des résulats
		double energycost = SolutionSolver.solutionCost(sol);
		double timeCost = SolutionSolver.solutionTimeCost(sol, nbDrones);
		this.currentEnergyCosts.add(energycost);
		this.currentTimeCosts.add(timeCost);
		this.currentSolutions.add(sol);
		j++;
		if(j >= nbTestByStep) {
			
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

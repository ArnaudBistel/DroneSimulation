package solutionSolver.solutionTester;

import java.util.ArrayList;
import java.util.List;

import map.MapPoint;
import map.SimulationMap;

public abstract class MultiTester {

	protected SimulationMap map;
	protected int nbStep;
	protected int nbTestByStep;
	protected int nbDrones;
	
	protected List<Double> energyCostResults;
	protected List<Double> timeCostResults;
	protected List< List<List<MapPoint>> > solutionsHistory; 
	
	protected double i = 0; //valeur variant entre 0 et nbStep
	protected double j = 0; //valeur variant entre 0 et nbTestByStep
	
	public MultiTester(SimulationMap map, int nbStep, int nbTestByStep, int nbDrones) {
		this.map = map;
		this.nbDrones = nbDrones;
		this.nbStep = nbStep;
		this.nbTestByStep = nbTestByStep;
		this.energyCostResults = new ArrayList<>();
		this.timeCostResults = new ArrayList<>();
		this.solutionsHistory = new ArrayList<>();
	} 
	
	public void runMultiTest() {
		while(!runMultiTestStep());
	}
	
	public abstract boolean runMultiTestStep(); 
	//renvoie true si le multiTest est terminer

	public int getNbStep() {
		return nbStep;
	}

	public List<Double> getEnergyCostResults() {
		return energyCostResults;
	}

	public List<Double> getTimeCostResults() {
		return timeCostResults;
	}
	
	public List< List<List<MapPoint>> > getSolutionsHistory() {
		return solutionsHistory;
	}
	
	public double getAvancement() {
		return ((double)i * (double)nbTestByStep + (double)j) / ((double)nbStep * (double)nbTestByStep);
	}
	
	protected double meanOfList(List<Double> list) {
		//fait la moyenne des doubles d'une liste
		double v = 0;
		for (Double e : list) {
			v += e;
		}
		return v / list.size();
	}
	
	protected int bestSolution(List<Double> energyCost){
		//récupère l'id de la mailleur solution d'une liste
		int id = 0;
		double minCost = energyCost.get(0);
		for(int i = 1; i < energyCost.size(); i++) {
			if(energyCost.get(i) < minCost) {
				id = i;
				minCost = energyCost.get(i);
			}
		}
		return id;
	}
	
	
}

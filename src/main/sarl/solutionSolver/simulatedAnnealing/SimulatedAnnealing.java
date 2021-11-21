package solutionSolver.simulatedAnnealing;

import java.util.ArrayList;
import java.util.List;

import map.MapPoint;
import map.SimulationMap;
import solutionSolver.FakeSimulationMap;
import solutionSolver.SolutionSolver;

public class SimulatedAnnealing extends SolutionSolver {

	public SimulatedAnnealing(FakeSimulationMap map) {
		super(map);
	}
	
	@Override
	public List<List<MapPoint>> Solve() {
		//Fonction solve avec des paramètres par défauts
		return Solve(5.0, 0.5);
	}

	public List<List<MapPoint>> Solve(double t, double treshold) {
		//résolution par recuit simulé
		
		List<List<MapPoint>> s = getInitialSolution(); //s initialiser a la solution initial
		
		return null;
	}
	
	private List<List<MapPoint>> getInitialSolution() {
		//Pour chaque point de livraisons, on créer une liste contenant :
		// le depot le plus proche -> le point -> le depot le plus proche
		
		List<List<MapPoint>> res = new ArrayList<List<MapPoint>>();
		for(MapPoint p : map.getClients()) {
			ArrayList<MapPoint> list = new ArrayList<>();
			MapPoint cW = getClosestWharehouse(p);
			list.add(cW);
			list.add(p);
			list.add(cW);
			res.add(list);
		}
		return res;
	}
	
	static private MapPoint getClosestWharehouse(MapPoint p) {
		//return le depot le plus proche d'un point de livraison
		
		return null;
	}

}

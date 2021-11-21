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
		return Solve(5.0, 0.5, 0.9, 5);
	}

	public List<List<MapPoint>> Solve(double tempInit, double treshold, double alpha, int nbItPalier) {
		//résolution par recuit simulé
		
		//initialisation
		double t = tempInit;
		List<List<MapPoint>> s = getInitialSolution(); //s initialiser a la solution initial
		
		//boucle sur la temperature
		while(t > treshold) {
			//boucle sur n iteration par palier de temperature
			for(int i = 0; i < nbItPalier; i++) {
				//selection d'un voisin de s
				List<List<MapPoint>> neighbor = getRandomNeighbor(s);
				
				//test si le voisin est garder
				
				
			}
			
			//reduction de la temperature
			t = t * alpha;
		}
		
		return s;
	}
	
	private List<List<MapPoint>> getInitialSolution() {
		//Pour chaque point de livraisons, on créer une liste contenant :
		//le depot le plus proche -> le point -> le depot le plus proche
		
		List<List<MapPoint>> res = new ArrayList<List<MapPoint>>();
		for(MapPoint p : map.getClients()) {
			ArrayList<MapPoint> list = new ArrayList<>();
			MapPoint cW = SolutionSolver.getClosestWharehouse(p, map);
			list.add(cW);
			list.add(p);
			list.add(cW);
			res.add(list);
		}
		return res;
	}
	
	private List<List<MapPoint>> getRandomNeighbor(List<List<MapPoint>> s) {
		return null;
	}

}

package solutionSolver.simulatedAnnealing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import map.MapPoint;
import map.MapPointType;
import map.SimulationMap;
import solutionSolver.SolutionSolver;
import utils.variables.SimulationParameters;

public class SimulatedAnnealing extends SolutionSolver {
	
	public SimulatedAnnealing(SimulationMap map, Random random) {
		super(map, random);
	}

	public SimulatedAnnealing(SimulationMap map) {
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
		List<MapPoint> s = getInitialSolution(); //s initialiser a la solution initial
		
		//boucle sur la temperature
		while(t > treshold) {
			//boucle sur n iteration par palier de temperature
			for(int i = 0; i < nbItPalier; i++) {
				//selection d'un voisin de s
				List<MapPoint> sPrime = getRandomNeighbor(s);
				
				//test si le voisin est gardé
				double costSPrime = solutionCost(convertListToListOfList(sPrime),true);
				if(sPrime.get(0).getType() == MapPointType.WAREHOUSE && costSPrime > 0) {
					//costSPrime < 0 veut dire que la solution générer ne 
					//respect pas les contraintes de l'exercice
					//exemple : le cout en energie d'une livraison dépasse la capacité d'un drone
					
					double costS = solutionCost(convertListToListOfList(s));
					double deltaF = costS - costSPrime;
					if(deltaF < 0 ) {					
						//le voisin est meilleur
						s = sPrime;
					}
					else {
						//le voisin est moins bon, on le prend avec une certaine probabilité
						double r = random.nextDouble();
						if(r < Math.exp(deltaF / t)) {
							s = sPrime;
						}
					}
				}
			}
			//reduction de la temperature
			t = t * alpha;
		}
		return convertListToListOfList(s);
	}
	
	public List<MapPoint> getInitialSolution() {
		//Pour chaque point de livraisons, on créer une liste contenant :
		//le depot le plus proche -> le point -> le depot le plus proche
		//construit la list de la manière suivante : 
		//client n+1 est le client le plus proche de n n'aillant pas déjà été selectionné
		//les clients non accessibles par drone sont ignorés
		
		List<MapPoint> res = new ArrayList<MapPoint>();
		List<MapPoint> clients = new ArrayList<MapPoint>(map.getClients());
		int size = map.getClients().size();
		
		//first add
		res.add(getClosestWharehouse(clients.get(0), map));
		res.add(clients.remove(0));
		
		//add all clients
		for(int i = 1; i < size; i++) {
			//look for closest clients and add it
			
			int iCC = i; //index of closest clients
			double dCC = Double.MAX_VALUE; //distance of closest clients
			for(int j = 0; j < clients.size(); j++) {
				double d = getDistanceBetweenTwoPoints(res.get(res.size()-1), clients.get(j));
				if(d < dCC) {
					dCC = d;
					iCC = j;
				}
			}
			MapPoint target = clients.remove(iCC);
			res.add(getClosestWharehouse(target,map));
			res.add(target);
		}
		
		//remove inaccessible clients
		for(int i = res.size() - 1; i >= 0 ; i -= 2) {
			double d = pathEnergyCost(Arrays.asList( res.get(i-1), res.get(i), res.get(i-1)));
			if(d > SimulationParameters.DRONE_MAX_ENERGY) {
				res.remove(i);
				res.remove(i-1);
			}
		}
		return res;
	}
	
	private List<MapPoint> getRandomNeighbor(List<MapPoint> s) {
		//get a neighbor solution by using a random neighbor function
		
		int choice = random.nextInt(3);
		int idx0 = random.nextInt(s.size());
		int idx1 = random.nextInt(s.size());
		switch (choice) {
		case 0:
			return swap(new ArrayList<MapPoint>(s),idx0,idx1);
		case 1:
			return relocate(new ArrayList<MapPoint>(s),idx0,idx1);
		default: //value 2
			return TwoOpt(new ArrayList<MapPoint>(s),idx0,idx1);
		}
	}
	
	private <T> List<T> swap(List<T> solution, int idx0, int idx1) {
		//this method swap the elements at pos idx0 and idx1
		
		T tmp = solution.get(idx0);
		solution.set(idx0, solution.get(idx1));
		solution.set(idx1, tmp);
		
		return solution;
	}
	
	private <T> List<T> relocate(List<T> solution, int idx0, int idx1) {
		//this method do a shift between pos idx0 and idx1
		
		solution.add(idx0,solution.remove(idx1));
		return solution;
	}
	
	private <T> List<T> TwoOpt(List<T> solution, int idx0, int idx1) {
		//this method do mirror rotation on the list between pos idx0 and idx1
		
		int a = idx0;
		int b = idx1;
		if(b < a) {
			a = idx1;
			b = idx0;
		}
		while(a < b) {
			swap(solution,a,b);
			a++;
			b--;
		}
		return solution;
	}
}
package solutionSolver.simulatedAnnealing;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import map.MapPoint;
import map.MapPointType;
import map.SimulationMap;
import solutionSolver.SolutionSolver;

public class SimulatedAnnealingAnalyser {
	static final int NB_TRY_PER_VALUE = 25;

	public SimulatedAnnealingAnalyser() {
		super();
	}
	
	public static void startAnalyzing(String outputFileName) {
		SimulationMap map = new SimulationMap(100, 1, 1718);
		uniqueTest(map);
		
	}
	
	public static void uniqueTest(SimulationMap map) {
		SimulatedAnnealing sim = new SimulatedAnnealing(map);
		DecimalFormat df = new DecimalFormat("0.00");
		double initCost = SolutionSolver.solutionCost(sim.convertListToListOfList(sim.getInitialSolution()));
		info("initial cost : " + df.format(initCost));
		
		long startTime = System.nanoTime();
		List<List<MapPoint>> solution = sim.Solve(500,0.05,0.9999,10);
		double stopTime = System.nanoTime();
		
		double finalCost = SolutionSolver.solutionCost(solution);
		info("final cost : " + df.format(finalCost));
		info("improve : " + df.format((initCost - finalCost)));
		
		List<MapPoint> clts = new ArrayList<MapPoint>(map.getClients());
		for(List<MapPoint> e : solution)
			clts.removeAll(e);
		info("clients ignored : " + clts.size());
		info("exec (ms) : " + ((stopTime - startTime) / 1000000));
	}
	
	private static void info(String s) {
		System.out.println(s);
	}
	
	private static void printSolution2d(List<List<MapPoint>> s) {
		int i = 0;
		int j = 0;
		System.out.print("{");
		for(List<MapPoint> l : s) {
			j = 0;
			System.out.print(((i != 0)? "," : "") + "{");
			for(MapPoint e : l) {
				System.out.print(((j != 0)? "," : "") + 
						((e.getType() == MapPointType.WAREHOUSE)?
								"W" : ("[" + e.getScaledX() + ";" + e.getScaledY() + "]"))
						);
				j++;
			}
			System.out.print("}");
			i++;
		}
		System.out.println("}");
	}
	
	private static void printSolution(List<MapPoint> s) {
		int i = 0;
		int j = 0;
		System.out.print("{");
		for(MapPoint e : s) {
			System.out.print(((j != 0)? "," : "") + 
					((e.getType() == MapPointType.WAREHOUSE)?
							"W" : ("[" + e.getScaledX() + ";" + e.getScaledY() + "]"))
					);
			j++;
		}
		System.out.println("}");
	}
}

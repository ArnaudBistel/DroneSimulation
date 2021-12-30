package solutionSolver.simulatedAnnealing;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import map.MapPoint;
import map.MapPointType;
import map.SimulationMap;
import solutionSolver.SolutionSolver;
import utils.fileWriter.ExcelFileWriter;

public class SimulatedAnnealingAnalyser {

	public static final int TEST_SEED = 0;
	private static int[] test_seeds;
	
	//loop settings
	public static final int NB_TRY_PER_VALUE = 100;//25
	public static final int NB_STEP_TEST = 15;//50
	
	//default values
	public static final double DFT_TEMP = 200;
	public static final double DFT_THRESHOLD = 0.1;
	public static final double DFT_ALPHA = 0.9;
	public static final int DFT_NB_ITER_PALIER = 10;
	
	//temp
	public static final double INIT_TEMP = 200;
	public static final double STEP_TEMP = 100;
	//alpha
	public static final double INIT_ALPHA = 0.9;
	public static final double STEP_ALPHA = 0.25;
	//nb iter palier
	public static final int INIT_NB_ITER_PALIER = 1;
	public static final int STEP_NB_ITER_PALIER = 20;
	
	//print settings
	public static final int LOADING_BAR_SIZE = 40;
	

	static DecimalFormat df = new DecimalFormat("0.00");
	static DecimalFormat tf = new DecimalFormat("00");//remaining time format
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	

	public SimulatedAnnealingAnalyser() {
		super();
	}
	
	public static void startAnalyzing(String outputFileName) {

		//init seeds
		Random random = new Random(TEST_SEED);
		test_seeds = new int[NB_TRY_PER_VALUE];
		for(int i = 0; i < NB_TRY_PER_VALUE; i++) {
			test_seeds[i] = random.nextInt();
		}
		
		SimulationMap map = new SimulationMap(100, 1, test_seeds[0] + TEST_SEED, Arrays.asList(1.0f,5.0f)); //generate a map
		SimulatedAnnealing sim = new SimulatedAnnealing(map);
		double initCost = SolutionSolver.solutionCost(sim.convertListToListOfList(sim.getInitialSolution()));

		double[] costResultTemp = new double[NB_STEP_TEST];
		double[] costResultAlpha = new double[NB_STEP_TEST];
		double[] costResultNbIterPalier = new double[NB_STEP_TEST];
		

		double[] timeResultTemp = new double[NB_STEP_TEST];
		double[] timeResultAlpha = new double[NB_STEP_TEST];
		double[] timeResultNbIterPalier = new double[NB_STEP_TEST];
		
		double initTime = System.nanoTime();
		double initialCost = 0;
		info(getAvancementStringBar(initTime, 0, -1));//print 0%
		
		for(int i = 0; i < NB_TRY_PER_VALUE; i++) {
			map = new SimulationMap(100, 1, test_seeds[i % test_seeds.length] + TEST_SEED, Arrays.asList(1.0f,5.0f));
			for(int j = 0; j < NB_STEP_TEST; j++) {
				//Temp section
				double temp = INIT_TEMP + j * STEP_TEMP;
				double[] r = singleTest(sim, temp, DFT_THRESHOLD, DFT_ALPHA, DFT_NB_ITER_PALIER);
				costResultTemp[j] += r[0];
				timeResultTemp[j] += r[1];

				//alpha section
				double alphaDiv = 1 + j * STEP_ALPHA;
				double alpha = 1 - ((1 - INIT_ALPHA)/alphaDiv);
				r = singleTest(sim, DFT_TEMP, DFT_THRESHOLD, alpha, DFT_NB_ITER_PALIER);
				costResultAlpha[j] += r[0];
				timeResultAlpha[j] += r[1];

				//Temp nb iter palier
				int nbIterPalier = INIT_NB_ITER_PALIER + j * STEP_NB_ITER_PALIER;
				r = singleTest(sim, DFT_TEMP, DFT_THRESHOLD, DFT_ALPHA, nbIterPalier);
				costResultNbIterPalier[j] += r[0];
				timeResultNbIterPalier[j] += r[1];
				
				//print avancement
				info(getAvancementStringBar(initTime, i, j));
			}
			
			initialCost += SolutionSolver.solutionCost(
					sim.convertListToListOfList(sim.getInitialSolution())
					);
		}
		
		for(int j = 0; j < NB_STEP_TEST; j++) {
			costResultTemp[j] /= NB_TRY_PER_VALUE;
			timeResultTemp[j] /= NB_TRY_PER_VALUE;
			costResultAlpha[j] /= NB_TRY_PER_VALUE;
			timeResultAlpha[j] /= NB_TRY_PER_VALUE;
			costResultNbIterPalier[j] /= NB_TRY_PER_VALUE;
			timeResultNbIterPalier[j] /= NB_TRY_PER_VALUE;
		}
		
		//construct initial cost
		initialCost = initialCost / NB_TRY_PER_VALUE;
		ExcelFileWriter.printInFile(outputFileName +"_"+TEST_SEED, initialCost,
				costResultTemp, costResultAlpha, costResultNbIterPalier,
				timeResultTemp, timeResultAlpha, timeResultNbIterPalier);
		info("analyze finished");
	}
	
	public static double[] singleTest(SimulatedAnnealing sim, double initTemp,
		double tempTreshold, double alpha, int nbItPalier) {
		//process a simulated annealing resolution and return [solution cost, execution time]
		
		long startTime = System.nanoTime();
		List<List<MapPoint>> solution = sim.Solve(initTemp, tempTreshold, alpha, nbItPalier);
		double stopTime = System.nanoTime();
		double cost = SolutionSolver.solutionCost(solution);
		
		double[] res = {cost, (stopTime - startTime) / 1000000}; 
		return res;
	}
	
	public static void test(SimulatedAnnealing sim, SimulationMap map) {
		//SimulatedAnnealing sim = new SimulatedAnnealing(map);
		double initCost = SolutionSolver.solutionCost(sim.convertListToListOfList(sim.getInitialSolution()));
		info("initial cost : " + df.format(initCost));
		
		long startTime = System.nanoTime();
		List<List<MapPoint>> solution = sim.Solve(DFT_TEMP, DFT_THRESHOLD, DFT_ALPHA, DFT_NB_ITER_PALIER);
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
		
		System.out.println("[" + dtf.format(LocalDateTime.now()) + "] " + s);
	}
	
	private static String getAvancementStringBar(double initTime, float num_try, float num_step) {
		float pct = (num_try * NB_STEP_TEST + num_step + 1) / (NB_STEP_TEST * NB_TRY_PER_VALUE);
		int nbCharLoaded = (int)(pct * LOADING_BAR_SIZE);
		String output = "";
		
		for(int i = 0; i < nbCharLoaded; i++) {
			output += "#";
		}
		for(int i = nbCharLoaded; i < LOADING_BAR_SIZE; i++) {
			output += ".";
		}
		
		double currentTime = (System.nanoTime() - initTime) / 1000000;
		double remainingTime = currentTime * (1 - pct) / pct;
		
		int seconds = (int)remainingTime / 1000;
		int minutes = seconds / 60;
		int hours = minutes / 60;
		
		output = "analysing : [" + output + "] (" + df.format(100*pct) + "%) - remaining time : " 
				+ tf.format(hours) + ":" + tf.format(minutes) + ":" + tf.format(seconds%60);
		return output;
	}
	
	public static void printSolution2d(List<List<MapPoint>> s) {
		//print solution like : {{W,[12,15],W},{W,[28,2],[6,42],W}}
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
	
	public static void printSolution(List<MapPoint> s) {
		//print solution like : {W,[12,15],W,[28,2],[6,42]}
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

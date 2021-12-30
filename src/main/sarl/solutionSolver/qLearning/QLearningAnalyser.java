package solutionSolver.qLearning;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import map.MapPoint;
import map.SimulationMap;
import solutionSolver.SolutionSolver;
import utils.fileWriter.ExcelFileWriterQLearning;

public class QLearningAnalyser {
	
	public static final int TEST_SEED = 0;
	private static int[] test_seeds;
	
	public static final int DFT_REWARD_WH = -1;
	public static final int DFT_REWARD_C = 20;
	public static final int DFT_PENALTY = -100;
	
	public static final double DFT_ALPHA = 0.8;
	public static final double DFT_GAMMA = 0.8;
	public static final double DFT_EPSILON = 0.9;
	
	public static final double INIT_ALPHA = 0.1;
	public static final double STEP_ALPHA = 2;
	
	public static final double INIT_GAMMA = 0.1;
	public static final double STEP_GAMMA = 2;
	
	public static final double INIT_EPSILON = 0.1;
	public static final double STEP_EPSILON = 2;
	
	public static final int NB_TRY_PER_VALUE = 25;//25
	public static final int NB_STEP_TEST = 10;
	public static final int NB_EPISODE = 50;//50
	
	public static final int LOADING_BAR_SIZE = 40;
	
	static DecimalFormat df = new DecimalFormat("0.00");
	static DecimalFormat tf = new DecimalFormat("00");//remaining time format
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	public QLearningAnalyser() {
		super();
	}
	
	public static void startQLearningAnalyzing(String outputFileName) {
		Random random = new Random(TEST_SEED);
		test_seeds = new int[NB_TRY_PER_VALUE];
		for(int i = 0; i < NB_TRY_PER_VALUE; i++) {
			test_seeds[i] = random.nextInt();
		}
		
		SimulationMap map = new SimulationMap(10, 1, test_seeds[0] + TEST_SEED);
		QLearning sim = new QLearning(map);
		
		double[] costResultAlpha = new double[NB_STEP_TEST];
		double[] costResultGamma = new double[NB_STEP_TEST];
		double[] costResultEpsilon = new double[NB_STEP_TEST];
		double[] costResultNbEpisode = new double[NB_STEP_TEST];

		double[] timeResultAlpha = new double[NB_STEP_TEST];
		double[] timeResultGamma = new double[NB_STEP_TEST];
		double[] timeResultEpsilon = new double[NB_STEP_TEST];
		double[] timeResultNbEpisode = new double[NB_STEP_TEST];
		
		double initTime = System.nanoTime();
		double initialCost = 0;
		info(getAvancementStringBar(initTime, 0, -1));
		
		for(int i = 0; i < NB_TRY_PER_VALUE; i++) {
			map = new SimulationMap(10, 1, test_seeds[i % test_seeds.length] + TEST_SEED);
			for(int j = 0; j < NB_STEP_TEST; j++) {
				//alpha section
				double alphaDiv = 1 + j * STEP_ALPHA;
				double alpha = 1 - ((1 - INIT_ALPHA)/alphaDiv);
				double[] r = singleTest(sim, NB_EPISODE, alpha, DFT_GAMMA, DFT_EPSILON, DFT_REWARD_WH, DFT_REWARD_C, DFT_PENALTY);
				costResultAlpha[j] += r[0];
				timeResultAlpha[j] += r[1];
				
				//gamma section
				double gammaDiv = 1 + j * STEP_GAMMA;
				double gamma = 1 - ((1 - INIT_GAMMA)/gammaDiv);
				r = singleTest(sim, NB_EPISODE, DFT_ALPHA, gamma, DFT_EPSILON, DFT_REWARD_WH, DFT_REWARD_C, DFT_PENALTY);
				costResultGamma[j] += r[0];
				timeResultGamma[j] += r[1];

				//epsilon section
				double epsilonDiv = 1 + j * STEP_EPSILON;
				double epsilon = 1 - ((1 - INIT_EPSILON)/epsilonDiv);
				r = singleTest(sim, NB_EPISODE, DFT_ALPHA, DFT_GAMMA, epsilon, DFT_REWARD_WH, DFT_REWARD_C, DFT_PENALTY);
				costResultEpsilon[j] += r[0];
				timeResultEpsilon[j] += r[1];
				
				//print avancement
				info(getAvancementStringBar(initTime, i, j));
			}
			
			//initialCost += SolutionSolver.solutionCost(sim.convertListToListOfList());
		}
		
		for(int j = 0; j < NB_STEP_TEST; j++) {
			costResultAlpha[j] /= NB_TRY_PER_VALUE;
			timeResultAlpha[j] /= NB_TRY_PER_VALUE;
			costResultGamma[j] /= NB_TRY_PER_VALUE;
			timeResultGamma[j] /= NB_TRY_PER_VALUE;
			costResultEpsilon[j] /= NB_TRY_PER_VALUE;
			timeResultEpsilon[j] /= NB_TRY_PER_VALUE;
		}
		
		initialCost = initialCost / NB_TRY_PER_VALUE;
		ExcelFileWriterQLearning.printInFile(outputFileName +"_"+TEST_SEED, initialCost,
				costResultAlpha, costResultGamma, costResultEpsilon, costResultNbEpisode,
				timeResultAlpha, timeResultGamma, timeResultEpsilon, timeResultNbEpisode);
		
		info("analyze finished");
	}
	
	private static double[] singleTest(QLearning sim, int nb_episode, double alpha, 
			double gamma, double epsilon, int reward_wh, int reward_c, int penalty) {
			long startTime = System.nanoTime();
			List<List<MapPoint>> solution = sim.Solve(nb_episode, alpha, gamma, epsilon, reward_wh, reward_c, penalty);
			double stopTime = System.nanoTime();
			double cost = SolutionSolver.solutionCost(solution);
			//double newcost = cost /=nb_episode;
			double[] res = {cost, (stopTime - startTime) / 1000000}; 	

			return res;
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
}
/*package solutionSolver.qLearning;

import map.SimulationMap;

import java.util.ArrayList;
import java.util.List;

import map.MapPoint;
import solutionSolver.SolutionSolver;

public class QLearning extends SolutionSolver {
	
    int[][] R;
    double[][] Q;
    int reward = 1;
    int penalty = -1;
    double alpha = 0.1;
    double gamma = 0.9;
	
	public QLearning(SimulationMap map) {
		super(map);
	}

	@Override
	public List<List<MapPoint>> Solve() {
		List<List<MapPoint>> result;
		initializeQ();
		for (int i=0; i<nbTotalState(map);i++) {
			
		}
		//for pour chaque episode jusqua etat final
		// choisir un état au pif
			//for chaque etat d'episode
			//choisir action grace à la politique
			//calculateNewQ();
		return result;	
	}
	
	private double[][] initializeQ() {
        for (int i = 0; i < nbTotalState(map); i++){
            for(int j = 0; j < nbTotalState(map); j++){
                Q[i][j] = 0;
            }
        }
		return Q;
    }
	
    private boolean isFinalState(int state) {
        int i = state / height(map);
        int j = state - i * width(map);
        return false;
    }
    
    private List<List<Object>> possibleActionsFromState(int state) {
    	List<List<Object>> result = new ArrayList<List<Object>>();
        for (int i = 0; i < nbTotalState(map); i++) {
            if (R[state][i] != penalty) {
                result.add(i);
            }
        }
        return result;
    }
    
    private double maxQ(int nextState) {
        List<List<Object>> actionsFromState = possibleActionsFromState(nextState);
        double maxValue = -1;
        for (List<List<MapPoint>> nextAction : actionsFromState) {
            double value = Q[nextState][nextAction];
            if (value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }
    
    private double[][] calculateNewQ(int State, double[][]Q, int [][]R, int gamma, int alpha) {
    	int nextState=0; //a revoir
    	double maxQ = maxQ(nextState);
    	double q=Q[State][nextState];
    	int r=R[State][nextState];
    	double res;
    	res=q+alpha*(r+gamma*maxQ-q);
    	Q[State][nextState]=res;
    	return Q;
    }
    
    private int[][] calculateNewR() {
    	return R;
    }
    
	private int height(SimulationMap map) {
		return map.getHeight();
	}
	
	private int width(SimulationMap map) {
		return map.getWidth();
	}
	
	private int nbTotalState(SimulationMap map) {
		return map.getWidth()*map.getHeight();
	}

}*/
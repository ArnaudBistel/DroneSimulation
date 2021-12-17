package solutionSolver.qLearning;

import map.SimulationMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.xtext.xbase.scoping.batch.ITypeImporter.Client;

import map.MapPoint;
import map.MapPointType;
import solutionSolver.SolutionSolver;
import utils.graph.Vertex;
import utils.variables.SimulationParameters;

public class QLearning extends SolutionSolver {
	
	public QLearning(SimulationMap map) {
		super(map);
	}
	
	@Override
	public List<List<MapPoint>> Solve() {
		return Solve(5,0.9,0.9,1,100,-1);
	}
	
	public List<List<MapPoint>> Solve(int nb_episode, double alpha, double gamma, double epsilon, int reward, int penalty) {
		
		List<MapPoint> warehouse = new ArrayList<MapPoint>(map.getWarehouses());	
		List<List<Integer>> T = transitionMatrix();
		List<List<Double>> Q = initializeQ();
		List<List<Integer>> R = initializeR(T,reward,penalty);
		List<MapPoint> way_drone = new ArrayList<MapPoint>();
			
		for (int episode=0;episode<nb_episode;episode++) {
			
			way_drone.clear();
			List<MapPoint> clients_temp = new ArrayList<MapPoint>(map.getClients());;
			List<MapPoint> temp = new ArrayList<MapPoint>();
			
			int pos_list = random.nextInt(warehouse.size());
			MapPoint e = warehouse.get(pos_list);
			way_drone.add(e);
			temp.add(e);
			int index_etat = pos_list;
			
			while (clients_temp.size()!=0) {
				
				int index_action = getNextAction(index_etat,epsilon,Q);
				int old_etat = index_etat;
				e = getNextLocation(index_etat,index_action,T);
				int new_ind = T.get(index_etat).get(index_action);
				index_etat = new_ind;
				
				int rew = R.get(old_etat).get(index_action);
	
				double old_Q_value = Q.get(old_etat).get(index_action);
				double new_Q_value = old_Q_value + (alpha * (rew + gamma * getQmax(index_etat,Q) - old_Q_value));
				
				Q.get(old_etat).set(index_action, new_Q_value);
				
				if (e.getType()==MapPointType.CLIENT) {
					if (way_drone.contains(e)==false){
						temp.add(e);
						double cout_livraison = solutionCost(convertListToListOfList(temp),true);
						if  (cout_livraison >= 0){
							if (temp.size()<=5) {
								//System.out.println("le client "+ clients.indexOf(e) +" a été enlevé");
								clients_temp.remove(e);
								//System.out.println("il reste "+ clients.size()+" clients à enlever");
								way_drone.add(e);
								//System.out.println("cest validé "+temp);
							}
						} else {
							//System.out.println("cout de livraison trop élevé");
						}
					} else {
						//System.out.println("on a déjà livré le client");
					}
				} else {
					MapPoint lastaddpoint = way_drone.get(way_drone.size()-1);
					if (lastaddpoint.getType()==MapPointType.CLIENT) {
						//System.out.println("************WAREHOUSE***********");
						temp.clear();
						temp.add(e);
						way_drone.add(e);
					} else {
						//System.out.println("on est déjà dans une warehouse");
					}
				}
			}
		
			//System.out.println("way_drone de base: "+way_drone.size());
			for(int i = way_drone.size() - 1; i > 0 ; i -= 2) {
				double d = pathEnergyCost(Arrays.asList(way_drone.get(i-1), way_drone.get(i), way_drone.get(i-1)));
				if(d > SimulationParameters.DRONE_MAX_ENERGY) {
					way_drone.remove(i);
					way_drone.remove(i-1);
				}
			}
			//System.out.println("way_drone apres opti: "+way_drone.size());

		}
		List<List<MapPoint>> final_way = convertListToListOfList(way_drone);
		//System.out.println(final_way);
		return final_way;	
	}
	
	
	private List<List<Integer>> transitionMatrix(){
		int taille = map.getWarehouses().size() + map.getClients().size();
		List<List<Integer>> T = new ArrayList<>();
		for (int i=0; i<taille;i++) {
			List<Integer> listVoisin = new ArrayList<Integer>();
			List<Vertex> listVertex = map.getGraph().getVertex(i).getConnectedVertices();
			for (int j=0; j<listVertex.size();j++) {
				listVoisin.add(listVertex.get(j).getId());
			}
			T.add(listVoisin);
		}
		return T;
	}
	
	private List<List<Double>> initializeQ() {

		int taille = map.getWarehouses().size() + map.getClients().size();
		List<List<Double>> Q = new ArrayList<>();
        for (int i=0; i<taille;i++){
        	List<Double> listVoisin = new ArrayList<Double>();
        	List<Vertex> listVertex = map.getGraph().getVertex(i).getConnectedVertices();
            for(int j=0; j<listVertex.size();j++){
				listVoisin.add(0.0);
            }
            Q.add(listVoisin);
        }
		return Q;
    }
    
    private List<List<Integer>> initializeR(List<List<Integer>> T, int reward, int penalty) {
		int index_warehouse = map.getWarehouses().size() -1;
		List<List<Integer>> R = new ArrayList<>();
        for (List<Integer> listVoisin : T){
        	List<Integer> Liste = new ArrayList<Integer>();
            for(int element : listVoisin){
				if (element<=index_warehouse) {
					Liste.add(reward);
				} else {
					Liste.add(penalty);
				}
            }
            R.add(Liste);
        }
		return R;
    }
    
    private int getNextAction(int index_etat, double epsilon, List<List<Double>> Q) {
    	List<Double> get_list = Q.get(index_etat);
    	if (Math.random()<epsilon) {
    		return random.nextInt(get_list.size());
    	} else {
    		double temp = 0;
    		int result = 0;
    		for (int i=0;i<get_list.size();i++) {
    			double new_temp = Q.get(index_etat).get(i);
    			if (temp < new_temp) {
    				temp = new_temp;
    				result = i;
    			}
    		}
    		return result;
    	}
    }
    
    private MapPoint getNextLocation(int index_etat, int index_action, List<List<Integer>>T){
		List<MapPoint> clients = new ArrayList<MapPoint>(map.getClients());
		List<MapPoint> warehouse = new ArrayList<MapPoint>(map.getWarehouses());
		
    	int new_index_etat = T.get(index_etat).get(index_action);
    	
    	if (new_index_etat <= warehouse.size()-1) {
    		MapPoint e = warehouse.get(new_index_etat);
    		return e;
    	} else {
    		MapPoint e = clients.get(new_index_etat - warehouse.size());
    		return e;
    	}
    }
    
    private double getQmax(int index_etat, List<List<Double>> Q) {
    	double temp = 0;
    	List<Double> get_list = Q.get(index_etat);
    	for (int i=0;i<get_list.size();i++) {
    		double new_temp = Q.get(index_etat).get(i);
			if (temp < new_temp) {
				temp = new_temp;
			}
    	}
    	return temp;
    }
}
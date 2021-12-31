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
		return Solve(20,0.9,0.9,0.8,20,10,-5);
	}
	
	public List<List<MapPoint>> Solve(int nb_episode, double alpha, double gamma, double epsilon, int reward_wh, int reward_c, int penalty) {

		List<MapPoint> warehouse = new ArrayList<MapPoint>(map.getWarehouses());
		List<List<Integer>> T = initializeT();
		List<List<Integer>> T_temp = initializeT_negawarehouse();
		List<MapPoint> way_drone = new ArrayList<MapPoint>();
		List<MapPoint> voyage_drone = new ArrayList<MapPoint>();
		List<MapPoint> clients_opti = new ArrayList<>(optiListClient());
		
		List<List<Double>> Q_star = Learn(nb_episode,alpha, gamma, epsilon, reward_wh, reward_c, penalty);
		
		//System.out.println("********************** NEW SOLVE ********************");
		//System.out.println("Q_star "+Q_star);
		int pos_list = 0;
		MapPoint e = warehouse.get(pos_list);
		way_drone.add(e);
		voyage_drone.add(e);
		int index_etat = pos_list;
		
		while (clients_opti.size()!=0) {
			//System.out.println(clients_opti.size());
			//System.out.println(way_drone.size());
			//System.out.println(e);
			
			int index_action = getMaxAction(index_etat,Q_star,T_temp);
			e = getNextLocation(index_etat,index_action,T);	
			
			//System.out.println(Q_star);
			//System.out.println(e);
			
			way_drone.add(e);
			voyage_drone.add(e);
			clients_opti.remove(e);
			
			
			int new_index_etat = T.get(index_etat).get(index_action);
			if (e.getType()==MapPointType.CLIENT) {
				T_temp = updateT(T_temp,new_index_etat);
			} else {
				//double cout_livraison = pathEnergyCost(voyage_drone);
				//System.out.println("cout livraison "+cout_livraison+" "+voyage_drone.size()+" "+voyage_drone);
				voyage_drone.clear();
				voyage_drone.add(e);
			}
			
			
			//System.out.println(new_index_etat);
			//System.out.println(T_temp);
			//System.out.println(Q_star);
			
			index_etat = new_index_etat;
		}
		//System.out.println(way_drone.size());
		List<List<MapPoint>> final_way = convertListToListOfList(way_drone);
		//System.out.println(way_drone);
		//System.out.println(final_way);
		return final_way;
	}
	
	public List<List<Double>> Learn(int nb_episode, double alpha, double gamma, double epsilon, int reward_wh, int reward_c, int penalty) {

		List<MapPoint> warehouse = new ArrayList<MapPoint>(map.getWarehouses());
		List<List<Integer>> T = initializeT_negawarehouse();
		List<List<Integer>> T_positif = initializeT();
		List<List<Double>> Q = initializeQ();
		
		for (int episode=0;episode<nb_episode;episode++) {
			List<MapPoint> clients_opti = new ArrayList<>(optiListClient());
			List<List<Integer>> R = initializeR(clients_opti,T,reward_wh,reward_c,penalty);
			List<MapPoint> voyage_drone = new ArrayList<MapPoint>();
			List<List<Integer>> T_temp = initializeT_negawarehouse();
			
			int pos_list = 0;
			MapPoint e = warehouse.get(pos_list);
			voyage_drone.add(e);
			int index_etat = pos_list;
			
			//System.out.println("***** NEW EPISODE *****");
			//System.out.println(Q);
			
			while (clients_opti.size()!=0) {
				
				int index_action = getNextAction(index_etat,0.01,Q,T_temp);
				e = getNextLocation(index_etat,index_action,T);
				
				if (e.getType()==MapPointType.CLIENT) {
					voyage_drone.add(e);
					MapPoint f = getClosestWharehouse(e,map);
					voyage_drone.add(f);
					//double cout_chemin = solutionCost(convertListToListOfList(voyage_drone),true);
					double cout_chemin = pathEnergyCost(voyage_drone);
					
					if (cout_chemin <= SimulationParameters.DRONE_MAX_ENERGY) {
						clients_opti.remove(e);
						voyage_drone.remove(voyage_drone.size()-1);
					} else {
						e = getClosestWharehouse(e,map);
						int index_warehouse = 0;
						//Recup de index_action
						for (int j=0;j<warehouse.size();j++) {
							MapPoint g = warehouse.get(j);
							if (g==e) {
								index_warehouse = j-1;
							}
						}
						for (int k=0; k<warehouse.size();k++) {
							//System.out.println(T.get(index_etat).get(k));
							if (T_positif.get(index_etat).get(k)==index_warehouse) {
								index_action = k;
							}
						}
						voyage_drone.clear();
						voyage_drone.add(e);
					}
				} else {
					voyage_drone.clear();
					voyage_drone.add(e);
				}
				int new_index_etat = T_positif.get(index_etat).get(index_action);
				//System.out.println("new index "+new_index_etat+" est un "+e.getType()+" "+e);
				int rew = R.get(index_etat).get(index_action);
				
				if (e.getType()==MapPointType.CLIENT) {
					T_temp = updateT(T_temp,new_index_etat);
					//System.out.println("T_temp update "+T_temp);
				}
				//System.out.println("verif T "+T_temp);
				//System.out.println("verif Q "+Q);

				double old_Q_value = Q.get(index_etat).get(index_action);
				double new_Q_value = old_Q_value + alpha * (rew + (gamma * getQmax(new_index_etat,Q)) - old_Q_value);
				Q.get(index_etat).set(index_action, new_Q_value);
				index_etat = new_index_etat;
			}
		}
		return Q;
	}
	
	public List<MapPoint> optiListClient() {
		
		List<MapPoint> res = new ArrayList<MapPoint>();
		List<MapPoint> clients = new ArrayList<MapPoint>(map.getClients());
		int size = map.getClients().size();
		
		res.add(getClosestWharehouse(clients.get(0), map));
		res.add(clients.remove(0));
		
		for(int i = 1; i < size; i++) {	
			int iCC = i;
			double dCC = Double.MAX_VALUE;
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
		for(int i = res.size() - 1; i >= 0 ; i -= 2) {
			double d = pathEnergyCost(Arrays.asList( res.get(i-1), res.get(i), res.get(i-1)));
			if(d > SimulationParameters.DRONE_MAX_ENERGY) {
				res.remove(i);
				res.remove(i-1);
			}
		}
		for (int i=0;i<res.size();i++) {
			MapPoint e=res.get(i);
			if (e.getType()==MapPointType.WAREHOUSE) {
				res.remove(e);
			}
		}
		return res;
	}
	
	private List<List<Integer>> initializeT() {
		int nb_list = map.getWarehouses().size() + map.getClients().size();
		List<List<Integer>> T = new ArrayList<>();
		for (int i=0; i<nb_list;i++) {
			List<Integer> listVoisin = new ArrayList<Integer>();
			List<Vertex> listVertex = map.getGraph().getVertex(i).getConnectedVertices();
			for (int j=0; j<listVertex.size();j++) {
				listVoisin.add(listVertex.get(j).getId());
			}
			T.add(listVoisin);
		}
		return T;
	}
	
	private List<List<Integer>> initializeT_negawarehouse() {
		int nb_list = map.getWarehouses().size() + map.getClients().size();
		int nb_warehouse = map.getWarehouses().size();
		
		List<List<Integer>> T = new ArrayList<>();
		
		for (int i=0; i<nb_warehouse;i++) {
			List<Integer> listVoisin = new ArrayList<Integer>();
			List<Vertex> listVertex = map.getGraph().getVertex(i).getConnectedVertices();
			for (int j=0; j<listVertex.size();j++) {
				if (listVertex.get(j).getId()<nb_warehouse) {
					listVoisin.add(-1);
				} else {
					listVoisin.add(listVertex.get(j).getId());
				}
				
			}
			T.add(listVoisin);
		}
		
		for (int i=nb_warehouse; i<nb_list;i++) {
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
    
    private List<List<Integer>> initializeR(List<MapPoint> clients_opti, List<List<Integer>> T, int reward_warehouse, int reward_client, int penalty) {
    	int index_warehouse = map.getWarehouses().size()-1;
    	List<MapPoint> clients_total =  map.getClients();
    	List<Integer> num_clients_eviter = new ArrayList<Integer>();
    	for (int i=0;i<clients_total.size();i++) {
    		MapPoint e = clients_total.get(i);
    		if (clients_opti.contains(e)==false) {
    			num_clients_eviter.add(i+map.getWarehouses().size());
    		}
    	}
		List<List<Integer>> R = new ArrayList<>();
        for (List<Integer> listVoisin : T){
        	List<Integer> Liste = new ArrayList<Integer>();
            for(int element : listVoisin){
				if (element<=index_warehouse) {
					Liste.add(reward_warehouse);
				} else {
					//System.out.println(element);
					if (num_clients_eviter.contains(element)==true) {
						Liste.add(penalty);
					} else {
						Liste.add(reward_client);				
					}
				}
            }
            R.add(Liste);
        }
		return R;
    }
    
    private List<List<Integer>> updateT(List<List<Integer>> T_temp, int index_etat) {
    	int start_count = map.getWarehouses().size();
    	for (int j=0;j<start_count;j++) {
    		List<Integer> list = T_temp.get(j);
    		for (int i=0;i<list.size();i++) {
    			int e = list.get(i);
    			if (e==index_etat) {
    				list.set(i, -1);
    			}
    		}
    	}
    	
    	for (int j=start_count;j<T_temp.size();j++) {
    		List<Integer> list = T_temp.get(j);
    		for (int i=start_count;i<list.size();i++) {
    			int e = list.get(i);
    			if (e==index_etat) {
    				list.set(i, -1);
    			}
    		}
    	}
    	return T_temp;
    }
    
    private int getNextAction(int index_etat, double epsilon, List<List<Double>> Q, List<List<Integer>> T_temp) {

    	List<Integer> get_possible = T_temp.get(index_etat);
    	//System.out.println(epsilon + " "+ index_etat +" "+ get_possible);
    	List<Double> get_list = Q.get(index_etat);

    	double nb_random = Math.random();
    	if (nb_random < epsilon) {
    		int action_random = random.nextInt(get_list.size());
    		while (T_temp.get(index_etat).get(action_random) < 0) {
    			action_random = random.nextInt(get_list.size());
    		}
    		return action_random;
    		
    	} else {
    		double temp = -1;
    		int result = 0;
    		for (int i=0;i<get_list.size();i++) {
    			//System.out.println(new_temp);
    			if (get_possible.get(i)>=0) {
    				double new_temp = Q.get(index_etat).get(i);
    				if (temp < new_temp) {
    					temp = new_temp;
    					result = i;
    				}
    			}
    		}
    		return result;
    	}
    }
    
    private int getMaxAction(int index_etat, List<List<Double>> Q, List<List<Integer>> T_temp) {
    	List<Double> get_list = Q.get(index_etat);
    	List<Integer> get_possible = T_temp.get(index_etat);
    	//System.out.println(get_possible);
    	for (int k=0;k<get_possible.size();k++) {
    		if (get_possible.get(k)<0) {
    			
    			//Q.get(index_etat).set(k, -10000000000.0);
    		}
    	}
		double tempo = -10000000.0;
		int result = 0;
		for (int i=0;i<get_list.size();i++) {
			if (get_possible.get(i)>=0) {
				double new_temp = Q.get(index_etat).get(i);
				if (tempo < new_temp) {
					tempo = new_temp;
					result = i;
				}
			}
			//System.out.println(new_temp);

		}
		return result;
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
    	double temp = Double.MIN_VALUE;
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
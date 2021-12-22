package solutionSolver;

import map.MapPoint;
import map.MapPointType;
import map.SimulationMap;
import utils.variables.SimulationParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.impl.AvalonLogger;

public abstract class SolutionSolver {
	
	protected SimulationMap map;
	protected Random random;

	protected static int nbWeighE;
	protected static int nbCapacityE;
	protected static int nbEnergyE;

	public SolutionSolver(SimulationMap map, Random random) {
		this.map = map;
		this.random = random;
	}
	
	public SolutionSolver(SimulationMap map) {
		this.map = map;
		this.random = new Random();
	}
	
	abstract public List<List<MapPoint>> Solve();
	
	public static double droneEnergyCost(double kg, double km) {
		double droneBattery = 814*2;
		// linear function for 1 to 5 kg and for 0 to 1 kg
		double c1 = 95; // Drone's consumption for 1 kg for 1 km
		double consumptionPerKm;
		if (kg < 1) {
			double c0 = 110; // Drone's consumption for 0 kg for 1 km
			consumptionPerKm = droneBattery/lerp(c0, c1, kg);
		} else { // we assume that the weight is between 0 and 5
			double c5 = 75; // Drone's consumption for 5 kg for 1 km
			consumptionPerKm = droneBattery/lerp(c1, c5, (kg-1.0)/4.0);
		}
		return consumptionPerKm * km;
	}
	
	public static double lerp(double a, double b, double alpha) {
		return a + alpha * (b - a);
	}
	
	public static double pathEnergyCost(List<MapPoint> deliveryOrder) {
		// Calculate the cost of a path depending on packages and distances
		// Example of path : w0 -> c1 -> c3 -> w0 (first and last point aren't necessary the same)
		double energyCost = 0;
		float totalWeight = 0;
		
		// Get total package weight
		for (int i = 1; i < deliveryOrder.size() - 1 ; i++) {
			totalWeight += deliveryOrder.get(i).getPackageWeight();
		}
		
		for (int i = 0; i < deliveryOrder.size() - 1 ; i++) {
			// Calculate energy between the 2 points
			double distance = getDistanceBetweenTwoPoints(deliveryOrder.get(i), deliveryOrder.get(i+1));
			energyCost += droneEnergyCost(totalWeight, distance);
			// Reduce total package weight
			totalWeight = totalWeight - deliveryOrder.get(i+1).getPackageWeight();
		}
		return energyCost;
	}
	

	public static double solutionCost(List<List<MapPoint>> s) {
		return solutionCost(s,false);
	}
	
	public static double solutionCost(List<List<MapPoint>> s, boolean blockBadValue) {
		//calculate the cost of a deliveries solution
		//if blockBadValue is true, return -1 when the solution isn't viable

		//TODO : replace with global constants
		int nbMaxPackages = 3;
		double maxWeight = 5;
		double maxEnergy = SimulationParameters.DRONE_MAX_ENERGY;
		double coef = 1;
		
		double cost = 0;
		for(List<MapPoint> deliveryOrder : s) {
			if(deliveryOrder.size() > 2) {
				//assuming that delivery order starts and ends with warehouse
				
				double currentCost = pathEnergyCost(deliveryOrder);
				
				//test section
				if(blockBadValue) {					
					if(currentCost > maxEnergy) {
						//max energy cost reached
						nbEnergyE++;
						coef = -1;
					}
					
					if(deliveryOrder.size() - 2 > nbMaxPackages) {
						// max number of packages reached
						nbCapacityE++;
						coef = -1;
					}
					
					double currentWeight = 0;
					for(MapPoint e : deliveryOrder)
						currentWeight += e.getPackageWeight();
					
					if(currentWeight > maxWeight) {
						//max weight reached
						nbWeighE++;
						coef = -1;
					}
				}
				
				
				cost += currentCost;
			}
		}
		return cost * coef;
	}
	

	public static double solutionCostSimpleList(List<MapPoint> solution){
		return solutionCostSimpleList(solution,false);
	}
	
	public static double solutionCostSimpleList(List<MapPoint> solution, boolean blockBadValue){
		//use a simple list like 0,1,2,0,1,2,3,0,1 where 0 is a warehouse
		//convert it like : {{0,1,2,0},{0,1,2,3,0},{0,1,0}}
		//if blockBadValue is true, return -1 when the solution isn't viable
		
		//TODO : replace with global constants
		int nbMaxPackages = 3;
		double maxWeight = 5;
		double maxEnergy = SimulationParameters.DRONE_MAX_ENERGY;
		
		double cost = 0;
		double currentWeight = 0;
		List<MapPoint> currentList = new ArrayList<>();
		currentList.add(solution.get(0));
		for(int i = 1; i < solution.size(); i++) {
			
			MapPoint p = solution.get(i);
			
			if(p.getType() == MapPointType.WAREHOUSE && (currentList.size() == 0)) {
				//if p is warehouse and close list
				
				currentList.add(currentList.get(0)); //return to origin warehouse 
				double currentCost = pathEnergyCost(currentList);
				
				//test section
				if(blockBadValue) {					
					if(currentCost > maxEnergy) //max energy cost reached
						return -1;
					if(currentList.size() - 2 > nbMaxPackages) // max number of packages reached
						return -1;
					if(currentWeight > maxWeight) //max weight reached
						return -1;
				}
				
				currentWeight = 0;
				cost += currentCost;
				currentList.clear();
			} else {
				currentList.add(p);
				currentWeight += p.getPackageWeight();
			}
		}
		
		return cost;
	}
	
	public static double solutionPriceCost(List<List<MapPoint>> solution, int NbDrones) {
		double whCost = solutionCost(solution);
		return whCost * SimulationParameters.COUT_WH;
	}
	
	public static float solutionTimeCost (List<List<MapPoint>> s, int nDrone) {
		float availableTime[] = new float [nDrone];
		for (int i = 0 ; i < nDrone; i++) {
			availableTime[i] = 0;
		}
		for (List<MapPoint> route : s) {
			int minIndex = nDrone;
			float minVal = -1;
			for (int i = 0 ; i < nDrone; i++) {
				if ((availableTime[i] < minVal) || minVal == -1) {
					minIndex = i;
					minVal = availableTime[i];
				}
			}
			availableTime[minIndex] += routeTimeCost(route);
					
		}
		float last_arrived = 0;
		for (int i = 0 ; i < nDrone; i++) {
			last_arrived = Math.max(last_arrived, availableTime[i]);
		}
		return last_arrived;
		
	}
	
	public static float routeTimeCost (List<MapPoint> r) {
		float totalDist = 0;
		for(int i = 0; i < r.size()-1; i++) {
			totalDist += Math.sqrt(Math.pow(r.get(i).getPixelX() - r.get(i+1).getPixelX(), 2) + 
					Math.pow(r.get(i).getPixelY() - r.get(i+1).getPixelY(), 2));
		}
		return totalDist * 1000 / (15 * 28);
	}
	
	public static float stepNumberToRealTime(int steps) {
		return 200 * steps / 28;
	}
	
	public List<List<MapPoint>> convertListToListOfList(List<MapPoint> solution){
		//convert a simple list like 0,1,2,0,1,2,3,0,1 where 0 is a warehouse
		//to a list of list like : {{0,1,2,0},{0,1,2,3,0},{0,1,0}}
		
		return convertListToListOfList(solution,false);
	}
	
	public List<List<MapPoint>> convertListToListOfList(List<MapPoint> solution, boolean endToClosestWarehouse){
		//convert a simple list like 0,1,2,0,1,2,3,0,1 where 0 is a warehouse
		//to a list of list like : {{0,1,2,0},{0,1,2,3,0},{0,1,0}}
		//ending each list by the nearest warehouse or not
		
		List<List<MapPoint>> res = new ArrayList<>();
		int currentListId = 0;
		res.add(new ArrayList<>());

		for(MapPoint p : solution) {
			if(p.getType() == MapPointType.WAREHOUSE && (res.get(currentListId).size() != 0)) {
				//if p is warehouse and close list
				
				if(endToClosestWarehouse) {	
					res.get(currentListId).add(getClosestWharehouse(p, map));
				} else {					
					res.get(currentListId).add(res.get(currentListId).get(0)); //return to origin warehouse 
				}
				currentListId++;
				res.add(new ArrayList<>());
			}
			res.get(currentListId).add(p);
			
		}
		
		return res;
	}
	
	public static MapPoint getClosestWharehouse(MapPoint p, SimulationMap map) {
	    // Return the closest warehouse to the MapPoint p
		List<MapPoint> warehouses = map.getWarehouses();
		double minimalDistance = -1;
		MapPoint closestWarehouse = null;
		for (MapPoint warehouse : warehouses) {
			// Calculate the distance
			double distance = getDistanceBetweenTwoPoints(warehouse, p);
			if (minimalDistance > distance || minimalDistance == -1) {
				// New minimal distance
				minimalDistance = distance;
				closestWarehouse = warehouse;
			}
		}
	    return closestWarehouse;
	}
	
	public static double getDistanceBetweenTwoPoints(MapPoint a, MapPoint b) {
		return Math.sqrt(Math.pow((a.getScaledX() - b.getScaledX()),2) + Math.pow((a.getScaledY() - b.getScaledY()),2));
	}
	
	public static boolean isValidPath(List<MapPoint> deliveryOrder) {
		return pathEnergyCost(deliveryOrder) <= SimulationParameters.DRONE_MAX_ENERGY;
	}
}
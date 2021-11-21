package solutionSolver;

import map.MapPoint;
import map.SimulationMap;
import java.util.List;

public abstract class SolutionSolver {
	
	protected FakeSimulationMap map;
	
	public SolutionSolver(FakeSimulationMap map) {
		this.map = map;
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
		int energyCost = 0;
		int totalWeight = 0;
		
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
		//calculate the cost of a deliveries solution
		
		double cost = 0;
		for(List<MapPoint> deliveryOrder : s) {
			if(deliveryOrder.size() > 2) {
				//assuming there are atleast 3 point : wharehouse -> client -> wharehouse
				
				cost += pathEnergyCost(deliveryOrder);
			}
		}
		return cost;
	}
	
	public static MapPoint getClosestWharehouse(MapPoint p, FakeSimulationMap map) {
	    // Return the closest warehouse to the MapPoint p
		List<MapPoint> warehouses = map.getWareHouses();
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
		return Math.sqrt(Math.pow((a.getX() - b.getX()),2) + Math.pow((a.getY() - b.getY()),2));
	}
}
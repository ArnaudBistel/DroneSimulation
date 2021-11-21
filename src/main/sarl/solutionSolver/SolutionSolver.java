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
	
	public double droneEnergyCost(double kg, double km) {
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
	
	public double lerp(double a, double b, double alpha) {
		return a + alpha * (b - a);
	}
}
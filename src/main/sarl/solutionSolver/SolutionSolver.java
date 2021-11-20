package solutionSolver;

import map.MapPoint;
import map.SimulationMap;
import java.util.List;

public abstract class SolutionSolver {
	
	protected SimulationMap map;
	
	public SolutionSolver(SimulationMap map) {
		this.map = map;
	}
	
	abstract public List<List<MapPoint>> Solve();
}

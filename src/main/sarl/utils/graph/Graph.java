package utils.graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import map.MapPoint;
import map.MapPointType;
import map.SimulationMap;
import solutionSolver.SolutionSolver;

public class Graph {

	Map<Integer,Vertex> map;
	SimulationMap simMap;
	
	public Graph(SimulationMap simMap) {
		this.map = new HashMap<Integer, Vertex>();
		this.simMap = simMap;
	}
	
	public void addVertex(int id, MapPoint point) {
		Vertex newVertex = new Vertex(id, point.getPosition(), point);
		for (Vertex v : map.values()) {
			boolean costCondition, weightCondition;
			weightCondition = v.getPackageWeight() + newVertex.getPackageWeight() <= 5;
			costCondition = processCostCondition(point, v.getPoint());
			if(weightCondition && costCondition) {
				Edge newEdge = new Edge(v, newVertex, (int)newVertex.getPosition().getDistance(v.getPosition()));
				newVertex.addEdge(newEdge);
				v.addEdge(newEdge);
			}
		}
		this.map.put(id, newVertex);
	}
	
	public boolean processCostCondition(MapPoint newPoint, MapPoint otherPoint) {
		boolean ret = false;
		if(otherPoint.getType() == MapPointType.WAREHOUSE) {
			ret = SolutionSolver.isValidPath(Arrays.asList(newPoint,otherPoint));
		}
		else {
			ret = SolutionSolver.isValidPath(Arrays.asList(SolutionSolver.getClosestWharehouse(newPoint, simMap), newPoint, otherPoint, SolutionSolver.getClosestWharehouse(otherPoint, simMap))) || 
					SolutionSolver.isValidPath(Arrays.asList(SolutionSolver.getClosestWharehouse(otherPoint, simMap), otherPoint, newPoint, SolutionSolver.getClosestWharehouse(newPoint, simMap)));
		}
		
		return ret;
	}
	
	public Vertex getVertex(int id) {
		return this.map.get(id);
	}
}

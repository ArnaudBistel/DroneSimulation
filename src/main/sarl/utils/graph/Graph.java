package utils.graph;

import java.util.HashMap;
import java.util.Map;

import map.MapPoint;

public class Graph {

	Map<Integer,Vertex> map;
	
	public Graph() {
		this.map = new HashMap<Integer, Vertex>();
	}
	
	public void addVertex(int id, MapPoint point) {
		Vertex newVertex = new Vertex(id, point.getPosition(), point);
		for (Vertex v : map.values()) {
			if(v.getPackageWeight() + newVertex.getPackageWeight() <= 5) {
				Edge newEdge = new Edge(v, newVertex, (int)newVertex.getPosition().getDistance(v.getPosition()));
				newVertex.addEdge(newEdge);
				v.addEdge(newEdge);
			}
		}
		this.map.put(id, newVertex);
	}
	
	public Vertex getVertex(int id) {
		return this.map.get(id);
	}
}

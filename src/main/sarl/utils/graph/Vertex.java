package utils.graph;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.geometry.d2.i.Point2i;

import map.MapPoint;
import map.MapPointType;

public class Vertex {

	int id;
	Point2i position;
	MapPoint point;
	List<Edge> edges;
	
	
	public Vertex(int id, int x, int y, MapPoint point) {
		this(id, new Point2i(x,y), point);
	}
	
	public Vertex(int id, Point2i position, MapPoint point) {
		this.id = id;
		this.position = position;
		this.point = point;
		this.edges = new ArrayList<Edge>();
	}
	
	public void addEdge(Edge edge) {
		this.edges.add(edge);
	}
	
	public int getId() {
		return this.id;
	}
	
	public MapPoint getPoint() {
		return this.point;
	}
	
	public Point2i getPosition() {
		return this.position;
	}
	
	public float getPackageWeight () {
		return this.point.getPackageWeight();
	}
	
	public List<Vertex> getConnectedVertices(){
		List<Vertex> ret = new ArrayList<Vertex>();
		
		for(Edge e : edges) {
			ret.add((e.getV1() == this ? e.getV2() : e.getV1()));
		}
		
		return ret;
	}
	
	//Returns the Edge connecting this Vertex to the Vertex with specified id, null if the two vertices aren't connected
	public Edge getEdge(int id) {
		Edge ret = null;
		for(Edge e : this.edges) {
			if(e.getV1().getId() == id || e.getV2().getId() == id) {
				ret = e;
			}
		}
		return ret;
	}
}

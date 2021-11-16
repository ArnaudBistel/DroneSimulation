package utils.graph;

public class Edge {

	int weight;
	Vertex V1,V2;
	
	public Edge(Vertex V1, Vertex V2, int weight) {
		this.V1 = V1;
		this.V2 = V2;
		this.weight = weight;
	}
	
	public Vertex getV1() {
		return this.V1;
	}
	
	public Vertex getV2() {
		return this.V2;
	}
	
}

package map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import utils.graph.Graph;
import utils.graph.Vertex;

public class SimulationMap {

	int nbClients, nbWarehouse, width, height;
	List<MapPoint> clientList, warehouseList;
	Random random;
	
	Graph graph;
	
	public SimulationMap(int nbClients, int nbWarehouse) {
		this(nbClients, nbWarehouse, 1020, 791, 0);
	}
	
	public SimulationMap(int nbClients, int nbWarehouse, int seed) {
		this(nbClients, nbWarehouse, 1020, 791, seed);
	}
	
	public SimulationMap(int nbClients, int nbWarehouse, int width, int height) {
		this(nbClients, nbWarehouse, width, height, 0);
	}
	
	public SimulationMap(int nbClients, int nbWarehouse, int width, int height, int seed) {
		this.nbClients = nbClients;
		this.nbWarehouse = nbWarehouse;
		this.width = width;
		this.height = height;
		this.clientList = new ArrayList<MapPoint>();
		this.warehouseList = new ArrayList<MapPoint>();
		if(seed == 0) {
			random = new Random();
		} else {
			random = new Random(seed);
		}
		generateMap();
	}
	
	public void generateMap() {
		int x, y, packageWeight;
		for(int i = 0;i<this.nbWarehouse;i++) {
			x = random.nextInt(this.width);
			y = random.nextInt(this.height);
			this.warehouseList.add(new MapPoint(x, y, MapPointType.WAREHOUSE, null));
			//System.out.println("Warehouse #" + i + " : " + x + "-" + y);
		}
		for(int i = 0;i<this.nbClients;i++) {
			x = random.nextInt(this.width);
			y = random.nextInt(this.height);
			packageWeight = random.nextInt(4) + 1;
			this.clientList.add(new MapPoint(x, y, MapPointType.CLIENT, Arrays.asList(packageWeight)));
			//System.out.println("Client #" + i + " : " + x + "-" + y + " --- Package Weight : " + packageWeight);
		}
		generateGraph();
	}
	
	public void generateGraph() {
		graph = new Graph(this);
		for(int i = 0; i < this.warehouseList.size(); i++) {
			graph.addVertex(i, this.warehouseList.get(i));
		}
		for(int i = 0; i < this.clientList.size(); i++) {
			graph.addVertex(i + this.warehouseList.size(), this.clientList.get(i));
		}
		for (int i = 0; i<this.nbClients+this.nbWarehouse;i++) {
			List<Vertex> list = graph.getVertex(i).getConnectedVertices();
			String str = "";
			for(Vertex v : list) {
				str += v.getId() + " ";
			}
			//System.out.println("Vertex #" + i + " : " + str);
		}
	}
	
	public Graph getGraph() {
		return this.graph;
	}
	
	public List<MapPoint> getClients(){
		return this.clientList;
	}
	
	public List<MapPoint> getWarehouses(){
		return this.warehouseList;
	}
}

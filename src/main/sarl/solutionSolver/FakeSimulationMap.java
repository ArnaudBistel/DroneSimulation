package solutionSolver;

import map.MapPoint;
import map.MapPointType;
import map.SimulationMap;
import utils.graph.Graph;
import utils.graph.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FakeSimulationMap extends SimulationMap {

	int nbClients, nbWarehouse, width, height;
	List<MapPoint> clientList, warehouseList;
	Graph graph;
	
	public FakeSimulationMap(int nbClients, int nbWarehouse) {
		super(nbClients, nbWarehouse);
		this.nbClients = nbClients;
		this.nbWarehouse = nbWarehouse;
		this.width = 50;
		this.height = 50;
		clientList = new ArrayList<MapPoint>();
		warehouseList = new ArrayList<MapPoint>();
		fakeGenerateMap();
	}
	
	public FakeSimulationMap(int nbClients, int nbWarehouse, int width, int height) {
		super(nbClients, nbWarehouse);
		this.nbClients = nbClients;
		this.nbWarehouse = nbWarehouse;
		this.width = width;
		this.height = height;
		clientList = new ArrayList<MapPoint>();
		warehouseList = new ArrayList<MapPoint>();
		fakeGenerateMap();
	}
	
	public Graph getGraph() {
		return graph;
	}

	public List<MapPoint> getClients(){
		return this.clientList;
	}
	
	public List<MapPoint> getWareHouses(){
		return this.warehouseList;
	}
	
	public void fakeGenerateMap() {
		System.out.println("\n\n\n\n===========FakeGeneration===========\n\n");
		Random random = new Random();
		int x, y, packageWeight;
		for(int i = 0;i<this.nbWarehouse;i++) {
			x = random.nextInt(this.width);
			y = random.nextInt(this.height);
			this.warehouseList.add(new MapPoint(x, y, MapPointType.WAREHOUSE, null));
			System.out.println("Warehouse #" + i + " : " + x + "-" + y);
			/* Add a second warehouse for testing
			int x2 = random.nextInt(this.width);
			int y2 = random.nextInt(this.height);
			this.warehouseList.add(new MapPoint(x2, y2, MapPointType.WAREHOUSE, null));
			System.out.println("Warehouse #" + (i+1) + " : " + x2 + "-" + y2); */
		}
		for(int i = 0;i<this.nbClients;i++) {
			x = random.nextInt(this.width);
			y = random.nextInt(this.height);
			packageWeight = random.nextInt(4) + 1;
			this.clientList.add(new MapPoint(x, y, MapPointType.CLIENT, Arrays.asList(packageWeight)));
			System.out.println("Client #" + i + " : " + x + "-" + y + " --- Package Weight : " + packageWeight);
			/* Testing getClosestWharehouse
			MapPoint closestWarehouse = SolutionSolver.getClosestWharehouse(getClients().get(i), this);
			System.out.println("Closest Warehouse : " + closestWarehouse.getX() + "-" + closestWarehouse.getY()); */
		}
		fgenerateGraph();
	}
	
	public void fgenerateGraph() {
		graph = new Graph();
		graph.addVertex(0, this.warehouseList.get(0));
		for(int i = 0; i < this.clientList.size(); i++) {
			graph.addVertex(i+1, this.clientList.get(i));
		}
		for (int i = 0; i<this.nbClients+this.nbWarehouse;i++) {
			List<Vertex> list = graph.getVertex(i).getConnectedVertices();
			String str = "";
			for(Vertex v : list) {
				str += v.getId() + " ";
			}
			System.out.println("Vertex #" + i + " : " + str);
		}
	}

}

package solutionSolver;

import map.MapPoint;
import map.MapPointType;
import map.SimulationMap;
import utils.graph.Graph;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FakeSimulationMap extends SimulationMap {

	int nbClients, nbWarehouse, width, height;
	List<MapPoint> clientList, warehouseList;
	
	public FakeSimulationMap(int nbClients, int nbWarehouse) {
		super(nbClients, nbWarehouse);
		fakeGenerateMap();
	}
	
	public Graph getGraph() {
		return new Graph();
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
		}
		for(int i = 0;i<this.nbClients;i++) {
			x = random.nextInt(this.width);
			y = random.nextInt(this.height);
			packageWeight = random.nextInt(4) + 1;
			this.clientList.add(new MapPoint(x, y, MapPointType.CLIENT, Arrays.asList(packageWeight)));
			System.out.println("Client #" + i + " : " + x + "-" + y + " --- Package Weight : " + packageWeight);
		}
		generateGraph();
	}

}

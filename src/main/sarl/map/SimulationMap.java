package map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import solutionSolver.SolutionSolver;
import utils.graph.Graph;
import utils.graph.Vertex;
import utils.variables.SimulationParameters;

public class SimulationMap {

	int nbClients, nbWarehouse, width, height;
	List<MapPoint> clientList, warehouseList;
	Random random;
	
	Graph graph;
	
	public SimulationMap(int nbClients, int nbWarehouse) {
		this(nbClients, nbWarehouse, SimulationParameters.mapWidth, SimulationParameters.mapHeight, 0);
	}
	
	public SimulationMap(int nbClients, int nbWarehouse, int seed) {
		this(nbClients, nbWarehouse, SimulationParameters.mapWidth, SimulationParameters.mapHeight, seed);
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
		int x, y;
		x=0;
		y=0;
		float packageWeight = 0;
		for(int i = 0;i<this.nbWarehouse;i++) {
			x = random.nextInt(this.width);
			y = random.nextInt(this.height);
			this.warehouseList.add(new MapPoint(x, y, MapPointType.WAREHOUSE, null));
			//System.out.println("Warehouse #" + i + " : " + x + "-" + y);
		}
		for(int i = 0;i<this.nbClients;i++) {
			MapPoint tmp_client = null;
			NumberFormat formatter = NumberFormat.getInstance(Locale.US);
			formatter.setMaximumFractionDigits(1);
			formatter.setMinimumFractionDigits(1);
			formatter.setRoundingMode(RoundingMode.HALF_UP);
			boolean cond = false;
			while(!cond) 
			{
				x = random.nextInt(this.width);
				y = random.nextInt(this.height);
				packageWeight = random.nextFloat() * 5 ;
				packageWeight = new Float(formatter.format(packageWeight));
				tmp_client = new MapPoint(x, y, MapPointType.CLIENT, Arrays.asList(packageWeight));
				MapPoint closest_warehouse = SolutionSolver.getClosestWharehouse(tmp_client, this);
				cond = SolutionSolver.isValidPath(Arrays.asList(closest_warehouse, tmp_client, closest_warehouse));
			}
			
			this.clientList.add(tmp_client);
			// System.out.println("Client #" + i + " : " + x + "-" + y + " --- Package Weight : " + packageWeight);
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
	
	public void exportData(String filename) throws IOException {
		System.out.println("Filename export : " + filename);
		CSVWriter writer = new CSVWriter(new FileWriter(filename));
		List<String[]> data = new ArrayList<String[]>();
		data.add(new String[] {String.valueOf(this.nbClients)});
		
		data.add(new String[] {String.valueOf(this.nbWarehouse)});
		for (MapPoint warehouse : this.warehouseList) {
			data.add(new String[] {String.valueOf(warehouse.getPixelX()), String.valueOf(warehouse.getPixelY())});
		}
		for (MapPoint client : this.clientList) {
			data.add(new String[] {String.valueOf(client.getPixelX()), String.valueOf(client.getPixelY())});
		}
		for (MapPoint client : this.clientList) {
			data.add(new String[] {String.valueOf(client.getPackageWeight())});
		}
		writer.writeAll(data);
		writer.flush();
		
	}
	
	public void importData (String filename) throws Exception {
		CSVReader reader = new CSVReader(new FileReader(filename));
	    List<String[]> list = reader.readAll();
	    this.nbClients = Integer.valueOf(list.get(0)[0]);
	    this.nbWarehouse = Integer.valueOf(list.get(1)[0]);
	    this.warehouseList.clear();
	    this.clientList.clear();
	    
	    int x, y;
	    for(int i = 2; i<this.nbWarehouse + 2; i++) {
			x = Integer.valueOf(list.get(i)[0]);
			y = Integer.valueOf(list.get(i)[1]);
			this.warehouseList.add(new MapPoint(x, y, MapPointType.WAREHOUSE, null));
			//System.out.println("Warehouse #" + i + " : " + x + "-" + y);
		}
		for(int i = this.nbWarehouse + 2;i<this.nbWarehouse + this.nbClients + 2;i++) {
			x = Integer.valueOf(list.get(i)[0]);
			y = Integer.valueOf(list.get(i)[1]);
			float packageWeight = Float.valueOf(list.get(i + this.nbClients)[0]);
			MapPoint tmp_client = new MapPoint(x, y, MapPointType.CLIENT, Arrays.asList(packageWeight));
			this.clientList.add(tmp_client);
		}
		generateGraph();
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
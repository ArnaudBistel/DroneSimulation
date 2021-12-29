package map;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.geometry.d2.i.Point2i;

import utils.variables.SimulationParameters;

public class MapPoint {
	
	MapPointType type;
	Point2i position;
	List<Object> params;
	
	public MapPoint(int x, int y, MapPointType type, List<Object> params ) {
		this.position = new Point2i(x,y);
		this.type = type;
		this.params = new ArrayList<Object>();
		if(params != null) {
			this.params.addAll(params);
		}
	}
	
	public MapPointType getType() {
		return this.type;
	}
	
	public Point2i getPosition() {
		return this.position;
	}
	
	public int getPixelX() {
		return (int) this.position.getX();
	}
	
	public int getPixelY() {
		return (int) this.position.getY();
	}
	
	public double getScaledX() {
		return this.position.getX() / SimulationParameters.mapRatio;
	}
	
	public double getScaledY() {
		return this.position.getY() / SimulationParameters.mapRatio;
	}
	
	// TODO : package weight function 
	public float getPackageWeight() {
		return (this.type == MapPointType.CLIENT ? (float) params.get(0) : 0);
	}
	
}

package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import map.MapPoint;
import javafx.scene.canvas.GraphicsContext;

public class Client extends Circle{
	
	private boolean delivered = false;
	private MapPoint mapPoint;
	
	
	public Client (MapPoint mapPoint) {
		super();
		this.mapPoint = mapPoint;
		this.setCenterX(this.mapPoint.getPosition().getX());
		this.setCenterY(this.mapPoint.getPosition().getY());
		this.setRadius(20);
		this.setFill(Color.RED);
	}
	
	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
		if (this.delivered) {
			this.setFill(Color.GREEN);
		} else {
			this.setFill(Color.RED);
		}
	}
	
	public boolean getDelivered() {
		return this.delivered;
	}

	public void drawItself(GraphicsContext gc) {
		gc.strokeOval(this.getCenterX() - this.getRadius(), this.getCenterY() - this.getRadius(), this.getRadius(), this.getRadius());
		gc.setLineWidth(3);
		gc.setFill(this.getFill());		
		gc.fillOval(this.getCenterX() - this.getRadius(), this.getCenterY() - this.getRadius(), this.getRadius(), this.getRadius());
		gc.fill();
		gc.stroke();	
	}

	
}
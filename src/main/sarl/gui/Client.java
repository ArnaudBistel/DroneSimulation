package gui;

import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import map.MapPoint;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.*;
public class Client extends Circle{
	
	private boolean delivered = false;
	private MapPoint mapPoint;
	
	
	public Client (MapPoint mapPoint, Boolean simulation) {
		super();
		this.mapPoint = mapPoint;
		if (simulation) {
			this.setRadius(10);
			this.setCenterX(this.mapPoint.getPosition().getX());
			this.setCenterY(this.mapPoint.getPosition().getY());
			this.setFill(Color.RED);
		} else {
			this.setRadius(15);
			this.setCenterX((Double)this.mapPoint.getPosition().getX() / 2 );
			this.setCenterY((Double)this.mapPoint.getPosition().getY() / 2);
			this.setFill(Color.GREEN);
		}
		
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
	

	public StackPane drawItselfResults() {
		this.setFill(this.getFill());	
		Text text = new Text(""+mapPoint.getPackageWeight());
		text.setFont(Font.font ("Segoe UI Semibold", 18));
		text.setFill(Color.WHITE);
		text.setBoundsType(TextBoundsType.VISUAL); 
		StackPane stack = new StackPane();
		stack.getChildren().addAll(this, text);
		stack.setTranslateX(this.mapPoint.getPosition().getX() / 2 );
		stack.setTranslateY(this.mapPoint.getPosition().getY() / 2 );
		return stack;	
	}	

	public MapPoint getMapPoint() {
		return mapPoint;
	}

	
}
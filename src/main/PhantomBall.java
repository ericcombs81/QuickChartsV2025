package main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PhantomBall extends Circle {

	// If the length of points changes from 100, you will have to change it in three
	// marked spots in class "top"
	public double xPosition;
	public double yPosition;
	public Color color;

	public PhantomBall(double x, double y) {
		this.xPosition = x;
		this.yPosition = y;
		this.color = Color.GREY;
	}
	
	public double getX() {
		return this.xPosition;
	}
	
	public double getY() {
		return this.yPosition;
	}
}

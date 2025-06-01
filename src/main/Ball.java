package main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends Circle {

	// If the length of points changes from 100, you will have to change it in three
	// marked spots in class "top"
	public Point[] points = new Point[100];
	public boolean isSelected = false;
	public boolean mouseOver = false;
	public static Point startPoint;
	public Point endPoint = new Point();
	public Point location;
	public Integer number = 1;
	public Color color;

	public Ball(double x, double y, int number) {
		isSelected = true;
		points[0] = new Point(x, y);
		location = new Point(points[0].getX(), points[0].getY());
		this.number = number;
		this.setCenterX(x);
		this.setCenterY(y);
		this.color = Color.RED;
		// Bottom.hint.setVisible(false);
	}

	public Ball(Ball input) {
		this.number = input.number;
		for (int i = 0; i < this.points.length; i++) {
			if (input.points[i] != null) {
				Point point = new Point(input.points[i].getX(), input.points[i].getY());
				this.points[i] = point;
				location = new Point(points[0].getX(), points[0].getY());
			} else {
				this.points[i] = null;
			}
		}
		this.isSelected = input.isSelected;
		this.color = input.color;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void update(double progress) {
		location.x = (Double) (startPoint.x + ((endPoint.x - startPoint.x) * progress));
		location.y = (Double) (startPoint.y + ((endPoint.y - startPoint.y) * progress));
		Field.af.drawFrame();

	}

	public void setColor(Color color) {
		this.color = color;
	}
}

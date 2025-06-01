package main;

import javafx.scene.input.MouseEvent;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;


public class SelectionTool {
	
	public static Boolean isSelecting = false;
	public static Double startX = 0.0;
	public static Double startY = 0.0;
	public static Double currentX = 0.0;
	public static Double currentY = 0.0;
	
	//Called every single time the right mouse key is pressed
	//The incoming X and Y are already scaled to 1000 by 533
	public static void mousePressed(Double x, Double y) {
		isSelecting = true;
		startX = x;
		startY = y;
		currentX = x;
		currentY = y;
		Frame.field.af.updateSelection();
	}

	// Gets called ALWAYS when mouse is dragged AND isSelecting is True
	//The incoming X and Y are already scaled to 1000 by 533
	public static void dragged(Double x, Double y) {
		currentX = x;
		currentY = y;
		Frame.field.af.updateSelection();
		
	}

	//The incoming X and Y are already scaled to 1000 by 533
	public static void release(Double x, Double y) {
		isSelecting = false;
		Frame.field.af.updateSelection();
	}
	
	public static void selectDaBalls() {
		//Create a Bounds
		double selectionX = Math.min(startX, currentX);
		double selectionY = Math.min(startY, currentY);
		double selectionWidth = Math.abs(currentX - startX);
		double selectionHeight = Math.abs(currentY - startY);
		

		// Define rectangle bounds
		Bounds selectionBounds = new BoundingBox(selectionX, selectionY, selectionWidth, selectionHeight);
		
		for(int i = 0; i< Frame.field.balls.length; i++) {
			if(Frame.field.balls[i] != null) {
				if (Field.balls[i].points[Main.set.getValue() - 1] != null) {
					Point2D point2D = new Point2D(Field.balls[i].points[Main.set.getValue()-1].getX()/ (Frame.field.scale / 10), Field.balls[i].points[Main.set.getValue()-1].getY() / (Frame.field.scale / 10));
					System.out.println(point2D.toString());
					if(selectionBounds.contains(point2D)) {
						Field.balls[i].isSelected = true;
					} else {
						Field.balls[i].isSelected = false;
					}
				}
			}
		}
		
		Frame.field.af.drawBalls();
	}
	
	//THis is called after key released and the rectangle vanishes
	public static void weDone() {
		
	}

}

package main;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class PrintableAnimationFrame extends Pane {

	static Boolean isMoving = false; // The field is not moving by default
	public boolean mouseOver = false; // No points have been moused over unless triggered
	public static boolean lineShapeClicked = false;
	public static boolean linePresentOnField = false;
	public Point linePoint1;
	public Point linePoint2;
	public double x, y; // References the coordinates of a mouse click. Changed when mouse is clicked
	public static Boolean started = false;
	@SuppressWarnings("removal")
	public static Long startTime = new Long(0);
	public static int startSet = 1;
	public static int endSet = 1;
	public static int currentSet = 1;
	public static int entireDuration;
	public static int currentTarget = 2;
	public static int durationMinusPrevious = 0;
	public static int selected = -1;
	public boolean ballActive = false;
	public int ballActiveNumber = -1;
	public Double mouseX;
	public Double mouseY;
	public Double mouseDraggedX;
	public Double mouseDraggedY;

	PrintableAnimationFrame() {

		this.setPrefSize(Field.width * Field.scale, Field.height * Field.scale);
		drawFrame();
	}

	public void drawFrame() {
		this.getChildren().clear();
		drawBalls();
	}

	public void drawBalls() {
		for (int i = 0; i < Field.balls.length; i++) {
			if (Field.balls[i] != null) {
				// If the animation isn't moving, Draw the balls
				if (isMoving == false) {
					Circle circle = new Circle();
					circle.setCenterX(Field.balls[i].points[Main.set.getValue() - 1].getX());
					circle.setCenterY(Field.balls[i].points[Main.set.getValue() - 1].getY());
					circle.setRadius(.5 * Field.scale);
					if (Field.balls[i].number < 100) {
						circle.setStroke(Color.BLACK);
					}
					if (Field.balls[i].isSelected) {
						circle.setFill(Color.BLACK);
					} else {
						circle.setFill(Color.BLACK);
					}

					// Add the number to the ball

					if (Field.balls[i].number > 0 && Field.balls[i].number < 10) {
						Text text = new Text(Field.balls[i].number.toString());
						Font f = Font.font("Dialog", FontWeight.BOLD, 9);
						text.setFill(Color.WHITE);
						text.setFont(f);
						text.setX(Field.balls[i].points[Main.set.getValue() - 1].getX() - 2); // Set X coordinate
						text.setY(Field.balls[i].points[Main.set.getValue() - 1].getY() + 3); // Set Y coordinate
						this.getChildren().addAll(circle, text);
					}

					if (Field.balls[i].number > 9 && Field.balls[i].number < 100) {
						Text text = new Text(Field.balls[i].number.toString());
						text.setStyle("-fx-text-fill: white;");
						Font f = Font.font("Dialog", FontWeight.BOLD, 8);
						text.setFill(Color.WHITE);
						text.setFont(f);
						text.setX(Field.balls[i].points[Main.set.getValue() - 1].getX() - 5); // Set X coordinate
						text.setY(Field.balls[i].points[Main.set.getValue() - 1].getY() + 3); // Set Y coordinate
						this.getChildren().addAll(circle, text);
					}

					if (Field.balls[i].number > 99) {
						Text text = new Text(Field.balls[i].number.toString());
						text.setStyle("-fx-text-fill: white;");
						Font f = Font.font("Dialog", FontWeight.BOLD, 8);
						text.setFill(Color.WHITE);
						text.setFont(f);
						text.setX(Field.balls[i].points[Main.set.getValue() - 1].getX() - 7); // Set X coordinate
						text.setY(Field.balls[i].points[Main.set.getValue() - 1].getY() + 3); // Set Y coordinate
						this.getChildren().addAll(circle, text);
					}
					// else if the animation IS moving (isMoving == true)
				}
			}
		}
	}
}
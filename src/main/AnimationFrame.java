package main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class AnimationFrame extends Pane {

	private static animationTimer animationTimer;
	static Boolean isMoving = false; // The field is not moving by default
	public boolean mouseOver = false; // No points have been moused over unless triggered
	public static boolean lineShapeClicked = false;
	public static boolean smartLineShapeClicked = false;
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
	public PhantomBall[] phantomPoints = new PhantomBall[150];
	public Rectangle selectionRectangle = new Rectangle();

	AnimationFrame() {

		this.setPrefSize(Field.width * Field.scale, Field.height * Field.scale);

		// mousePressed listener
		this.setOnMousePressed((MouseEvent event) -> {
			mousePressed(event);
		});

		this.setOnMouseDragged((MouseEvent event) -> {
			mouseDraggedX = event.getX() / (Frame.field.scale / 10);
			mouseDraggedY = event.getY() / (Frame.field.scale / 10);
			dragged(mouseDraggedX, mouseDraggedY);

		});

		// Mouse Released Handler
		this.setOnMouseReleased((MouseEvent event) -> {

			// Selection Tool
			if (SelectionTool.isSelecting) {
				Double mouseReleasedX = event.getX() / (Frame.field.scale / 10);
				Double mouseReleasedY = event.getY() / (Frame.field.scale / 10);
				SelectionTool.release(mouseReleasedX, mouseReleasedY);
			} else { // next comes what happens if right button is not pressed down

				// If it was dragging a ball, save state. ballActiveNumber would be anything but
				// -1
				if (ballActiveNumber != -1) {
					State state = new State();
					Frame.urt.undoPush(state);
				}
				ballActive = false;
				ballActiveNumber = -1;
			}
		});

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

					circle.setFill(Field.balls[i].color);

					// Add the number to the ball

					if (Field.balls[i].number > 0 && Field.balls[i].number < 10) {
						Text text = new Text(Field.balls[i].number.toString());
						Font f = Font.font("Dialog", FontWeight.BOLD, 9 * Frame.field.scale / 10);
						text.setFont(f);
						if (Field.balls[i].isSelected) {
							text.setFill(Color.WHITE);
						}
						text.setX(Field.balls[i].points[Main.set.getValue() - 1].getX() - 2 * Frame.field.scale / 10); // Set
																														// X
																														// coordinate
						text.setY(Field.balls[i].points[Main.set.getValue() - 1].getY() + 3 * Frame.field.scale / 10); // Set
																														// Y
																														// coordinate
						this.getChildren().addAll(circle, text);
					}

					if (Field.balls[i].number > 9 && Field.balls[i].number < 100) {
						Text text = new Text(Field.balls[i].number.toString());
						Font f = Font.font("Dialog", FontWeight.BOLD, 8 * Frame.field.scale / 10);
						text.setFont(f);
						if (Field.balls[i].isSelected) {
							text.setFill(Color.WHITE);
						}
						text.setX(Field.balls[i].points[Main.set.getValue() - 1].getX() - 5 * Frame.field.scale / 10); // Set
																														// X
																														// coordinate
						text.setY(Field.balls[i].points[Main.set.getValue() - 1].getY() + 3 * Frame.field.scale / 10); // Set
																														// Y
																														// coordinate
						this.getChildren().addAll(circle, text);
					}

					if (Field.balls[i].number > 99) {
						Text text = new Text(Field.balls[i].number.toString());
						Font f = Font.font("Dialog", FontWeight.BOLD, 8 * Frame.field.scale / 10);
						text.setFont(f);
						if (Field.balls[i].isSelected) {
							text.setFill(Color.WHITE);
						}
						text.setX(Field.balls[i].points[Main.set.getValue() - 1].getX() - 7 * Frame.field.scale / 10); // Set
																														// X
																														// coordinate
						text.setY(Field.balls[i].points[Main.set.getValue() - 1].getY() + 3 * Frame.field.scale / 10); // Set
																														// Y
																														// coordinate
						this.getChildren().addAll(circle, text);
					}
					// else if the animation IS moving (isMoving == true)
				} else {

					Circle circle = new Circle();
					circle.setStroke(Color.BLACK);
					circle.setFill(Field.balls[i].color);
					circle.setCenterX(Field.balls[i].location.getX());
					circle.setCenterY(Field.balls[i].location.getY());
					circle.setRadius(.5 * Field.scale);

					if (Field.balls[i].number > 0 && Field.balls[i].number < 10) {
						Text text = new Text(Field.balls[i].number.toString());
						Font f = Font.font("Dialog", FontWeight.BOLD, 9);
						text.setFont(f);
						text.setX(Field.balls[i].location.getX() - 2); // Set X coordinate
						text.setY(Field.balls[i].location.getY() + 3); // Set Y coordinate
						this.getChildren().addAll(circle, text);
					}

					if (Field.balls[i].number > 9 && Field.balls[i].number < 100) {
						Text text = new Text(Field.balls[i].number.toString());
						Font f = Font.font("Dialog", FontWeight.BOLD, 8);
						text.setFont(f);
						text.setX(Field.balls[i].location.getX() - 5); // Set X coordinate
						text.setY(Field.balls[i].location.getY() + 3); // Set Y coordinate
						this.getChildren().addAll(circle, text);
					}

					if (Field.balls[i].number > 99) {
						Text text = new Text(Field.balls[i].number.toString());
						Font f = Font.font("Dialog", FontWeight.BOLD, 8);
						text.setFont(f);
						text.setX(Field.balls[i].location.getX() - 7); // Set X coordinate
						text.setY(Field.balls[i].location.getY() + 3); // Set Y coordinate
						this.getChildren().addAll(circle, text);
					}

				}
			}
		}

		if (phantomPoints[0] != null) {
			for (int i = 0; i < 150; i++) {
				if (phantomPoints[i] != null) {
					Circle circle = new Circle();

					circle.setCenterX(phantomPoints[i].getX());
					circle.setCenterY(phantomPoints[i].getY());
					circle.setRadius(.5 * Field.scale);
					circle.setFill(phantomPoints[i].color);
					this.getChildren().addAll(circle);

				}
			}

		}
	}

	public static int entireDuration(int startSet, int endSet) {
		int entireDuration = 0;
		for (int i = startSet - 1; i < endSet - 1; i++) {
			entireDuration += Field.durations[i];
		}

		return entireDuration;
	}

	public static int currentDurationCalculation(int currentTarget) {
		int time = 0;
		for (int i = startSet - 1; i < currentTarget - 1; i++) {
			time += Field.durations[i];
		}
		return time;
	}

	public static int previous(int currentSet) {
		int answer = 0;

		for (int i = startSet - 1; i < currentSet - 1; i++) {
			answer += Field.durations[i];
		}

		return answer;
	}

	public static void animate() {

		// Get the start set from the Right side bar and see if it is valid. If so, set
		// it to the start set.
		Integer startSetEntered = Right.playbackFrom;
		System.out.println("animate from " + Right.playbackFrom);
		for (int i = 0; i < Field.balls.length; i++) {
			if (Field.balls[i] != null) {
				if (Field.balls[i].points[startSetEntered - 1] != null
						&& Field.balls[i].points[startSetEntered] != null) {
					startSet = startSetEntered;
					break;
				} else {
					startSet = 1;

					String message = "You cannot start from the set entered on the right. The set either doesn't exist, or the set immediately after it doesn't exist.";

					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Enter a new starting set.");
					alert.setContentText(message);

					// Get the dialog pane
					DialogPane dialogPane = alert.getDialogPane();

					// Set the minimum width and height to make the dialog bigger
					dialogPane.setMinWidth(600);
					dialogPane.setMinHeight(200);

					alert.showAndWait();

					return;
				}
			}
		}
		// Clear the AnimationFrame
		Field.af.getChildren().clear();

		// No balls will be selected during the animation. This stores the # of the
		// selected ball.
		for (int i = 0; i < Field.balls.length; i++) {
			if (Field.balls[i] != null) {
				if (Field.balls[i].isSelected == true) {
					Field.balls[i].isSelected = false;
					selected = i;
					break;
				}
			}
		}

		// Return to set one to start this animation
		Field.setSet(new SimpleIntegerProperty(startSet));
		Left.setLabel.setText("Set: " + Main.set.getValue());
		Top.note.setText(Top.notes[Main.set.getValue() - 1]);
		Top.countsLabel.setText("Counts: " + Top.counts[Main.set.getValue() - 1]);

		// for every duration, if it isn't null, move from set[j] to set[j+1] for
		// duration[j]

		// set endSet to the last set that has balls and durations.
		int lastSetWithPoints = 0;
		for (int i = 0; i < Field.balls.length; i++) {
			if (Field.balls[i] != null) {
				for (int j = 0; j < Field.balls[i].points.length; j++) {
					if (Field.balls[i].points[j] != null) {
						lastSetWithPoints++;
					}
				}
				break;
			}
		}

		int lastSetWithDurations = 0;
		for (int i = 0; i < Field.durations.length; i++) {
			if (Field.durations[i] == -1) {
				break;
			}
			if (Field.durations[i] != -1) {
				lastSetWithDurations = i + 1;
			}
		}

		if (lastSetWithDurations < lastSetWithPoints) {
			endSet = lastSetWithDurations;
		} else {
			endSet = lastSetWithPoints;
		}

		startTimer();

		// timer.start();

		// Field.af.drawFrame();
	}

	private static void startTimer() {
		animationTimer = new animationTimer();
		animationTimer.start();
	}

	public static void stopTimer() {
		if (animationTimer != null) {
			animationTimer.stop();
		}
	}

	private void dragged(Double mouseDraggedX, Double mouseDraggedY) {

		// For the selection tool
		if (SelectionTool.isSelecting) {
			SelectionTool.dragged(mouseDraggedX, mouseDraggedY);
		} else {
			// this is the default action when right button is not clicked down

			if (ballActive) {
				// Double changeX = mouseDraggedX - mouseX;
				// Double changeY = mouseDraggedY - mouseY;
				mouseX = mouseDraggedX;
				mouseY = mouseDraggedY;

				if (FullScreenView.isFullScreen) {
					Field.balls[ballActiveNumber].points[Main.set.getValue() - 1].setLocation(
							mouseDraggedX * (Frame.field.scale / 10), mouseDraggedY * (Frame.field.scale / 10));
					Field.af.drawFrame();
				} else {

					Field.balls[ballActiveNumber].points[Main.set.getValue() - 1].setLocation(mouseDraggedX,
							mouseDraggedY);
					// Field.balls[ballActiveNumber].points[Main.set.getValue() - 1].getX() +
					// changeX,
					// Field.balls[ballActiveNumber].points[Main.set.getValue() - 1].getY() +
					// changeY);
					Field.af.drawFrame();
				}
			}
			//System.out.println("MouseX = " + mouseDraggedX + ": MouseY = " + mouseDraggedY);
		}
	}

	public void mousePressed(MouseEvent e) {

		// for selection tool with right click
		if (e.getButton() == MouseButton.SECONDARY) {
			Double currentX = e.getX() / (Frame.field.scale / 10);
			Double currentY = e.getY() / (Frame.field.scale / 10);
			SelectionTool.mousePressed(currentX, currentY);
		} else {
			// This "else" is for anything but right click

			this.x = e.getX();
			this.y = e.getY();

			// Add functionality to pick up a ball if it is double clicked and then drop it
			// on the next click

			// if the double click happens within 1/2 circleDiameter +/- X && +-Y
			// Then deselect everything but it

			// Have that circle's X and Y coordinates match the mouse's and continue to
			// Until you click again, at which point the X/Y coordinates are set in stone.

			if (lineShapeClicked != true && smartLineShapeClicked != true) {
				for (int i = 0; i < Field.balls.length; i++) {
					if (Field.balls[i] != null) {
						if (Field.balls[i].points[Main.set.getValue() - 1] != null) {
							// Check to see if you clicked on an existing point, and if so, select it
							if (this.x <= Field.balls[i].points[Main.set.getValue() - 1].getX() + (.5 * Field.scale + 1)
									&& this.x >= Field.balls[i].points[Main.set.getValue() - 1].getX()
											- (.5 * Field.scale + 1)
									&& this.y <= Field.balls[i].points[Main.set.getValue() - 1].getY()
											+ (.5 * Field.scale + 1)
									&& this.y >= Field.balls[i].points[Main.set.getValue() - 1].getY()
											- (.5 * Field.scale + 1)) {

								// Multiple select. It control is being pushed.
								if (e.isControlDown()) {
									// toggle selection of this ball
									if (Field.balls[i].isSelected == false) {
										Field.balls[i].setIsSelected(true);
									} else {
										Field.balls[i].setIsSelected(false);
									}
									mouseX = e.getX();
									mouseY = e.getY();
									ballActive = true;
									ballActiveNumber = i;
									// Field.af.drawFrame();

									drawBalls();
									State state = new State();
									Frame.urt.undoPush(state);
									return;

								}

								if (!ColorChooser.colorChooserOpen) {
									// Then deselect everything but it
									for (int j = 0; j < Field.balls.length; j++) {
										if (Field.balls[j] != null) {
											Field.balls[j].setIsSelected(false);
										}
									}

									Field.balls[i].setIsSelected(true);
									mouseX = e.getX();
									mouseY = e.getY();
									ballActive = true;
									ballActiveNumber = i;
									// Field.af.drawFrame();

									drawBalls();
									State state = new State();
									Frame.urt.undoPush(state);
									return;
								} else {

									Color theSetColor = ((Color) Top.cc.chosenButton.getBackground().getFills().get(0)
											.getFill());

									Field.balls[i].color = theSetColor;
									Field.af.drawBalls();
									State state = new State();
									Frame.urt.undoPush(state);
									return;
								}

							}
						}
					}
				}
				// This only works on set one. Anywhere else - nothing happens.
				// Assertion: You clicked an empty spot -> create and draw a new point there
				if (mouseOver == false) {
					if (Main.set.getValue() == 1) {
						this.x = e.getX();
						this.y = e.getY();

						// All balls un-selected
						for (int j = 0; j < Field.balls.length; j++) {
							if (Field.balls[j] != null) {
								Field.balls[j].setIsSelected(false);
							}
						}
						// New ball made and placed in first null slot
						for (int i = 0; i < Field.balls.length; i++) {
							if (Field.balls[i] == null) {
								Field.balls[i] = new Ball(x, y, i + 1);

								if (ColorChooser.colorChooserOpen == true) {
									Field.balls[i].setColor(
											((Color) Top.cc.chosenButton.getBackground().getFills().get(0).getFill()));
								} else {
									Field.balls[i].setColor(Color.RED);
								}

								// The ball before it is unselected
								if (i > 0) {
									Field.balls[i - 1].isSelected = false;
								}
								// And this one is selected
								Field.balls[i].isSelected = true;

								// Figure out what the last set is that has points and set that to
								// lastExistingSet
								int lastExistingSet = 1;
								// for every single ball, find one that exists
								// But if set one was deleted, and re added, it would only have one set.
								// Therefore, just find the max.
								a: for (int k = 0; k < Field.balls.length; k++) {
									if (Field.balls[k] != null) {
										int thisBallMaxSet = 1;
										// And then find the last set that isn't null
										for (int l = 0; l < Field.balls[k].points.length; l++) {
											// Save that to lastExistingSet
											if (Field.balls[k].points[l] != null) {
												thisBallMaxSet = l + 1;
											}
										}
										if (thisBallMaxSet > lastExistingSet) {
											lastExistingSet = thisBallMaxSet;
										}
									}
								}

								// for every set past current set, fill this new ball with this exact same point
								// in those sets.
								for (int a = 1; a < lastExistingSet; a++) {
									Field.balls[i].points[a] = new Point(x, y);
								}

								Field.af.drawFrame();
								State state = new State();
								Frame.urt.undoPush(state);
								return;
							}
						}
					}
				}

				// else the lineShapeClicked is true
			} else if (lineShapeClicked == true) {
				if (linePoint1 == null) {
					linePoint1 = new Point(x, y);
					linePresentOnField = true;
					Frame.field.redrawField();
				} else {
					linePoint2 = new Point(x, y);
					lineShapeClicked = false;
					linePresentOnField = true;
					Right.lineButton.setVisible(true);
					Frame.field.redrawField();
					return;
				}
				// else the smartLineSpaheClicked is true
			} else {
				if (linePoint1 == null) {
					linePoint1 = new Point(x, y);
					linePresentOnField = true;
					Frame.field.redrawField();
					zeroOutPhantomLineArray();
				} else {
					linePoint2 = new Point(x, y);
					lineShapeClicked = false;
					linePresentOnField = true;
					Right.lineButton.setVisible(true);
					Frame.field.redrawField();
					zeroOutPhantomLineArray();
				}
				// At this lovely point, there should be a line on the field, and an erase
				// button. But wait, there is more!

				int numPoints = 10;

				// Compute step sizes for x and y
				if (this.linePoint2 != null) {
					double stepX = (linePoint2.x - linePoint1.x) / (double) (numPoints - 1);
					double stepY = (linePoint2.y - linePoint1.y) / (double) (numPoints - 1);

					// Generate the ball positions
					for (int i = 0; i < numPoints; i++) { // Start from 0 to include the first point
						int ballX = (int) (linePoint1.x + i * stepX);
						int ballY = (int) (linePoint1.y + i * stepY);
						phantomPoints[i] = new PhantomBall(ballX, ballY);
					}
					Frame.field.redrawField();
					drawBalls();
					// This is all good, except if I continue to click I just keep adding more
					// point2's and make some wicked cool lines.
					// Gotta do something at this point, yo.
				}
			}
		}
	}

	public void zeroOutPhantomLineArray() {
		for (int i = 0; i < 150; i++) {
			phantomPoints[i] = null;
		}
	}

	public void resizeAnimationFrame(Double newCurrentScale) {
		this.setPrefSize(Field.width * newCurrentScale, Field.height * newCurrentScale);
	}
	
	//This is the selection rectangle, called from the SelectionTool upon right click, drag, and release 
	public void updateSelection() {
		if (SelectionTool.isSelecting) {
			if (!this.getChildren().contains(selectionRectangle)) {
			    this.getChildren().add(selectionRectangle);
			    selectionRectangle.setFill(Color.rgb(0, 0, 255, 0.2));  // 0.2 = 20% opacity
			    SelectionTool.selectDaBalls();

			}

	        double selectionWidth = Math.abs(SelectionTool.currentX * Frame.field.scale / 10 - SelectionTool.startX * Frame.field.scale / 10);
	        double selectionHeight = Math.abs(SelectionTool.currentY * Frame.field.scale / 10 - SelectionTool.startY * Frame.field.scale / 10);
	        double selectionX = Math.min(SelectionTool.startX * Frame.field.scale / 10, SelectionTool.currentX * Frame.field.scale / 10 );
	        double selectionY = Math.min(SelectionTool.startY * Frame.field.scale / 10, SelectionTool.currentY * Frame.field.scale / 10);

	        // Update the rectangle's position and size
	        selectionRectangle.setX(selectionX);
	        selectionRectangle.setY(selectionY);
	        selectionRectangle.setWidth(selectionWidth);
	        selectionRectangle.setHeight(selectionHeight);
	        SelectionTool.selectDaBalls();
	        
	    } else {
	    	//this is triggered at the release
	    	this.getChildren().remove(selectionRectangle);
	    	SelectionTool.weDone();
	    }
	}
}
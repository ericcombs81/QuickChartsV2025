package main;

import java.io.IOException;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class KeyHandler {

	// The Main method has a KeyEvent listener that sends the codes to this class

	@SuppressWarnings({ "static-access", "unused" })
	public static void handlePressed(KeyEvent e) {
		

		if(e.getCode() == KeyCode.O) {
			Utilities.snapToGrid();
			Double yCoord = Frame.field.balls[0].points[0].getY();
			System.out.println(yCoord);
		}

		if (e.getCode() == KeyCode.U) {
			Frame.urt.undoPop();
		}

		if (e.getCode() == KeyCode.R) {
			Frame.urt.redoPop();
		}

		if (e.getCode() == KeyCode.V) {
			if(FullScreenView.isFullScreen) { 
			FullScreenView.toggleFullScreen();
			}
			Utilities.start3D();
		}
		
		if (e.getCode() == KeyCode.F) {
			FullScreenView.toggleFullScreen();
		}

		if (e.getCode() == KeyCode.P) {
			PrintableScreen ps = new PrintableScreen();
		}

		if (e.getCode() == KeyCode.B) {
			if (Main.set.getValue() != 1) {
				Utilities.switchSets(Main.set.getValue() - 1);
			}
		}

		if (e.getCode() == KeyCode.N) {
			if (Main.fullVersion == true) {

				if (Main.set.getValue() != 100) {
					Utilities.switchSets(Main.set.getValue() + 1);
				}
			} else {
				if (Main.set.getValue() <= 3) {
					Utilities.switchSets(Main.set.getValue() + 1);
				}
			}
		}
		
		if(FullScreenView.isFullScreen && e.isShiftDown() && e.getCode() == KeyCode.EQUALS) {
			if(FullScreenView.thisScale <= 33) {
				FullScreenView.thisScale +=1;
				FullScreenView.drawFullScreen();
				Main.frame.field.drawField(Main.frame.field.gc);
				Main.frame.field.redrawField();
			}
			
		}
		
		if(FullScreenView.isFullScreen && e.isShiftDown() && e.getCode() == KeyCode.MINUS) {
			if(FullScreenView.thisScale > 10) {
				FullScreenView.thisScale -=1;
				FullScreenView.drawFullScreen();
				Main.frame.field.drawField(Main.frame.field.gc);
				Main.frame.field.redrawField();
			}
		}
		
		if(FullScreenView.isFullScreen && e.getCode() == KeyCode.ESCAPE) {
			FullScreenView.minimize();
			FullScreenView.isFullScreen = false;
		}
		
		

		// set 1
		if (e.getCode() == KeyCode.DIGIT1) {
			Utilities.switchSets(1);
		}

		// set 2
		if (e.getCode() == KeyCode.DIGIT2) {
			Utilities.switchSets(2);
		}

		// set 3
		if (e.getCode() == KeyCode.DIGIT3) {
			Utilities.switchSets(3);
		}

		// set 4
		if (e.getCode() == KeyCode.DIGIT4) {
			Utilities.switchSets(4);
		}

		// set 5
		if (e.getCode() == KeyCode.DIGIT5) {

			if (Main.fullVersion == true) {
				Utilities.switchSets(5);
			}

		}

		// set 6
		if (e.getCode() == KeyCode.DIGIT6) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(6);
			}
		}

		// set 7
		if (e.getCode() == KeyCode.DIGIT7) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(7);
			}
		}

		// set 8
		if (e.getCode() == KeyCode.DIGIT8) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(8);
			}
		}

		// set 9
		if (e.getCode() == KeyCode.DIGIT9) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(9);
			}
		}

		// set 10
		if (e.getCode() == KeyCode.DIGIT0) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(10);
			}
		}

		KeyCodeCombination combination11 = new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.SHIFT_DOWN);
		KeyCodeCombination combination12 = new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.SHIFT_DOWN);
		KeyCodeCombination combination13 = new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.SHIFT_DOWN);
		KeyCodeCombination combination14 = new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.SHIFT_DOWN);
		KeyCodeCombination combination15 = new KeyCodeCombination(KeyCode.DIGIT5, KeyCombination.SHIFT_DOWN);
		KeyCodeCombination combination16 = new KeyCodeCombination(KeyCode.DIGIT6, KeyCombination.SHIFT_DOWN);
		KeyCodeCombination combination17 = new KeyCodeCombination(KeyCode.DIGIT7, KeyCombination.SHIFT_DOWN);
		KeyCodeCombination combination18 = new KeyCodeCombination(KeyCode.DIGIT8, KeyCombination.SHIFT_DOWN);
		KeyCodeCombination combination19 = new KeyCodeCombination(KeyCode.DIGIT9, KeyCombination.SHIFT_DOWN);
		KeyCodeCombination combination20 = new KeyCodeCombination(KeyCode.DIGIT0, KeyCombination.SHIFT_DOWN);

		// set 11
		if (combination11.match(e)) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(11);
			}
		}

		// set 12
		if (combination12.match(e)) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(12);
			}
		}

		// set 13
		if (combination13.match(e)) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(13);
			}
		}

		// set 14
		if (combination14.match(e)) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(14);
			}
		}

		// set 15
		if (combination15.match(e)) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(15);
			}
		}

		// set 16
		if (combination16.match(e)) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(16);
			}
		}

		// set 17
		if (combination17.match(e)) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(17);
			}
		}

		// set 18
		if (combination18.match(e)) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(18);
			}
		}

		// set 19
		if (combination19.match(e)) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(19);
			}
		}

		// set 20
		if (combination20.match(e)) {
			if (Main.fullVersion == true) {
				Utilities.switchSets(20);
			}
		}

		if (AnimationFrame.isMoving == false) {

			boolean ballsExist = false;
			for (int i = 0; i < Field.balls.length; i++) {
				if (Field.balls[i] != null) {
					ballsExist = true;
					break;
				}
			}
			if (ballsExist) {
				// M for Move
				if (e.getCode() == KeyCode.M) {
					if (!Java3D.threeDRunning) {
						Frame.bottom.play();
					}
				}
				AnimationFrame.isMoving = false;
				Field.af.drawFrame();

				int selected = 0;
				for (int i = 0; i < Field.balls.length; i++) {
					if (Field.balls[i] != null) {
						if (Field.balls[i].isSelected) {
							selected = i;
							break;
						}
					}
				}

				// Reverse Tab (Shift + z)
				if (e.getCode() == KeyCode.Z) {
					Field.balls[selected].setIsSelected(false);
					for (int i = selected - 1; i >= 0; i--) {
						if (Field.balls[i] != null) {
							if (Field.balls[i].isSelected == false) {

								Field.balls[i].setIsSelected(true);
								Field.af.drawFrame();
								selected = 0;
								break;
							}
						}
					}
					boolean isItSelected = false;
					for (int j = 0; j < Field.balls.length; j++) {
						if (Field.balls[j] != null) {
							if (Field.balls[j].isSelected == true) {
								isItSelected = true;
							}
						}
					}
					if (isItSelected == false) {
						for (int i = Field.balls.length - 1; i >= 0; i--) {
							if (Field.balls[i] != null) {
								Field.balls[i].setIsSelected(true);
								isItSelected = true;
								break;
							}
						}
					}
					Field.af.drawFrame();
				}

				// CHANGE THIS TO SHIFT + TAB
				// Tab (x)

				if (e.getCode() == KeyCode.X) {

					// Make it so that nothing is selected
					Field.balls[selected].setIsSelected(false);

					// For every ball AFTER the selection
					for (int i = selected + 1; i < Field.balls.length; i++) {
						// if the ball exists, select it, get out of here. Done
						if (Field.balls[i] != null) {
							Field.balls[i].setIsSelected(true);
							Field.af.drawFrame();
							return;
						}
					}
					// But if you did NOT find one, you better look at the beginning for one
					// So for all balls from the beginning to the end, see if you can find one
					for (int j = 0; j < Field.balls.length; j++) {
						if (Field.balls[j] != null) {
							Field.balls[j].setIsSelected(true);
							Field.af.drawFrame();
							return;
						}

					}

					Field.af.drawFrame();

				}

				// up
				if (e.getCode() == KeyCode.UP) {
					for (int i = 0; i < Field.balls.length; i++) {
						if (Field.balls[i] != null) {
							if (Field.balls[i].isSelected) {
								Field.balls[i].points[Main.set.getValue() - 1].setLocation(
										Field.balls[i].points[Main.set.getValue() - 1].getX(),
										Field.balls[i].points[Main.set.getValue() - 1].getY() - 1.5625 * Frame.field.scale / 10);
								
										//Logic to print out the coordinate chart (this is a test)
										String testX = CoordinateCalculator.getXString(Field.balls[i].points[Main.set.getValue()-1]);
										String testY = CoordinateCalculator.getYString(Field.balls[i].points[Main.set.getValue()-1]);
										System.out.println(testX);
										System.out.println(testY);
							}
						}
					}
					Field.af.drawFrame();
					State state = new State();
					Frame.urt.undoPush(state);
					
					// down
				} else if (e.getCode() == KeyCode.DOWN) {
					for (int i = 0; i < Field.balls.length; i++) {
						if (Field.balls[i] != null) {
							if (Field.balls[i].isSelected) {
								Field.balls[i].points[Main.set.getValue() - 1].setLocation(
										Field.balls[i].points[Main.set.getValue() - 1].getX(),
										Field.balls[i].points[Main.set.getValue() - 1].getY() + 1.5625* Frame.field.scale / 10);
							}
						}
					}
					Field.af.drawFrame();
					State state = new State();
					Frame.urt.undoPush(state);
					// left
				} else if (e.getCode() == KeyCode.LEFT) {
					for (int i = 0; i < Field.balls.length; i++) {
						if (Field.balls[i] != null) {
							if (Field.balls[i].isSelected) {
								Field.balls[i].points[Main.set.getValue() - 1].setLocation(
										Field.balls[i].points[Main.set.getValue() - 1].getX() - 1.5625* Frame.field.scale / 10,
										Field.balls[i].points[Main.set.getValue() - 1].getY());
							}
						}
					}
					Field.af.drawFrame();
					State state = new State();
					Frame.urt.undoPush(state);					
					// right
				} else if (e.getCode() == KeyCode.RIGHT) {
					for (int i = 0; i < Field.balls.length; i++) {
						if (Field.balls[i] != null) {
							if (Field.balls[i].isSelected) {
								Field.balls[i].points[Main.set.getValue() - 1].setLocation(
										Field.balls[i].points[Main.set.getValue() - 1].getX() + 1.5625* Frame.field.scale / 10,
										Field.balls[i].points[Main.set.getValue() - 1].getY());
							}
						}
					}
					Field.af.drawFrame();
					State state = new State();
					Frame.urt.undoPush(state);
					//Test X Cooridinates with right arrow press
					//String message = CoordinateCalculator.getXString(Frame.field.balls[0].points[0]);
					//System.out.println(message);

				}

				// up

				if (e.getCode() == KeyCode.W) {
					for (int i = 0; i < Field.balls.length; i++) {
						if (Field.balls[i] != null) {
							if (Field.balls[i].isSelected) {
								Field.balls[i].points[Main.set.getValue() - 1].setLocation(
										Field.balls[i].points[Main.set.getValue() - 1].getX(),
										Field.balls[i].points[Main.set.getValue() - 1].getY() - 3.125* Frame.field.scale / 10);
							}
						}
					}
					Field.af.drawFrame();
					State state = new State();
					Frame.urt.undoPush(state);
					// down
				} else if (e.getCode() == KeyCode.S) {
					for (int i = 0; i < Field.balls.length; i++) {
						if (Field.balls[i] != null) {
							if (Field.balls[i].isSelected) {
								Field.balls[i].points[Main.set.getValue() - 1].setLocation(
										Field.balls[i].points[Main.set.getValue() - 1].getX(),
										Field.balls[i].points[Main.set.getValue() - 1].getY() + 3.125* Frame.field.scale / 10);
							}
						}
					}
					Field.af.drawFrame();
					State state = new State();
					Frame.urt.undoPush(state);
					// left
				} else if (e.getCode() == KeyCode.A) {
					for (int i = 0; i < Field.balls.length; i++) {
						if (Field.balls[i] != null) {
							if (Field.balls[i].isSelected) {
								Field.balls[i].points[Main.set.getValue() - 1].setLocation(
										Field.balls[i].points[Main.set.getValue() - 1].getX() - 3.125* Frame.field.scale / 10,
										Field.balls[i].points[Main.set.getValue() - 1].getY());
							}
						}
					}
					Field.af.drawFrame();
					State state = new State();
					Frame.urt.undoPush(state);
					// right
				} else if (e.getCode() == KeyCode.D) {
					for (int i = 0; i < Field.balls.length; i++) {
						if (Field.balls[i] != null) {
							if (Field.balls[i].isSelected) {
								Field.balls[i].points[Main.set.getValue() - 1].setLocation(
										Field.balls[i].points[Main.set.getValue() - 1].getX() + 3.125* Frame.field.scale / 10,
										Field.balls[i].points[Main.set.getValue() - 1].getY());
							}
						}
					}
					Field.af.drawFrame();
					State state = new State();
					Frame.urt.undoPush(state);
				}

			}
		}

	}

	public static void handleReleased(KeyEvent e) {

		int selected = 0;
		for (int i = 0; i < Field.balls.length; i++) {
			if (Field.balls[i] != null) {
				if (Field.balls[i].isSelected) {
					selected = i;
					break;
				}
			}
		}
		if (AnimationFrame.isMoving == false) {
			// delete
			if (e.getCode() == KeyCode.DELETE || e.getCode() == KeyCode.BACK_SPACE) {
				Field.balls[selected] = null;

				for (int i = selected + 1; i < Field.balls.length; i++) {
					if (Field.balls[i] != null) {
						Field.balls[i].setIsSelected(true);
						selected = 0;
						Field.af.drawFrame();
						break;
					}
				}

				boolean selectedPresent = false;
				for (int k = 0; k < Field.balls.length; k++) {
					if (Field.balls[k] != null) {
						if (Field.balls[k].isSelected == true) {
							selectedPresent = true;
							break;
						}
					}
				}
				if (selectedPresent == false) {
					for (int l = 0; l < Field.balls.length; l++) {
						if (Field.balls[l] != null) {
							if (Field.balls[l].isSelected == false) {
								Field.balls[l].setIsSelected(true);
								Field.af.drawFrame();
								break;
							}
						}
					}
				}
				State state1 = new State();
				Frame.urt.undoPush(state1);
				Field.af.drawFrame();
			}

		}
	}

}

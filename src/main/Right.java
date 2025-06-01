package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;


public class Right extends VBox {

	public static Button lineButton;
	public static Boolean isPlaybackAudioChecked = true;
	public static Integer playbackFrom = 1;
	public static ComboBox<Integer> comboBox;

	@SuppressWarnings("static-access")
	public Right() {

		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.getStyleClass().add("myBackground");

		VBox setBox = new VBox();
		setBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		setBox.getStyleClass().add("myBackground");
		VBox copyBox = new VBox();
		copyBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		copyBox.getStyleClass().add("myBackground");
		VBox shapeBox = new VBox();
		shapeBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		shapeBox.getStyleClass().add("myBackground");
		setBox.setStyle("-fx-background-color: #c0c0c0;");
		setBox.setSpacing(10);
		copyBox.setStyle("-fx-background-color: #c0c0c0;");
		copyBox.setSpacing(10);
		shapeBox.setStyle("-fx-background-color: #c0c0c0;");
		shapeBox.setSpacing(10);

		this.setPrefSize(150, Field.height * Field.scale);
		this.setStyle("-fx-background-color: #c0c0c0;");

		Button nextButton = new Button("Next Set");
		nextButton.getStyleClass().add("myButton");
		nextButton.setPrefWidth(100);
		nextButton.setOnAction(event -> {
			if (Main.set.getValue() != 100) {
				Utilities.switchSets(Main.set.getValue() + 1);
				Field.af.drawFrame();
			}
		});
		nextButton.setFocusTraversable(false);
		setBox.getChildren().add(nextButton);

		Button prevButton = new Button("Previous Set");
		prevButton.setPrefWidth(100);
		prevButton.getStyleClass().add("myButton");
		prevButton.setOnAction(event -> {
			if (Main.set.getValue() != 1) {
				Utilities.switchSets(Main.set.getValue() - 1);
				Field.af.drawFrame();
			}
		});
		prevButton.setFocusTraversable(false);
		setBox.getChildren().add(prevButton);

		Button copyButton = new Button("Copy Set");
		copyButton.getStyleClass().add("myButton");
		copyButton.setPrefWidth(100);
		copyButton.setOnAction(event -> {
			try {
				copySet();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		copyButton.setFocusTraversable(false);
		copyBox.getChildren().add(copyButton);

		Button pasteButton = new Button("Paste");
		pasteButton.getStyleClass().add("myButton");
		pasteButton.setPrefWidth(100);
		pasteButton.setOnAction(event -> {
			try {
				pasteSet();
				State state = new State();
				Frame.urt.undoPush(state);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		pasteButton.setFocusTraversable(false);
		copyBox.getChildren().add(pasteButton);

		lineButton = new Button("Erase Line");
		lineButton.getStyleClass().add("myButton");
		lineButton.setPrefWidth(100);
		lineButton.setOnAction(event -> {
			Field.af.zeroOutPhantomLineArray();
			Field.af.linePresentOnField = false;
			Field.af.smartLineShapeClicked = false;
			Field.af.linePoint1 = null;
			Field.af.linePoint2 = null;
			lineButton.setVisible(false);
			Field.af.drawFrame();
			Frame.field.redrawField();
			
		});
		lineButton.setVisible(false);
		lineButton.setFocusTraversable(false);
		shapeBox.getChildren().add(lineButton);

		Button threeD = new Button("3D View (v)");
		threeD.getStyleClass().add("myButton");
		threeD.setFocusTraversable(false);
		threeD.setOnAction(e -> {
			Utilities.start3D();
		});

		// Playback parameters
		VBox playBack = new VBox();

		playBack.setPadding(new Insets(0, 20, 0, 0));
		Label playBackLabel = new Label("Playback:");
		playBackLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

		Label startSet = new Label("Start set: ");

		comboBox = new ComboBox<>(FXCollections.observableArrayList(
		        1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,
		        21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,
		        41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,
		        61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,
		        80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,
		        100
		));
		comboBox.getSelectionModel().selectFirst(); // Default selection is 1
		comboBox.setFocusTraversable(false);

		CheckBox playbackAudio = new CheckBox("Audio On");
		playbackAudio.setSelected(true);
		playbackAudio.setFocusTraversable(false);

		// Add an event handler to the CheckBox
		playbackAudio.setOnAction(event -> {
			isPlaybackAudioChecked = playbackAudio.isSelected();

		});

		playBack.getChildren().addAll(playBackLabel, startSet, comboBox);

		this.setSpacing(30);
		this.setPadding(new Insets(50, 0, 0, 20));
		this.getChildren().addAll(setBox, copyBox, shapeBox, threeD, playBack, playbackAudio);
		
		comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		        playbackFrom = newValue; // Update intStartSet with the new value from ComboBox
		        System.out.println(playbackFrom);
		    }
		});

	}

	public void copySet() throws IOException {
		String userHomeDirectory = System.getProperty("user.home");
		String copyFilePath = userHomeDirectory + File.separator + ".QuickChartsConfig" + File.separator
				+ ".copy.txt";
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(new File(copyFilePath)));

			// Number of balls
			int numberOfBalls = 0;
			for (int i = 0; i < Field.balls.length; i++) {
				if (Field.balls[i] != null) {
					numberOfBalls++;
				}
			}

			out.write(Integer.toString(numberOfBalls));
			out.newLine();

			// write out the x and y of each ball.
			for (int i = 0; i < Field.balls.length; i++) {
				if (Field.balls[i] == null) {
					out.write("null");
					out.newLine();
					out.write("null");
					out.newLine();
					out.write("null");
					out.newLine();
					out.write("null");
					out.newLine();
					out.write("null");
					out.newLine();
					out.write("null");
					out.newLine();
				} else {
					// isSelected
					out.write(Boolean.toString(Field.balls[i].isSelected));
					out.newLine();

					// location.x
					out.write(Double.toString(Field.balls[i].location.x));
					out.newLine();

					// location.y
					out.write(Double.toString(Field.balls[i].location.y));
					out.newLine();

					// number
					out.write(Integer.toString(Field.balls[i].number));
					out.newLine();

					// point at this set
					out.write(Double.toString(Field.balls[i].points[Main.set.getValue() - 1].getX()));
					out.newLine();

					// all the points y
					out.write(Double.toString(Field.balls[i].points[Main.set.getValue() - 1].getY()));
					out.newLine();
				}
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pasteSet() throws IOException {

		// Figure out the number of balls currently on the field before the paste
		// attempt
		Integer numberBalls = 0;
		for (int i = 0; i < Field.balls.length; i++) {
			if (Field.balls[i] != null) {
				numberBalls++;
			}
		}

		// Open up the file that has the copied set
		String userHomeDirectory = System.getProperty("user.home");
		String copyFilePath = userHomeDirectory + File.separator + ".QuickChartsConfig" + File.separator
				+ ".copy.txt";
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(copyFilePath)));
			String temp = in.readLine();
			Integer copiedNumberOfBalls = Integer.parseInt(temp);

			// If this isn't the first set, and the number of marchers aren't equal, show
			// error message
			if (Main.set.getValue() != 1) {
				if (numberBalls != copiedNumberOfBalls) {
					String message = "Cannot paste: the copied number of marchers does not match the current project's number of marchers.";
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText(null);
					alert.setContentText(message);
					alert.showAndWait();

					return;
				}
			}

			// If this is the first set and there are more marchers on the field than in the
			// copied set, show an error
			if (Main.set.getValue() == 1) {
				if (numberBalls > copiedNumberOfBalls) {
					String message = "Existing number of marchers (" + numberBalls
							+ ") is greater than the number of marchers you are trying to paste ("
							+ copiedNumberOfBalls + ").  That is a problem.";
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText(null);
					alert.setContentText(message);
					alert.showAndWait();
					return;
				}
			}

			// Otherwise, you are good to go. Lets get at it.
			for (int i = 0; i < Field.balls.length; i++) {
				String selected = in.readLine();
				if (selected.equals("null")) {
				} else {
					// Check to see if this is both 1) the first set and 2) the ONLY set
					if (Main.set.getValue() == 1) {
						boolean ballsExist = false;
						for (int j = 0; j < Field.balls.length; j++) {
							if (Field.balls[i] != null) {
								ballsExist = true;
								break;
							}
						}
						if (!ballsExist) {
							Field.balls[i] = new Ball(0, 0, 1);
							Field.balls[i].isSelected = Boolean.parseBoolean(selected);
						} else {

							for (int j = 0; j < Field.balls[i].points.length; j++) {
								if (Field.balls[i].points[1] == null) {
									Field.balls[i] = new Ball(0, 0, 1);
									Field.balls[i].isSelected = Boolean.parseBoolean(selected);
								}
							}
						}

					} else {
						Field.balls[i].isSelected = Boolean.parseBoolean(selected);
					}
				}

				String locationX = in.readLine();
				if (locationX.equals("null")) {
				} else {
					Field.balls[i].location.x = Double.parseDouble(locationX);
				}

				String locationY = in.readLine();
				if (locationY.equals("null")) {
				} else {
					Field.balls[i].location.y = Double.parseDouble(locationY);
				}

				String number = in.readLine();
				if (number.equals("null")) {
				} else {
					Field.balls[i].number = Integer.parseInt(number);
				}

				String pointX = in.readLine();
				String pointY = in.readLine();
				if (pointX.equals("null")) {
				} else {
					Field.balls[i].points[Main.set.getValue() - 1] = (new Point(Double.parseDouble(pointX),
							Double.parseDouble(pointY)));
				}

			}
			Field.af.drawFrame();

		} catch (FileNotFoundException e) {
			String message = "File not found.  You have either not copied anything yet, or you deleted the \"copy.txt\" file.";
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();

		}
	}

}

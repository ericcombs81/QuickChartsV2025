package main;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Rotate;

public class Field extends Region {
	public static Ball[] balls = new Ball[150];
	public static double[] gridYCoords = new double[50];
	public static double scale = 10;
	public static double width = 100;
	public static double height = 53.3;
	public double yardLines = 5 * scale;
	public static Canvas canvas = new Canvas(width * scale, height * scale);
	public static GraphicsContext gc = canvas.getGraphicsContext2D();
	public static Long[] durations = new Long[100];
	public static double topHash = (height * scale) / 3;
	public static double bottomHash = ((height * scale) / 3) * 2;
	public static double topHashCollege = .375 * height * scale;
	public static double bottomHashCollege = .625 * height * scale;
	public static double topHashCollegeFudged = (height * scale) / 42 * 16;
	public static double bottomHashCollegeFudged = (height * scale) / 42 * 26;
	public int circleDiameter = 1 * (int) scale;
	public static Boolean grid = true;
	public static Boolean tick = true;
	public static Rectangle border;
	public static AnimationFrame af = new AnimationFrame();
	public Pane fieldPane = new Pane();
	public static String gridStyle = "hsm2";
	public static String hashStyle = "highSchool";
	public static Boolean zeroPointsPresent = true;

	public Field() {
		// Update width and height based on the current currentScale
		double currentScale = FullScreenView.isFullScreen ? FullScreenView.thisScale : scale;
		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.getStyleClass().add("myBackground");

		this.setStyle("-fx-background-color: #c0c0c0;");
		fieldPane.setPrefSize(width * currentScale, height * currentScale);
		fieldPane.setStyle("-fx-background-color: #00FF00;");

		drawField(gc);

		fieldPane.getChildren().addAll(canvas, border);
		this.getChildren().add(fieldPane);
		fieldPane.getChildren().add(af);

		for (int i = 0; i < durations.length; i++) {
			durations[i] = 2000L;
		}

		if (Main.firstField) {
			// This is the code to fetch message.txt from the web and always display it if
			// it exists
			Platform.runLater(() -> {
				try {
					String message = ShowMessage.showMessage();
					if (message != null && !message.isEmpty()) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("QuickCharts Message");
						alert.setHeaderText(null);

						// Create a TextFlow to display the message with hyperlinks
						TextFlow textFlow = new TextFlow();
						textFlow.setPrefWidth(Region.USE_COMPUTED_SIZE);

						// Parse the message to detect and create hyperlinks
						String[] parts = message.split("\\s+");
						for (String part : parts) {
							if (part.startsWith("http://") || part.startsWith("https://")) {
								Hyperlink link = new Hyperlink(part);
								link.setOnAction(event -> openWebpage(part));
								textFlow.getChildren().add(link);
							} else {
								textFlow.getChildren().add(new javafx.scene.text.Text(part + " "));
							}
						}

						// Set the content of the alert dialog to the TextFlow
						alert.getDialogPane().setContent(textFlow);

						// Set the size of the dialog based on the length of the message
						double width = textFlow.prefWidth(-1) + 50; // Add some padding
						double height = textFlow.prefHeight(width);
						alert.getDialogPane().setPrefSize(width, height);

						alert.showAndWait();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			// alert if there is a new version
			Platform.runLater(() -> {

				if (OpeningVersionCheck.doIShowThis()) {
					OpeningVersionCheck ovc = new OpeningVersionCheck();
				}
				;

			});
		}

		State state = new State();
		Frame.urt.undoPush(state);

	}

	private void openWebpage(String url) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					desktop.browse(new URI(url));
				} catch (IOException | URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void setSet(IntegerProperty set) {
		Main.set = set;
		Top.note.setText(Top.notes[Main.set.getValue() - 1]);
		Top.countsLabel.setText("Counts: " + Top.counts[Main.set.getValue() - 1]);
	}

	@SuppressWarnings("static-access")
	public void drawField(GraphicsContext gc) {
		// Update width and height based on the current currentScale
		double currentScale = FullScreenView.isFullScreen ? FullScreenView.thisScale : scale;
		gc.clearRect(0, 0, width * currentScale, height * currentScale);
		double yardLineSpacing = width * currentScale / 20.0;
		double gridSpacing = yardLineSpacing / 4;

		// Grid Lines
		if (grid == true) {
			gc.setStroke(Color.web("#c0c0c0"));
			gc.setLineWidth(0.7 * currentScale / 10);

			// Vertical
			for (double i = 0; i < 20; i++) {
				double x = i * yardLineSpacing;
				for (int j = 1; j < 4; j++) {
					double y = j * gridSpacing;
					gc.strokeLine(x + y, 2, x + y, height * currentScale - 2);
				}
			}

			// Horizontal grid markings

			if (gridStyle.equals("hsm1")) {
				// High School Method #1: Ignore the margin of error, divide all three portions
				// equally

				for (int i = 0; i < gridYCoords.length; i++) {
					gridYCoords[i] = 90000000;
				}

				double evenGridSpacing = (height * currentScale / 3 / 14);

				for (double i = (((height * currentScale) / 3) * 2); i < height * currentScale; i += evenGridSpacing) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
					// Also, record all of the coordinates in the array
					for (int j = 0; j < gridYCoords.length; j++) {
						if (gridYCoords[j] == 90000000) {
							gridYCoords[j] = i;
							break;
						} else {

						}
					}
				}

				for (double i = (((height * currentScale) / 3)); i < (((height * currentScale) / 3)
						* 2); i += evenGridSpacing) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
					// Also, record all of the coordinates in the array
					for (int j = 0; j < gridYCoords.length; j++) {
						if (gridYCoords[j] == 90000000) {
							gridYCoords[j] = i;
							break;
						} else {

						}
					}
				}

				for (double i = 0; i < (((height * currentScale) / 3)); i += evenGridSpacing) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
					// Also, record all of the coordinates in the array
					for (int j = 0; j < gridYCoords.length; j++) {
						if (gridYCoords[j] == 90000000) {
							gridYCoords[j] = i;
							break;
						} else {

						}
					}
				}
				for (int i = 0; i < gridYCoords.length; i++) {
					if (gridYCoords[i] == 90000000) {
						gridYCoords[i] = 0;
					}
				}
				Arrays.sort(gridYCoords);


			} else if (gridStyle.equals("hsm2")) {

				// High School Method #2: Build the Grid From the home hash mark (recommended)

				for (int i = 0; i < gridYCoords.length; i++) {
					gridYCoords[i] = 90000000;
				}

				// Start at home hash and draw 14 forward
				for (double i = ((height * currentScale) / 3) * 2; i < height
						* currentScale; i += (gridSpacing - .25)) {
					gc.strokeLine(2, i, width * currentScale - 2, i);

					// Also, record all of the coordinates in the array
					for (int j = 0; j < gridYCoords.length; j++) {
						if (gridYCoords[j] == 90000000) {
							gridYCoords[j] = i;
							break;
						} else {

						}
					}
				}

				// Start at home hash and draw 28 backwards
				for (double i = ((height * currentScale) / 3) * 2; i > 0; i -= (gridSpacing - .25)) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
					for (int j = 0; j < gridYCoords.length; j++) {
						if (gridYCoords[j] == 90000000) {
							gridYCoords[j] = i;
							break;
						} else {

						}
					}
				}
				for (int i = 0; i < gridYCoords.length; i++) {
					if (gridYCoords[i] == 90000000) {
						gridYCoords[i] = 0;
					}
				}
				Arrays.sort(gridYCoords);


			} else if (gridStyle.equals("hsm3")) {
				// High School Method #3: Superimpose the True Grid Over the Center of the Field
				// Marks (15" gap on both sides)
				// center to bottom

				for (int i = 0; i < gridYCoords.length; i++) {
					gridYCoords[i] = 90000000;
				}

				for (double i = height * currentScale / 2; i < height * currentScale; i += (gridSpacing - .25)) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
					// Also, record all of the coordinates in the array
					for (int j = 0; j < gridYCoords.length; j++) {
						if (gridYCoords[j] == 90000000) {
							gridYCoords[j] = i;
							break;
						} else {

						}
					}
				}

				// center to top
				for (double i = height * currentScale / 2; i > 0; i -= (gridSpacing - .25)) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
					// Also, record all of the coordinates in the array
					for (int j = 0; j < gridYCoords.length; j++) {
						if (gridYCoords[j] == 90000000) {
							gridYCoords[j] = i;
							break;
						} else {

						}
					}
				}
				for (int i = 0; i < gridYCoords.length; i++) {
					if (gridYCoords[i] == 90000000) {
						gridYCoords[i] = 0;
					}
				}
				Arrays.sort(gridYCoords);

			} else if (gridStyle.equals("cm1")) {
				// College Method #1: Ignore the margin of error

				for (int i = 0; i < gridYCoords.length; i++) {
					gridYCoords[i] = 90000000;
				}
				for (double i = 0; i < height * currentScale; i += (height * currentScale / 42)) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
					// Also, record all of the coordinates in the array
					for (int j = 0; j < gridYCoords.length; j++) {
						if (gridYCoords[j] == 90000000) {
							gridYCoords[j] = i;
							break;
						} else {

						}
					}
				}
				for (int i = 0; i < gridYCoords.length; i++) {
					if (gridYCoords[i] == 90000000) {
						gridYCoords[i] = 0;
					}
				}
				Arrays.sort(gridYCoords);

				for (int i = 0; i < gridYCoords.length; i++) {
					System.out.println(gridYCoords[i]);
				}

			} else if (gridStyle.equals("cm2")) {
				// College Method #2: Incorporate the True Measurements on the Grid System
				for (int i = 0; i < gridYCoords.length; i++) {
					gridYCoords[i] = 90000000;
					
				}
				for (double i = height * currentScale; i > 0; i -= gridSpacing) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
					// Also, record all of the coordinates in the array
					for (int j = 0; j < gridYCoords.length; j++) {
						if (gridYCoords[j] == 90000000) {
							gridYCoords[j] = i;
							break;
						} else {

						}
					}
				}
				for (int i = 0; i < gridYCoords.length; i++) {
					if (gridYCoords[i] == 90000000) {
						gridYCoords[i] = 0;
					}
				}
				Arrays.sort(gridYCoords);

				for (int i = 0; i < gridYCoords.length; i++) {
					System.out.println(gridYCoords[i]);
				}

			} else if (gridStyle.equals("version1")) {
				// Version 1
				for (int i = 0; i < gridYCoords.length; i++) {
					gridYCoords[i] = 90000000;
				}
				for (double i = gridSpacing; i < height * currentScale; i += gridSpacing) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
					// Also, record all of the coordinates in the array
					for (int j = 0; j < gridYCoords.length; j++) {
						if (gridYCoords[j] == 90000000) {
							gridYCoords[j] = i;
							break;
						} else {

						}
					}
				}
				for (int i = 0; i < gridYCoords.length; i++) {
					if (gridYCoords[i] == 90000000) {
						gridYCoords[i] = 0;
					}
				}
				Arrays.sort(gridYCoords);
			}
		}

		// zero point grid
		if (zeroPointsPresent) {
			gc.setStroke(Color.web("#909090"));
			gc.setLineWidth(.7 * currentScale / 10);
			if (gridStyle.equals("hsm1")) {
				double evenGridSpacing = (height * currentScale / 3 / 14);

				for (double i = 0; i < height * currentScale; i += evenGridSpacing * 4) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
				}
			} else if (gridStyle.equals("hsm2")) {
				// Start at home hash and draw 14 forward
				for (double i = ((height * currentScale) / 3) * 2; i < height * currentScale; i += (gridSpacing - .25)
						* 4) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
				}

				// Start at home hash and draw 28 backwards
				for (double i = ((height * currentScale) / 3) * 2; i > 0; i -= (gridSpacing - .25) * 4) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
				}

			} else if (gridStyle.equals("hsm3")) {
				// center to bottom
				for (double i = width * currentScale / 2; i < width * currentScale; i += (gridSpacing - .25) * 4) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
				}

				// center to top
				for (double i = width * currentScale / 2; i > 0; i -= (gridSpacing - .25) * 4) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
				}

			} else if (gridStyle.equals("cm1")) {
				// College Method #1: Ignore the margin of error
				for (double i = height * currentScale; i > 0; i -= (height * currentScale / 42) * 4) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
				}

			} else if (gridStyle.equals("cm2")) {
				// College Method #2: Incorporate the True Measurements on the Grid System
				for (double i = height * currentScale; i > 0; i -= gridSpacing * 4) {
					gc.strokeLine(2, i, width * currentScale - 2, i);
				}

			} else if (gridStyle.equals("version1")) {
				// Version 1
				for (double i = gridSpacing; i < height * currentScale; i += gridSpacing * 4) {
					gc.strokeLine(2, i, width * currentScale - 2, i);

				}
			}
		}

		// Yard Lines
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(2.9 * currentScale / 10);
		for (int i = 1; i < 20; i++) {
			double x = i * yardLineSpacing;
			gc.strokeLine(x, 2, x, height * currentScale - 2);
		}

		// Hash Marks / Tick Marks (High School)
		if (hashStyle.equals("highSchool")) {

			// Tick Marks
			if (tick == true) {
				gc.setStroke(Color.WHITE);
				gc.setLineWidth(1.5 * currentScale / 10);

				double tickSpacing = gridSpacing / 2;
				for (double i = 0; i < 20; i++) {
					double x = i * yardLineSpacing;
					for (int j = 1; j < 8; j++) {
						double y = j * tickSpacing;
						gc.strokeLine(x + y, topHash + 1 * currentScale, x + y, topHash - 1 * currentScale);
						gc.strokeLine(x + y, bottomHash + 1 * currentScale, x + y, bottomHash - 1 * currentScale);
					}
				}
			}

			// Hash Marks
			gc.setStroke(Color.WHITE);
			gc.setLineWidth(2.5 * currentScale / 10);

			for (double i = 1; i < 20; i++) {
				double x = i * yardLineSpacing;
				gc.strokeLine(x - currentScale, topHash, x + currentScale, topHash);
				gc.strokeLine(x - currentScale, bottomHash, x + currentScale, bottomHash);
			}

			// Hash Marks / Tick Marks - College
		} else if (hashStyle.equals("college")) {
			// Tick Marks
			if (tick == true) {
				gc.setStroke(Color.WHITE);
				gc.setLineWidth(1.5 * currentScale / 10);

				double tickSpacing = gridSpacing / 2;
				for (double i = 0; i < 20; i++) {
					double x = i * yardLineSpacing;
					for (int j = 1; j < 8; j++) {
						double y = j * tickSpacing;
						gc.strokeLine(x + y, topHashCollege + 1 * currentScale, x + y,
								topHashCollege - 1 * currentScale);
						gc.strokeLine(x + y, bottomHashCollege + 1 * currentScale, x + y,
								bottomHashCollege - 1 * currentScale);
					}
				}
			}

			// Hash Marks
			gc.setStroke(Color.WHITE);
			gc.setLineWidth(2.5 * currentScale / 10);

			for (double i = 1; i < 20; i++) {
				double x = i * yardLineSpacing;
				gc.strokeLine(x - currentScale, topHashCollege, x + currentScale, topHashCollege);
				gc.strokeLine(x - currentScale, bottomHashCollege, x + currentScale, bottomHashCollege);
			}
			// Hash Marks / Tick Marks - College Fudged
		} else if (hashStyle.equals("collegeFudged")) {
			// Tick Marks
			if (tick == true) {
				gc.setStroke(Color.WHITE);
				gc.setLineWidth(1.5 * currentScale / 10);

				double tickSpacing = gridSpacing / 2;
				for (double i = 0; i < 20; i++) {
					double x = i * yardLineSpacing;
					for (int j = 1; j < 8; j++) {
						double y = j * tickSpacing;
						gc.strokeLine(x + y, topHashCollegeFudged + 1 * currentScale, x + y,
								topHashCollegeFudged - 1 * currentScale);
						gc.strokeLine(x + y, bottomHashCollegeFudged + 1 * currentScale, x + y,
								bottomHashCollegeFudged - 1 * currentScale);
					}
				}
			}

			// Hash Marks
			gc.setStroke(Color.WHITE);
			gc.setLineWidth(2.5 * currentScale / 10);

			for (double i = 1; i < 20; i++) {
				double x = i * yardLineSpacing;
				gc.strokeLine(x - currentScale, topHashCollegeFudged, x + currentScale, topHashCollegeFudged);
				gc.strokeLine(x - currentScale, bottomHashCollegeFudged, x + currentScale, bottomHashCollegeFudged);
			}
		}

		if (border == null) {
			border = new Rectangle();
			border.setFill(null);
			border.setStroke(Color.BLACK);
			border.setStrokeWidth(2 * currentScale / 10);
		}

		border.setWidth(width * currentScale);
		border.setHeight(height * currentScale);

		// Draw the Yard Numbers
		Font f = Font.font("Dialog", FontWeight.BOLD, 28 * currentScale / 10);
		gc.setStroke(Color.WHITE);
		gc.setFill(Color.WHITE);
		gc.setFont(f);

		gc.fillText("50", (double) (width * currentScale) / 2 - 16 * currentScale / 10,
				(double) (currentScale * height) - 3 * (double) currentScale);
		gc.fillText("40", (double) (width * currentScale) / 2 - 15 * currentScale / 10 + 2 * (double) yardLineSpacing,
				(double) (height * currentScale) - 3 * (double) currentScale);
		gc.fillText("30", (double) (width * currentScale) / 2 - 15 * currentScale / 10 + 4 * (double) yardLineSpacing,
				(double) (height * currentScale) - 3 * (double) currentScale);
		gc.fillText("20", (double) (width * currentScale) / 2 - 15 * currentScale / 10 + 6 * (double) yardLineSpacing,
				(double) (height * currentScale) - 3 * (double) currentScale);
		gc.fillText("10", (double) (width * currentScale) / 2 - 15 * currentScale / 10 + 8 * (double) yardLineSpacing,
				(double) (height * currentScale) - 3 * (double) currentScale);

		gc.fillText("40", (double) (width * currentScale) / 2 - 14 * currentScale / 10 - 2 * (double) yardLineSpacing,
				(double) (height * currentScale) - 3 * (double) currentScale);
		gc.fillText("30", (double) (width * currentScale) / 2 - 14 * currentScale / 10 - 4 * (double) yardLineSpacing,
				(double) (height * currentScale) - 3 * (double) currentScale);
		gc.fillText("20", (double) (width * currentScale) / 2 - 14 * currentScale / 10 - 6 * (double) yardLineSpacing,
				(double) (height * currentScale) - 3 * (double) currentScale);
		gc.fillText("10", (double) (width * currentScale) / 2 - 14 * currentScale / 10 - 8 * (double) yardLineSpacing,
				(double) (height * currentScale) - 3 * (double) currentScale);

		gc.save();
		Rotate rotate = new Rotate(180, (double) (width * currentScale) / 2 + 16 * currentScale / 10,
				3.7 * (double) currentScale);
		gc.setTransform(rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(), rotate.getTx(),
				rotate.getTy());
		gc.fillText("50", (double) (width * currentScale) / 2 + 16 * currentScale / 10, 3.7 * (double) currentScale);

		Rotate rotate1 = new Rotate(180,
				(double) (width * currentScale) / 2 + 16 * currentScale / 10 + 2 * (double) yardLineSpacing,
				3.7 * (double) currentScale);
		gc.setTransform(rotate1.getMxx(), rotate1.getMyx(), rotate1.getMxy(), rotate1.getMyy(), rotate1.getTx(),
				rotate1.getTy());
		gc.fillText("40", (double) (width * currentScale) / 2 + 16 * currentScale / 10 + 2 * (double) yardLineSpacing,
				3.7 * (double) currentScale);

		Rotate rotate2 = new Rotate(180,
				(double) (width * currentScale) / 2 + 16 * currentScale / 10 + 4 * (double) yardLineSpacing,
				3.7 * (double) currentScale);
		gc.setTransform(rotate2.getMxx(), rotate2.getMyx(), rotate2.getMxy(), rotate2.getMyy(), rotate2.getTx(),
				rotate2.getTy());
		gc.fillText("30", (double) (width * currentScale) / 2 + 16 * currentScale / 10 + 4 * (double) yardLineSpacing,
				3.7 * (double) currentScale);

		Rotate rotate3 = new Rotate(180,
				(double) (width * currentScale) / 2 + 16 * currentScale / 10 + 6 * (double) yardLineSpacing,
				3.7 * (double) currentScale);
		gc.setTransform(rotate3.getMxx(), rotate3.getMyx(), rotate3.getMxy(), rotate3.getMyy(), rotate3.getTx(),
				rotate3.getTy());
		gc.fillText("20", (double) (width * currentScale) / 2 + 16 * currentScale / 10 + 6 * (double) yardLineSpacing,
				3.7 * (double) currentScale);

		Rotate rotate4 = new Rotate(180,
				(double) (width * currentScale) / 2 + 16 * currentScale / 10 + 8 * (double) yardLineSpacing,
				3.7 * (double) currentScale);
		gc.setTransform(rotate4.getMxx(), rotate4.getMyx(), rotate4.getMxy(), rotate4.getMyy(), rotate4.getTx(),
				rotate4.getTy());
		gc.fillText("10", (double) (width * currentScale) / 2 + 16 * currentScale / 10 + 8 * (double) yardLineSpacing,
				3.7 * (double) currentScale);

		Rotate rotate5 = new Rotate(180,
				(double) (width * currentScale) / 2 + 16 * currentScale / 10 - 2 * (double) yardLineSpacing,
				3.7 * (double) currentScale);
		gc.setTransform(rotate5.getMxx(), rotate5.getMyx(), rotate5.getMxy(), rotate5.getMyy(), rotate5.getTx(),
				rotate5.getTy());
		gc.fillText("40", (double) (width * currentScale) / 2 + 16 * currentScale / 10 - 2 * (double) yardLineSpacing,
				3.7 * (double) currentScale);

		Rotate rotate6 = new Rotate(180,
				(double) (width * currentScale) / 2 + 16 * currentScale / 10 - 4 * (double) yardLineSpacing,
				3.7 * (double) currentScale);
		gc.setTransform(rotate6.getMxx(), rotate6.getMyx(), rotate6.getMxy(), rotate6.getMyy(), rotate6.getTx(),
				rotate6.getTy());
		gc.fillText("30", (double) (width * currentScale) / 2 + 16 * currentScale / 10 - 4 * (double) yardLineSpacing,
				3.7 * (double) currentScale);

		Rotate rotate7 = new Rotate(180,
				(double) (width * currentScale) / 2 + 16 * currentScale / 10 - 6 * (double) yardLineSpacing,
				3.7 * (double) currentScale);
		gc.setTransform(rotate7.getMxx(), rotate7.getMyx(), rotate7.getMxy(), rotate7.getMyy(), rotate7.getTx(),
				rotate7.getTy());
		gc.fillText("20", (double) (width * currentScale) / 2 + 16 * currentScale / 10 - 6 * (double) yardLineSpacing,
				3.7 * (double) currentScale);

		Rotate rotate8 = new Rotate(180,
				(double) (width * currentScale) / 2 + 16 * currentScale / 10 - 8 * (double) yardLineSpacing,
				3.7 * (double) currentScale);
		gc.setTransform(rotate8.getMxx(), rotate8.getMyx(), rotate8.getMxy(), rotate8.getMyy(), rotate8.getTx(),
				rotate8.getTy());
		gc.fillText("10", (double) (width * currentScale) / 2 + 16 * currentScale / 10 - 8 * (double) yardLineSpacing,
				3.7 * (double) currentScale);
		gc.restore();

		if (Field.af.linePresentOnField == true) {

			gc.setStroke(Color.BLACK);
			if (Field.af.linePoint2 == null) {
				gc.setFill(Color.BLACK);
				gc.fillOval(Field.af.linePoint1.getX(), Field.af.linePoint1.getY(), 5, 5);
			} else {
				gc.strokeLine(Field.af.linePoint1.getX(), Field.af.linePoint1.getY(), Field.af.linePoint2.getX(),
						Field.af.linePoint2.getY());
			}
		}
	}

	public void redrawField() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.setFill(Color.TRANSPARENT);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		drawField(gc);
	}

	public GraphicsContext getGC() {
		return gc;
	}

	public void reSizeField(double newcurrentScale) {
		this.scale = newcurrentScale;
		this.yardLines = yardLines * newcurrentScale;
		this.canvas.setWidth(this.width * newcurrentScale);
		this.canvas.setHeight(this.height * newcurrentScale);
		this.topHash = (this.height * newcurrentScale) / 3;
		this.bottomHash = ((this.height * newcurrentScale) / 3) * 2;
		this.topHashCollege = .375 * height * newcurrentScale;
		this.bottomHashCollege = .625 * height * newcurrentScale;
		this.topHashCollegeFudged = (height * newcurrentScale) / 42 * 16;
		this.bottomHashCollegeFudged = (height * newcurrentScale) / 42 * 26;
		this.circleDiameter = 1 * (int) newcurrentScale;

		this.fieldPane.setPrefSize(this.width * newcurrentScale, this.height * newcurrentScale);
		this.border.setWidth(width * newcurrentScale);
		this.border.setHeight(height * newcurrentScale);
		af.resizeAnimationFrame(newcurrentScale);

	}
}

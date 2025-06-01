package main;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;


public class PrintableScreen extends Pane {

	public int startSet;
	public int endSet;
	public static Canvas canvas = new Canvas(Field.width * Field.scale, Field.height * Field.scale);
	public static GraphicsContext gc = canvas.getGraphicsContext2D();
	public static Rectangle border;
	public static PrintableAnimationFrame af;
	public static Label notes;
	public static Label count;
	public static Label set;
	public static Scene sceney;
	public static Stage stage;

	PrintableScreen() {
		
		
		VBox inMargin = new VBox();

		Font font1 = Font.font("Arial", FontWeight.BOLD, 20);
		Font font2 = Font.font("Arial", FontWeight.BOLD, 11);
		notes = new Label();
		notes.setPadding(new Insets(0, 0, 20, 0));
		notes.setFont(font2);
		notes.setWrapText(true);
		notes.setPrefWidth(Field.width * Field.scale);
		notes.setText(Top.notes[Main.set.getValue() - 1]);
		Pane fieldPane = new Pane();

		inMargin.setStyle("-fx-background-color:white;");
		fieldPane.setPrefSize(Field.width * Field.scale, Field.height * Field.scale);
		fieldPane.setStyle("-fx-background-color: white;");
		inMargin.setPadding(new Insets(0, 30, 30, 30));

		HBox setCount = new HBox();
		setCount.setSpacing(20);
		setCount.setPadding(new Insets(10, 0, 0, 0));
		set = new Label();
		set.setFont(font1);
		set.setText("Set: " + Main.set.getValue());
		count = new Label();
		count.setFont(font1);
		count.setText("Counts: " + Top.counts[Main.set.getValue() - 1]);
		setCount.getChildren().addAll(set, count);

		drawField(gc);

		af = new PrintableAnimationFrame();

		fieldPane.getChildren().addAll(canvas, border, af);

		inMargin.getChildren().addAll(notes, fieldPane, setCount);
		this.getChildren().add(inMargin);
		// PRINTED PAGE MARGINS
		inMargin.setPadding(new Insets(50, 50, 50, 50));

		sceney = new Scene(this);
		stage = new Stage();
		stage.setOnCloseRequest(e -> closeScene());
		stage.initOwner(Main.primary);
		stage.setTitle("Printable Screen");
		stage.setScene(sceney);
		stage.setResizable(false);
		stage.show();
	}

	public void drawField(GraphicsContext gc) {
		
		gc.clearRect(0,  0,  Field.width*Field.scale, Field.width*Field.scale);

		double yardLineSpacing = Field.width * Field.scale / 20.0;
		double gridSpacing = yardLineSpacing / 4;

		// Grid Lines
		if (Field.grid == true) {
			gc.setStroke(Color.web("#c0c0c0"));
			gc.setLineWidth(0.7);

			// Vertical
			for (double i = 0; i < 20; i++) {
				double x = i * yardLineSpacing;
				for (int j = 1; j < 4; j++) {
					double y = j * gridSpacing;
					gc.strokeLine(x + y, 2, x + y, Field.height * Field.scale - 2);
				}
			}
			
			// Horizontal grid markings

			if (Field.gridStyle.equals("hsm1")) {
				// High School Method #1: Ignore the margin of error, divide all three portions
				// equally

				double evenGridSpacing = (Field.height * Field.scale / 3 / 14);

				for (double i = (((Field.height * Field.scale) / 3) * 2); i < Field.height * Field.scale; i += evenGridSpacing) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

				for (double i = (((Field.height * Field.scale) / 3)); i < (((Field.height * Field.scale) / 3) * 2); i += evenGridSpacing) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

				for (double i = 0; i < (((Field.height * Field.scale) / 3)); i += evenGridSpacing) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

			} else if (Field.gridStyle.equals("hsm2")) {

				// High School Method #2: Build the Grid From the home hash mark (recommended)
				// Start at home hash and draw 14 forward
				for (double i = ((Field.height * Field.scale) / 3) * 2; i < Field.height * Field.scale; i += (gridSpacing - .25)) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

				// Start at home hash and draw 28 backwards
				for (double i = ((Field.height * Field.scale) / 3) * 2; i > 0; i -= (gridSpacing - .25)) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

			} else if (Field.gridStyle.equals("hsm3")) {
				// High School Method #3: Superimpose the True Grid Over the Center of the Field
				// Marks (15" gap on both sides)
				// center to bottom
				for (double i = Field.width * Field.scale / 2; i < Field.width * Field.scale; i += (gridSpacing - .25)) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

				// center to top
				for (double i = Field.width * Field.scale / 2; i > 0; i -= (gridSpacing - .25)) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

			} else if (Field.gridStyle.equals("cm1")) {
				// College Method #1: Ignore the margin of error
				for (double i = 0; i < Field.height * Field.scale; i += (Field.height * Field.scale / 42)) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

			} else if (Field.gridStyle.equals("cm2")) {
				// College Method #2: Incorporate the True Measurements on the Grid System
				for (double i = Field.height * Field.scale; i > 0; i -= gridSpacing) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

			} else if (Field.gridStyle.equals("version1")) {
				// Version 1
				for (double i = gridSpacing; i < Field.height * Field.scale; i += gridSpacing) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);

				}
			}
		}

		// zero point grid
		if (Field.zeroPointsPresent) {
			gc.setStroke(Color.web("#000000"));
			gc.setLineWidth(.7);
			if (Field.gridStyle.equals("hsm1")) {
				double evenGridSpacing = (Field.height * Field.scale / 3 / 14);

				for (double i = 0; i < Field.height * Field.scale; i += evenGridSpacing * 4) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}
			} else if (Field.gridStyle.equals("hsm2")) {
				// Start at home hash and draw 14 forward
				for (double i = ((Field.height * Field.scale) / 3) * 2; i < Field.height * Field.scale; i += (gridSpacing - .25) * 4) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

				// Start at home hash and draw 28 backwards
				for (double i = ((Field.height * Field.scale) / 3) * 2; i > 0; i -= (gridSpacing - .25) * 4) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

			} else if (Field.gridStyle.equals("hsm3")) {
				// center to bottom
				for (double i = Field.height * Field.scale / 2; i < Field.height * Field.scale; i += (gridSpacing - .25) * 4) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

				// center to top
				for (double i = Field.height * Field.scale / 2; i > 0; i -= (gridSpacing - .25) * 4) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

			} else if (Field.gridStyle.equals("cm1")) {
				// College Method #1: Ignore the margin of error
				for (double i = Field.height * Field.scale; i > 0; i -= (Field.height * Field.scale / 42) * 4) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

			} else if (Field.gridStyle.equals("cm2")) {
				// College Method #2: Incorporate the True Measurements on the Grid System
				for (double i = Field.height * Field.scale; i > 0; i -= gridSpacing * 4) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);
				}

			} else if (Field.gridStyle.equals("version1")) {
				// Version 1
				for (double i = gridSpacing; i < Field.height * Field.scale; i += gridSpacing * 4) {
					gc.strokeLine(2, i, Field.width * Field.scale - 2, i);

				}
			}
		}

		// Yard Lines
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2.0);
		for (int i = 1; i < 20; i++) {
			double x = i * yardLineSpacing;
			gc.strokeLine(x, 2, x, Field.height * Field.scale - 2);
		}
		
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2.5);

		//Hash Marks (High School)
		if (Field.hashStyle.equals("highSchool")) {

			for (double i = 1; i < 20; i++) {
				double x = i * yardLineSpacing;
				gc.strokeLine(x - Field.scale, Field.topHash, x + Field.scale, Field.topHash);
				gc.strokeLine(x - Field.scale, Field.bottomHash, x + Field.scale, Field.bottomHash);
			}
			
		//Hash Marks - College	
		} else if (Field.hashStyle.equals("college")) {

			for (double i = 1; i < 20; i++) {
				double x = i * yardLineSpacing;
				gc.strokeLine(x - Field.scale, Field.topHashCollege, x + Field.scale, Field.topHashCollege);
				gc.strokeLine(x - Field.scale, Field.bottomHashCollege, x + Field.scale, Field.bottomHashCollege);
			}
		// Hash Marks - College Fudged	
		} else if (Field.hashStyle.equals("collegeFudged")) {

			for (double i = 1; i < 20; i++) {
				double x = i * yardLineSpacing;
				gc.strokeLine(x - Field.scale, Field.topHashCollegeFudged, x + Field.scale, Field.topHashCollegeFudged);
				gc.strokeLine(x - Field.scale, Field.bottomHashCollegeFudged, x + Field.scale, Field.bottomHashCollegeFudged);
			}
		}

		border = new Rectangle();
		border.setWidth(Field.width * Field.scale);
		border.setHeight(Field.height * Field.scale);
		border.setFill(null);
		border.setStroke(Color.BLACK);
		border.setStrokeWidth(2);

		// Draw the Yard Numbers
		Font f = Font.font("Dialog", FontWeight.BOLD, 28);
		gc.setStroke(Color.BLACK);
		gc.setFill(Color.BLACK);
		gc.setFont(f);

		gc.fillText("50", (double) (Field.width * Field.scale) / 2 - 16,
				(double) (Field.scale * Field.height) - 3 * (double) Field.scale);
		gc.fillText("40", (double) (Field.width * Field.scale) / 2 - 15 + 2 * (double) yardLineSpacing,
				(double) (Field.height * Field.scale) - 3 * (double) Field.scale);
		gc.fillText("30", (double) (Field.width * Field.scale) / 2 - 15 + 4 * (double) yardLineSpacing,
				(double) (Field.height * Field.scale) - 3 * (double) Field.scale);
		gc.fillText("20", (double) (Field.width * Field.scale) / 2 - 15 + 6 * (double) yardLineSpacing,
				(double) (Field.height * Field.scale) - 3 * (double) Field.scale);
		gc.fillText("10", (double) (Field.width * Field.scale) / 2 - 15 + 8 * (double) yardLineSpacing,
				(double) (Field.height * Field.scale) - 3 * (double) Field.scale);

		gc.fillText("40", (double) (Field.width * Field.scale) / 2 - 14 - 2 * (double) yardLineSpacing,
				(double) (Field.height * Field.scale) - 3 * (double) Field.scale);
		gc.fillText("30", (double) (Field.width * Field.scale) / 2 - 14 - 4 * (double) yardLineSpacing,
				(double) (Field.height * Field.scale) - 3 * (double) Field.scale);
		gc.fillText("20", (double) (Field.width * Field.scale) / 2 - 14 - 6 * (double) yardLineSpacing,
				(double) (Field.height * Field.scale) - 3 * (double) Field.scale);
		gc.fillText("10", (double) (Field.width * Field.scale) / 2 - 14 - 8 * (double) yardLineSpacing,
				(double) (Field.height * Field.scale) - 3 * (double) Field.scale);

		gc.save();
		Rotate rotate = new Rotate(180, (double) (Field.width * Field.scale) / 2 + 16, 3.7 * (double) Field.scale);
		gc.setTransform(rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(), rotate.getTx(),
				rotate.getTy());
		gc.fillText("50", (double) (Field.width * Field.scale) / 2 + 16, 3.7 * (double) Field.scale);

		Rotate rotate1 = new Rotate(180,
				(double) (Field.width * Field.scale) / 2 + 16 + 2 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);
		gc.setTransform(rotate1.getMxx(), rotate1.getMyx(), rotate1.getMxy(), rotate1.getMyy(), rotate1.getTx(),
				rotate1.getTy());
		gc.fillText("40", (double) (Field.width * Field.scale) / 2 + 16 + 2 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);

		Rotate rotate2 = new Rotate(180,
				(double) (Field.width * Field.scale) / 2 + 16 + 4 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);
		gc.setTransform(rotate2.getMxx(), rotate2.getMyx(), rotate2.getMxy(), rotate2.getMyy(), rotate2.getTx(),
				rotate2.getTy());
		gc.fillText("30", (double) (Field.width * Field.scale) / 2 + 16 + 4 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);

		Rotate rotate3 = new Rotate(180,
				(double) (Field.width * Field.scale) / 2 + 16 + 6 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);
		gc.setTransform(rotate3.getMxx(), rotate3.getMyx(), rotate3.getMxy(), rotate3.getMyy(), rotate3.getTx(),
				rotate3.getTy());
		gc.fillText("20", (double) (Field.width * Field.scale) / 2 + 16 + 6 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);

		Rotate rotate4 = new Rotate(180,
				(double) (Field.width * Field.scale) / 2 + 16 + 8 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);
		gc.setTransform(rotate4.getMxx(), rotate4.getMyx(), rotate4.getMxy(), rotate4.getMyy(), rotate4.getTx(),
				rotate4.getTy());
		gc.fillText("10", (double) (Field.width * Field.scale) / 2 + 16 + 8 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);

		Rotate rotate5 = new Rotate(180,
				(double) (Field.width * Field.scale) / 2 + 16 - 2 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);
		gc.setTransform(rotate5.getMxx(), rotate5.getMyx(), rotate5.getMxy(), rotate5.getMyy(), rotate5.getTx(),
				rotate5.getTy());
		gc.fillText("40", (double) (Field.width * Field.scale) / 2 + 16 - 2 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);

		Rotate rotate6 = new Rotate(180,
				(double) (Field.width * Field.scale) / 2 + 16 - 4 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);
		gc.setTransform(rotate6.getMxx(), rotate6.getMyx(), rotate6.getMxy(), rotate6.getMyy(), rotate6.getTx(),
				rotate6.getTy());
		gc.fillText("30", (double) (Field.width * Field.scale) / 2 + 16 - 4 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);

		Rotate rotate7 = new Rotate(180,
				(double) (Field.width * Field.scale) / 2 + 16 - 6 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);
		gc.setTransform(rotate7.getMxx(), rotate7.getMyx(), rotate7.getMxy(), rotate7.getMyy(), rotate7.getTx(),
				rotate7.getTy());
		gc.fillText("20", (double) (Field.width * Field.scale) / 2 + 16 - 6 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);

		Rotate rotate8 = new Rotate(180,
				(double) (Field.width * Field.scale) / 2 + 16 - 8 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);
		gc.setTransform(rotate8.getMxx(), rotate8.getMyx(), rotate8.getMxy(), rotate8.getMyy(), rotate8.getTx(),
				rotate8.getTy());
		gc.fillText("10", (double) (Field.width * Field.scale) / 2 + 16 - 8 * (double) yardLineSpacing,
				3.7 * (double) Field.scale);
		gc.restore();

	}

	public void closeScene() {
		stage.setScene(null);
		stage.close();
	}
}


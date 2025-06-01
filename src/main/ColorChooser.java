package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class ColorChooser extends VBox {

	public static Boolean colorChooserOpen = false;
	public Button chosenButton;
	public Integer selected = -1;

	ColorChooser() {
		colorChooserOpen = true;

		// No balls will be selected during the color choosing. This stores the # of the
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
		Field.af.drawBalls();

		Font font = new Font("Times New Roman", 14);
		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.getStyleClass().add("myBackground");
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setAlignment(Pos.CENTER);
		this.setSpacing(10);

		Label instructions = new Label(
				"Choose a color.  Click existing points to change their color, or add new points.");
		instructions.setFont(font);
		instructions.setStyle("-fx-font-weight: bold");
		instructions.setPadding(new Insets(0, 0, 5, 0));

		GridPane colorGrid = new GridPane();
		colorGrid.setAlignment(Pos.CENTER);
		colorGrid.setHgap(15);
		colorGrid.setVgap(15);

		colorGrid.setStyle("-fx-border-color: black; -fx-border-width: 5px");
		colorGrid.setPadding(new Insets(10, 10, 10, 10));
		Button btn1 = new Button("Color 1 ");
		btn1.getStyleClass().add("myColorButton");
		Button btn2 = new Button("Color 2 ");
		btn2.getStyleClass().add("myColorButton");
		Button btn3 = new Button("Color 3 ");
		btn3.getStyleClass().add("myColorButton");
		Button btn4 = new Button("Color 4 ");
		btn4.getStyleClass().add("myColorButton");
		Button btn5 = new Button("Color 5 ");
		btn5.getStyleClass().add("myColorButton");
		Button btn6 = new Button("Color 6 ");
		btn6.getStyleClass().add("myColorButton");
		Button btn7 = new Button("Color 7 ");
		btn7.getStyleClass().add("myColorButton");
		Button btn8 = new Button("Color 8 ");
		btn8.getStyleClass().add("myColorButton");
		Button btn9 = new Button("Color 9 ");
		btn9.getStyleClass().add("myColorButton");
		Button btn10 = new Button("Color 10 ");
		btn10.getStyleClass().add("myColorButton");
		Button btn11 = new Button("Color 11");
		btn11.getStyleClass().add("myColorButton");
		Button btn12 = new Button("Color 12");
		btn12.getStyleClass().add("myColorButton");
		btn1.setOnAction(event -> {
			chosenButton.setStyle("-fx-background-color: #F0F8FF");
		});
		btn2.setOnAction(event -> {
			chosenButton.setStyle("-fx-background-color: #F0F8FF");
		});
		btn3.setOnAction(event -> {
			chosenButton.setStyle("-fx-background-color: #00DECC");
		});
		btn4.setOnAction(event -> {
			chosenButton.setStyle("-fx-background-color: #5DDDF9");
		});
		btn5.setOnAction(event -> {
			chosenButton.setStyle("-fx-background-color: #FFB6C1");
		});
		btn6.setOnAction(event -> {
			chosenButton.setStyle("-fx-background-color: #0000FF");
		});
		btn7.setOnAction(event -> {
			chosenButton.setStyle("-fx-background-color: #8A2BE2");
		});
		btn8.setOnAction(event -> {
			chosenButton.setStyle("-fx-background-color: #2E8B57");
		});
		btn9.setOnAction(event -> {
			chosenButton.setStyle("-fx-background-color: #DA70D6");
		});
		btn10.setOnAction(event -> {
			chosenButton.setStyle("-fx-background-color: #FA8072");
		});
		btn11.setOnAction(event -> {
			chosenButton.setStyle("-fx-background-color: red");
		});
		btn12.setOnAction(event -> {
			chosenButton.setStyle("-fx-background-color: #FFD700");
		});

		btn1.setStyle("-fx-font-weight: bold; -fx-background-color: #F0F8FF"); // Alice Blue
		btn2.setStyle("-fx-font-weight: bold; -fx-background-color: #FAEBD7"); // Antique white
		btn3.setStyle("-fx-font-weight: bold; -fx-background-color: #00DECC"); // Aqua
		btn4.setStyle("-fx-font-weight: bold; -fx-background-color: #5DDDF9"); // Aquamarine
		btn5.setStyle("-fx-font-weight: bold; -fx-background-color: #FFB6C1"); // LightPink
		btn6.setStyle("-fx-font-weight: bold; -fx-background-color: #0000FF"); // Blue
		btn7.setStyle("-fx-font-weight: bold; -fx-background-color: #8A2BE2"); // Blue Violet
		btn8.setStyle("-fx-font-weight: bold; -fx-background-color: #2E8B57"); // SeaGreen
		btn9.setStyle("-fx-font-weight: bold; -fx-background-color: #DA70D6"); // Orchid
		btn10.setStyle("-fx-font-weight: bold; -fx-background-color: #FA8072"); // Salmon
		btn11.setStyle("-fx-font-weight: bold; -fx-background-color: red"); // red
		btn12.setStyle("-fx-font-weight: bold; -fx-background-color: #FFD700"); // Gold

		colorGrid.add(btn1, 0, 0);
		colorGrid.add(btn2, 1, 0);
		colorGrid.add(btn3, 2, 0);
		colorGrid.add(btn4, 3, 0);
		colorGrid.add(btn5, 0, 1);
		colorGrid.add(btn6, 1, 1);
		colorGrid.add(btn7, 2, 1);
		colorGrid.add(btn8, 3, 1);
		colorGrid.add(btn9, 0, 2);
		colorGrid.add(btn10, 1, 2);
		colorGrid.add(btn11, 2, 2);
		colorGrid.add(btn12, 3, 2);

		Label or = new Label("Or pick your own: ");
		or.setFont(font);
		or.setStyle("-fx-font-weight: bold");
		ColorPicker colorPicker = new ColorPicker();
		colorPicker.setOnAction(event -> {
			Color chosenColor = colorPicker.getValue();
			String hexColor = toHexString(chosenColor);
			chosenButton.setStyle("-fx-background-color: " + hexColor + ";");

		});
		chosenButton = new Button("Chosen Color");
		chosenButton.setStyle("-fx-background-color: red");

		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setSpacing(10);
		hbox.getChildren().addAll(or, colorPicker, chosenButton);

		this.getChildren().addAll(instructions, colorGrid, hbox);

		Scene ccScene = new Scene(this);
		Stage ccStage = new Stage();
		ccStage.initOwner(Main.primary);
		ccStage.setTitle("Choose Colors");
		ccStage.setOnCloseRequest(event -> {
			colorChooserOpen = false;
			if (selected != -1) {
				Field.balls[selected].isSelected = true;
			}
			Field.af.drawBalls();

		});
		ccStage.setScene(ccScene);
		ccStage.setResizable(false);
		ccStage.show();
	}

	private String toHexString(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}
}


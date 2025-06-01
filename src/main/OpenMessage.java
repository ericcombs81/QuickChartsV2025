package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class OpenMessage extends VBox {

	public Font largeFont = new Font("Dialog", 14);
	public Boolean showAgain = true;

	public OpenMessage() {

		Text text = new Text(
				"Welcome to QuickCharts!  Please view the \"Getting Started\" video in the help menu.  I hope you enjoy using it as much as I have enjoyed creating it! - Eric Combs");
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefWidth(200);
		flowPane.setStyle("-fx-alignment: justify;");
		flowPane.getChildren().add(text);

		this.setSpacing(10);
		this.setAlignment(Pos.CENTER);
		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.getStyleClass().add("myBackground");
		this.setPrefSize(300, 200);
		text.setStyle("-fx-font-weight: bold; -fx-alignment: justify;");
		text.setFont(largeFont);
		text.setWrappingWidth(290);
		CheckBox checkBox = new CheckBox("Don't show this again.");
		checkBox.setOnAction(event -> {
			if (checkBox.isSelected()) {
				try {
					doNotShowAgain();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

			}
		});
		Button gotIt = new Button("Got it!");
		gotIt.getStyleClass().add("topButton");
		gotIt.setOnAction(event -> {
			Stage stage = (Stage) gotIt.getScene().getWindow();
			if (!showAgain) {
				try {
					doNotShowAgain();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			stage.close();
		});

		this.getChildren().addAll(text, checkBox, gotIt);
		Scene scene = new Scene(this);
		Stage stage = new Stage();
		stage.initOwner(Main.primary);
		stage.setTitle("Welcome!");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public void doNotShowAgain() throws IOException {
		String homeDirectory = System.getProperty("user.home");
		BufferedWriter out = new BufferedWriter(
				new FileWriter(new File(homeDirectory + "\\.QuickChartsConfig\\loadConfig.txt")));
		out.write("Do Not Show Again");
		out.close();
	}
}
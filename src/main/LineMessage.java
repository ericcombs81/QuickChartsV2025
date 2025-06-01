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


public class LineMessage extends VBox {

	public Font largeFont = new Font("Dialog", 12);
	public Boolean showAgain = true;

	public LineMessage() {

		Text text = new Text(
				"Making a reference line: click a point on the field where you want the line to begin.  Then click a second point on the field where you want the line to end.  A line will appear.  It is just for reference.  Then, move any points onto it that you want.  When you are satisfied, click on the \"Erase Line\" button that will appear on the right.");
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefWidth(300);
		flowPane.setStyle("-fx-alignment: justify;");
		flowPane.getChildren().add(text);

		this.setSpacing(10);
		this.setAlignment(Pos.CENTER);
		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.getStyleClass().add("myBackground");
		this.setPrefSize(350, 200);
		text.setStyle("-fx-font-weight: bold; -fx-alignment: justify;");
		text.setFont(largeFont);
		text.setWrappingWidth(290);
		CheckBox checkBox = new CheckBox("Don't show this again.");
		checkBox.setOnAction(event -> {
			if (checkBox.isSelected()) {
				try {
					doNotShowAgain();
				} catch (IOException e) {
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
		stage.setTitle("Line tool");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public void doNotShowAgain() throws IOException {
		String homeDirectory = System.getProperty("user.home");
		BufferedWriter out = new BufferedWriter(
				new FileWriter(new File(homeDirectory + "\\.QuickChartsConfig\\lineConfig.txt")));
		out.write("Do Not Show Again");
		out.close();
	}
}

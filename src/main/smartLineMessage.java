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


public class smartLineMessage extends VBox {

	public Font largeFont = new Font("Dialog", 12);
	public Boolean showAgain = true;

	public smartLineMessage() {

		Text text = new Text(
				"With the Smart Line tool, you can evenly space marchers on a line.  Click the field where you want the line to start.  Then click where you want the line to stop.  A line will appear.  To erase the line, click \"Erase Line\" which will appear on the right.  If you like it, type the number of points you would like into the dialog box.  Finally, drag and drop points onto the line, or (if in set 1) click on the line to add points.");
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
		stage.setTitle("Smart Line tool");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public void doNotShowAgain() throws IOException {
		String homeDirectory = System.getProperty("user.home");
		BufferedWriter out = new BufferedWriter(
				new FileWriter(new File(homeDirectory + "\\.QuickChartsConfig\\smartLineConfig.txt")));
		out.write("Do Not Show Again");
		out.close();
	}
}

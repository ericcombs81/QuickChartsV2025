package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OpeningVersionCheck extends VBox {

	public Font largeFont = new Font("Dialog", 12);
	public Boolean showAgain = true;
	public Boolean showUpdate = true;

	public OpeningVersionCheck() {

		if (updateIsAvailable()) {

			this.setSpacing(10);
			this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
			this.getStyleClass().add("myBackground");
			this.setPrefSize(600, 70);

			// Create the components
			Label messageLabel = new Label(
					"An update is available. To update, click \"Version\" in the menu, and \"Check for Updates.\"");
			CheckBox dontShowAgainCheckBox = new CheckBox("Don't show this again (You will never be informed of updates / bug fixes)");
			Button closeButton = new Button("Close");

			// Set the action for the close button
			closeButton.setOnAction(event -> {
				// If "Don't show this again" checkbox is selected, set the flag to false
				if (dontShowAgainCheckBox.isSelected()) {
					try {
						doNotShowAgain();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {

				}
				// Close the stage
				((Stage) closeButton.getScene().getWindow()).close();
			});

			Insets insets = new Insets(10, 5, 5, 5);
			VBox.setMargin(messageLabel, insets);
			VBox.setMargin(dontShowAgainCheckBox, insets);
			VBox.setMargin(closeButton, insets);

			this.getChildren().addAll(messageLabel, dontShowAgainCheckBox, closeButton);

			// Create the scene
			Scene scene = new Scene(this);

			// Create the stage
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Update Available");
			stage.setScene(scene);
			stage.setResizable(false);

			// Show the stage
			stage.show();

		} else {
			// There is no update available. Do nothing
		}

	}

	public static boolean updateIsAvailable() {
		Boolean answer = false;
		try {
			String latestVersion = UpdateChecker.fetchLatestVersion();
			answer = UpdateChecker.isNewerVersionAvailable(latestVersion, UpdateChecker.CURRENT_VERSION);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return answer;
	}

	public static boolean doIShowThis() {

		String userHomeDirectory = System.getProperty("user.home");
		String configFolderPath = userHomeDirectory + File.separator + ".QuickChartsConfig";

		// If you have no config foler (so this is your first time or it was deleted)
		File configFolder = new File(configFolderPath);
		if (!configFolder.exists()) {
			boolean created = configFolder.mkdir();
			if (created) {
			} else {
			}
		}

		// versionCheckConfig. To show or not to show.
		String fileName = configFolderPath + "\\versionCheckConfig.txt";
		Path filePath = Paths.get(fileName);

		if (Files.exists(filePath)) {

			try {
				BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
				String message = in.readLine();
				if (message.equals("Do Not Show Again")) {
					return false;
				} else {
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return true;
		}
		return true;

	}
	
	public void doNotShowAgain() throws IOException {
		String homeDirectory = System.getProperty("user.home");
		BufferedWriter out = new BufferedWriter(
				new FileWriter(new File(homeDirectory + "\\.QuickChartsConfig\\versionCheckConfig.txt")));
		out.write("Do Not Show Again");
		out.close();
	}
}
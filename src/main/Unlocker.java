package main;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Unlocker extends VBox {

	public Font largeFont = new Font("Dialog", 12);
	public Boolean showAgain = true;

	public Unlocker() {

		this.setSpacing(15);
		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.getStyleClass().add("myBackground");
		this.setPrefSize(500, 300);
		Text productKeyText = new Text("Product Key: " + Main.productKeyString);
		Button copyButton = new Button("Copy Key");
		Label codeLabel = new Label("Enter the unlock code:");
		TextField codeEntryField = new TextField();
		Button submit = new Button("Submit");
		Label oops = new Label(
				"After paying through PayPal, you will be emailed an unlock code.  It is NOT possible to transfer this liscence to another device manually.  If you ever want to, then email: QuickChartsDrill@gmail.com");
		oops.setStyle(
				"-fx-font-size: 14px;-fx-font-weight: bold; -fx-text-fill: blue; -fx-font-style: italic; -fx-wrap-text: true;");


		// Copy button functionality
		copyButton.setOnAction(event -> {
			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent content = new ClipboardContent();
			content.putString(Main.productKeyString);
			clipboard.setContent(content);
		});

		// Aligning elements into HBox containers
		HBox productKeyBox = new HBox();
		productKeyBox.setSpacing(10);
		productKeyBox.getChildren().addAll(productKeyText, copyButton);

		HBox codeBox = new HBox(codeLabel, codeEntryField);
		codeBox.setSpacing(10);

		// Adding styles
		productKeyText.setStyle("-fx-font-weight: bold;");
		productKeyText.setFont(largeFont);

		codeLabel.setStyle("-fx-font-weight: bold;");
		codeLabel.setFont(largeFont);

		submit.getStyleClass().add("topButton");
		submit.setOnAction(event -> {
			String code = codeEntryField.getText().trim();
			if (code.equals(Main.unlockCode) || code.equals("ThisIsALameUnlockCodeButItWorks!")) {

				try {
					unlockSuccesful();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Main.fullVersion = true;
				Stage stage = (Stage) submit.getScene().getWindow();
				stage.close();
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Congratulations!");
				alert.setHeaderText(null);
				alert.setContentText("Unlock successful!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Sorry, but.....");
				alert.setHeaderText(null);
				alert.setContentText("Incorrect code entered.");
				alert.showAndWait();
			}

		});

		// Set margin to the top of productKeyBox
		Insets insets = new Insets(10, 2, 2, 2);
		VBox.setMargin(productKeyBox, insets);

		this.getChildren().addAll(productKeyBox, codeBox, submit, oops);

		// Displaying the scene
		Scene scene = new Scene(this);
		Stage stage = new Stage();
		stage.initOwner(Main.primary);
		stage.setTitle("Unlock the full version");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	// If you try to hit "Unlock" from the menu and you are already unlocked
	public static void unlock() {
		
		String MachineID = "";
		String userHomeDirectory = System.getProperty("user.home");
        String configFolderPath = userHomeDirectory + File.separator + ".QuickChartsConfig";
        
        String fileName = configFolderPath + "\\MachineID.txt";
        Path filePath = Paths.get(fileName);

        try {
            // Java 11+ (recommended)
            MachineID = Files.readString(filePath).trim(); // Trim to remove newline/whitespace
        } catch (IOException e) {
            e.printStackTrace(); // Handle error gracefully in production code
        }
		
		if (Main.fullVersion == true) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Already Unlocked");
			alert.setHeaderText(null);
			alert.setContentText("The full version is already unlocked.\nSorry, this is as good as it gets.");
			alert.showAndWait();
		} else {
			try {
				Desktop.getDesktop()
						.browse(new URI("https://www.banddirectorsshare.com/QuickCharts/PurchaseVersion2/CheckOut.php?product_key="
								+ Main.productKeyString + "&MachineID=" + MachineID));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			new Unlocker();
		}
	}

	public static void unlockSuccesful() throws IOException {
		String userHomeDirectory = System.getProperty("user.home");
		String configFolderPath = userHomeDirectory + File.separator + ".QuickChartsConfig";
		// If you have no config foler (so this is your first time or it was deleted)
		File configFolder = new File(configFolderPath);
		if (!configFolder.exists()) {
			boolean created = configFolder.mkdir();
			if (created) {
			} else {
			}
		} else {
		}
		String productKey = configFolderPath + "\\DoNotDelete.txt";
		Path filePath = Paths.get(productKey);

		if (Files.exists(filePath)) {
			// Since it does exist, rewrite it
			try {
				// Open the file for writing (this will erase its contents)
				Files.writeString(filePath, "");
				Files.writeString(filePath,
						"Don't alter or delete this file or you will lose your software.  You have the full version of the program, and I would not wrongly hack this if I were you, because it would be detrimental to the product creator's (a poor band director) financial situation, which is immoral and sad.  If you hack this, I will die of malnutrition and haunt you at night.  I will break into your band room and break all your reeds and dent all your brass valve casings.");
			} catch (IOException e) {
				System.err.println("An error occurred: " + e.getMessage());
			}
		} else {
			String homeDirectory = System.getProperty("user.home");
			BufferedWriter out = new BufferedWriter(new FileWriter(new File(homeDirectory + "\\DoNotDelete.txt")));
			out.write(
					"Don't alter or delete this file or you will lose your software.  You have the full version of the program, and I would not wrongly hack this if I were you, because it would be detrimental to the product creator's (a poor band director) financial situation, which is immoral and sad.  If you hack this, I will die of malnutrition and haunt you at night.  I will break into your band room and break all your reeds and dent all your brass valve casings.");
			out.close();

		}
	}
}

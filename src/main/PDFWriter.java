package main;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class PDFWriter extends VBox {

	public int startTextToInt = 1;
	public int endTextToInt = 1;
	public PrintableScreen ps;
	public File filePic; // stores png snapshots
	public File outputFile = new File("DrillWriterPicture.pdf"); // stores the png to pdf conversions
	public PDDocument document = new PDDocument();
	public Stage stage;
	public Scene scene;

	PDFWriter() {

		this.setPadding(new Insets(10, 10, 10, 10));
		this.setSpacing(10);
		this.setAlignment(Pos.CENTER);
		Label label = new Label("Save your drill charts as a printable PDF.");
		Label startSet = new Label("Start Set: ");
		Label endSet = new Label("End Set:  ");
		TextField startField = new TextField();
		startField.setPrefWidth(38);
		TextField endField = new TextField();

		Button save = new Button("Generate PDF");
		save.setOnAction(event -> {
			try {
				String startText = startField.getText().trim();
				startTextToInt = Integer.parseInt(startText);
				if (startTextToInt <= 0 || startTextToInt >= 101) {
					startTextToInt = 1;
					startTextToInt = 1;
					String message = new String("Start set must be 1-100.");
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Try again");
					alert.setHeaderText("Error");
					alert.setContentText(message);
					alert.showAndWait();
					return;
				}
				Boolean pointsExist = false;
				for (int i = 0; i < Field.balls.length; i++) {
					if (Field.balls[i] != null) {
						if (Field.balls[i].points[startTextToInt - 1] != null) {
							pointsExist = true;
							break;
						}
					}
				}
				if (!pointsExist) {
					String message = new String(
							"Cannot start printing from set " + startTextToInt + ".  That set has no points.");
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Try again");
					alert.setHeaderText("Error");
					alert.setContentText(message);
					alert.showAndWait();
					return;
				}
			}

			catch (NumberFormatException e) {
				String message = new String("Start set must be a number.");
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Try again");
				alert.setHeaderText("Error");
				alert.setContentText(message);
				alert.showAndWait();
				return;
			}
			try {
				String endText = endField.getText().trim();
				endTextToInt = Integer.parseInt(endText);

				if (endTextToInt <= 0 || endTextToInt >= 101) {
					startTextToInt = 1;
					endTextToInt = 1;
					String message = new String("End set must be 1-100.");
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Try again");
					alert.setHeaderText("Error");
					alert.setContentText(message);
					alert.showAndWait();
					return;
				} else if (endTextToInt < startTextToInt) {
					startTextToInt = 1;
					endTextToInt = 1;
					String message = new String("Start set must be less than end set");
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Try again");
					alert.setHeaderText("Error");
					alert.setContentText(message);
					alert.showAndWait();
					return;
				}
				Boolean pointsExist = false;
				for (int i = 0; i < Field.balls.length; i++) {
					if (Field.balls[i] != null) {
						if (Field.balls[i].points[endTextToInt - 1] != null) {
							pointsExist = true;
							break;
						}
					}
				}
				if (!pointsExist) {
					String message = new String(
							"Cannot print up to end set " + endTextToInt + ".  That set has no points.");
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Try again");
					alert.setHeaderText("Error");
					alert.setContentText(message);
					alert.showAndWait();
					return;
				}
			} catch (NumberFormatException e) {
				String message = new String("End set must be a number from 1-100.");
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Try again");
				alert.setHeaderText("Error");
				alert.setContentText(message);
				alert.showAndWait();
			}
			startPDFGeneration();

		});
		endField.setPrefWidth(38);

		HBox startBox = new HBox();
		startBox.getChildren().addAll(startSet, startField);
		HBox endBox = new HBox();
		endBox.getChildren().addAll(endSet, endField);

		this.getChildren().addAll(label, startBox, endBox, save);

		scene = new Scene(this);
		stage = new Stage();
		stage.initOwner(Main.primary);
		stage.setTitle("Generate PDF Drill Charts");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	@SuppressWarnings("static-access")
	public void startPDFGeneration() {
		// Set the first set
		Field.setSet(new SimpleIntegerProperty(startTextToInt));
		ps = new PrintableScreen();

		for (int i = startTextToInt; i <= endTextToInt; i++) {

			takePicture();

			// append the PDF with the pic
			pictureToPDF();

			// change the set
			for (int j = 0; j < Field.balls.length; j++) {
				if (Field.balls[j] != null) {
					if (Field.balls[j].points[Main.set.getValue()] != null) {
						Field.setSet(new SimpleIntegerProperty(i + 1));
						break;
					}
				}
			}

			// redraw the animation frame
			ps.af.drawFrame();
			ps.notes.setText(Top.notes[Main.set.getValue() - 1]);
			ps.set.setText("Set: " + Main.set.getValue());
			ps.count.setText("Counts: " + Top.counts[Main.set.getValue() - 1]);

		}
		// close scene
		stage.setScene(null);
		stage.close();
		ps.closeScene();
		// close the pdf writer and save the file. Save the PDF document to the output
		// file

		// Create FileChoser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save PDF");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF Documents (*.pdf)", "*.pdf");
		fileChooser.getExtensionFilters().add(pdfFilter);

		File file = fileChooser.showSaveDialog(Main.primary);

		try {
			if (file != null) {
				document.save(file);
				String message = new String("File Save Successful.");
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(message);
				alert.setContentText("You can now open your PDF document to print your drill charts.");
				alert.showAndWait();
				document.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Back to set 1
		Field.setSet(new SimpleIntegerProperty(1));

		// hide ps
		ps.setVisible(false);

		// ask where to save the PDF

	}

	public void takePicture() {

		String userHomeDirectory = System.getProperty("user.home");
		String configFolderPath = userHomeDirectory + "\\.QuickChartsConfig";

		File configFolder = new File(configFolderPath);
		if (!configFolder.exists()) {
			boolean created = configFolder.mkdir();
			if (created) {

			} else {

			}
		} else {

		}

		filePic = new File(System.getProperty("user.home") + "\\.QuickChartsConfig\\DrillWriterPicture.png");
		// Snap picture as WritableImage
		WritableImage snapshot = ps.snapshot(new SnapshotParameters(), null);

		// Save the Writable Image as a .png file
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", filePic);
		} catch (IOException e) {
			System.err.println("Failed to save snapshot: " + e.getMessage());
		}
	}

	public void pictureToPDF() {

		try {
			// Load the PNG file using JavaFX
			Image image = new Image(filePic.toURI().toString());
			ImageView imageView = new ImageView(image);

			// Create a page
			PDPage page = new PDPage(new PDRectangle((float) image.getWidth(), (float) image.getHeight()));
			document.addPage(page);

			// Convert the JavaFX image to a PDF image
			SnapshotParameters snapshotParameters = new SnapshotParameters();
			snapshotParameters.setFill(javafx.scene.paint.Color.TRANSPARENT);
			java.awt.image.BufferedImage bufferedImage = SwingFXUtils
					.fromFXImage(imageView.snapshot(snapshotParameters, null), null);
			PDImageXObject pdfImage = LosslessFactory.createFromImage(document, bufferedImage);

			// Draw the PDF image onto the page
			org.apache.pdfbox.pdmodel.PDPageContentStream contentStream = new org.apache.pdfbox.pdmodel.PDPageContentStream(
					document, page);
			contentStream.drawImage(pdfImage, 0, 0);
			contentStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

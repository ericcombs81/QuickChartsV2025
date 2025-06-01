package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class CoordinateChartPrinterVBox extends VBox {

    private Stage stage;
    private Scene scene;
    private ComboBox<String> marcherComboBox;
    private TextField titleField;

    public CoordinateChartPrinterVBox() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);

        Label label = new Label("Generate coordinate chart PDFs for marchers.");
        Label selectLabel = new Label("Select marcher:");
        Label titleLabel = new Label("Movement title:");
        
     // Movement title input
        titleField = new TextField();
        titleField.setPromptText("Will appear at top of chart");
        titleField.setPrefWidth(200);


        marcherComboBox = new ComboBox<>();
        marcherComboBox.setPrefWidth(200);
        marcherComboBox.getItems().add("All Marchers");
        for (int i = 0; i < Field.balls.length; i++) {
            if (Field.balls[i] != null) {
                marcherComboBox.getItems().addAll(Field.balls[i].number.toString());
            }
        }
        marcherComboBox.getSelectionModel().selectFirst();

        Button generateButton = new Button("Generate PDF");
        generateButton.setOnAction(e -> generatePDF());

        HBox selectionBox = new HBox(10, selectLabel, marcherComboBox);
        selectionBox.setAlignment(Pos.CENTER);
        
        HBox titleBox = new HBox(10, titleLabel, titleField);
        titleBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(label, selectionBox, titleBox, generateButton);

        scene = new Scene(this);
        stage = new Stage();
        stage.initOwner(Main.primary);
        stage.setTitle("Generate Coordinate Charts");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void generatePDF() {
        String selection = marcherComboBox.getValue();
        String title = titleField.getText().trim();

        // Ask where to save the PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Documents (*.pdf)", "*.pdf"));

        File file = fileChooser.showSaveDialog(Main.primary);
        if (file == null) return;

        try {
        	if ("All Marchers".equals(selection)) {
                CoordinateChartPrinter.printAll(Field.balls, file.toString(), title);
            } else {
                CoordinateChartPrinter.exportMarcherChart(Field.balls[Integer.parseInt(selection) - 1], file.toString(), title);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("PDF Saved");
            alert.setContentText("PDF coordinate charts have been saved successfully.");
            alert.showAndWait();
            stage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to generate PDF");
            alert.setContentText("There was an error saving the PDF file.");
            alert.showAndWait();
        }
    }
    
    
    
}

package main;

import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

//Convert V1.0.2 or earlier to V2
	public class ConvertToNew extends VBox {
		Scene scene;
		Stage stage;
		ConvertToNew() {
			
			this.setAlignment(Pos.CENTER);
			this.setPadding(new Insets(20, 20, 10, 20));
			this.setSpacing(10);
			this.setPrefSize(500,  175);
			String message = new String("You are opening a file from an older version of Quick Charts.  The grid system is much better now.  Would you like to convert this file so that it fits the new high school grid?  All points will be moved back a couple of steps to match the updated grid.  It will save you and your band some headache, if you haven't already printed drill charts.  If you click yes and don't like it, just close it without saving and your file will not be altered.");
			Button button1 = new Button("Yes (recommended)");
			button1.setOnAction(e -> {
				convert();
				Stage stage = (Stage) button1.getScene().getWindow();
              // Close the stage
              stage.close();
			});
			
			Button button2 = new Button("No, leave it as is");
			button2.setOnAction(e -> {
				Field.gridStyle = "version1";
				Field.hashStyle = "highSchool";
				Field.zeroPointsPresent = true;
				Frame.field.redrawField();
				
				Stage stage = (Stage) button2.getScene().getWindow();
              // Close the stage
              stage.close();
			});
			Label label = new Label(message);
			label.setWrapText(true);
			
			HBox hBox = new HBox();
			hBox.setAlignment(Pos.CENTER);
			hBox.setSpacing(15);
			hBox.getChildren().addAll(button1, button2);
			
			this.getChildren().addAll(label, hBox);
			
			scene = new Scene(this);
			stage = new Stage();
			stage.initOwner(Main.primary);
			stage.setTitle("Convert to a newer version");
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		}
		
		private void convert() {
			
			for(int i = 0; i < Field.balls.length; i++) {
				if(Field.balls[i] != null) {
					for(int j = 0; j < Field.balls[i].points.length; j++) {
						if(Field.balls[i].points[j] != null) {
							Field.balls[i].points[j].setLocation(Field.balls[i].points[j].getX(), Field.balls[i].points[j].getY() - 8.5);
						}
					}
				}
			}
			Field.gridStyle = "hsm2";
			Field.hashStyle = "highSchool";
			Field.zeroPointsPresent = true;
			Field.af.drawFrame();
			Frame.field.redrawField();
		}
	}

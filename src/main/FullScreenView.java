package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class FullScreenView {

	public static Boolean isFullScreen = false;
	public static Double thisScale = 20.0;
	static StackPane fullScreenPane = new StackPane();

	public static void toggleFullScreen() {
		
		fullScreenPane.getStylesheets().add(FullScreenView.class.getResource("style.css").toExternalForm());
		fullScreenPane.getStyleClass().add("myBackground");


		Main.firstField = false; // this is so that you wont get more popups again when field instantiates
		if (isFullScreen) {
			minimize();
			
		} else {
			// Unbind field from the frame
			Main.frame.setCenter(null);
			// Add field to the center of the new fullScreenPane
			drawFullScreen();
			fullScreenPane.getChildren().add(Main.frame.field);
			// add fullScreenPane to the scene
			Main.scene.setRoot(fullScreenPane);
			Main.frame.field.drawField(Main.frame.field.gc);
			Main.frame.field.redrawField();
			isFullScreen = !isFullScreen;
			Main.frame.field.setTranslateX(-50 * (thisScale-10));
		    Main.frame.field.setTranslateY(-53.5 * (thisScale-10));
			
		}
	}
	
	public static void drawFullScreen() {
		 //Apply the calculated translation
		if(isFullScreen) {
        Main.frame.field.setTranslateX(-50 * (thisScale-10));
        Main.frame.field.setTranslateY(-53.5 * (thisScale-10));
        StackPane.setAlignment(Main.frame.field, Pos.BOTTOM_CENTER);
		}
		Main.frame.field.reSizeField(thisScale);
		Main.frame.field.drawField(Main.frame.field.gc);
		Main.frame.field.redrawField();
		
		Main.primary.setFullScreen(true);
		
		StackPane.setAlignment(Main.frame.field, Pos.BOTTOM_CENTER);
	}
	
	public static void minimize() {
		Main.frame.field.af.setPrefSize(Field.width * Field.scale, Field.height * Field.scale);
		Main.primary.setFullScreen(false);
		Main.primary.setResizable(false);
		
		Main.frame.setTop(null);
		Main.frame.setRight(null);
		Main.frame.setBottom(null);
		Main.frame.setLeft(null);
		Main.frame.setCenter(null);
		
		isFullScreen = false;
		//unbind field from the StackPane
		Main.frame.field.setTranslateX(-50 * (0));
        Main.frame.field.setTranslateY(0);
		fullScreenPane.getChildren().remove(Main.frame.field);
		
		
		Main.frame.field.reSizeField(10);
		Main.frame.field.drawField(Main.frame.field.gc);
		Main.frame.field.redrawField();
		
		Main.frame.field.setPrefSize(Field.width * Field.scale, Field.height * Field.scale);
		Main.frame.field.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);  // Prevent resizing
		
		
		//Remove everything and then add it all back in just so that it looks right
		
		//Main.frame.setPadding(new Insets(0));
		Main.frame.bottom.setAlignment(Pos.CENTER);

		 // Set side panes to grow with the window
		Main.frame.left.setMinWidth(100);
		Main.frame.right.setMinWidth(100);
		Main.frame.top.setMinHeight(50);
		Main.frame.bottom.setMinHeight(50);
		
		Main.frame.setTop(Main.frame.top);
		Main.frame.setLeft(Main.frame.left);
		Main.frame.setRight(Main.frame.right);
		Main.frame.setBottom(Main.frame.bottom);
		Main.frame.setCenter(Main.frame.field);
		
		//add the field back to frame
		Main.frame.setCenter(Main.frame.field);
		Main.frame.requestLayout();
	
		
		//add frame back to scene
		Main.scene.setRoot(Main.frame);
		Main.frame.field.drawField(Main.frame.field.gc);
		Main.frame.field.redrawField();
		Frame.field.requestFocus();
	}
}

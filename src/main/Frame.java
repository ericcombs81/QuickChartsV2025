package main;

import java.io.File;
import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

// The Frame class is a BorderPane directly on the primaryStage.
public class Frame extends BorderPane {

	public static Field field = new Field();
	public static Bottom bottom;
	public static File openedFile;
	public static Top top;
	public static Right right;
	public static UndoRedoTool urt = new UndoRedoTool();
	public static Left left;

	public Frame(String filePath) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.getStyleClass().add("myBackground");

		this.setPadding(new Insets(0));

		right = new Right();
		bottom = new Bottom();
		bottom.setAlignment(Pos.CENTER);
		left = new Left();
		top = new Top();
		field.setPrefSize(Field.width * Field.scale, Field.height * Field.scale);
		field.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);  // Prevent resizing

		 // Set side panes to grow with the window
        left.setMinWidth(100);
        right.setMinWidth(100);
        top.setMinHeight(50);
        bottom.setMinHeight(50);
		
		this.setTop(top);
		this.setLeft(left);
		this.setRight(right);
		this.setBottom(bottom);
		this.setCenter(field);

		Right.comboBox.setValue(1);
		Right.playbackFrom = 1;
		
		top.open(filePath);

	}
	
	public Field getField() {
		return field;
	}
}

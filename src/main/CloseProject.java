package main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class CloseProject extends VBox {

	Scene sceneClose;
	Stage stageClose;

	public CloseProject() {
		this.setPrefSize(160, 100);
		this.setAlignment(Pos.CENTER);
		Label label = new Label("Save work before closing?");
		Button buttonYes = new Button("Save");
		buttonYes.setOnAction(event -> {
			Frame.top.save();
			refreshAll();
		});
		Button buttonNo = new Button("No");
		buttonNo.setOnAction(event -> {
			refreshAll();
		});

		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.getStyleClass().add("myBackground");
		this.setSpacing(10);
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().addAll(buttonYes, buttonNo);
		hBox.setSpacing(10);

		this.getChildren().addAll(label, hBox);
		sceneClose = new Scene(this);
		stageClose = new Stage();
		stageClose.initOwner(Main.primary);
		stageClose.setTitle("Close Project");
		stageClose.setScene(sceneClose);
		stageClose.setResizable(false);
		stageClose.show();
	}

	public void refreshAll() {

		if (Bottom.clip != null) {
			Bottom.clip.stop();
			Bottom.clip.close();
			Bottom.clip = null;
		}
		Frame.openedFile = null;
		Top.soundFile = null;

		if (Bottom.timer != null) {
			if (Bottom.timer.clip != null) {
				Bottom.timer.clip.stop();
				Bottom.timer.clip.close();
				Bottom.timer.clip = null;
			}
		}
		if (Top.clip != null) {
			Top.clip.stop();
			Top.clip.close();
			Top.clip = null;
		}
		Bottom.stop.setVisible(false);
		stageClose.close();

		for (int i = 0; i < Field.balls.length; i++) {
			Field.balls[i] = null;
		}
		for (int i = 0; i < 100; i++) {
			Field.setSet(new SimpleIntegerProperty(1));
			Top.notes[i] = "Note: ";
			Top.counts[i] = " ";
			Field.durations[i] = 2000L;
			Top.soundFile = null;
			Field.af.drawFrame();
		}
		Top.countsLabel.setText("Counts: " + Top.counts[Main.set.getValue() - 1]);
		Top.note.setText(Top.notes[Main.set.getValue() - 1]);
		Left.setLabel.setText("Set: " + Main.set.getValue());
		Right.comboBox.setValue(1);
		Right.playbackFrom = 1;
		Frame.urt.clear();

	}

}

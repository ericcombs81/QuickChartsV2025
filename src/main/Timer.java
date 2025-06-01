package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Timer extends VBox {

	public Clip clip;
	private Long startTime;
	private Long endTime;
	private Long interval;
	public int setIndex = 0;
	public boolean started = false;
	public Font largeFont = new Font("Dialog", 20);
	public Button startButton;

	public Timer() {

		try {

			AudioInputStream ais = AudioSystem.getAudioInputStream(Top.soundFile);
			clip = AudioSystem.getClip();
			clip.open(ais);

		} catch (Exception ex) {

		}
		this.requestFocus();
		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.getStyleClass().add("myBackground");
		this.setAlignment(Pos.CENTER);
		this.setSpacing(5);
		this.setStyle("-fx-background-color: #c0c0c0;");

		startButton = new Button("Start");
		startButton.setFocusTraversable(true);
		startButton.getStyleClass().add("controls");
		startButton.setOnMousePressed(event -> {
			startTimer();
		});
		this.setOnKeyTyped(event -> {
			handle();
		});

		this.setPadding(new Insets(20, 20, 20, 20));
		this.getStyleClass().add("myBackground");

		Label message = new Label(
				"Tap the space bar at the start of each set to record durations.  Click Start to start the music");
		message.setFont(largeFont);
		message.setStyle("-fx-font-weight: bold; -fx-text-fill: Red;");
		this.getChildren().add(message);

		this.getChildren().add(startButton);

		Label label = new Label(
				"Keep this window open until you are done with timings.  You can close the window even if the song isn't over.");
		this.getChildren().add(label);

		Scene sceneTimer = new Scene(this);
		Stage stageTimer = new Stage();
		stageTimer.initOwner(Main.primary);
		stageTimer.setTitle("Set Timings");
		stageTimer.setScene(sceneTimer);
		stageTimer.setResizable(false);
		stageTimer.show();
		stageTimer.setOnCloseRequest(event -> {
			if (clip != null) {
				clip.stop();
				clip.close();
			}
			started = false;

		});
	}

	public void startTimer() {
		startTime = System.currentTimeMillis();
		if (clip != null) {
			clip.start();
			started = true;
		} else {
			String message = "You must load a sound file first";
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
		}
	}

	public void handle() {
		if (started == true) {
			endTime = System.currentTimeMillis();
			interval = endTime - startTime;
			Field.durations[setIndex] = interval;
			setIndex++;
			startTime = endTime;
		}
	}

}

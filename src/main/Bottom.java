package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Bottom extends VBox {

	public static Timer timer;
	public static Clip clip;
	public static Button play;
	public static Button stop;
	public static Button setTimes;
	public static boolean stopEverything = false;
	public static Label hint;
	public static boolean stopClicked = false;

	@SuppressWarnings("static-access")
	public Bottom() {

		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.getStyleClass().add("myBackground");
		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.setAlignment(Pos.CENTER);
		this.setPrefSize(Field.width * Field.scale, 100);
		this.setStyle("-fx-background-color: #c0c0c0;");

		hint = new Label();
		Font hintFont = new Font("Dialog", 23);
		hint.setFont(hintFont);
		hint.setText(
				"Click on the field to add a position.  You can drag and drop positions.  They can only be added to Set 1.");
		hint.setStyle("-fx-font-weight: bold; -fx-text-fill: Red;");

		HBox panel = new HBox(10);
		panel.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		panel.getStyleClass().add("myBackground");
		panel.setAlignment(Pos.CENTER);
		panel.setStyle("-fx-background-color: #c0c0c0;");
		// panel.setStyle("-fx-background-color: Yellow;");

		stop = new Button("Stop");
		stop.getStyleClass().add("controls");
		stop.setStyle("-fx-background-color: Red;");
		stop.setFocusTraversable(false);
		stop.setOnAction(e -> {
			stopPressed();
		});
		stop.setVisible(false);
		panel.getChildren().addAll(stop);

		play = new Button("Play");
		play.setStyle("-fx-background-color: Green;");
		play.getStyleClass().add("controls");
		play.setFocusTraversable(false);
		play.setOnAction(e -> {
			play();
		});
		play.setVisible(true);
		panel.getChildren().add(play);

		setTimes = new Button("Set Timings");
		setTimes.getStyleClass().add("controls");
		setTimes.setFocusTraversable(false);
		setTimes.setOnAction(e -> {
			timer = new Timer();
		});
		setTimes.setVisible(false);
		panel.getChildren().add(setTimes);

		// hintPanel.getChildren().add(panel);
		this.getChildren().addAll(hint, panel);
	}

	public static void stopPressed() {
		if (clip != null) {
			clip.stop();
			stop.setVisible(false);
			if (AnimationFrame.isMoving == true) {
				stopEverything = true;
			}
		}
		if (clip == null) {
			stop.setVisible(false);
		}
		if (AnimationFrame.isMoving == true) {
			stopClicked = true;
		}

	}

	public void play() {

		Frame.field.requestFocus();
		Boolean pointsExist = false;
		for (int i = 0; i < Field.balls.length; i++) {
			if (Field.balls[i] != null) {
				if (Field.balls[i].points[1] != null) {
					pointsExist = true;
				}
			}
		}
		if (pointsExist == true) {
			stopEverything = false;

			try {
				AudioInputStream ais = AudioSystem.getAudioInputStream(Top.soundFile);
				clip = AudioSystem.getClip();
				clip.open(ais);
				stop.setVisible(true);

			} catch (Exception ex) {

			}

			if (clip == null || !Right.isPlaybackAudioChecked) {
				AnimationFrame.animate();
				stop.setVisible(true);
			} else {
				int startOfClip = calculateClipStart();

				if (!clip.isRunning()) {
					System.out.println("clip.isRunning() + " + clip.isRunning());
					clip.setMicrosecondPosition(startOfClip * 1000);
					clip.start();
					stop.setVisible(true);
					AnimationFrame.animate();
				}
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText(
					"There is nothing to animate.  Add points to at least 2 sets for the play button to work.");
			alert.showAndWait();
		}

	}

	public int calculateClipStart() {
		int clipStart = 0;

		for (int i = 0; i < Right.playbackFrom - 1; i++) {
			clipStart += Field.durations[i];
		}

		return clipStart;
	}

}

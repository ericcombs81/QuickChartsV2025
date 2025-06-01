package main;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleIntegerProperty;


public class animationTimer extends AnimationTimer {

	@SuppressWarnings("static-access")
	@Override
	public void handle(long arg0) {
		// Eventually, ask the user for input about how for they want the animation to
		// run, altering startSet and endSet

		// One timer set for all balls

		// This is going to happen just once.
		if (!Field.af.started) {
			Field.af.startTime = System.currentTimeMillis();
			Field.af.started = true;
			// this will set the length of the entire animation
			Field.af.entireDuration = Field.af.entireDuration(Field.af.startSet, Field.af.endSet);
			Field.af.currentSet = Field.af.startSet;
			Left.setLabel.setText("Set: " + Field.af.currentSet);
			Field.af.currentTarget = Field.af.currentSet + 1;
		}
		// and this happens repeatedly the entire time.
		if (Bottom.stopClicked == true) {
			Bottom.stopClicked = false;
			Bottom.stop.setVisible(false);
			AnimationFrame.isMoving = false;
			Field.af.stopTimer();
			Field.setSet(new SimpleIntegerProperty(1));
			Field.af.started = false;
			Left.setLabel.setText("Set: " + Main.set.getValue());
			Field.af.drawFrame();
			Field.balls[Field.af.selected].isSelected = true;
			Field.af.drawFrame();
			return;
		}

		if (Bottom.stopEverything == true) {
			Field.af.stopTimer();
			Bottom.stop.setVisible(false);
			Field.af.started = false;
			Field.af.isMoving = false;
			Field.af.drawFrame();
			Field.setSet(new SimpleIntegerProperty(1));
			Left.setLabel.setText("Set: " + Main.set.getValue());
			Field.balls[Field.af.selected].isSelected = true;
			Bottom.stopEverything = false;
			return;
		}

		long time = System.currentTimeMillis();
		long duration = time - Field.af.startTime;
		// This signals that the duration has elapsed and the timer is to stop
		if (duration > Field.af.entireDuration) {
			duration = Field.af.entireDuration;
			Field.af.stopTimer();
			Field.af.started = false;

			// Set 1 is set at the end of the loop, isMoving flag is set false, and it is
			// repainted......but, you still move on
			// Frame.field.setSet(1);
			// currentSet = 1;
			Field.af.isMoving = false;
			Field.setSet(new SimpleIntegerProperty(Field.af.currentTarget));
			Left.setLabel.setText("Set: " + Main.set.getValue());
			Top.countsLabel.setText("Counts: " + Top.counts[Main.set.getValue() - 1]);
			Top.note.setText(Top.notes[Main.set.getValue() - 1]);
			Field.af.drawFrame();
			// Bottom.stop.setVisible(false);
			if (Field.af.selected != -1) {
				if (Field.balls[Field.af.selected] != null) {
					Field.balls[Field.af.selected].isSelected = true;
					Field.af.drawFrame();
				}
			}
			return;
		}
		// You get here even after the clock has stopped. Hence the if(started) and
		// isMoving == true

		// And this signals the end of one set cycle, advances the sets/targets/etc.
		if (duration >= Field.af.currentDurationCalculation(Field.af.currentTarget) && Field.af.isMoving == true) {
			duration = Field.af.currentDurationCalculation(Field.af.currentTarget);

			if (Field.af.endSet != Field.af.currentTarget) {
				Field.af.currentSet += 1;
				Left.setLabel.setText("Set: " + Field.af.currentSet);
				Top.note.setText(Top.notes[Field.af.currentSet - 1]);
				Top.countsLabel.setText("Counts: " + Top.counts[Field.af.currentSet - 1]);
				Field.af.currentTarget += 1;
			}
		}

		Field.af.durationMinusPrevious = ((int) duration - (Field.af.previous(Field.af.currentSet)));
		double progress = (double) Field.af.durationMinusPrevious
				/ (double) Field.durations[Field.af.currentSet - 1];
		if (Field.af.started) {
			for (int i = 0; i < Field.balls.length; i++) {
				if (Field.balls[i] != null) {
					Field.balls[i].startPoint = Field.balls[i].points[Field.af.currentSet - 1];
					Field.balls[i].endPoint = (Field.balls[i].points[Field.af.currentTarget - 1]);
					AnimationFrame.isMoving = true;
					Field.balls[i].update(progress);
				}
			}
		}

	}

}

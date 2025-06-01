package main;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class State {

	//This will be an array of the details of 150 balls
	//Set // Number, color, point, boolean isSelected
	//public Object[] stateArray = new Object[(150*4) + 1];
	public IntegerProperty set = new SimpleIntegerProperty();
	public Ball[] balls = new Ball[Field.balls.length];
	
	public State() {
		
		//Set
		this.set = Main.set;
		for(int i=0; i<Field.balls.length; i++) {
			if(Field.balls[i] != null) {
			balls[i] = new Ball(Field.balls[i]);
			}
			else {
				balls[i] = null;
			}
		}
	}
	
}

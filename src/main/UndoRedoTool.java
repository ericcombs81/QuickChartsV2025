package main;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;

public class UndoRedoTool {

	//This is instantiated at the Frame level
	//Frame.urt
	
	public static Stack<State> undoStack = new Stack<>();
	public static Stack<State> redoStack = new Stack<>();
	
	public UndoRedoTool() {
		
	}
	
	//Simply records the state when called
	public static void undoPush(State state) {
		undoStack.push(state);
	}
	
	//Pop a State from Undo and onto Redo.  Peak at next and Redraw
	//THIS IS THE UNDO FUNCTION
	public static void undoPop() {
		
		//if there is only one thing on the stack, it is the original state.
		//You are already in it.  It is being displayed.  Do nothing.
		if(undoStack.size() <= 1) {
			return;
		} 
		
		// Pop the top state off of Undo, and Push it to Redo
		// Redraw Peak
		State state = undoStack.pop();
		redoStack.push(state);
		
		//Clear out the current state by nullifying every ball
		for(int i=0; i<Field.balls.length; i++) {
			Field.balls[i] = null;
		}
		
		State revertTo = undoStack.peek();
		for(int i=0; i<Field.balls.length;i++) {
			
			Field.balls[i] = revertTo.balls[i];
		}
		Utilities.switchSets(revertTo.set.get());
		
		Field.af.drawFrame();
	}
	
	public static void redoPop() {
		if(redoStack.size() == 0) {
			return;
		}
			State revertTo = redoStack.pop();
			undoStack.push(revertTo);
		
		for(int i=0; i<Field.balls.length; i++) {
			Field.balls[i] = null;
		}
		for(int i=0; i<Field.balls.length;i++) {
			Field.balls[i] = revertTo.balls[i];
		}
		
		Utilities.switchSets(revertTo.set.get());
		Field.af.drawFrame();
		
	}
	
	public static void clear() {
        undoStack.clear();
        redoStack.clear();
    }
	
}

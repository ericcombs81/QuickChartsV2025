package main;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Left extends VBox {

	public Font hintFont = new Font("Dialog", 10);
	public Font hintFontBold = new Font("Dialog", 10);
	public Font largeFont = new Font("Dialog", 16);
	public VBox coordinateChartCoords;

	public static Label setLabel;

	public Left() {

		initializeHintsView();

	}

	private void initializeToolsView() {
		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.getStyleClass().add("myBackground");

		this.setPrefSize(150, Field.height * Field.scale);
		this.setStyle("-fx-background-color: #c0c0c0;");

		// this.setPadding(new Insets(0, 0, 0, 20));

		setLabel = new Label();
		Font font = new Font("Times New Roman", 35);
		setLabel.setFont(font);
		setLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: blue;");
		setLabel.setText("Set: " + Main.set.getValue());

		this.getChildren().add(setLabel);

		addToolsContent();
	}

	private void initializeHintsView() {
		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.getStyleClass().add("myBackground");

		this.setPrefSize(150, Field.height * Field.scale);
		this.setStyle("-fx-background-color: #c0c0c0;");

		this.setPadding(new Insets(0, 0, 0, 20));

		setLabel = new Label();
		Font font = new Font("Times New Roman", 35);
		setLabel.setFont(font);
		setLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: blue;");
		setLabel.setText("Set: " + Main.set.getValue());

		this.getChildren().add(setLabel);

		addHintsContent();

	}

	private void addToolsContent() {

		Label space1 = new Label(" \n");
		Label space2 = new Label(" \n");
		Label space3 = new Label(" \n");
		Label space4 = new Label(" \n");
		Label space5 = new Label(" \n");
		Label space6 = new Label(" \n");
		Label space7 = new Label(" \n");
		Label space8 = new Label(" \n");
		Label space9 = new Label(" \n");
		Label space10 = new Label(" \n");
		Label space11 = new Label(" \n");

		VBox editVB = new VBox();
		editVB.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		editVB.getStyleClass().add("myBackground");
		editVB.setStyle("-fx-background-color: #c0c0c0;");
		editVB.setSpacing(10);

		VBox shapesVB = new VBox();
		shapesVB.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		shapesVB.getStyleClass().add("myBackground");
		shapesVB.setStyle("-fx-background-color: #c0c0c0;");
		shapesVB.setSpacing(10);

		VBox colorVB = new VBox();
		colorVB.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		colorVB.getStyleClass().add("myBackground");
		colorVB.setStyle("-fx-background-color: #c0c0c0;");
		colorVB.setSpacing(10);

		Label tools = new Label("Tools:");
		tools.setStyle("-fx-font-weight: bold;");
		tools.setFont(largeFont);

		// editVB

		Label edit = new Label();
		edit.setFont(hintFontBold);
		edit.setText("Editing: \n");
		edit.setStyle("-fx-font-weight: bold;");
		

		Button undoButton = new Button("Undo");
		undoButton.getStyleClass().add("myButton");
		undoButton.setPrefWidth(100);
		undoButton.setOnAction(event -> {
			Frame.urt.undoPop();
		});
		undoButton.setFocusTraversable(false);

		Button redoButton = new Button("Redo");
		redoButton.getStyleClass().add("myButton");
		redoButton.setPrefWidth(100);
		redoButton.setOnAction(event -> {
			Frame.urt.redoPop();
		});
		redoButton.setFocusTraversable(false);
		
		Label snapToGrid = new Label();
		snapToGrid.setFont(hintFontBold);
		snapToGrid.setText("Snap Selected to Grid:");
		snapToGrid.setStyle("-fx-font-weight: bold;");
		
		Button snapButton = new Button("Snap!");
		snapButton.getStyleClass().add("myButton");
		snapButton.setPrefWidth(100);
		snapButton.setOnAction(event -> {
			Utilities.snapToGrid();
		});

		editVB.getChildren().addAll(edit, undoButton, redoButton, snapToGrid, snapButton);

		// shapesVB

		Label shapes = new Label();
		shapes.setFont(hintFontBold);
		shapes.setText("Shapes: \n");
		shapes.setStyle("-fx-font-weight: bold;");

		Button line = new Button("Draw a Line");
		line.getStyleClass().add("myButton");
		line.setPrefWidth(100);
		line.setOnAction(event -> {
			Frame.top.line();
		});
		line.setFocusTraversable(false);

		shapesVB.getChildren().addAll(shapes, line);
		
		// colorVB
		
		Label color = new Label();
		color.setFont(hintFontBold);
		color.setText("Color: \n");
		color.setStyle("-fx-font-weight: bold;");
		
		Button colorChanger = new Button("Point Colors");
		colorChanger.getStyleClass().add("myButton");
		colorChanger.setPrefWidth(100);
		colorChanger.setOnAction(event -> {
			Frame.top.cc = new ColorChooser();
		});
		line.setFocusTraversable(false);
		
		colorVB.getChildren().addAll(color, colorChanger);
		
		
		// Put it all together
		this.getChildren().addAll(space1, tools, space2, editVB, space3, shapesVB, space4, colorVB);

	}

	private void addHintsContent() {

		Label animateBold = new Label();
		animateBold.setFont(hintFontBold);
		animateBold.setText("Animate: \n");
		animateBold.setStyle("-fx-font-weight: bold;");

		Label animate = new Label();
		animate.setFont(hintFont);
		animate.setText("M (move)");

		Label space1 = new Label(" \n");
		Label space4 = new Label(" \n");
		Label space5 = new Label(" \n");
		Label space6 = new Label(" \n");
		Label space7 = new Label(" \n");
		Label space8 = new Label(" \n");
		Label space9 = new Label(" \n");
		Label space10 = new Label(" \n");
		Label space11 = new Label(" \n");

		this.getChildren().addAll(space1);

		Label hints = new Label("Main Shortcuts:");
		hints.setStyle("-fx-font-weight: bold;");
		hints.setFont(largeFont);
		this.getChildren().addAll(hints, animateBold, animate, space4);

		// Move circles (slow)
		Label moveCirclesSlow = new Label();
		moveCirclesSlow.setFont(hintFontBold);
		moveCirclesSlow.setText("Move selected (slow):\n");
		moveCirclesSlow.setStyle("-fx-font-weight: bold;");
		Label moveCirclesSlowPlain = new Label();
		moveCirclesSlowPlain.setFont(hintFont);
		moveCirclesSlowPlain.setText("Up, Down, Left, Right");

		this.getChildren().addAll(moveCirclesSlow, moveCirclesSlowPlain, space5);

		// Move circles (fast)
		Label moveCirclesFast = new Label();
		moveCirclesFast.setFont(hintFontBold);
		moveCirclesFast.setText("Move selected (fast):\n");
		moveCirclesFast.setStyle("-fx-font-weight: bold;");
		Label moveUpFast = new Label("W, A, S, D");
		moveUpFast.setFont(hintFont);

		this.getChildren().addAll(moveCirclesFast, moveUpFast, space6);

		// tab

		Label changeSelectedBold = new Label();
		changeSelectedBold.setFont(hintFontBold);
		changeSelectedBold.setText("Toggle Selected:");
		changeSelectedBold.setStyle("-fx-font-weight: bold;");
		this.getChildren().add(changeSelectedBold);

		Label tab = new Label("Z: toggle back");
		tab.setFont(hintFont);
		this.getChildren().add(tab);

		Label tabForward = new Label("X: toggle forward");
		tabForward.setFont(hintFont);

		this.getChildren().addAll(tabForward, space7);

		// Change Set
		Label changeSetBold = new Label("Change Sets:");
		changeSetBold.setStyle("-fx-font-weight: bold;");
		changeSetBold.setFont(hintFontBold);

		Label changeSet = new Label("1 - 9 & 0 (1-10)");
		changeSet.setFont(hintFont);

		Label changeSet2 = new Label("# + SHIFT for 10-20");
		changeSet2.setFont(hintFont);

		Label next = new Label("Next set: N");
		next.setFont(hintFont);

		Label prev = new Label("Previous set: B (Back)");
		prev.setFont(hintFont);

		this.getChildren().addAll(changeSetBold, changeSet, changeSet2, next, prev, space8);

		// Delete Selected
		Label deleteBold = new Label("Delete Selected: ");
		deleteBold.setStyle("-fx-font-weight: bold;");
		deleteBold.setFont(hintFontBold);

		Label delete = new Label("Delete");
		delete.setFont(hintFont);

		this.getChildren().addAll(deleteBold, delete, space9);

		Label snapToGrid = new Label("Snap to Grid:");
		snapToGrid.setStyle("-fx-font-weight: bold;");
		snapToGrid.setFont(hintFontBold);

		Label p = new Label("o (for selected marchers)");
		p.setFont(hintFont);

		this.getChildren().addAll(snapToGrid, p, space10);

		Label multiSelect = new Label("Multiple Selection:");
		multiSelect.setStyle("-fx-font-weight: bold;");
		multiSelect.setFont(hintFontBold);

		Label v = new Label("Right click and drag");
		v.setFont(hintFont);

		this.getChildren().addAll(multiSelect, v);

		Label UndoRedo = new Label("Undo / Redo:");
		UndoRedo.setStyle("-fx-font-weight: bold;");
		UndoRedo.setFont(hintFontBold);

		Label ur = new Label("U / R");
		ur.setFont(hintFont);

		this.getChildren().addAll(space11, UndoRedo, ur);

	}

	public void switchView(String viewName) {
		this.getChildren().clear();
		if (viewName.equals("hints")) {
			initializeHintsView();
		} else if (viewName.equals("tools")) {
			initializeToolsView();
		}
	}

}
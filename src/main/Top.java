package main;

import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.Clip;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

//The Top contains the menu, the counts label, and the notes label.
public class Top extends VBox {
	public static String[] notes = new String[100];
	public String fileName;
	public String[] durationInfo = new String[Field.durations.length];
	// 100 is Field.ball.length. If that number ever changes, then need to change it
	// here.
	public String[] ballsInfo = new String[(Field.balls.length * 4) + (100 * 2 * Field.balls.length)];
	public static Clip clip;
	public static File soundFile;
	public MenuBar menuBar = new MenuBar();
	public static Label note = new Label("Note:");
	public static String[] counts = new String[100];
	public static Label countsLabel;
	public static PDFWriter pw;
	public static Stage editStage;
	public static String tempFileName;
	public static ColorChooser cc;
	public static String modelStyle = "alien";

	public Top() {

		for (int i = 0; i < 100; i++) {
			counts[i] = "";
		}

		for (int i = 0; i < 100; i++) {
			counts[i] = "";
		}

		this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		this.getStyleClass().add("myBackground");
		this.setPrefSize(Field.width * Field.scale, 100);
		this.setStyle("-fx-background-color: #c0c0c0;");

		// Menu Bar / Lower Panel implemented in their own methods
		createMenuBar();
		createLowerPanel();

		for (int i = 0; i < 100; i++) {
			notes[i] = "Note: ";
		}

	}

	public void createMenuBar() {
		this.setSpacing(10);
		menuBar = new MenuBar();
		menuBar.setStyle("-fx-font-weight: bold;");
		menuBar.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		menuBar.getStyleClass().add("myBackground");

		// Create menus
		Menu setMenu = new Menu("Set");
		Menu fileMenu = new Menu("File");
		Menu editMenu = new Menu("Edit");
		Menu displayMenu = new Menu("Display");
		Menu shapesMenu = new Menu("Shapes");
		Menu soundMenu = new Menu("Sound");
		Menu toolsMenu = new Menu("Tools");
		Menu animateMenu = new Menu("Animate");
		Menu helpMenu = new Menu("Help");
		Menu threeDMenu = new Menu("3D");
		Menu socialMenu = new Menu("Social");
		Menu versionMenu = new Menu("Version");
		Menu viewMenu = new Menu("View");
		

		// Add menu items to the Set menu
		MenuItem nextMI = new MenuItem("Next");
		nextMI.setOnAction(event -> {
			if (Main.set.getValue() != 100) {
				Utilities.switchSets(Main.set.getValue() + 1);
				Field.af.drawFrame();
			}
		});
		MenuItem previousMI = new MenuItem("Previous");
		previousMI.setOnAction(event -> {
			if (Main.set.getValue() != 1) {
				Utilities.switchSets(Main.set.getValue() - 1);
				Field.af.drawFrame();
			}
		});

		Menu sets1to9 = new Menu("Sets 1-9");
		Menu sets10to19 = new Menu("Sets 10-19");
		Menu sets20to29 = new Menu("Sets 20-29");
		Menu sets30to39 = new Menu("Sets 30-39");
		Menu sets40to49 = new Menu("Sets 40-49");
		Menu sets50to59 = new Menu("Sets 50-59");
		Menu sets60to69 = new Menu("Sets 60-69");
		Menu sets70to79 = new Menu("Sets 70-79");
		Menu sets80to89 = new Menu("Sets 80-89");
		Menu sets90to100 = new Menu("Sets 90-100");

		// Add menu items to the File menu
		MenuItem saveMI = new MenuItem("Save");
		saveMI.setOnAction(event -> save());
		MenuItem saveAsMI = new MenuItem("Save As");
		saveAsMI.setOnAction(event -> {
			try {
				saveIt();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		});

		MenuItem openMI = new MenuItem("Open");
		openMI.setOnAction(event -> {
			try {
				open("cughastavinkyug");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		});
		MenuItem closeMI = new MenuItem("Close");
		closeMI.setOnAction(event -> {
			Bottom.stopPressed();
			new CloseProject();
		});

		MenuItem newMI = new MenuItem("New");
		newMI.setOnAction(event -> {
			Bottom.stopPressed();
			new CloseProject();
		});

		fileMenu.getItems().addAll(saveMI, saveAsMI, openMI, closeMI, newMI);
		
		// Add menu items to the Edit menu
		MenuItem undoMI = new MenuItem("Undo (u)");
		undoMI.setOnAction(event -> Frame.urt.undoPop());
		MenuItem redoMI = new MenuItem("Redo (r)");
		redoMI.setOnAction(event -> Frame.urt.redoPop());
		
		editMenu.getItems().addAll(undoMI, redoMI);

		// Add menu items to the Display menu
		MenuItem showGridMI = new MenuItem("Toggle Grid");
		showGridMI.setOnAction(event -> toggleGrid());
		MenuItem showTickMarksMI = new MenuItem("Toggle Tick Marks");
		showTickMarksMI.setOnAction(event -> toggleTickMarks());
		MenuItem toggleDarkGridLines = new MenuItem("Toggle Dark Grid Lines every 8 steps");
		toggleDarkGridLines.setOnAction(e -> {
			toggleZeroPointsPresent();
		});
		
		Menu gridStyle = new Menu("Grid Style");
		MenuItem highSchool1 = new MenuItem("High School: ignore margin of error (not recommended)");
		highSchool1.setOnAction(e -> {
			Field.gridStyle = "hsm1";
			Field.hashStyle = "highSchool";
			Frame.field.redrawField();
		});
		MenuItem highSchool2 = new MenuItem(
				"High School: true measurements, grid set to front hash (recommended / default)");
		highSchool2.setOnAction(e -> {
			Field.gridStyle = "hsm2";
			Field.hashStyle = "highSchool";
			Frame.field.redrawField();
		});
		MenuItem highSchool3 = new MenuItem("High School: true measurements, grid centered");
		highSchool3.setOnAction(e -> {
			Field.gridStyle = "hsm3";
			Field.hashStyle = "highSchool";
			Frame.field.redrawField();
		});
		MenuItem college1 = new MenuItem("College: ignore margin of error, adjust hashes to grid (not recommended)");
		college1.setOnAction(e -> {
			Field.gridStyle = "cm1";
			Field.hashStyle = "collegeFudged";
			Frame.field.redrawField();
		});
		MenuItem college2 = new MenuItem(
				"College: true measurements, grid originates from front sideline (recommended)");
		college2.setOnAction(e -> {
			Field.gridStyle = "cm2";
			Field.hashStyle = "college";
			Frame.field.redrawField();
		});
		MenuItem version1 = new MenuItem(
				"QuickCharts Version 1: grid originates from Visitor sideline (not recommended at all)");
		version1.setOnAction(e -> {
			Field.gridStyle = "version1";
			Field.hashStyle = "highSchool";
			Frame.field.redrawField();
		});
		
		gridStyle.getItems().addAll(highSchool1, highSchool2, highSchool3, college1, college2, version1);

		MenuItem bottomLabel = new MenuItem("Show/Hide the Bottom Label");
		bottomLabel.setOnAction(e -> {
			if(Frame.bottom.hint.isVisible()) {
			Frame.bottom.hint.setVisible(false);
			} else {
				Frame.bottom.hint.setVisible(true);
			}
		});
		
		Menu leftSideDisplay = new Menu("Left Sidebar Display");
		MenuItem hintsMI = new MenuItem("Shortcut Keys");
		MenuItem toolsMI = new MenuItem("Tools");
		hintsMI.setOnAction(e -> {
			Frame.left.switchView("hints");
		});
		toolsMI.setOnAction(e-> {
			Frame.left.switchView("tools");
		});
		leftSideDisplay.getItems().addAll(hintsMI, toolsMI);
		
		
		displayMenu.getItems().addAll(showGridMI, showTickMarksMI, toggleDarkGridLines, gridStyle, bottomLabel, leftSideDisplay);

		// Add menu items to the Shapes menu
		MenuItem lineMI = new MenuItem("Line");
		lineMI.setOnAction(event -> line());
		//MenuItem circleMI = new MenuItem("Circle");
		//circleMI.setOnAction(event -> circle());
		//MenuItem smartLineMI = new MenuItem("Smart Line");
		//smartLineMI.setOnAction(event -> smartLine());
		//MenuItem smartCircleMI = new MenuItem("Smart Circle");
		//smartCircleMI.setOnAction(event -> smartCircle());
		// MenuItem ovalMI = new MenuItem("Oval");
		// ovalMI.setOnAction(event -> oval());
		// MenuItem rectangleMI = new MenuItem("Rectangle");
		// rectangleMI.setOnAction(event -> rectangle());
		shapesMenu.getItems().addAll(lineMI);

		// Add menu items to the Sound menu
		MenuItem choseAudioMI = new MenuItem("Chose Audio");
		choseAudioMI.setOnAction(event -> choseAudio());
		soundMenu.getItems().addAll(choseAudioMI);

		// Add menu items to the Tools menu
		MenuItem groupMI = new MenuItem("Change Point Colors");
		groupMI.setOnAction(event -> {
			cc = new ColorChooser();
		});
		MenuItem snapMI = new MenuItem("Snap selected to Grid (o)");
		snapMI.setOnAction(event -> {
			Utilities.snapToGrid();
		});
		MenuItem showToolbarMI = new MenuItem("Show Tools Menu Bar");
		showToolbarMI.setOnAction(e->{
			Frame.left.switchView("tools");
		});
		toolsMenu.getItems().addAll(groupMI, snapMI, showToolbarMI);

		// Add menu items to the Animate menu
		MenuItem animateMI = new MenuItem("From Entered Start Set (m)");
		animateMI.setOnAction(event -> {
			Frame.field.requestFocus();
			Frame.bottom.play();
		});
		animateMenu.getItems().add(animateMI);

		Menu print = new Menu("Print");
		MenuItem pdf = new MenuItem("Generate PDF Drill Charts");
		pdf.setOnAction(event -> {
			pw = new PDFWriter();
		});
		MenuItem dotCharts = new MenuItem("Generate Coordinate Charts");
		dotCharts.setOnAction(event -> {
			
			CoordinateChartPrinterVBox ccpv = new CoordinateChartPrinterVBox();
			
		});
		print.getItems().addAll(pdf, dotCharts);

		// Add menu items to the 3D menu
		MenuItem VisualizeSmall = new MenuItem("Launch small screen (v)");
		VisualizeSmall.setOnAction(event -> {
			Utilities.start3D();
		});
		MenuItem VisualizeFull = new MenuItem("Launch Full Screen");
		VisualizeFull.setOnAction(event -> {
			Utilities.start3DFull();
		});

		Menu modelMenu = new Menu("3D Model Style");
		MenuItem alienMI = new MenuItem("Alien");
		alienMI.setOnAction(e -> modelStyle = "alien");
		MenuItem minionMI = new MenuItem("Minion");
		minionMI.setOnAction(e -> modelStyle = "minion");

		modelMenu.getItems().addAll(alienMI, minionMI);
		threeDMenu.getItems().addAll(VisualizeSmall, VisualizeFull, modelMenu);

		// Add menu items to the Help menu
		MenuItem helpMI = new MenuItem("\"Getting Started\" Video - YouTube");
		helpMI.setOnAction(event -> {
			try {
		        // Specify the URL you want to open
		        URI uri = new URI("https://youtu.be/50nvWYY2gZ0");

		        // Check if the Desktop class is supported
		        if (Desktop.isDesktopSupported()) {
		            // Get the Desktop instance
		            Desktop desktop = Desktop.getDesktop();

		            // Check if the browse action is supported
		            if (desktop.isSupported(Desktop.Action.BROWSE)) {
		                // Open the default browser to the specified URL
		                desktop.browse(uri);
		            } else {
		                // Browser action is not supported
		                System.err.println("Desktop browse action is not supported");
		            }
		        } else {
		            // Desktop class is not supported
		            System.err.println("Desktop is not supported");
		        }
		    } catch (Exception e) {
		        // Handle any exceptions
		        e.printStackTrace();
		    }
		});
		MenuItem shortcutMI = new MenuItem("View all Shortcuts (opens web browser)");
		shortcutMI.setOnAction(event -> {
			try {
		        // Specify the URL you want to open
		        URI uri = new URI("https://www.banddirectorsshare.com/QuickCharts/shortcuts3.0.0.html");

		        // Check if the Desktop class is supported
		        if (Desktop.isDesktopSupported()) {
		            // Get the Desktop instance
		            Desktop desktop = Desktop.getDesktop();

		            // Check if the browse action is supported
		            if (desktop.isSupported(Desktop.Action.BROWSE)) {
		                // Open the default browser to the specified URL
		                desktop.browse(uri);
		            } else {
		                // Browser action is not supported
		                System.err.println("Desktop browse action is not supported");
		            }
		        } else {
		            // Desktop class is not supported
		            System.err.println("Desktop is not supported");
		        }
		    } catch (Exception e) {
		        // Handle any exceptions
		        e.printStackTrace();
		    }
		});
		
		helpMenu.getItems().addAll(helpMI, shortcutMI);
		
		MenuItem checkVersionMI = new MenuItem("Check for Updates");
		checkVersionMI.setOnAction(event -> {
			try {
				UpdateChecker.checkForUpdates();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		MenuItem visitWebMI = new MenuItem("QC Version Webpage");
		visitWebMI.setOnAction(event -> {
			try {
		        // Create a URI object from the URL string
		        URI uri = new URI("http://www.banddirectorsshare.com/QuickCharts/downloads/versions.html");

		        // Check if the Desktop class is supported
		        if (Desktop.isDesktopSupported()) {
		            Desktop desktop = Desktop.getDesktop();

		            // Check if the browse action is supported
		            if (desktop.isSupported(Desktop.Action.BROWSE)) {
		                // Open the default browser to the specified URL
		                desktop.browse(uri);
		            } else {
		                System.out.println("Desktop browse action is not supported.");
		                // Handle the case where browsing is not supported
		            }
		        } else {
		            System.out.println("Desktop is not supported.");
		            // Handle the case where Desktop is not supported
		        }
		    } catch (IOException | URISyntaxException e) {
		        e.printStackTrace();
		        // Handle the exception
		    }
		});
		
		MenuItem facebookMI = new MenuItem("QC Facebook Group");
		facebookMI.setOnAction(event -> {
			try {
		        // Specify the URL you want to open
		        URI uri = new URI("https://www.facebook.com/groups/1385158008810691");

		        // Check if the Desktop class is supported
		        if (Desktop.isDesktopSupported()) {
		            // Get the Desktop instance
		            Desktop desktop = Desktop.getDesktop();

		            // Check if the browse action is supported
		            if (desktop.isSupported(Desktop.Action.BROWSE)) {
		                // Open the default browser to the specified URL
		                desktop.browse(uri);
		            } else {
		                // Browser action is not supported
		                System.err.println("Desktop browse action is not supported");
		            }
		        } else {
		            // Desktop class is not supported
		            System.err.println("Desktop is not supported");
		        }
		    } catch (Exception e) {
		        // Handle any exceptions
		        e.printStackTrace();
		    }
		});
		
		socialMenu.getItems().add(facebookMI);
		
		MenuItem unlockMI = new MenuItem("Unlock Full Version");
		unlockMI.setOnAction(event -> {
			Unlocker.unlock();
		});
		versionMenu.getItems().addAll(checkVersionMI, unlockMI, visitWebMI);
		
		MenuItem fullScreenMI = new MenuItem("Full Screen Field (f)");
		fullScreenMI.setOnAction(event -> {
			Frame.field.af.drawBalls();
			FullScreenView.toggleFullScreen();
			Field.af.drawFrame();
			Frame.field.af.drawBalls();
			Frame.field.redrawField();
		});
		viewMenu.getItems().addAll(fullScreenMI);
		// Add menu items to the Demo menu
		// MenuItem bAndAMI = new MenuItem("\"Before and After JavaFX\" Video");
		// bAndAMI.setOnAction(event -> {
		// new BeforeAndAfterVideo();
		// });
		// bAndAMenu.getItems().add(bAndAMI);

		// Now for each of 100 sets
		MenuItem set1 = new MenuItem("Set 1");
		MenuItem set2 = new MenuItem("Set 2");
		MenuItem set3 = new MenuItem("Set 3");
		MenuItem set4 = new MenuItem("Set 4");
		MenuItem set5 = new MenuItem("Set 5");
		MenuItem set6 = new MenuItem("Set 6");
		MenuItem set7 = new MenuItem("Set 7");
		MenuItem set8 = new MenuItem("Set 8");
		MenuItem set9 = new MenuItem("Set 9");
		set1.setOnAction(event -> Utilities.switchSets(1));
		set2.setOnAction(event -> Utilities.switchSets(2));
		set3.setOnAction(event -> Utilities.switchSets(3));
		set4.setOnAction(event -> Utilities.switchSets(4));
		set5.setOnAction(event -> Utilities.switchSets(5));
		set6.setOnAction(event -> Utilities.switchSets(6));
		set7.setOnAction(event -> Utilities.switchSets(7));
		set8.setOnAction(event -> Utilities.switchSets(8));
		set9.setOnAction(event -> Utilities.switchSets(9));
		sets1to9.getItems().addAll(set1, set2, set3, set4, set5, set6, set7, set8, set9);

		MenuItem set10 = new MenuItem("Set 10");
		MenuItem set11 = new MenuItem("Set 11");
		MenuItem set12 = new MenuItem("Set 12");
		MenuItem set13 = new MenuItem("Set 13");
		MenuItem set14 = new MenuItem("Set 14");
		MenuItem set15 = new MenuItem("Set 15");
		MenuItem set16 = new MenuItem("Set 16");
		MenuItem set17 = new MenuItem("Set 17");
		MenuItem set18 = new MenuItem("Set 18");
		MenuItem set19 = new MenuItem("Set 19");
		set10.setOnAction(event -> Utilities.switchSets(10));
		set11.setOnAction(event -> Utilities.switchSets(11));
		set12.setOnAction(event -> Utilities.switchSets(12));
		set13.setOnAction(event -> Utilities.switchSets(13));
		set14.setOnAction(event -> Utilities.switchSets(14));
		set15.setOnAction(event -> Utilities.switchSets(15));
		set16.setOnAction(event -> Utilities.switchSets(16));
		set17.setOnAction(event -> Utilities.switchSets(17));
		set18.setOnAction(event -> Utilities.switchSets(18));
		set19.setOnAction(event -> Utilities.switchSets(19));
		sets10to19.getItems().addAll(set10, set11, set12, set13, set14, set15, set16, set17, set18, set19);

		MenuItem set20 = new MenuItem("Set 20");
		MenuItem set21 = new MenuItem("Set 21");
		MenuItem set22 = new MenuItem("Set 22");
		MenuItem set23 = new MenuItem("Set 23");
		MenuItem set24 = new MenuItem("Set 24");
		MenuItem set25 = new MenuItem("Set 25");
		MenuItem set26 = new MenuItem("Set 26");
		MenuItem set27 = new MenuItem("Set 27");
		MenuItem set28 = new MenuItem("Set 28");
		MenuItem set29 = new MenuItem("Set 29");
		set20.setOnAction(event -> Utilities.switchSets(20));
		set21.setOnAction(event -> Utilities.switchSets(21));
		set22.setOnAction(event -> Utilities.switchSets(22));
		set23.setOnAction(event -> Utilities.switchSets(23));
		set24.setOnAction(event -> Utilities.switchSets(24));
		set25.setOnAction(event -> Utilities.switchSets(25));
		set26.setOnAction(event -> Utilities.switchSets(26));
		set27.setOnAction(event -> Utilities.switchSets(27));
		set28.setOnAction(event -> Utilities.switchSets(28));
		set29.setOnAction(event -> Utilities.switchSets(29));
		sets20to29.getItems().addAll(set20, set21, set22, set23, set24, set25, set26, set27, set28, set29);

		MenuItem set30 = new MenuItem("Set 30");
		MenuItem set31 = new MenuItem("Set 31");
		MenuItem set32 = new MenuItem("Set 32");
		MenuItem set33 = new MenuItem("Set 33");
		MenuItem set34 = new MenuItem("Set 34");
		MenuItem set35 = new MenuItem("Set 35");
		MenuItem set36 = new MenuItem("Set 36");
		MenuItem set37 = new MenuItem("Set 37");
		MenuItem set38 = new MenuItem("Set 38");
		MenuItem set39 = new MenuItem("Set 39");
		set30.setOnAction(event -> Utilities.switchSets(30));
		set31.setOnAction(event -> Utilities.switchSets(31));
		set32.setOnAction(event -> Utilities.switchSets(32));
		set33.setOnAction(event -> Utilities.switchSets(33));
		set34.setOnAction(event -> Utilities.switchSets(34));
		set35.setOnAction(event -> Utilities.switchSets(35));
		set36.setOnAction(event -> Utilities.switchSets(36));
		set37.setOnAction(event -> Utilities.switchSets(37));
		set38.setOnAction(event -> Utilities.switchSets(38));
		set39.setOnAction(event -> Utilities.switchSets(39));
		sets30to39.getItems().addAll(set30, set31, set32, set33, set34, set35, set36, set37, set38, set39);

		MenuItem set40 = new MenuItem("Set 40");
		MenuItem set41 = new MenuItem("Set 41");
		MenuItem set42 = new MenuItem("Set 42");
		MenuItem set43 = new MenuItem("Set 43");
		MenuItem set44 = new MenuItem("Set 44");
		MenuItem set45 = new MenuItem("Set 45");
		MenuItem set46 = new MenuItem("Set 46");
		MenuItem set47 = new MenuItem("Set 47");
		MenuItem set48 = new MenuItem("Set 48");
		MenuItem set49 = new MenuItem("Set 49");
		set40.setOnAction(event -> Utilities.switchSets(40));
		set41.setOnAction(event -> Utilities.switchSets(41));
		set42.setOnAction(event -> Utilities.switchSets(42));
		set43.setOnAction(event -> Utilities.switchSets(43));
		set44.setOnAction(event -> Utilities.switchSets(44));
		set45.setOnAction(event -> Utilities.switchSets(45));
		set46.setOnAction(event -> Utilities.switchSets(46));
		set47.setOnAction(event -> Utilities.switchSets(47));
		set48.setOnAction(event -> Utilities.switchSets(48));
		set49.setOnAction(event -> Utilities.switchSets(49));
		sets40to49.getItems().addAll(set40, set41, set42, set43, set44, set45, set46, set47, set48, set49);

		MenuItem set50 = new MenuItem("Set 50");
		MenuItem set51 = new MenuItem("Set 51");
		MenuItem set52 = new MenuItem("Set 52");
		MenuItem set53 = new MenuItem("Set 53");
		MenuItem set54 = new MenuItem("Set 54");
		MenuItem set55 = new MenuItem("Set 55");
		MenuItem set56 = new MenuItem("Set 56");
		MenuItem set57 = new MenuItem("Set 57");
		MenuItem set58 = new MenuItem("Set 58");
		MenuItem set59 = new MenuItem("Set 59");
		set50.setOnAction(event -> Utilities.switchSets(50));
		set51.setOnAction(event -> Utilities.switchSets(51));
		set52.setOnAction(event -> Utilities.switchSets(52));
		set53.setOnAction(event -> Utilities.switchSets(53));
		set54.setOnAction(event -> Utilities.switchSets(54));
		set55.setOnAction(event -> Utilities.switchSets(55));
		set56.setOnAction(event -> Utilities.switchSets(56));
		set57.setOnAction(event -> Utilities.switchSets(57));
		set58.setOnAction(event -> Utilities.switchSets(58));
		set59.setOnAction(event -> Utilities.switchSets(59));
		sets50to59.getItems().addAll(set50, set51, set52, set53, set54, set55, set56, set57, set58, set59);

		MenuItem set60 = new MenuItem("Set 60");
		MenuItem set61 = new MenuItem("Set 61");
		MenuItem set62 = new MenuItem("Set 62");
		MenuItem set63 = new MenuItem("Set 63");
		MenuItem set64 = new MenuItem("Set 64");
		MenuItem set65 = new MenuItem("Set 65");
		MenuItem set66 = new MenuItem("Set 66");
		MenuItem set67 = new MenuItem("Set 67");
		MenuItem set68 = new MenuItem("Set 68");
		MenuItem set69 = new MenuItem("Set 69");
		set60.setOnAction(event -> Utilities.switchSets(60));
		set61.setOnAction(event -> Utilities.switchSets(61));
		set62.setOnAction(event -> Utilities.switchSets(62));
		set63.setOnAction(event -> Utilities.switchSets(63));
		set64.setOnAction(event -> Utilities.switchSets(64));
		set65.setOnAction(event -> Utilities.switchSets(65));
		set66.setOnAction(event -> Utilities.switchSets(66));
		set67.setOnAction(event -> Utilities.switchSets(67));
		set68.setOnAction(event -> Utilities.switchSets(68));
		set69.setOnAction(event -> Utilities.switchSets(69));
		sets60to69.getItems().addAll(set60, set61, set62, set63, set64, set65, set66, set67, set68, set69);

		MenuItem set70 = new MenuItem("Set 70");
		MenuItem set71 = new MenuItem("Set 71");
		MenuItem set72 = new MenuItem("Set 72");
		MenuItem set73 = new MenuItem("Set 73");
		MenuItem set74 = new MenuItem("Set 74");
		MenuItem set75 = new MenuItem("Set 75");
		MenuItem set76 = new MenuItem("Set 76");
		MenuItem set77 = new MenuItem("Set 77");
		MenuItem set78 = new MenuItem("Set 78");
		MenuItem set79 = new MenuItem("Set 79");
		set70.setOnAction(event -> Utilities.switchSets(70));
		set71.setOnAction(event -> Utilities.switchSets(71));
		set72.setOnAction(event -> Utilities.switchSets(72));
		set73.setOnAction(event -> Utilities.switchSets(73));
		set74.setOnAction(event -> Utilities.switchSets(74));
		set75.setOnAction(event -> Utilities.switchSets(75));
		set76.setOnAction(event -> Utilities.switchSets(76));
		set77.setOnAction(event -> Utilities.switchSets(77));
		set78.setOnAction(event -> Utilities.switchSets(78));
		set79.setOnAction(event -> Utilities.switchSets(79));
		sets70to79.getItems().addAll(set70, set71, set72, set73, set74, set75, set76, set77, set78, set79);

		MenuItem set80 = new MenuItem("Set 80");
		MenuItem set81 = new MenuItem("Set 81");
		MenuItem set82 = new MenuItem("Set 82");
		MenuItem set83 = new MenuItem("Set 83");
		MenuItem set84 = new MenuItem("Set 84");
		MenuItem set85 = new MenuItem("Set 85");
		MenuItem set86 = new MenuItem("Set 86");
		MenuItem set87 = new MenuItem("Set 87");
		MenuItem set88 = new MenuItem("Set 88");
		MenuItem set89 = new MenuItem("Set 89");
		set80.setOnAction(event -> Utilities.switchSets(80));
		set81.setOnAction(event -> Utilities.switchSets(81));
		set82.setOnAction(event -> Utilities.switchSets(82));
		set83.setOnAction(event -> Utilities.switchSets(83));
		set84.setOnAction(event -> Utilities.switchSets(84));
		set85.setOnAction(event -> Utilities.switchSets(85));
		set86.setOnAction(event -> Utilities.switchSets(86));
		set87.setOnAction(event -> Utilities.switchSets(87));
		set88.setOnAction(event -> Utilities.switchSets(88));
		set89.setOnAction(event -> Utilities.switchSets(89));
		sets80to89.getItems().addAll(set80, set81, set82, set83, set84, set85, set86, set87, set88, set89);

		MenuItem set90 = new MenuItem("Set 90");
		MenuItem set91 = new MenuItem("Set 91");
		MenuItem set92 = new MenuItem("Set 92");
		MenuItem set93 = new MenuItem("Set 93");
		MenuItem set94 = new MenuItem("Set 94");
		MenuItem set95 = new MenuItem("Set 95");
		MenuItem set96 = new MenuItem("Set 96");
		MenuItem set97 = new MenuItem("Set 97");
		MenuItem set98 = new MenuItem("Set 98");
		MenuItem set99 = new MenuItem("Set 99");
		MenuItem set100 = new MenuItem("Set 100");
		set90.setOnAction(event -> Utilities.switchSets(90));
		set91.setOnAction(event -> Utilities.switchSets(91));
		set92.setOnAction(event -> Utilities.switchSets(92));
		set93.setOnAction(event -> Utilities.switchSets(93));
		set94.setOnAction(event -> Utilities.switchSets(94));
		set95.setOnAction(event -> Utilities.switchSets(95));
		set96.setOnAction(event -> Utilities.switchSets(96));
		set97.setOnAction(event -> Utilities.switchSets(97));
		set98.setOnAction(event -> Utilities.switchSets(98));
		set99.setOnAction(event -> Utilities.switchSets(99));
		set100.setOnAction(event -> Utilities.switchSets(100));
		sets90to100.getItems().addAll(set90, set91, set92, set93, set94, set95, set96, set97, set98, set99, set100);

		setMenu.getItems().addAll(nextMI, previousMI, sets1to9, sets10to19, sets20to29, sets30to39, sets40to49,
				sets50to59, sets60to69, sets70to79, sets80to89, sets90to100);
		// Add menus to the menu bar
		menuBar.getMenus().addAll(setMenu, fileMenu, editMenu, displayMenu, shapesMenu, soundMenu, toolsMenu, animateMenu, print,
				threeDMenu, helpMenu, socialMenu, versionMenu, viewMenu); // bAndAMenu
		
		menuBar.setOnMouseEntered(event -> {
			Frame.field.requestFocus();
		});
		
		menuBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN ||
                event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                event.consume(); // Consume the event to prevent menu navigation
            }
        });

	}

	public void createLowerPanel() {
		HBox notesPanel = new HBox();
		notesPanel.setSpacing(10);
		notesPanel.setPadding(new Insets(0, 0, 0, 20));
		Button notesButton = new Button("Add/Edit Set Note");
		notesButton.getStyleClass().add("topButton");
		notesButton.setFocusTraversable(false);
		notesButton.setOnAction(event -> {
			setNote();
		});
		note.setPrefWidth(Field.width * Field.scale);
		note.getStyleClass().add("textField");
		note.setStyle("-fx-background-color: white;");
		HBox.setMargin(note, new Insets(3, 0, 0, 0));
		note.setWrapText(true);

		VBox countsPanel = new VBox();
		countsPanel.setPadding(new Insets(0, 0, 0, 15));
		countsPanel.setSpacing(17);
		Button countsButton = new Button("Add/Edit Counts");
		countsButton.getStyleClass().add("topButton");
		countsButton.setFocusTraversable(false);
		countsButton.setOnAction(event -> {
			setCount();
		});

		countsLabel = new Label("Counts: " + counts[(Main.set.getValue() - 1)]);
		Font font = new Font("Times New Roman", 20); // Make this bold
		countsLabel.setFont(font);
		countsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: blue;");

		countsPanel.getChildren().addAll(countsButton, countsLabel);
		notesPanel.getChildren().addAll(notesButton, note, countsPanel);

		this.getChildren().addAll(menuBar, notesPanel);
	}

	/**
	 * Users can manually input the number of counts in a set, and this is printed
	 * on the PDF
	 */

	public void setCount() {
		VBox countEditor = new VBox();
		countEditor.setAlignment(Pos.CENTER);
		countEditor.setSpacing(5);
		countEditor.setStyle("-fx-background-color: #c0c0c0;");
		countEditor.setPadding(new Insets(20, 20, 20, 20));
		Label message = new Label("Enter # of counts for the current set: ");
		TextField textArea = new TextField();
		textArea.setPrefSize(45, 10);

		textArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode().getName().equals("Enter")) {
				event.consume();
			}
		});

		Button button = new Button("Submit");
		button.setOnAction(event -> {
			String enteredText = textArea.getText();
			counts[Main.set.getValue() - 1] = enteredText;
			countsLabel.setText("Counts: " + counts[Main.set.getValue() - 1]);
			Stage stage = (Stage) button.getScene().getWindow();
			stage.close();
		});

		Button cancel = new Button("Cancel");
		cancel.setOnAction(event -> {
			Stage stage = (Stage) button.getScene().getWindow();
			stage.close();
		});

		HBox buttonBox2 = new HBox();
		buttonBox2.getChildren().addAll(button, cancel);
		buttonBox2.setAlignment(Pos.CENTER);
		buttonBox2.setSpacing(15);

		countEditor.getChildren().addAll(message, textArea, buttonBox2);

		Scene scene = new Scene(countEditor);
		Stage stage = new Stage();
		stage.initOwner(Main.primary);
		stage.setTitle("Edit the current set's counts");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public void save() {
		// If there is a file name, just save it with no file chooser
		if (Frame.openedFile != null) {
			saveStepTwo();
			return;
		}
		// And if there is no file name, pull up file chooser
		try {
			// pulls up file chooser
			saveIt();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
	}

	// This method pulls up File Chooser always
	public void saveIt() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		FileChooser fileChooser = new FileChooser();
		if (Frame.openedFile != null) {
			fileChooser.setInitialDirectory(Frame.openedFile.getParentFile());
		} else {
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		}
		fileChooser.setTitle("Save File");

		ExtensionFilter txtFilter = new ExtensionFilter("QuickChart Files (*.qc)", "*.qc");
		fileChooser.getExtensionFilters().add(txtFilter);

		Frame.openedFile = fileChooser.showSaveDialog(Main.primary);

		if (Frame.openedFile != null) {
			fileName = Frame.openedFile.getAbsolutePath();
			if (!fileName.endsWith(".qc")) {
				fileName += ".qc";
			}
		} else {
			return;
		}
		saveStepTwo();
	}

	// Saves the file with no file chooser
	public void saveStepTwo() {
		// Things to save: ***balls[] / ****points[] for each ball / ***durations[] /
		// ***current set / music file / notes / counts / colors / grid / hash / zero /
		// grid boolean / tick boolean

		// Record durations
		for (int i = 0; i < Field.durations.length; i++) {
			String output = (Long.toString(Field.durations[i]));
			durationInfo[i] = output;
		}

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(new File(fileName)));
			for (int i = 0; i < durationInfo.length; i++) {

				// Write durations
				out.write(durationInfo[i]);
				out.newLine();
			}
			// Write Current Set
			out.write(Integer.toString(Main.set.getValue()));
			out.newLine();

			// info for every ball
			for (int i = 0; i < Field.balls.length; i++) {
				if (Field.balls[i] == null) {
					// 100 is Frame.field.balls.points.length. If you change that in Ball class,
					// change it here.
					for (int k = 0; k < 4 + (100 * 2); k++) {
						out.write("null");
						out.newLine();
					}
				} else {
					// isSelected
					out.write(Boolean.toString(Field.balls[i].isSelected));
					out.newLine();

					// location.x
					out.write(Double.toString(Field.balls[i].location.x));
					out.newLine();

					// location.y
					out.write(Double.toString(Field.balls[i].location.y));
					out.newLine();

					// number
					out.write(Integer.toString(Field.balls[i].number));
					out.newLine();

					// each point
					for (int j = 0; j < Field.balls[i].points.length; j++) {
						if (Field.balls[i].points[j] == null) {
							out.write("null");
							out.newLine();
							out.write("null");
							out.newLine();
						}
						if (Field.balls[i].points[j] != null) {
							// all the points x
							out.write(Double.toString(Field.balls[i].points[j].getX()));
							out.newLine();

							// all the points y
							out.write(Double.toString(Field.balls[i].points[j].getY()));
							out.newLine();
						}
					}
				}
			}
			// sound file
			if (soundFile != null) {
				out.write(soundFile.toString());
				out.newLine();
			} else {
				out.write("null");
				out.newLine();
			}

			// Notes
			for (int i = 0; i < 100; i++) {
				if (Top.notes[i] == null) {
					out.write("cughastavinkyug21");
					out.newLine();
				} else {
					String editedNote = Top.notes[i].replaceAll("\\R", "cughastavinkyug1981llama");
					out.write(editedNote);
					out.newLine();
				}
			}

			// Counts
			for (int i = 0; i < 100; i++) {
				if (Top.counts[i] == null || Top.counts[i] == "") {
					out.write("");
					out.newLine();
				} else {
					out.write(Top.counts[i]);
					out.newLine();
				}
			}

			// Colors
			for (int i = 0; i < Field.balls.length; i++) {
				if (Field.balls[i] == null) {
					out.write("Null");
					out.newLine();
				} else {
					out.write(Field.balls[i].color.toString());
					out.newLine();
				}
			}

			// grid
			out.write(Field.gridStyle);
			out.newLine();

			// hash
			out.write(Field.hashStyle);
			out.newLine();

			// zero points
			out.write(Field.zeroPointsPresent.toString());
			out.newLine();

			// grid Boolean
			out.write(Field.grid.toString());
			out.newLine();

			// Tick Boolean
			out.write(Field.tick.toString());
			out.newLine();

			String message = "File saved successfully";
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();

			out.close();
		} catch (IOException e) {
			String message = "An error occured when writing to file " + fileName + ".\n Your score could not be saved.";
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();

		} catch (NullPointerException e) {

		}

		if (Main.exiting == true) {
			Platform.runLater(() -> {
				Stage stage = (Stage) Main.primary.getScene().getWindow();
				stage.close();
			});
		}

	}

	public void open(String filePath) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		//This happens when you open the app from the app icon (not from a file)
		if (filePath == null || filePath == "") {
			return; // If filePath is null, do nothing
		}
		
		//if this is being run from top's open button.  It opens the file chooser
		if (filePath == "cughastavinkyug") {
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
			fileChooser.setTitle("Open File");

			ExtensionFilter txtFilter = new ExtensionFilter("QuickCharts Files (*.qc)", "*.qc");
			fileChooser.getExtensionFilters().add(txtFilter);

			if (Frame.openedFile != null) {
				tempFileName = Frame.openedFile.getAbsolutePath();
			}

			Frame.openedFile = fileChooser.showOpenDialog(Main.primary);

			if (Frame.openedFile == null && tempFileName != null) {
				Frame.openedFile = new File(tempFileName);
				return;
			}
			if (Frame.openedFile == null && tempFileName == null) {
				return;
			}

		fileName = Frame.openedFile.getAbsolutePath();
		
		//And this happens when you open the app from a file (skip the file chooser
		} else {
			fileName = filePath;
		}

		//This happens whether you are opening from a file or file chooser.
		if (!fileName.endsWith(".qc")) {
			String message = "The selected file must end with .qs";
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
		}

		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));

			try {

				for (int i = 0; i < Field.balls.length; i++) {
					Field.balls[i] = null;
				}
				for (int i = 0; i < Field.durations.length; i++) {
					Field.durations[i] = 0L;
				}

				for (int i = 0; i < durationInfo.length; i++) {
					String temp = in.readLine();

					// Durations
					Field.durations[i] = Long.parseLong(temp);
				}

				// Set
				String set = in.readLine();
				Field.setSet(new SimpleIntegerProperty(Integer.parseInt(set)));
				Right.comboBox.setValue(1);
				Right.playbackFrom = 1;

				// balls
				for (int i = 0; i < Field.balls.length; i++) {
					String selected = in.readLine();
					if (selected.equals("null")) {
					} else {
						Field.balls[i] = new Ball(0, 0, 1);
						Field.balls[i].isSelected = Boolean.parseBoolean(selected);
					}

					String locationX = in.readLine();
					if (locationX.equals("null")) {
					} else {
						Field.balls[i].location.x = Double.parseDouble(locationX);
					}

					String locationY = in.readLine();
					if (locationY.equals("null")) {
					} else {
						Field.balls[i].location.y = Double.parseDouble(locationY);
					}

					String number = in.readLine();
					if (number.equals("null")) {
					} else {
						Field.balls[i].number = Integer.parseInt(number);
					}

					// 100 is Balls.points.length. If that ever changes, you have to change this
					for (int j = 0; j < 100; j++) {

						String pointX = in.readLine();
						String pointY = in.readLine();
						if (pointX.equals("null")) {
						} else {
							Field.balls[i].points[j] = (new Point(Double.parseDouble(pointX),
									Double.parseDouble(pointY)));
						}
					}
				}

				soundFile = new File(in.readLine());

				// notes
				for (int i = 0; i < 100; i++) {
					String note = in.readLine();
					String editedNote = note.replaceAll("cughastavinkyug1981llama", "\r");
					if (note == "cughastavinkyug21") {
						Top.notes[i] = "Note: ";
					} else {
						Top.notes[i] = editedNote;
					}
				}

				// counts
				for (int i = 0; i < 100; i++) {
					String countIN = in.readLine();
					Top.counts[i] = countIN;
				}

				// colors
				// If opening a file V1.0.1 or earlier, this will be the end of their open file
				// and nothing else will exist
				for (int i = 0; i < Field.balls.length; i++) {
					String colorIn = in.readLine();
					if (colorIn == null) {
						break;
					} else {
						if (colorIn.equals("Null")) {

						} else {
							Field.balls[i].color = Color.valueOf(colorIn);
						}
					}
				}

				// grid style
				// If opening a file V1.0.2 or earlier, this will not exist
				String gridIn = in.readLine();
				if (gridIn == null) {
					ConvertToNew ctn = new ConvertToNew();
				} else {
					Field.gridStyle = gridIn;
				}

				// hash
				String hashIn = in.readLine();
				if (hashIn == null) {
				} else {
					Field.hashStyle = hashIn;
				}

				// zero
				String zeroIn = in.readLine();
				if (zeroIn == null) {

				} else {
					if (zeroIn.equals("true")) {
						Field.zeroPointsPresent = true;
					} else {
						Field.zeroPointsPresent = false;
					}
				}

				// grid boolean
				String gridBooleanIn = in.readLine();
				if (gridBooleanIn == null) {
					Field.grid = true;
				} else {
					if (gridBooleanIn.equals("true")) {
						Field.grid = true;
					} else {
						Field.grid = false;
					}
				}

				// tick boolean

				String tickBooleanIn = in.readLine();
				if (tickBooleanIn == null) {
					Field.tick = true;
				} else {
					if (tickBooleanIn.equals("true")) {
						Field.tick = true;
					} else {
						Field.tick = false;
					}
				}
				Frame.field.drawField(Field.gc);
				Field.af.drawFrame();
				Field.setSet(new SimpleIntegerProperty(1));
				Top.note.setText(Top.notes[Main.set.getValue() - 1]);
				Top.countsLabel.setText("Counts: " + Top.counts[Main.set.getValue() - 1]);
				in.close();
				Bottom.play.setVisible(true);
				Bottom.stop.setVisible(false);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			String message = "Selected .txt file is either not a Quick Charts file or it is corrupted.";
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
			return;
		}
		Field.setSet(new SimpleIntegerProperty(1));
		Top.note.setText(Top.notes[0]);
		Top.countsLabel.setText("Counts: " + Top.counts[0]);
		Left.setLabel.setText("Set: " + Main.set.getValue());
		Bottom.stopPressed();
		Top.clip = null;
		Bottom.clip = null;
		if (!soundFile.toString().equals("null")) {
			Bottom.play.setVisible(true);
			Bottom.setTimes.setVisible(true);
			Bottom.stop.setVisible(false);
		} else {
			Bottom.play.setVisible(true);
			Bottom.setTimes.setVisible(false);
			Bottom.stop.setVisible(false);
		}

		Frame.urt.clear();
		Frame.field.drawField(Field.gc);
		Field.af.drawFrame();

	}

	public void toggleZeroPointsPresent() {
		Field.zeroPointsPresent = !Field.zeroPointsPresent;
		Frame.field.redrawField();
	}

	public void toggleGrid() {
		if (Field.zeroPointsPresent && Field.grid) {
			toggleZeroPointsPresent();
		}
		Field.grid = !Field.grid;
		Frame.field.redrawField();
	}

	public void toggleTickMarks() {
		Field.tick = !Field.tick;
		Frame.field.redrawField();
	}

	// This is just a pop up that appears the first time the line tool is used
	@SuppressWarnings("static-access")
	public void line() {

		String userHomeDirectory = System.getProperty("user.home");
		String configFolderPath = userHomeDirectory + File.separator + ".QuickChartsConfig";

		File configFolder = new File(configFolderPath);
		if (!configFolder.exists()) {
			boolean created = configFolder.mkdir();
			if (created) {
			} else {
			}
		} else {
		}

		String fileName = configFolderPath + "\\lineConfig.txt";
		Path filePath = Paths.get(fileName);

		if (Files.exists(filePath)) {

			try {
				BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
				String message = in.readLine();
				if (message.equals("Do Not Show Again")) {
				} else {
					new LineMessage();
				}
			} catch (IOException e) {
				// om = new OpenMessage();
				e.printStackTrace();
			}
		} else {

			new LineMessage();
		}

		Field.af.lineShapeClicked = true;
	}
	
	public void smartLine() {
		String userHomeDirectory = System.getProperty("user.home");
		String configFolderPath = userHomeDirectory + File.separator + ".QuickChartsConfig";

		File configFolder = new File(configFolderPath);
		if (!configFolder.exists()) {
			boolean created = configFolder.mkdir();
			if (created) {
			} else {
			}
		} else {
		}

		String fileName = configFolderPath + "\\smartLineConfig.txt";
		Path filePath = Paths.get(fileName);

		if (Files.exists(filePath)) {

			try {
				BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
				String message = in.readLine();
				if (message.equals("Do Not Show Again")) {
				} else {
					new smartLineMessage();
				}
			} catch (IOException e) {
				// om = new OpenMessage();
				e.printStackTrace();
			}
		} else {

			new smartLineMessage();
		}

		Field.af.smartLineShapeClicked = true;
	}

	public void circle() {

	}
	
	public void smartCircle() {
		
	}

	public void oval() {

	}

	public void rectangle() {

	}

	public void choseAudio() {
		try {
			openSound();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void openSound()
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter audioFilter = new ExtensionFilter("*.wav files", "*.wav");
		fileChooser.getExtensionFilters().add(audioFilter);

		soundFile = fileChooser.showOpenDialog(Main.primary);

		if (!soundFile.toString().endsWith(".wav")) {
			String message = "All sound files must be in .wav format.  Java is weird like that.";
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
			return;
		}
		if (soundFile != null) {
			Bottom.play.setVisible(true);
			Bottom.setTimes.setVisible(true);
		}
	}

	public static void setNote() {

		VBox noteEditor = new VBox();
		noteEditor.setAlignment(Pos.CENTER);
		noteEditor.setSpacing(5);
		noteEditor.setStyle("-fx-background-color: #c0c0c0;");
		noteEditor.setPadding(new Insets(20, 20, 20, 20));
		Label message = new Label("Enter notes for the current set: ");
		TextArea textArea = new TextArea();
		textArea.setPrefWidth(Field.width * Field.scale);
		textArea.setPrefRowCount(3);
		textArea.setWrapText(true);
		if (notes[Main.set.getValue() - 1] == "Note: ") {
			textArea.setText(null);
		} else {
			textArea.setText(notes[Main.set.getValue() - 1]);
		}

		Button button = new Button("Submit");
		button.setOnAction(event -> {
			String enteredText = textArea.getText();
			if (enteredText == null) {
				notes[Main.set.getValue() - 1] = "Note: ";
				note.setText(notes[Main.set.getValue() - 1]);
			} else {
				notes[Main.set.getValue() - 1] = enteredText;
				note.setText(notes[Main.set.getValue() - 1]);
			}
			Stage noteStage = (Stage) button.getScene().getWindow();

			// Close the stage
			noteStage.close();
		});

		Button cancel = new Button("Cancel");
		cancel.setOnAction(event -> {
			System.out.println("CLEEK");
			editStage.close();
		});

		HBox buttonBox = new HBox();
		buttonBox.setSpacing(15);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(button, cancel);

		noteEditor.getChildren().addAll(message, textArea, buttonBox);

		Scene editScene = new Scene(noteEditor);
		editStage = new Stage();
		editStage.initOwner(Main.primary);
		editStage.setTitle("Edit the current set's notes");
		editStage.setScene(editScene);
		editStage.setResizable(false);
		editStage.show();
	}

}

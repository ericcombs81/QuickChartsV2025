package main;
//Version 2.0.0 January 30, 2025 - April 14, 2025: Full Screen and zoom, multiple selection tool with right mouse button, snap to grid, print coordinate charts, new "how to" video and website upgrade.
//Version 1.1.0 May 17, 2024: Multi Select ability.  Deleted Demo.  Removed "How To Video" to website.
//Version 1.0.0, started April 2024
//Author: Eric Combs
//eric1981combs@gmail.com
//When updating the version, be sure to change the version in UpdateChecker.java as well, dude

import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {

    public static IntegerProperty set;
    public static Stage primary;
    public static Scene scene;
    public static Main mainInstance = new Main();
    public static OpenMessage om;
    public static Frame frame;
    public static String filePath = "";
    public static boolean exiting = false;
    public static boolean fullVersion = false;
    public static String unlockCode;
    public static String productKeyString;
    public static Boolean firstField = true; // set to false at full screen so message from net wont appear

    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        //This has to do with the file path of a file if you click it to open from the desktop
    	Parameters parameters = getParameters();
        if (parameters != null && !parameters.getRaw().isEmpty()) {
            // Get the file path from the command-line arguments
            filePath = parameters.getRaw().get(0);
            // Open the file specified by the file path

        }

        primary = primaryStage;
        set = new SimpleIntegerProperty(1);
        frame = new Frame(filePath);

        scene = new Scene(frame);

        // Set the key listener for the scene
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);

        primaryStage.setScene(scene);
        primaryStage.setTitle(
                "Eric Combs' QuickCharts V2.0.0                      Feel free to email the software designer at:     QuickChartsDrill@gmail.com  with bugs, suggestions, or for help.      \u00A9 2025 CodeCrafters, LLC ");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> {
            e.consume(); // Consume the event to prevent automatic window closing
            exiting = true;
            showExitConfirmation(); // Show exit confirmation dialog
        });
        primaryStage.show();

        // This code is for the pop-up message that opens the first time you open the application.
        String userHomeDirectory = System.getProperty("user.home");
        String configFolderPath = userHomeDirectory + File.separator + ".QuickChartsConfig";

        //If you have no config folder (so this is your first time or it was deleted)
        File configFolder = new File(configFolderPath);
        if (!configFolder.exists()) {
            boolean created = configFolder.mkdir();
            if (created) {
            } else {
            }
        } else {
        }

        //Opening Message. To show or not to show.
        String fileName = configFolderPath + "\\loadConfig.txt";
        Path filePath = Paths.get(fileName);

        if (Files.exists(filePath)) {

            try {
                BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
                String message = in.readLine();
                if (message.equals("Do Not Show Again")) {
                } else {
                    om = new OpenMessage();
                }
            } catch (IOException e) {
                om = new OpenMessage();
                e.printStackTrace();
            }
        } else {

            om = new OpenMessage();
        }
        
        
        //If MachineID file doesn't exist....create a file with a product key
        String MachineIDFile = configFolderPath + "\\MachineID.txt";
        filePath = Paths.get(MachineIDFile);
        
        if(!Files.exists(filePath)) {
        	try {
                // Generate a unique MachineID
        		String MachineID = getFinalMachineID();

                // Write the product key into MachineID.txt
                Files.write(filePath, MachineID.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace(); // Or log the error appropriately
            }
        } else {
        }
        
        //If product key file doesn't exist....create a file with a product key
        String productKey = configFolderPath + "\\productKey.txt";
        filePath = Paths.get(productKey);
        
        if(Files.exists(filePath)) {
        	//Since it does exist, read the key and generate the unlock code.
        	try {
                BufferedReader in = new BufferedReader(new FileReader(new File(productKey)));
                String key = in.readLine();
                productKeyString = key;
                unlockCode = Utilities.generateCode(key);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        	Utilities.createProductKey(filePath);
        	unlockCode = Utilities.generateCode(productKeyString);
        	System.out.println(productKeyString + " " + unlockCode);
        }
        
        
        //Lets check to see if this is a trial version or a full version:
        //Opening Message. To show or not to show.
        fileName = configFolderPath + "\\DoNotDelete.txt";
        filePath = Paths.get(fileName);

        if (Files.exists(filePath)) {

            try {
                BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
                String message = in.readLine();
                if (message.equals("Trial Version")) {
                	fullVersion = false;
                }
                if(message.equals("Don't alter or delete this file or you will lose your software.  You have the full version of the program, and I would not wrongly hack this if I were you, because it would be detrimental to the product creator's (a poor band director) financial situation, which is immoral and sad.  If you hack this, I will die of malnutrition and haunt you at night.  I will break into your band room and break all your reeds and dent all your brass valve casings.")) {
                	fullVersion = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Utilities.createVersionFile(filePath);
        }
        
        
        
        
        
        // To keep the buttons and text fields from taking focus, which makes the shortcut keys not work
        Frame.field.requestFocus();
    }

    // Handle the key pressed event
    private void handleKeyPressed(KeyEvent event) {
        KeyHandler.handlePressed(event);
    }

    private void handleKeyReleased(KeyEvent event) {
        KeyHandler.handleReleased(event);
    }

    private void showExitConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Save changes before exiting?");
        alert.setContentText("Changes will be lost if you don't save them.");

        ButtonType saveButtonType = new ButtonType("Save");
        ButtonType dontSaveButtonType = new ButtonType("Cancel");
        ButtonType exitButtonType = new ButtonType("Don't Save", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(saveButtonType, dontSaveButtonType, exitButtonType);

        alert.showAndWait().ifPresent(response -> {
            if (response == saveButtonType) {
               Frame.top.save();
            } else if (response == dontSaveButtonType) {
                exiting = false;
            } else if (response == exitButtonType) {
                // Handle exit action
                primary.close();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    //THE FOLLOWING ARE METHODS TO GENERATE THE HASHED MACHINEID
    public static String getCPUID() {
	    return runWMIC("wmic cpu get processorid");
	}

	// Motherboard
	public static String getMotherboardSerial() {
	    return runWMIC("wmic baseboard get serialnumber");
	}

	private static String runWMIC(String command) {
	    String result = "";
	    try {
	        Process process = Runtime.getRuntime().exec(command);
	        Scanner sc = new Scanner(process.getInputStream());
	        while (sc.hasNext()) {
	            String line = sc.next().trim();
	            if (!line.equalsIgnoreCase("ProcessorId") &&
	                !line.equalsIgnoreCase("SerialNumber")) {
	                result = line;
	                break;
	            }
	        }
	        sc.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	
	public static String getFinalMachineID() {
	    String raw = getCPUID() + getMotherboardSerial();
	    return hashSHA256(raw);
	}

	private static String hashSHA256(String input) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
	        StringBuilder hexString = new StringBuilder();
	        for (byte b : hash) {
	            hexString.append(String.format("%02x", b));
	        }
	        return hexString.toString();
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException(e);
	    }
	}
    
}
package main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import com.jme3.system.AppSettings;
import javafx.beans.property.SimpleIntegerProperty;


public class Utilities {

	public static void switchSets(int set) {
		
		if(Main.fullVersion == false) {
			if(set >= 5) {
				set = 4;
			}
		}

		
		// figure out if the desired set has any points in it.
		Boolean pointInSet = false;
		for (int i = 0; i < Field.balls.length; i++) {
			if (Field.balls[i] != null) {
				if (Field.balls[i].points[set - 1] != null) {
					pointInSet = true;
					break;
				}
			}
		}
		// If so, set the set to that set. Easy Peasy, rice and cheesy
		if (pointInSet) {
			
			Field.setSet(new SimpleIntegerProperty(set));
			Left.setLabel.setText("Set: " + Main.set.getValue().toString());
			Top.note.setText(Top.notes[Main.set.getValue() - 1]);
			Top.countsLabel.setText("Counts: " + Top.counts[Main.set.getValue() - 1]);
			// REPAINT THE FIELD
			return;
		}
		// Else, figure out what the last set was that had points in it.
		else {
			int lastExistingSet = 0;
			// for every single ball, find one that exists
			for (int i = 0; i < Field.balls.length; i++) {
				if (Field.balls[i] != null) {

					// And then find the last set that isn't null
					for (int j = 0; j < Field.balls[i].points.length; j++) {
						if (Field.balls[i].points[j] != null) {
							// Save that to lastExistingSet
							lastExistingSet = j;
						}
					}
				}
			}
			lastExistingSet += 1;
			// Copy those points into every single chart up to that point
			for (int i = 0; i < Field.balls.length; i++) {

				if (Field.balls[i] != null) {

					Double pointX = Field.balls[i].points[lastExistingSet - 1].getX();
					Double pointY = Field.balls[i].points[lastExistingSet - 1].getY();
					for (int j = lastExistingSet + 1; j <= set; j++) {
						Field.balls[i].points[j - 1] = new Point(pointX, pointY);
					}
				}
			}
			// Now that the selected set has points in it, set the set to set
			Field.setSet(new SimpleIntegerProperty(set));
			Left.setLabel.setText("Set: " + Main.set.getValue().toString());
			Top.note.setText(Top.notes[Main.set.getValue() - 1]);
			Top.countsLabel.setText("Counts: " + Top.counts[Main.set.getValue() - 1]);
		}
		// Repaint the hell out of this bad boy
		State state = new State();
		Frame.urt.undoPush(state);
		Field.af.drawFrame();
	}

	public static void start3D() {
		Java3D app = new Java3D();
		app.setShowSettings(false);
		AppSettings settings = new AppSettings(true);
		settings.setResolution(1280, 720);
		settings.setVSync(true);
		settings.setFullscreen(false);
		settings.setTitle("QuickCharts 3D Visualizer");
		app.setSettings(settings);
		app.start();
	}

	public static void start3DFull() {
		Java3D app = new Java3D();
		app.setShowSettings(false);
		AppSettings settings = new AppSettings(true);
		settings.setVSync(true);
		settings.setFullscreen(true);
		settings.setTitle("QuickCharts 3D Visualizer");
		app.setSettings(settings);
		app.start();
	}
	
	//This only happens if the DoNotDelete.txt version file doesn't exist
	public static void createProductKey(Path filePath) {
		
        // Generate a random product key
        String productKey = generateRandomKey(10);
        Main.unlockCode = generateCode(productKey);
        Main.productKeyString = productKey;
        

        // Write the product key to the specified file
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(productKey);
        } catch (IOException e) {
            System.err.println("Error writing product key to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String generateRandomKey(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
    
    //This is only called if the productVersion File doesn't exist
    public static void createVersionFile(Path filePath) {
    	// Write the file to disk
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write("Trial Version");
        } catch (IOException e) {
            System.err.println("Error writing product key to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static String generateCode(String productKey) {
        // Reverse the product key
        StringBuilder reversed = new StringBuilder(productKey).reverse();
        
        // Transform each character
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (Character.isDigit(c)) {
                // Handle digits
                c = (char) ((c - '0' + 1) % 10 + '0');
            } else if (Character.isLowerCase(c)) {
                // Handle lowercase letters
                c = (char) ((c - 'a' + 1) % 26 + 'a');
            } else if (Character.isUpperCase(c)) {
                // Handle uppercase letters
                c = (char) ((c - 'A' + 1) % 26 + 'A');
            }
            reversed.setCharAt(i, c);
        }
        
        return reversed.toString();
    }
    
    public static void snapToGrid() {

    	for(int i=0; i< Field.balls.length; i++) {
    		if(Field.balls[i] != null) {
    			if(Field.balls[i].isSelected) {
    				Double ballX = Field.balls[i].points[Main.set.getValue() - 1].getX() / (Frame.field.scale / 10);
    				Double ballY = Field.balls[i].points[Main.set.getValue() - 1].getY();/// (Frame.field.scale / 10);
    				ballX = (double) (Math.round(ballX / 12.5f) * 12.5);
    				
    				//Compute Y
    				double randomNumber = ballY;
    				double closest = findClosestInSortedArray(Field.gridYCoords, randomNumber);
    				
    				Field.balls[i].points[Main.set.getValue() - 1].x = ballX;
    				Field.balls[i].points[Main.set.getValue() - 1].y = closest / (Frame.field.scale / 10);
    				
    			}
    		}
    	}
    	
    	Field.af.drawFrame();
		State state = new State();
		Frame.urt.undoPush(state);
    }
    
    public static double findClosestInSortedArray(double[] array, double target) {
        if (array.length == 0) return Double.NaN;

        // Handle case where target is smaller than the first element
        if (target <= array[0]) {
            return array[0];
        }

        for (int i = 1; i < array.length; i++) {
            if (array[i] >= target) {
                // Compare target with the two nearest numbers
                double lower = array[i - 1];
                double higher = array[i];
                return (Math.abs(target - lower) <= Math.abs(target - higher)) ? lower : higher;
            }
        }

        // If target is greater than all elements, return the last one
        return array[array.length - 1];
    }

}

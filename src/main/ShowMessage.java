package main;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class ShowMessage {

    //This gets the text of message.txt off of the web and sends it back to Field.java
	public static String showMessage() throws IOException, java.util.NoSuchElementException {
        
		if(Main.fullVersion == true) {
		URL url = new URL("http://www.banddirectorsshare.com/QuickCharts/Messages/messageV2.txt");
        try (InputStream inputStream = url.openStream();
             Scanner scanner = new Scanner(inputStream)) {
            return scanner.nextLine(); // Assuming the version is provided as the first line in the text file
        } catch (java.util.NoSuchElementException e) {
        	System.out.println("No message to show");
        }
		} else {
			return "Thank you for trying the Trial Version of QuickCharts.  The trial version is limited to four sets.  To unlock the full version, click on \"Version\" \\ \"Unlock Full Version\" in the menu bar.";
		}
		return null;
    }
}
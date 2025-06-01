package main;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class UpdateChecker {
    private static final String VERSION_URL = "https://www.banddirectorsshare.com/QuickCharts/PurchaseVersion2/version.txt"; // URL to fetch latest version info
    public static final String CURRENT_VERSION = "2.0.0"; // Current version of the application


    public static void checkForUpdates() throws URISyntaxException {
        try {
            String latestVersion = fetchLatestVersion();
            if (isNewerVersionAvailable(latestVersion, CURRENT_VERSION)) {
                // Prompt the user with three options
                int choice = JOptionPane.showOptionDialog(null,
                        "An update is available! Do you want to download version " + latestVersion + "?",
                        "Update Available", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        new String[]{"Save and Download", "Download without Save", "Cancel"}, "Save and Download");
                if (choice == JOptionPane.YES_OPTION) {
                    // Perform the download operation here, saving the file before downloading
                    // You can use Desktop.browse() to open the download link in the default web browser
                	Frame.top.save();
                	String downloadURL = fetchLatestVersionUrl();
                    Desktop.getDesktop().browse(new URI(downloadURL));
                    // Exit the program
                    System.exit(0);
                } else if (choice == JOptionPane.NO_OPTION) {
                	String downloadURL = fetchLatestVersionUrl();
                    Desktop.getDesktop().browse(new URI(downloadURL));
                    // Exit the program
                    System.exit(0);
                    
                } else if (choice == JOptionPane.CANCEL_OPTION) {
                    // Cancel the update process
                }
            } else {
                // Inform the user that they are using the latest version
                JOptionPane.showMessageDialog(null, "You're using the latest version.", "No Updates", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            // Display an error message if failed to check for updates
            JOptionPane.showMessageDialog(null, "Failed to check for updates: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static String fetchLatestVersion() throws IOException {
        URL url = new URL(VERSION_URL);
        try (InputStream inputStream = url.openStream();
             Scanner scanner = new Scanner(inputStream)) {
            return scanner.nextLine(); // Assuming the version is provided as the first line in the text file
        }
    }

    public static boolean isNewerVersionAvailable(String latestVersion, String currentVersion) {
        // Compare version strings (you might need more sophisticated version comparison logic)
        return latestVersion.compareTo(currentVersion) > 0;
    }
    
    private static String fetchLatestVersionUrl() throws IOException {
        URL url = new URL("http://www.banddirectorsshare.com/version.txt");
        System.out.println(url);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            // Skip the first line
            reader.readLine();
            // Read the URL from the second line
            String urlLine = reader.readLine();
            // Close the reader
            reader.close();
            // Return the URL
            return urlLine.trim();
        }
    }
}
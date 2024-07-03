/**
 * Author: Ibraheem Dawod
 * Date: 01/17/2024
 * Description: Utility class for handling player input in a Sudoku game.
 * All methods are static
 * 
 * Methods:
 * - getNumInput(String strMessage): Obtains a byte input from the user.
 * - getNumInput(String strMessage, byte bytMin, byte bytMax): Obtains a byte input within a specified range from the user.
 * - getStringInput(String strMessage): Obtains a string input from the user.
 * - getFileInput(String strMessage): Displays available text files and returns the selected file name.
 */

// Import the Scanner and all of File IO
import java.util.Scanner;
import java.io.*;

public class PlayerInput {

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Gets a numeric input from the user between 1 and 9.
     * Displays the specified message, prompts the user for input, and handles invalid input.
     * The method continues to prompt until the user enters a valid numeric value between 1 and 9.
     * Returns the validated numeric input.
     */
    public static byte getNumInput(String strMessage) {
        // Variables to store the input and the boolean controlling the while loop(validating the input)
        byte bytInput = 0;
        boolean bolTryCatch = false;

        // Create a loop to repeatedly prompt the user until valid input is received
        do {
            System.out.println(strMessage);

            // Attempt to read a byte from the user's input
            try {
                bytInput = new Scanner(System.in).nextByte();
                bolTryCatch = true; // Set true to end loop, input is valid
            } catch (Exception e) {
                // Handle the exception if the user enters non-numeric input
                System.out.println("Please enter in a number value.");
            }

            // Check if the entered value is outside the specified range
            if (bytInput > 9 || bytInput < 1) {
                System.out.println("Please enter in a number between 1 and 9.");
                bolTryCatch = false; // Set to false to loop again
            }
        } while (!bolTryCatch);

        // Return the validated numeric input
        return bytInput;
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Gets a numeric input within a specified range from the user.
     * Displays the specified message, prompts the user for input, and handles invalid input.
     * The method continues to prompt until the user enters a valid numeric value within the specified range.
     * Overloads the previous method, taking in 3 parameters instead of 1.
     * Returns the validated numeric input.
     */
    public static byte getNumInput(String strMessage, byte bytMin, byte bytMax) {
        
        // Variables to store the input and the boolean controlling the while loop(validating the input)
        byte bytInput = 0;
        boolean bolTryCatch = false;

        // Create a loop to repeatedly prompt the user until valid input is received
        do {
            System.out.println(strMessage);

            // Attempt to read a byte from the user's input
            try {
                
                // Take a byte input of the user
                bytInput = new Scanner(System.in).nextByte();
                bolTryCatch = true; // Set true to break loop, input is valid
            } catch (Exception e) {
                // Handle the exception if the user enters non-numeric input
                System.out.println("Please enter in a number value.");
            }

            // Check if the entered value is outside the specified range
            if (bytInput > bytMax || bytInput < bytMin) {
                System.out.println("Please enter in a number between " + bytMin + " and " + bytMax + ".");
                bolTryCatch = false; // Set to false to loop again
            }
        } while (!bolTryCatch);

        // Return the validated numeric input
        return bytInput;
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Gets a string input from the user.
     * Displays the specified message and prompts the user for a string input.
     * Returns the string input provided by the user.
     */
    public static String getStringInput(String strMessage) {
        System.out.println(strMessage);

        // Get a String input from the user
        return new Scanner(System.in).nextLine();
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Gets a file input from the user.
     * Displays the specified message, lists available .txt files in the current directory, 
     * and prompts the user to choose a file. Returns the selected file name.
     */
    public static String getFileInput(String strMessage) {
        
        // Initialize variables for method
        String strFileName;
        byte bytFileChoice;
        
        // Print out message provided
        System.out.println(strMessage);
        

        // Retrieve the current working directory path using System.getProperty("user.dir")
        // Create a File object representing the current working directory
        // List files in the directory with a filter to include only those ending with ".txt"
        File[] files = new File(System.getProperty("user.dir")).listFiles((dir, name) -> name.endsWith(".txt"));

        // Print out all available files to user
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + ": " + files[i].getName());
        }

        // Get the choice of the file from the user
        bytFileChoice = getNumInput("", (byte)1, (byte) (files.length));

        // Show the file the user is playing from
        System.out.println("Playing from " + files[bytFileChoice - 1].getName() + ": ");

        // Return the selected file name
        // Subtract one to keep it in the length of the array
        return files[bytFileChoice - 1].getName();
    }
}
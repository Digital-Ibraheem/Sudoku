/**
 * Author: Ibraheem Dawod
 * Date: 01/17/2024
 * Description: GameBoard class serves as the fundamental structure for a Sudoku game, managing core functionalities.
 * It handles the game boards (answer and playing boards), player mistakes, and offers methods for various game operations,
 * including move validation, game saving, and checking if the board is completely filled.
 * 
 * Methods:
 * - GameBoard(): Default constructor that populates the boards using the CreateBoard class.
 * - GameBoard(String strFileName): Constructor with a parameter for loading a game from a file.
 * - populateBoard(String strFileName): Populates the boards from a specified file.
 * - validateMove(byte bytUserRow, byte bytUserCol, byte bytAns): Validates a player's move on the board.
 * - saveGame(): Saves the current game state to a file.
 * - isBoardFilled(): Checks if the Sudoku board is completely filled.
 * - toString(): Overrides the default toString method to represent the GameBoard as a formatted string.
 */

// Imports all of java IO
import java.io.*;

// Imports all java utilities
import java.util.*;
public class GameBoard
{
    // Two-dimensional arrays representing the answer and playing boards
    // These are protected so that they can't be accessed outside of this class except to children of GameBoard(SpecialBoard)
    protected byte[][] bytAnswerBoard = new byte[9][9];
    protected byte[][] bytPlayingBoard = new byte[9][9];

    // Number of mistakes made by the player
    // This is protected so that it can't be accessed outside of this class except to children of GameBoard(SpecialBoard)
    protected byte bytMistakes;

    // Flag indicating whether the game is won
    private boolean bolGameWon;

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Default constructor for GameBoard class.
     * Populates the game boards using the CreateBoard class.
     */
    public GameBoard() {
        
        // Call the populateBoard method in CreateBOard
        CreateBoard.populateBoard();
        
        // Reset mistakes
        this.bytMistakes = 0;
        
        // Copy both arrays from the CreateBoard class to this class
        for (int i = 0; i < bytAnswerBoard.length; i++) {
            for (int j = 0; j < bytAnswerBoard[i].length; j++) {
                this.bytPlayingBoard[i][j] = CreateBoard.getBytPlayingBoard()[i][j];
                this.bytAnswerBoard[i][j] = CreateBoard.getBytAnswerBoard()[i][j];
            }
        }
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Constructor for GameBoard class with a file name.
     * Populates the game boards using the provided file.
     * Takes in strFileName, the name of the file containing the Sudoku puzzle.
     */
    public GameBoard(String strFileName) {
        this.bytMistakes = 0;
     
        // call the function to populate board
        populateBoard(strFileName);
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Getter method to check if the game is won.
     * Returns true if the game is won, false otherwise.
     */
    public boolean getBolGameWon() {
        return bolGameWon;
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Populates the game boards from a file.
     * The method reads the provided file and fills both the answer and playing boards.
     * Takes in strFileNam, the name of the file containing the Sudoku puzzle.
     */
    public void populateBoard(String strFileName) {
        
        // Variable String to store the read-in line
        String strLine;

        // Create a try-catch block to catch any IO, EOF, or FileNotFoundException exceptions
        try 
        {
            // Create a new file reader using a Scanner to read from the specified file
            Scanner in = new Scanner(new FileReader(strFileName));

            // Loop through each row of the playing board
            for(int i = 0; i < bytPlayingBoard.length; i++) {
                // Set strLine to the next line of the file
                strLine = in.nextLine();

                // Loop through each column of the playing board
                for(int j = 0; j < bytPlayingBoard[i].length; j++) {
                    // Convert each character in the line to a numeric value and set it in the playing board
                    bytPlayingBoard[i][j] = (byte)(Character.getNumericValue(strLine.charAt(j)));
                }
            }

            // Loop through each row of the answer board
            for(int i = 0; i < bytAnswerBoard.length; i++) {
                // Set strLine to the next line of the file
                strLine = in.nextLine();

                // Loop through each column of the answer board
                for(int j = 0; j < bytAnswerBoard[i].length; j++) {
                    // Convert each character in the line to a numeric value and set it in the answer board
                    bytAnswerBoard[i][j] = (byte)(Character.getNumericValue(strLine.charAt(j)));
                }
            }

            // Close the file reader to release the associated resources
            in.close();
        }
        catch (FileNotFoundException e) 
        {
            // Handle the case where the specified file is not found
            System.out.println("Error: Cannot open file for reading");
        } 
        catch (NoSuchElementException e) 
        {
            // Handle the case where the end of the file is reached prematurely
            System.out.println("Error: EOF encountered, file may be corrupt");
        } 
        catch (IOException e) 
        {
            // Handle the case where an IO exception occurs
            System.out.println("Error: Cannot read from file");
        }
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Validates the user's move and updates the game state.
     * This method checks if the user's move is correct and updates the playing board accordingly.
     * It also handles mistakes and provides feedback to the user.
     * Takes in bytUserRow(row index of move), bytUserCol, bytAns(users guess)
     * Returns true if the move is valid, false otherwise.
     */
    public boolean validateMove(byte bytUserRow, byte bytUserCol, byte bytAns) {
        // Check if the user's guess matches the answer at the specified position
        // Also make sure that the user isn't guessing a number already been revaled
        if (bytAnswerBoard[bytUserRow - 1][bytUserCol - 1] == bytAns && bytPlayingBoard[bytUserRow - 1][bytUserCol - 1] == 0) {
            // Display a message indicating that the guess is correct
            System.out.println("\nYour guess is correct! (" + bytUserRow + ", " + bytUserCol + ") has been revealed to be " + bytAns + ".");

            // Update the playing board with the correct answer
            bytPlayingBoard[bytUserRow - 1][bytUserCol - 1] = bytAns;

            // Return true to allow the game to keep going
            return true;
        
        // If the number being guessed has already been revealed
        } else if(bytPlayingBoard[bytUserRow - 1][bytUserCol - 1] != 0){
            
            System.out.println("That number has already been revealed - you can't guess it again!");
            
            // Return true to allow the game to keep going
            return true;
        } else {
            // Increment the mistakes counter
            bytMistakes++;

            // Check if the user has reached the maximum allowed mistakes (3)
            if (bytMistakes == 3) {
                // Prompt the user for further action when three mistakes are made
                if (PlayerInput.getNumInput("\nUnfortunately, that guess is incorrect. Would you like to save your game to play later or end the game?\n1: Save your game\n2: End the game", (byte) 1, (byte) 2) == 1) {
                    // Reset the mistakes score to 0 and return true to continue the game
                    bytMistakes = 0;
                    saveGame();
                    return false;
                } else {
                    // Return false to indicate the user wants to return to the main menu
                    return false;
                }
            } else {
                // Display a message indicating an incorrect guess and the remaining mistakes
                System.out.println("\nUnfortunately, that guess is incorrect. You have " + (3 - bytMistakes) + " mistakes remaining.");

                // Return true to allow the user to continue making moves
                return true;
            }
        }
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Saves the current game state to a file.
     * This method prompts the user for a file name, checks its validity, and then writes the current
     * playing board and answer board to a text file. Any non-alphanumeric characters are not allowed in the file name.
     */
    public void saveGame() {
        // Regular expression to match alphanumeric characters
        // ^ means start of line, $ means end of line
        // a-z means all lowercase letters, A-Z means all uppercase letters
        String strRegex = "^[a-zA-Z0-9]+$";

        // Variables for file name and format validation
        String strFileName;
        boolean bolRightFormat;

        // Prompt the user for a valid file name
        do {
            strFileName = PlayerInput.getStringInput("\nPlease enter in the name of the file you would like to save the board to:");

            // Validate the file name format using the regular expression
            if (strFileName.matches(strRegex)) {
                bolRightFormat = true;
            } else {

                // Display an error message for invalid file name format
                System.out.println("\nPlease enter in an alphanumeric name for the file.\n");
                bolRightFormat = false;
            }

        } while (!bolRightFormat);

        try {

            // Create a new file writer with the provided or default file name
            PrintWriter out = new PrintWriter(new FileWriter(strFileName + ".txt"));

            // Write the playing board to the file
            for (int i = 0; i < bytPlayingBoard.length; i++) {
                for (int j = 0; j < bytPlayingBoard[i].length; j++) {
                    out.print(bytPlayingBoard[i][j]);
                }
                out.println(); // Move to the next line after each row
            }

            // Write the answer board to the file
            for (int i = 0; i < bytAnswerBoard.length; i++) {
                for (int j = 0; j < bytAnswerBoard[i].length; j++) {
                    out.print(bytAnswerBoard[i][j]);
                }
                out.println(); // Move to the next line after each row
            }

            // Close the file writer
            out.close();
        } catch (IOException e) {

            // Display an error message if writing to the file fails
            System.out.println("Error: Cannot write to file");
        }
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Checks if the game board is completely filled.
     * This method iterates through each cell of the playing board and checks if any cell is still empty (value 0).
     * If an empty cell is found, the method returns false, indicating the board is not fully filled.
     * If no empty cells are found, it sets the 'bolGameWon' variable to true and returns true, indicating
     * the board is fully filled.
     */
    public boolean isBoardFilled() {
        
        // Iterate through each row of the playing board
        for (int i = 0; i < bytPlayingBoard.length; i++) {
            
            // Iterate through each cell in the row
            for (int j = 0; j < bytPlayingBoard[i].length; j++) {
                
                // Check if the current cell is empty (value 0)
                if (bytPlayingBoard[i][j] == 0) {
                    
                    // Return false if an empty cell is found
                    return bolGameWon;
                }
            }
        }

        // If no empty cells are found, set 'bolGameWon' to true and return true
        bolGameWon = true;
        return bolGameWon;
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Overrides the toString method to represent the GameBoard as a string.
     * Formats the output with mistakes and the Sudoku board.
     * Returns String representation of the GameBoard.
     */
    @Override
    public String toString() {
        
        // Create a string to represent the output, including mistakes
        String strOutput = "\nMistakes: " + bytMistakes + "/3\n\n";

        // Add spaces and column numbers to the output string
        strOutput += "    ";
        for(int i=0; i<9; i++){

            // Add extra space every 3 columns
            if(i%3 == 0 && i!=0) {
                strOutput += "  ";
            }

            // Add the column number to the output
            strOutput += (i+1) + " ";
        }
        strOutput += "\n   ";

        // Add horizontal lines to the output
        for(int i=0; i<12; i++){
            strOutput += "--";
        }
        strOutput += "\n";

        // Loop through each row of the playing board
        for (int i = 0; i < bytPlayingBoard.length; i++) {

            // Add extra space and horizontal line every 3 rows
            if((i)%3 == 0 && i!=0) {
                strOutput += "    ";
                for(int k=0; k<21; k++){
                    strOutput += "-";
                }
                strOutput += "\n" + (i+1) + " | ";
            } else {

                // Add row number and separator to the output
                strOutput += (i+1) + " | ";
            }

            // Loop through each cell in the row
            for (int j = 0; j < bytPlayingBoard[i].length; j++) {

                // Add vertical separator every 3 columns
                if((j)%3 == 0 && j!=0) {
                    strOutput += "| ";
                }

                // Add cell value (or "●" if the cell is empty(0)) to the output
                strOutput += (bytPlayingBoard[i][j] == 0 ? "●" : bytPlayingBoard[i][j])  + " ";
            }

            // Add the closing vertical separator and new line to the output
            strOutput += " |\n";
        }

        // Add horizontal lines to the bottom of the output
        strOutput += "   ";
        for(int i=0; i<12; i++){
            strOutput += "--";
        }

        // Return the final string representation of the SpecialBoard
        return strOutput;
    }
}

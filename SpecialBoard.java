/**
 * Author: Ibraheem Dawod
 * Date: 01/17/2024
 * Description: SpecialBoard class represents an extended version of a Sudoku game board, inheriting from the GameBoard class.
 * It introduces additional features such as a timer, hints, and associated functionalities. The timer enforces a 30-minute time limit 
 * for completing the game, and players can use hints to reveal random empty cells on the board. SpecialBoard overrides the parent 
 * class's toString method, providing a formatted string representation that includes the timer, hints remaining, mistakes, and the 
 * Sudoku board.
 * 
 * Methods:
 * - SpecialBoard(): Default constructor that initializes the number of hints, timer length, and start time.
 * - SpecialBoard(String strFileName, byte bytHints, int startTime): Constructor with parameters for file name, hints, and start time.
 * - checkTimeLimit(): Checks if the time limit of 30 minutes has been reached.
 * - getHint(): Provides a hint to the player by revealing a random empty cell on the board.
 * - toString(): Overrides the parent class toString method to represent the SpecialBoard as a formatted string.
 */

// Import to allow us to reformat our text
import java.text.DecimalFormat;

public class SpecialBoard extends GameBoard {

    // Number of hints available. Length of timer in minutes
    private byte bytHints, bytTimer; 
    
    // Variables to hold and calculate the time elapsed for the current game
    private short shrStartTime = 0;
    private short shrTimeElapsedSinceStart = 0;

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Default constructor for SpecialBoard class. Initializes the number of hints, timer length, and start time.
     */
    public SpecialBoard() {

        // Calls the default constructor of the GameBoard superclass
        super(); 

        // Initializes the number of hints to 3
        this.bytHints = 3;

        // Initializes the timer length to 30 minutes
        this.bytTimer = 30;         
        
        // Converts current time to seconds and sets as start time
        this.shrStartTime =  (short) (System.currentTimeMillis() / 1000 - this.shrTimeElapsedSinceStart); 
        
        // Adds up every startTime so we can calculate the new start time for the next special game
        this.shrTimeElapsedSinceStart += this.shrStartTime;
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Constructor for SpecialBoard class with a file name, number of hints, and start time.
     * Initializes the board from a file, sets the start time, and initializes the number of hints.
     */
    public SpecialBoard(String strFileName, byte bytHints, byte bytTimer) {
        
        // Calls the superclass constructor with a file name parameter
        super(strFileName);
        
        // Initializes the number of hints to 3
        this.bytHints = bytHints;

        // Initializes the timer length to 30 minutes
        this.bytTimer = bytTimer;
        
        // Converts current time to seconds and sets as start time
        this.shrStartTime =  (short) (System.currentTimeMillis() / 1000 - this.shrTimeElapsedSinceStart); 
        
        // Adds up every startTime so 
        this.shrTimeElapsedSinceStart += this.shrStartTime;
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Checks if the time limit of 30 minutes has been reached.
     * 
     * Prints a message if the limit is reached and returns false, else returns true.
     */
    public boolean checkTimeLimit() {
        
        // Uses System to get the current time in  milliseconds, then converts to seconds
        short shrElapsedTime = (short)((System.currentTimeMillis() / 1000) - shrStartTime); // Calculates elapsed time in seconds
        short shrTimeLimit =  (short)(bytTimer * 60); // Converts timer length from minutes to seconds

        // Once time runs out, return false
        if (shrElapsedTime >= shrTimeLimit) {
            System.out.println("\n\nThe time limit of 30 minutes has been reached! Try a different strategy next time.");
            return false; // Returns false if time limit is reached
        }
        return true; // Returns true if time limit is not reached
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Provides a hint to the player by revealing a random empty cell on the board.
     * 
     * Decreases the number of available hints and prints the hint location and the value from the answer board.
     * If no hints are available, prints a message indicating so.
     */
    public void getHint() {
        byte bytRandomRow, bytRandomCol;
        if(bytHints > 0) {

            // Generate random row and column until an empty cell is found
            do {
                
                // Random number between 1 and 9 for row and column
                bytRandomRow = (byte)((Math.random() * 9) + 1); 
                bytRandomCol  = (byte)((Math.random() * 9) + 1);

                // Using the parents bytPlayingBoard variable, check if the current random coordinate is not equal to 0. If it isn't then generate another one
            } while(super.bytPlayingBoard[bytRandomRow-1][bytRandomCol-1] != 0);

            // Set the playing board cell with the value from the answer board
            super.bytPlayingBoard[bytRandomRow-1][bytRandomCol-1] = super.bytAnswerBoard[bytRandomRow-1][bytRandomCol-1];

            bytHints--; // Decrease the number of available hints

            // Print hint information
            // Use the parent classes value of bytAnswerBoard coordinate to show hint being revealed
            System.out.println("\nYou used a hint. You now have " + bytHints + " left! (" + bytRandomRow + ", " + bytRandomCol + ") has been revealed to be " + super.bytAnswerBoard[bytRandomRow-1][bytRandomCol-1] + ".");
        } else {
            System.out.println("\nUnfortunately, you have no more hints to use. :(");
        }
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Overrides the toString method to represent the SpecialBoard as a string.
     * 
     * Formats the output with a timer, hints remaining, mistakes, and the Sudoku board. Overrides the parent class toString.
     * Returns String representation of the SpecialBoard.
     */
    @Override
    public String toString() {
        // Calculate the elapsed time in seconds by subtracting the start time from the current time
        short shrElapsedTime = (short)((System.currentTimeMillis() / 1000) - shrStartTime); // Convert to seconds

        // Calculate minutes and seconds from the elapsed time
        byte bytMinutes = (byte)(shrElapsedTime / 60);
        short shrSeconds = (short)(shrElapsedTime % 60);

        // Create a decimal format for formatting minutes and seconds with leading zeros, using DecimalFormat input
        DecimalFormat decimalFormat = new DecimalFormat("00");

        // Create a string to represent the output, including timer, hints remaining, and mistakes
        String strOutput = "\nTimer: " + decimalFormat.format(bytMinutes) + ":" + decimalFormat.format(shrSeconds) + "\tHints Remaining: " + bytHints + "\tMistakes: " + bytMistakes + "/3\n\n";

        // Add spaces and column numbers to the output string
        strOutput += "    ";
        for(int i = 0; i < 9; i++){
            
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

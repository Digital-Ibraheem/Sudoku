/**
 * Author: Ibraheem Dawod
 * Date: 01/17/2024
 * Description: CreateBoard class generates a completely unique Sudoku board (answer and playing boards).
 * The class includes recursive methods to populate the board, remove digits to create a playable board, and check the 3x3 matrix.
 * Every method and variable in this class is static, as it used to populate the GameBoard and SpecialBoard classes(instances of these classes).
 *
 * Methods:
 * - populateBoard(): Populates the answer board with a unique solution.
 * - removeDigits(): Removes a specified number of digits to create a playable board.
 * - createBoard(): Recursive method to create a Sudoku board.
 * - check3x3Matrix(): Checks if a number already exists in the 3x3 matrix of the Sudoku board.
 */

// Import Math to use operations
import java.lang.Math;

// Import ArrayList
import java.util.ArrayList;
public class CreateBoard {
    // Static arrays to store the answer and playing boards
    private static byte[][] bytAnswerBoard = new byte[9][9];
    private static byte[][] bytPlayingBoard = new byte[9][9];

    // Static variables for the current row and column during board generation
    private static byte bytRow;
    private static byte bytCol;

    // ArrayList to store numbers that have been tried for a cell during board generation
    private static ArrayList<Byte> bytNums = new ArrayList<Byte>();

    // Number of digits to remove from the playable board
    private static byte bytDigitsToRemove = 40;

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Getter methods to retrieve the playing and answer boards.
     */
    public static byte[][] getBytAnswerBoard() {
        return bytAnswerBoard;
    }

    public static byte[][] getBytPlayingBoard() {
        return bytPlayingBoard;
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Populates the answer board with a unique solution.
     * This method initializes the row, column, and list of tried numbers.
     * It then generates the Sudoku board using the createBoard method.
     * Finally, it removes a specified number of digits to create a playable board.
     */
    public static void populateBoard() {
        // Initialize row, column, and the list of tried numbers
        bytRow = 0;
        bytCol = 0;
        bytNums.clear();

        // Initialize the answer board with zeros
        for (int i = 0; i < bytAnswerBoard.length; i++) {
            for (int j = 0; j < bytAnswerBoard[i].length; j++) {
                bytAnswerBoard[i][j] = 0;
            }
        }

        // Generate the Sudoku board using the createBoard method
        // While createBoard doesn't return true, call it again with the following parameters
        while (!createBoard(bytRow, bytCol, (byte) ((Math.random() * 9) + 1), (byte) 1)) {
            // Update row and column based on the result of the createBoard method

            // If reached the end of the column, go to the next row
            if (bytCol == 8) {
                bytCol = 0;
                bytRow++;

                // Otherwise just increment column
            } else {
                bytCol++;
            }
        }

        // Remove a specified number of digits to create a playable board
        removeDigits();
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Removes a specified number of digits to create a playable board.
     * This method initializes a counter for the number of digits to remove.
     * It copies the answer board to the playing board and randomly removes digits.
     */
    private static void removeDigits() {
        // Counter for the number of digits to remove
        byte bytCount = bytDigitsToRemove;

        // Copy the answer board to the playing board
        for (int i = 0; i < bytAnswerBoard.length; i++) {
            for (int j = 0; j < bytAnswerBoard[i].length; j++) {
                bytPlayingBoard[i][j] = bytAnswerBoard[i][j];
            }
        }

        // Randomly remove digits from the playing board
        while (bytCount > 0) {
            // Generate random row and column indices
            byte bytRowElement = (byte) (Math.random() * 9);
            byte bytColElement = (byte) (Math.random() * 9);

            // Check if the cell is not empty
            if (bytPlayingBoard[bytRowElement][bytColElement] != 0) {
                // Set the cell to 0 (remove digit) and decrement the counter
                bytPlayingBoard[bytRowElement][bytColElement] = 0;
                bytCount--;
            }
        }
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Recursively generates a Sudoku board by filling cells with random values.
     * 
     * This method is called recursively to create a Sudoku board, attempting to fill each cell
     * with a random value. It follows Sudoku rules, ensuring that no row, column, or 3x3 matrix
     * contains duplicate values. If a dead-end is reached during the generation, the method backtracks
     * and tries alternative values. This process continues until the entire board is filled.
     * Takes in the row, column, number to be checked, and counter for recursion depth.
     * Returns true having reached last element, otherwise returns false for the while loop in populateBoard.
     */
    private static boolean createBoard(byte bytRowElement, byte bytColElement, byte bytNumber, byte bytCounter) {

        // As long as this current number hasn't been set, and the counter hasn't reached 9(will be explained below)
        if(bytAnswerBoard[bytRowElement][bytColElement] == 0 || bytCounter == 9) {

            // For loop to check if number is in row
            for (int k = 0; k < 9; k++) {
                
                // Goes through 9 elements in the row checking if any of them are equal to the number we want to insert
                // If they are, enter the following if statement
                if (bytAnswerBoard[bytRowElement][k] == bytNumber) {

                    // If counter has reached 9
                    if (bytCounter == 9) {

                        // Set this current element back to 0 and backtrack(trying other numbers)
                        bytAnswerBoard[bytRowElement][bytColElement] = 0;

                        // If there are elements in the bytNums array, use those elements as guesses
                        if(bytNums.size() > 0) {

                            // If at the first element, stop backtracking and enter the first element in bytNums array as guess
                            if(bytColElement == 0 && bytRowElement == 0) {
                                return createBoard(bytRowElement, bytColElement, bytNums.get(0), (byte)1);

                                // Back track up a row
                            } else if(bytColElement == 0) {
                                return createBoard((byte)(bytRowElement-1), (byte)8, bytNums.get(0), bytCounter);

                                // Back track to the left a column
                            } else {
                                return createBoard(bytRowElement, (byte)(bytColElement - 1), bytNums.get(0), bytCounter);
                            }
                            // If there are no elements in the bytNums array
                        } else {
                            // Check if in the first column, but not the first row
                            if(bytColElement == 0 && bytRowElement != 0) {

                                // Go back a row, and set column to 8
                                return createBoard((byte)(bytRowElement - 1), (byte)8, bytNumber, bytCounter);
                            } else {

                                // Go back a column
                                return createBoard(bytRowElement, (byte)(bytColElement - 1), bytNumber, bytCounter);
                            }
                        }
                        // Counter has NOT reached 9
                    } else {

                        // If adding one to the number hoping to be added makes it greater than 9, set this back to 1
                        if((bytNumber+1) > 9) {
                            return createBoard(bytRowElement, bytColElement, (byte)1, bytCounter);

                            // Otherwise increment the number(this ensures it goes evenly from the initial random number iterating through all numbers 1-9)
                        } else {
                            return createBoard(bytRowElement, bytColElement, (byte)(bytNumber + 1), bytCounter);
                        }
                    }
                }
            }

            // If the counter is 9 and the current element hasn't been added to the bytNums array yet, add it
            if(bytCounter == 9 && !bytNums.contains(bytNumber)) {
                bytNums.add(bytNumber);
            }

            // Now are checking if the number is in the column
            for(int k=0; k<9; k++) {
                if(bytAnswerBoard[k][bytColElement] == bytNumber) {
                    // Number already exists in the column

                    // If counter is 9
                    if (bytCounter == 9) {

                        // Set this current element back to 0 and backtrack
                        bytAnswerBoard[bytRowElement][bytColElement] = 0;

                        // Don't backtrack if it's the first element
                        if(bytColElement == 0 && bytRowElement == 0) {
                            return createBoard(bytRowElement, bytColElement, bytNumber, (byte)1);

                            // Backtrack up a row
                        } else if(bytColElement == 0) {
                            return createBoard((byte)(bytRowElement-1), (byte)8, bytNumber, bytCounter);

                            // Backtrack left a column
                        } else {
                            return createBoard(bytRowElement, (byte)(bytColElement - 1), bytNumber, bytCounter);
                        }
                        // If counter is not 9
                    } else {

                        // If adding one to the number hoping to be added makes it greater than 9, set this back to 1
                        if((bytNumber+1) > 9) {

                            // Here we are incrementing the counter. The counter acts as a way to see if we reach a point where no number works in that square.
                            // What this does is it increments, and once it reaches 9(i.e the entire column), that means there is a number that works in the row but not in the column.
                            // Since we are filling the array row by row, it is necesarry it works in the row but not immediately necesarry it works in the column
                            // So whenever there is a bytCounter == 9, the current r value works in the row but not in the column, which makes backtracking much more efficient as it only tries numbers that work in the column
                            return createBoard(bytRowElement, bytColElement, (byte)1, (byte)(bytCounter + 1));

                            // Otherwise increment the number(this ensures it goes evenly from the initial random number iterating through all numbers 1-9)
                        } else {
                            return createBoard(bytRowElement, bytColElement, (byte)(bytNumber + 1), (byte)(bytCounter + 1));
                        }
                    }
                }
            }

            // Check 3x3 matrix if number exists
            if (!check3x3Matrix(bytRowElement, bytColElement, bytNumber)) {

                // If counter is 9
                if (bytCounter == 9) {

                    // Set current cell to 0 and backtrack
                    bytAnswerBoard[bytRowElement][bytColElement] = 0;

                    // Backtrack a row up, column to 8
                    if(bytColElement==0) {
                        return createBoard((byte)(bytRowElement - 1), (byte)8, bytNumber, bytCounter);
                    } else {

                        // Backtrack a column to the left
                        return createBoard(bytRowElement, (byte)(bytColElement - 1), bytNumber, bytCounter);
                    }

                    // If counter is not 9
                } else {

                    // If adding one to the number hoping to be added makes it greater than 9, set this back to 1
                    if((bytNumber+1) > 9) {

                        // Counter increments for same reason as it does in the column for loop
                        return createBoard(bytRowElement, bytColElement, (byte)1, (byte)(bytCounter+1));

                        // Otherwise increment the number(this ensures it goes evenly from the initial random number iterating through all numbers 1-9)
                    } else {
                        return createBoard(bytRowElement, bytColElement, (byte)(bytNumber+1), (byte)(bytCounter+1));
                    }
                }
            }

        }

        // The number can be inserted in the current position
        bytRow = bytRowElement;
        bytCol = bytColElement;
        bytAnswerBoard[bytRowElement][bytColElement] = bytNumber;

        // Once row and column is 8, we have reached the end of the array and board is filled
        if(bytRowElement == 8 && bytColElement == 8) {
            return true;

            // Return false to return to the while loop and allow it to insert the next number into the cell in order
        } else {
            return false;
        }
    }

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: Checks whether placing a specific value in a 3x3 matrix violates Sudoku rules.
     * This method verifies if placing a given value in a 3x3 matrix starting from the specified
     * position (i, j) would violate Sudoku rules. It checks for duplicate values within the
     * corresponding 3x3 matrix and returns true if the placement is valid, and false otherwise.
     * 
     * Takes in the row, column and the number that wishes to be placed
     */
    private static boolean check3x3Matrix(byte bytRowElement, byte bytColElement, byte bytNumber) {
        // Calculate the starting row index of the 3x3 matrix based on the current row index
        byte bytStartRow = (byte)(bytRowElement - bytRowElement % 3);

        // Calculate the starting column index of the 3x3 matrix based on the current column index
        byte bytStartCol = (byte)(bytColElement - bytColElement % 3);

        // Loop through each matrix row of the 3x3 matrix
        for (int i = 0; i < 3; i++) {
            // Loop through each matrix column of the 3x3 matrix
            for (int j = 0; j < 3; j++) {
                // Check if the current number already exists in the 3x3 matrix
                if (bytAnswerBoard[bytStartRow + i][bytStartCol + j] == bytNumber) {
                    // Return false if the number already exists in the 3x3 matrix
                    return false; // Number already exists in the 3x3 matrix
                }
            }
        }

        // If the loop completes without finding a matching number, return true
        // This indicates that the number can be inserted in the 3x3 matrix
        return true;
    }
}
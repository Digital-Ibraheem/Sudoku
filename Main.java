/**
 * Author: Ibraheem Dawod
 * Date: 01/17/2024
 * Description: Main class for the Sudoku game application.
 * This class handles the main game loop, allowing the player to choose the difficulty level,
 * play a standard Sudoku game or a special game, and manage game options such as saving,
 * exiting, and getting hints.
 * 
 * Methods:
 * - main(): The main method of the Sudoku program.
 */
public class Main {

    /**
     * Author: Ibraheem Dawod
     * Date: 01/17/2024
     * Description: The main method of the program, entry point for the Sudoku game.
     */
    public static void main(String[] args) {
        String strPlayerName, strFileName;
        byte bytMenuChoice;
        boolean bolGameRunning;
        boolean bolProgramRunning;

        GameBoard gameboard; // Instance of the standard Sudoku GameBoard
        gameboard = new GameBoard();

        SpecialBoard specialboard; // Instance of the special Sudoku SpecialBoard
        specialboard = new SpecialBoard();

        bolProgramRunning = true;

        // Get player's name
        strPlayerName = PlayerInput.getStringInput("Hello and welcome to Sudoku! Please enter in your name below: ");

        do {
            bolGameRunning = true;

            // Choose difficulty or game type
            bytMenuChoice = PlayerInput.getNumInput("\nHello " + strPlayerName + "! What difficulty of Sudoku would you like to play?\n\n1: Easy\n2: Medium\n3: Hard\n4: Create a new game\n5: Special game\n6: Play a saved game", (byte)1, (byte)6);

            // Create a new game based on the chosen difficulty or type
            switch (bytMenuChoice) {
                case 1:
                    gameboard = new GameBoard("easyboard.txt");
                    break;
                case 2:
                    gameboard = new GameBoard("mediumboard.txt");
                    break;
                case 3:
                    gameboard = new GameBoard("hardboard.txt");
                    break;
                case 4:
                    gameboard = new GameBoard();
                    break;
                case 5:
                    specialboard = new SpecialBoard();
                    break;
                default:
                    gameboard = new GameBoard(PlayerInput.getFileInput("\n\nWhich file would you like to play from?"));
            }

            // Check if it's a special game or standard game
            if (bytMenuChoice != 5) {
                // Standard Sudoku game
                System.out.println("\n\nHere is your board. To make a guess, enter the row, column, and number you would like to guess.\n");
                do {
                    System.out.println(gameboard.toString());

                    if (!gameboard.isBoardFilled()) {
                        // Save game, exit this board, or make a guess
                        byte bytGameChoice = PlayerInput.getNumInput("\n\nWould you like to:\n1: Make a guess\n2: Save this game to a file\n3: Exit the board", (byte)1, (byte)3);

                        if (bytGameChoice == 1) {
                            // Make a guess
                            if (bolGameRunning && bolProgramRunning) {
                                byte bytRow = PlayerInput.getNumInput("\nOnce ready, enter in the row of the number you would like to guess: ");
                                byte bytCol = PlayerInput.getNumInput("\nOnce ready, enter in the column of the number you would like to guess: ");
                                byte bytGuess = PlayerInput.getNumInput("\nOnce ready, enter in the number you would like to guess: ");

                                if (!gameboard.validateMove(bytRow, bytCol, bytGuess)) {
                                    bolGameRunning = false;
                                }
                            }
                        } else if (bytGameChoice == 2) {
                            // Save game
                            gameboard.saveGame();
                            bolGameRunning = false;
                        } else {
                            // Exit the board
                            bolGameRunning = false;
                        }
                    } else {
                        bolGameRunning = false;
                    }
                } while (bolGameRunning);
                
                // If the game is won, let the user know.
                if (gameboard.getBolGameWon()) {
                    System.out.println("\nCongrats " + strPlayerName + ", you've won!");
                }
            } else {
                // Special Sudoku game
                System.out.println("\n\nHere is your board. To make a guess, enter the row, column, and number you would like to guess.\n");
                do {
                    System.out.println(specialboard.toString());

                    if (!specialboard.isBoardFilled()) {
                        // Save game, exit this board, get a hint, or make a guess
                        byte bytGameChoice = PlayerInput.getNumInput("Would you like to:\n1: Make a guess\n2: Get a  hint\n3: Save this game to a file\n4: Exit the board", (byte)1, (byte)4);

                        if (bytGameChoice == 1) {
                            // Make a guess
                            if (bolGameRunning && bolProgramRunning) {
                                byte bytRow = PlayerInput.getNumInput("\nOnce ready, enter in the row of the number you would like to guess: ");
                                byte bytCol = PlayerInput.getNumInput("\nOnce ready, enter in the column of the number you would like to guess: ");
                                byte bytGuess = PlayerInput.getNumInput("\nOnce ready, enter in the number you would like to guess: ");

                                if (!specialboard.validateMove(bytRow, bytCol, bytGuess)) {
                                    bolGameRunning = false;
                                }
                            }
                        } else if (bytGameChoice == 2) {
                            // Get a hint
                            specialboard.getHint();
                        } else if (bytGameChoice == 3) {
                            // Save game
                            specialboard.saveGame();
                            bolGameRunning = false;
                        } else {
                            // Exit the board
                            bolGameRunning = false;
                        }
                    } else {
                        bolGameRunning = false;
                    }
                } while (bolGameRunning && specialboard.checkTimeLimit());

                // If the game is won, let the user know
                if (specialboard.getBolGameWon()) {
                    System.out.println("\nCongrats " + strPlayerName + ", you've won!");
                }
            }

            // Ask the player if they want to quit or return to the main menu
            if (PlayerInput.getNumInput("\n\nWould you like to quit the game, or return to the main menu?\n1: Quit\n2: Return to the main menu", (byte)1, (byte)2) == 1) {
                System.out.println("\n\nThank you for playing Sudoku " + strPlayerName + "! Goodbye.");
                bolProgramRunning = false;
            }
        } while (bolProgramRunning);
    }
}
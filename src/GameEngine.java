import java.util.*;

/**
 * This java class serves as the main controller of all the games
 * It manages the game setup, inputs, the player setup and the entire game loop.
 * Now based on which game the user chooses from the main menu this class launches the respective game
 */

public class GameEngine {
    private final Menu menu;
    private final InputHandler inputHandler;
    private Player player1;
    private Player player2;
    private boolean multiplayer = false;

    public GameEngine(){
        this.menu = new Menu();
        this.inputHandler = new InputHandler();
    }
    
    /**Entry point for the game*/
    public void start(){
        String[] gameChoices = {"SlidingPuzzle", "DotsAndBoxes"};
        menu.displayWelcome();

        while(true){
            // Determine game choice (0 for single-player Sliding Puzzle, 1 for multi-player Dots and Boxes)
            int gameChoice = getGameChoice();
            // Note: Assuming gameChoice 1 forces multiplayer for DotsAndBoxes
            multiplayer = (gameChoice == 1); 

            boolean playingSameGame = true;
            String currentGameName = gameChoices[gameChoice];

            while(playingSameGame) {
                setupPlayer(multiplayer);
                playGame(currentGameName);

                int nextAction = promptNextAction();

                if(nextAction == 3) {
                    // Quit
                    System.out.println("\nThank you for playing!");
                    inputHandler.close();
                    return;
                } else if(nextAction == 2) {
                    // Play different game
                    playingSameGame = false;
                }
                // If nextAction == 1, continue inner loop (play same game)
            }
        }
    }
    
    /** Gets game choice  */
    private int getGameChoice() {
        int gameChoice;
        while(true){
            // Assuming getGameChoice asks for 0 or 1
            gameChoice = inputHandler.getGameChoice(); 
            if(gameChoice == 0 || gameChoice == 1){
                return gameChoice;
            } else {
                menu.displayError("Not a valid game please try again!");
            }
        }
    }
    
    /** Prompts for choice after quit */
    private int promptNextAction() {
        menu.displayMessage("\nWhat would you like to do?");
        menu.displayMessage("1. Play again (same game)");
        menu.displayMessage("2. Play a different game");
        menu.displayMessage("3. Quit");

        int choice;
        while(true) {
            String input = inputHandler.getInput("Enter your choice (1-3): ");
            try {
                choice = Integer.parseInt(input);
                if(choice >= 1 && choice <= 3) {
                    return choice;
                } else {
                    menu.displayError("Please enter 1, 2, or 3");
                }
            } catch (NumberFormatException e) {
                menu.displayError("Please enter a valid number");
            }
        }
    }
    
    /** Prompts player details */
    private void setupPlayer(boolean multiplayer){
        if (multiplayer) {
            String name = inputHandler.getPlayerName(multiplayer, "Player 1");
            player1 = new Player(name);

            name = inputHandler.getPlayerName(multiplayer, "Player 2");
            player2 = new Player(name);

        } else{
            String name = inputHandler.getPlayerName(multiplayer, "Player 1");
            player1 = new Player(name);
        }
    }
    
    /*
     * Creates the game and runs it based on the users choice.
     */
    private void playGame(String name){
        Game game;
        if(name.equals("SlidingPuzzle")){
            game = new SlidingPuzzle(menu, inputHandler);
            game.setPlayer(player1);
        }else{
            DotsAndBoxes dotsAndBoxes = new DotsAndBoxes(menu, inputHandler);
            dotsAndBoxes.setPlayer(player1, player2);
            game = dotsAndBoxes;
        }

        game.initializeGame();

        int flag = 1;
        while(game.isActive(flag) && !game.isGameWon()){
            menu.printBoard(game.getBoardDisplay()); 
            flag = game.makeMove();
        }
        
        menu.printBoard(game.getBoardDisplay());
        
        if(game.isGameWon()){
            game.displayVictory();
        } else{
            game.displaySummary();
        }
    }
}
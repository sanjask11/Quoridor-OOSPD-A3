import java.util.*;
import java.util.HashMap;
import java.util.Map;

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

    /** In-memory saved games session only*/
    private final Map<String, Game> savedGames = new HashMap<>();

    public GameEngine() {
        this.menu = new Menu();
        this.inputHandler = new InputHandler();
    }

    /** Entry point for the arcade */
    public void start() {
        String[] gameChoices = {"SlidingPuzzle", "DotsAndBoxes", "Quoridor"};
        menu.displayWelcome();

        while (true) {
            int gameChoice = getGameChoice();
            multiplayer = (gameChoice == 1) || (gameChoice == 2);
            String currentGameName = gameChoices[gameChoice];
            boolean playingSameGame = true;

            while (playingSameGame) {
                setupPlayer(multiplayer);
                playGame(currentGameName);

                int nextAction = promptNextAction();
                if (nextAction == 3) {
                    System.out.println("\nThank you for playing!");
                    inputHandler.close();
                    return;
                } else if (nextAction == 2) {
                    playingSameGame = false;
                }
            }
        }
    }

    /** Gets valid game choice */
    private int getGameChoice() {
        int gameChoice;
        while (true) {
            gameChoice = inputHandler.getGameChoice();
            if (gameChoice == 0 || gameChoice == 1 || gameChoice == 2)
                return gameChoice;
            menu.displayError("Not a valid game please try again!");
        }
    }

    /** Handles next action after a game ends or is paused */
    private int promptNextAction() {
        menu.displayMessage("\nWhat would you like to do?");
        menu.displayMessage("1. Play again (same game)");
        menu.displayMessage("2. Play a different game");
        menu.displayMessage("3. Quit");

        while (true) {
            String input = inputHandler.getInput("Enter your choice (1-3): ");
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 3) return choice;
            } catch (NumberFormatException ignored) {}
            menu.displayError("Please enter 1, 2, or 3");
        }
    }

    /** Sets up players */
    private void setupPlayer(boolean multiplayer) {
        if (multiplayer) {
            player1 = new Player(inputHandler.getPlayerName(true, "Player 1"));
            player2 = new Player(inputHandler.getPlayerName(true, "Player 2"));
        } else {
            player1 = new Player(inputHandler.getPlayerName(false, "Player 1"));
        }
    }

    /** Runs the selected game, with support for pause/resume */
    private void playGame(String name) {
        Game game;

        // Resume existing saved game if available
        if (savedGames.containsKey(name)) {
            boolean resume = inputHandler.askYesNo(
                    "\nA saved " + name + " game was found. Resume it?");
            if (resume) {
                game = savedGames.remove(name);
                System.out.println("Resuming your previous game...\n");
            } else {
                savedGames.remove(name);
                game = createNewGame(name);
                game.initializeGame();
            }
        } else {
            game = createNewGame(name);
            game.initializeGame();
        }

        int flag = 1;
        while (game.isActive(flag) && !game.isGameWon()) {
            menu.printBoard(game.getBoardDisplay());

            String command = inputHandler.getInput(
                    "(Enter move or 'p' to pause): ").trim();

            if (command.equalsIgnoreCase("p") || command.equalsIgnoreCase("pause")) {
                handlePauseMenu(name, game);
                return; // exit back to main menu
            }

            flag = game.makeMoveFromInput(command);
        }

        menu.printBoard(game.getBoardDisplay());
        if (game.isGameWon()) {
            game.displayVictory();
            savedGames.remove(name); // clear any saved copy
        } else {
            game.displaySummary();
        }
    }

    /** Handles pause menu options */
    private void handlePauseMenu(String gameName, Game game) {
        System.out.println("\n=== Game Paused ===");
        System.out.println("1. Resume");
        System.out.println("2. Save and return to main menu");
        System.out.println("3. Quit game");

        while (true) {
            String input = inputHandler.getInput("Choice: ").trim();
            switch (input) {
                case "1" -> {
                    System.out.println("Resuming game...\n");
                    runGameLoop(gameName, game);
                    return;
                }
                case "2" -> {
                    savedGames.put(gameName, game);
                    System.out.println("Game saved. Returning to main menu...");
                    return;
                }
                case "3" -> {
                    System.out.println("Exiting game. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Please enter 1, 2, or 3.");
            }
        }
    }

    /** Resumes a game loop after resuming */
    private void runGameLoop(String name, Game game) {
        int flag = 1;
        while (game.isActive(flag) && !game.isGameWon()) {
            menu.printBoard(game.getBoardDisplay());
            String command = inputHandler.getInput(
                    "(Enter move or 'p' to pause): ").trim();

            if (command.equalsIgnoreCase("p") || command.equalsIgnoreCase("pause")) {
                handlePauseMenu(name, game);
                return;
            }

            flag = game.makeMoveFromInput(command);
        }

        menu.printBoard(game.getBoardDisplay());
        if (game.isGameWon()) {
            game.displayVictory();
            savedGames.remove(name);
        } else {
            game.displaySummary();
        }
    }

    /** Factory method for new game creation */
    private Game createNewGame(String name) {
        if (name.equals("SlidingPuzzle")) {
            SlidingPuzzle g = new SlidingPuzzle(menu, inputHandler);
            g.setPlayer(player1);
            return g;
        } else if (name.equals("DotsAndBoxes")) {
            DotsAndBoxes g = new DotsAndBoxes(menu, inputHandler);
            g.setPlayer(player1, player2);
            return g;
        } else {
            Quoridor g = new Quoridor(menu, inputHandler);
            g.setPlayer(player1, player2);
            return g;
        }
    }
}

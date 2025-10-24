/**
 * Abstract base for all the games in the terminal arcade defining all the shared features between the games
 * Each game extends this base to fulfill all the functionalities of the game
 */

abstract class Game {
    protected Board board;
    protected Player player;
    protected Menu menu;
    protected InputHandler inputHandler;
    protected boolean gameActive;

    public Game(Menu menu, InputHandler inputHandler){
        this.menu = menu;
        this.inputHandler = inputHandler;
        this.gameActive = false;
    }

    public abstract void initializeGame();
    public abstract boolean isGameWon();

    public abstract int makeMove();
    public abstract void displayVictory();
    public abstract void displaySummary();

    public abstract String[][] getBoardDisplay();

    public void setPlayer(Player player){
        this.player = player;
    }

    public boolean isActive(int flag){
        return gameActive;
    }

    protected void setGameActive(boolean active){
        this.gameActive = active;
    }

}

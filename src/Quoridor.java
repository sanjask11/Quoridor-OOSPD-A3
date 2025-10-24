import java.util.*;

public class Quoridor extends Game {
    private Player player1;
    private Player player2;
    private int current = 0;
    private final int ROWS = 9;
    private final int COLUMNS = 9;
    private final int INITIAL_WALLS = 10;
    
    // Game-specific state: Pawn positions (0-indexed)
    private int[] pawn1Pos = new int[2]; // {row, col}
    private int[] pawn2Pos = new int[2];
    
    // Game-specific state: Wall counts
    private int walls1 = INITIAL_WALLS;
    private int walls2 = INITIAL_WALLS;
    
    // Game-specific state: Wall placement status (0=unclaimed, 1=wall present)
    // hWalls: 8 rows (between rows 0-8) by 9 columns. A wall spans 2 columns (e.g., c, c+1)
    private int[][] hWalls = new int[ROWS - 1][COLUMNS]; 
    
    // vWalls: 9 rows by 8 columns (between columns 0-8). A wall spans 2 rows (e.g., r, r+1)
    private int[][] vWalls = new int[ROWS][COLUMNS - 1];

    public Quoridor(Menu menu, InputHandler inputHandler) {
        super(menu, inputHandler);
    }

    public void initializeGame(){

    }

    public Tile.DisplayInfo[][] getDisplayGrid(){

    }

    public String[][] getBoardDisplay() {
        return board.getBoardDisplay(getDisplayGrid());
    }

    public int makeMove(){
        return 0;
    }

    public boolean isGameWon() {
        // Player 1 wins if they reach the top row (row 0)
        if (pawn1Pos[0] == 0) return true;
        // Player 2 wins if they reach the bottom row (row 8)
        if (pawn2Pos[0] == ROWS - 1) return true;
        return false;
    }

    private void displayScore() {
        String[] messages = {
                "\nWalls Remaining:",
                player1.getName() + " (" + initial(player1.getName()) + "): " + walls1 + 
                " | " + player2.getName() + " (" + initial(player2.getName()) + "): " + walls2,
                "It is " + (current == 0 ? player1.getName() : player2.getName()) + "'s turn."
        };
        menu.displayMessages(messages);
    }
    
    public void displayVictory() {
        String winnerName = (pawn1Pos[0] == 0) ? player1.getName() : player2.getName();
        menu.displayMessage("\nGame Over! Congratulations " + winnerName + ", you reached the goal!");
    }

    public void displaySummary() {
        String summary = "Game ended prematurely. Final positions:\n" +
                player1.getName() + " at (" + (pawn1Pos[0] + 1) + ", " + (pawn1Pos[1] + 1) + ")" +
                "\n" + player2.getName() + " at (" + (pawn2Pos[0] + 1) + ", " + (pawn2Pos[1] + 1) + ")";
        menu.displayMessage(summary);
    }

    public void setPlayer(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }
    
    public boolean isActive(int flag) {
        return flag == 1;
    }

    protected void setGameActive(boolean var1) {
        this.gameActive = var1;
    }

    private char initial(String name) {
        // Implementation for getting the first letter of the name
        if (name == null) return '?';
        for (int i = 0; i < name.length(); i++) {
            char c = Character.toUpperCase(name.charAt(i));
            if (c >= 'A' && c <= 'Z') {
                return c;
            }
        }
        return '?';
    }
}
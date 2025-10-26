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

    public void initializeGame() {
        // 1. Initialize Board
        board = new Board(ROWS, COLUMNS);
        Tile[][] tiles = new Tile[ROWS][COLUMNS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                tiles[r][c] = new Tile(String.valueOf(r * COLUMNS + c)); // Tile value is irrelevant, used for consistency
            }
        }
        board.setPieces(tiles);

        // 2. Set initial pawn positions (P1: bottom center, P2: top center)
        pawn1Pos = new int[]{ROWS - 1, COLUMNS / 2};
        pawn2Pos = new int[]{0, COLUMNS / 2};

        // 3. Set initial wall counts and empty wall arrays
        walls1 = INITIAL_WALLS;
        walls2 = INITIAL_WALLS;
        
        // Arrays are initialized to 0 by default, so hWalls and vWalls are ready.

        setGameActive(true);

        String[] instructions = {
                "\nQuoridor (9x9) Instructions:",
                "Move Pawn: M [U/D/L/R] → Move your pawn one square: Up, Down, Left, or Right.", 
                "Wall (1-indexed): H r c   → Place horizontal wall starting at (r, c) (spans c and c+1)",
                "Wall (1-indexed): V r c   → Place vertical wall starting at (r, c) (spans r and r+1)",
                
                "Q                 → Quit",
        };
        menu.displayMessages(instructions);
    }

    /** Builds the DisplayInfo grid for the Board's printer. */
    public Tile.DisplayInfo[][] getDisplayGrid() {
        Tile.DisplayInfo[][] grid = new Tile.DisplayInfo[ROWS][COLUMNS];
        Tile[][] pieces = board.getPieces(); 
        
        // Player labels (using initials)
        char label1 = initial(player1.getName());
        char label2 = initial(player2.getName());

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                Tile.DisplayInfo info = pieces[r][c].getDisplayInfo(null);
                
                // Set Pawn Content
                if (r == pawn1Pos[0] && c == pawn1Pos[1]) {
                    info.centerContent = String.valueOf(label1);
                } else if (r == pawn2Pos[0] && c == pawn2Pos[1]) {
                    info.centerContent = String.valueOf(label2);
                } else {
                    info.centerContent = " ";
                }
                
                // Overlay Wall State (Edges)
                // Top Edge (r-1, c) is the hWall[r-1][c] slot
                if (r > 0) info.topEdge = hWalls[r - 1][c] == 1;
                
                // Bottom Edge (r, c) is the hWall[r][c] slot
                if (r < ROWS - 1) info.bottomEdge = hWalls[r][c] == 1; 

                // Left Edge (r, c-1) is the vWall[r][c-1] slot
                if (c > 0) info.leftEdge = vWalls[r][c - 1] == 1;
                
                // Right Edge (r, c) is the vWall[r][c] slot
                if (c < COLUMNS - 1) info.rightEdge = vWalls[r][c] == 1;
                
                grid[r][c] = info;
            }
        }
        return grid;
    }
    
    public String[][] getBoardDisplay() {
        displayScore(); // Display score/walls before the board
        return board.getBoardDisplay(getDisplayGrid());
    }

    public int makeMove() {
        Player player = current == 0 ? player1 : player2;
        
        String input = inputHandler.getMove(player, " to move (M U/D/L/R | H/V r c | q): ");

        if (input.equalsIgnoreCase("q")) {
            return -1;
        }

        String[] parts = input.trim().split("\\s+");

        char moveType = Character.toUpperCase(parts[0].charAt(0));

        if (moveType == 'M') {
            if (parts.length != 2) {
                menu.displayError("Invalid Move format. Use: M [U/D/L/R]");
                return 1;
            }
            char direction = Character.toUpperCase(parts[1].charAt(0));
            
            // Call the updated move function
            if (tryMovePawnDirection(direction)) {
                current = 1 - current;
                return 1;
            }
            
        } else if (moveType == 'H' || moveType == 'V') {
            if (parts.length != 3) {
                menu.displayError("Invalid Wall format. Use: H/V r c");
                return 1;
            }
            int r, c;
            try {
                r = Integer.parseInt(parts[1]) - 1; 
                c = Integer.parseInt(parts[2]) - 1;
            } catch (NumberFormatException e) {
                menu.displayError("Coordinates must be numbers for wall placement.");
                return 1;
            }

            if (tryPlaceWall(moveType, r, c)) {
                current = 1 - current;
                return 1;
            }
        } else {
            menu.displayError("Invalid move type. Use M, H, or V.");
        }

        return 1;
    }

    /**
     * Attempts to move the current player's pawn one space in the given direction.
     * Direction: U, D, L, or R.
     */
    private boolean tryMovePawnDirection(char direction) {
        int[] currentPos = current == 0 ? pawn1Pos : pawn2Pos;
        int[] otherPos = current == 0 ? pawn2Pos : pawn1Pos;
        
        int r = currentPos[0];
        int c = currentPos[1];

        int targetR = r;
        int targetC = c;
        
        // 1. Calculate the Target Coordinates
        switch (direction) {
            case 'U': targetR--; break;
            case 'D': targetR++; break;
            case 'L': targetC--; break;
            case 'R': targetC++; break;
            default:
                menu.displayError("Invalid direction. Use U, D, L, or R.");
                return false;
        }
        
        // 2. Bounds Check
        if (!board.isValidPosition(targetR, targetC)) {
            menu.displayError("Move is out of board bounds.");
            return false;
        }
        
        // 3. Wall Blocking Check
        // Check for a wall between the current position (r, c) and the target position (targetR, targetC)
        if (isWallBlocking(r, c, targetR, targetC)) {
            menu.displayError("Move blocked by a wall.");
            return false;
        }

        // 4. Pawn Blocking Check
        if (targetR == otherPos[0] && targetC == otherPos[1]) {
            // This is where advanced Quoridor rules (jumping, diagonal) would go.
            menu.displayError("Space blocked by the other pawn.");
            return false;
        }

        // 5. Commit Move
        if (current == 0) pawn1Pos = new int[]{targetR, targetC};
        else pawn2Pos = new int[]{targetR, targetC};
        
        return true;
    }
    
    private boolean isWallBlocking(int r1, int c1, int r2, int c2) {
        if (r1 == r2) { // Horizontal move (c1 -> c2)
            int minC = Math.min(c1, c2);
            // Check vertical wall slot between r1 and minC
            return vWalls[r1][minC] == 1;
        } else if (c1 == c2) { // Vertical move (r1 -> r2)
            int minR = Math.min(r1, r2);
            // Check horizontal wall slot between minR and c1
            return hWalls[minR][c1] == 1;
        }
        return false; // Should not happen with simple movement
    }

    private boolean tryPlaceWall(char orientation, int r, int c) {
        int wallCount = current == 0 ? walls1 : walls2;
        
        if (wallCount <= 0) {
            menu.displayError("No walls remaining.");
            return false;
        }
        
        // Wall placement validation
        if (!isWallPlacementValid(orientation, r, c)) {
            return false;
        }

        // Wall placement (a wall is 2 slots long)
        if (orientation == 'H') {
            hWalls[r][c] = 1;
            hWalls[r][c + 1] = 1; // Wall spans c and c+1
        } else { // 'V'
            vWalls[r][c] = 1;
            vWalls[r + 1][c] = 1; // Wall spans r and r+1
        }
        
        // Commit wall placement
        if (current == 0) walls1--;
        else walls2--;
        
        return true;
    }
    
    // Checks placement bounds, overlap, and crossover
    private boolean isWallPlacementValid(char orientation, int r, int c) {
        // Bounds Check (H: r E [0, 7], c E [0, 7]. V: r E [0, 7], c E [0, 7])
        if (orientation == 'H') {
            if (r < 0 || r >= ROWS - 1 || c < 0 || c >= COLUMNS - 1) {
                menu.displayError("Horizontal wall (H r c) requires r in [1, 8] and c in [1, 8] (1-indexed).");
                return false;
            }
            // Overlap check (needs 2 consecutive slots to be empty)
            if (hWalls[r][c] == 1 || hWalls[r][c + 1] == 1) {
                menu.displayError("Wall overlaps an existing wall.");
                return false;
            }
            // Crossover check (vertical wall must not intersect at r, c)
            if (vWalls[r][c] == 1 && vWalls[r + 1][c] == 1) {
                 menu.displayError("Wall cannot cross an existing vertical wall.");
                 return false;
            }

        } else { // 'V'
            if (r < 0 || r >= ROWS - 1 || c < 0 || c >= COLUMNS - 1) {
                menu.displayError("Vertical wall (V r c) requires r in [1, 8] and c in [1, 8] (1-indexed).");
                return false;
            }
            // Overlap check (needs 2 consecutive slots to be empty)
            if (vWalls[r][c] == 1 || vWalls[r + 1][c] == 1) {
                menu.displayError("Wall overlaps an existing wall.");
                return false;
            }
            // Crossover check
            if (hWalls[r][c] == 1 && hWalls[r][c + 1] == 1) {
                 menu.displayError("Wall cannot cross an existing horizontal wall.");
                 return false;
            }
        }
        return true;
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
    @Override
    public int makeMoveFromInput(String command) {
        if (command.equalsIgnoreCase("p") || command.equalsIgnoreCase("pause")) {
            return 1; // handled by GameEngine
        }

        Player player = current == 0 ? player1 : player2;
        String input = command.trim();

        if (input.equalsIgnoreCase("q")) {
            return -1;
        }

        String[] parts = input.split("\\s+");
        if (parts.length == 0) {
            menu.displayError("Please enter a move.");
            return 1;
        }

        char moveType = Character.toUpperCase(parts[0].charAt(0));

        if (moveType == 'M') {
            if (parts.length != 2) {
                menu.displayError("Invalid Move format. Use: M [U/D/L/R]");
                return 1;
            }
            char direction = Character.toUpperCase(parts[1].charAt(0));
            if (tryMovePawnDirection(direction)) {
                current = 1 - current;
            }
            return 1;

        } else if (moveType == 'H' || moveType == 'V') {
            if (parts.length != 3) {
                menu.displayError("Invalid Wall format. Use: H/V r c");
                return 1;
            }
            try {
                int r = Integer.parseInt(parts[1]) - 1;
                int c = Integer.parseInt(parts[2]) - 1;
                if (tryPlaceWall(moveType, r, c)) {
                    current = 1 - current;
                }
            } catch (NumberFormatException e) {
                menu.displayError("Coordinates must be numbers for wall placement.");
            }
            return 1;

        } else {
            menu.displayError("Invalid move type. Use M, H, or V.");
            return 1;
        }
    }
}

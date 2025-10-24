import java.util.*;

public class DotsAndBoxes extends Game {
    private Player player1;
    private Player player2;
    private int current = 0;

    // Board dimensions
    private int rows;
    private int columns;

    private int[][] hOwner;
    private int[][] vOwner;

    // boxOwner is now stored in Tile.boxOwner
    private char[] labels;

    public DotsAndBoxes(Menu menu, InputHandler inputHandler) {
        super(menu, inputHandler);
    }

    public void initializeGame() {
        int[] dimensions = {-1, -1};

        while (dimensions[0] == -1 || dimensions[1] == -1) {
            dimensions = inputHandler.getBoardDimensions();
        }

        rows = dimensions[0];
        columns = dimensions[1];

        board = new Board(rows, columns);
        
        // Initialize the Tile array for the Board (using the Box constructor)
        Tile[][] tiles = new Tile[rows][columns];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                tiles[r][c] = new Tile(); 
            }
        }
        board.setPieces(tiles);

        // Initialize edge-specific arrays
        hOwner = new int[rows + 1][columns]; 
        vOwner = new int[rows][columns + 1];
        initializeOwnerArrays(tiles); // Pass tiles for boxOwner initialization

        labels = new char[]{initial(player1.getName()), initial(player2.getName())};
        setGameActive(true);

        String[] instructions = {
                "\nInput format (1-indexed):",
                "H r c   → horizontal edge at row r, col c   (r∈[1," + (rows + 1) + "], c∈[1," + columns + "])",
                "V r c   → vertical edge   at row r, col c   (r∈[1," + rows + "], c∈[1," + (columns + 1) + "])",
        };

        menu.displayMessages(instructions);
    }

    /** Fill all owner arrays with -1 and initialize Tile.boxOwner. */
    private void initializeOwnerArrays(Tile[][] tiles) {
        for (int i = 0; i < rows + 1; i++) {
            for (int j = 0; j < columns; j++) {
                hOwner[i][j] = -1;
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns + 1; j++) {
                vOwner[i][j] = -1;
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                tiles[i][j].boxOwner = -1; // Initialize the Tile's boxOwner
            }
        }
    }
    
    /** Builds the DisplayInfo grid for the Board's printer. */
    public Tile.DisplayInfo[][] getDisplayGrid() {
        Tile.DisplayInfo[][] grid = new Tile.DisplayInfo[rows][columns];
        Tile[][] tiles = board.getPieces(); 

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                // Start with the Tile's basic display info (center content)
                Tile.DisplayInfo info = tiles[r][c].getDisplayInfo(labels);
                
                // Overlay the edge state from the game's external arrays
                info.topEdge = hOwner[r][c] != -1;
                info.bottomEdge = hOwner[r + 1][c] != -1;
                info.leftEdge = vOwner[r][c] != -1;
                info.rightEdge = vOwner[r][c + 1] != -1;
                
                grid[r][c] = info;
            }
        }
        return grid;
    }
    
    /** Build a String[][] snapshot of the current board for printing/rendering it.*/
    public String[][] getBoardDisplay() {
        return board.getBoardDisplay(getDisplayGrid());
    }

    public boolean isGameWon() {
        Tile[][] tiles = board.getPieces();
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                if (tiles[x][y].boxOwner == -1) { // Check Tile's boxOwner
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Reads the users input for the move where it is given as H/V r c where
     * H/h -> Horizontal, V/v -> Vertical, r -> row number, c-> column number, q -> quit
     * it gives the point to the player who closes the square and also allows the user to keep track of the scores
     */
    public int makeMove() {
        Player player = current == 0 ? player1 : player2;
        String input = inputHandler.getMove(player, " to move. Edge (H r c | V r c | q): ");

        if (input.equals("q")) {
            return -1;
        }

        Move mv = parseOneIndexed(input, rows, columns);

        if (mv == null) {
            menu.displayMessage("Could not parse/out of range.");
            return 1;
        }

        if (!canClaim(mv.orient, mv.r0, mv.c0)) {
            menu.displayMessage("Illegal move (off board or already claimed).");
            return 1;
        }

        int made = claim(mv.orient, mv.r0, mv.c0, current);

        if (made > 0) {
            if (current == 0) player1.addScore(made);
            else player2.addScore(made);
            menu.displayMessage(player.getName() + " closed " + made + " box" + (made == 1 ? "" : "es") + " and goes again!");
        } else {
            current = 1 - current;
        }

        displayScore();

        return 1;
    }
    
    /** Checks if the user can claim an edge , if it is in-range and still unclaimed. */
    private boolean canClaim(char orient, int r, int c) {
        orient = Character.toUpperCase(orient);
        if (orient == 'H') {
            if (r < 0 || r > rows || c < 0 || c >= columns) return false;
            return hOwner[r][c] == -1;
        } else if (orient == 'V') {
            if (r < 0 || r >= rows || c < 0 || c > columns) return false;
            return vOwner[r][c] == -1;
        }
        return false;
    }

    /** Allows the user to claim an edge and try to close a box */
    private int claim(char orient, int r, int c, int playerId) {
        orient = Character.toUpperCase(orient);
        int closed = 0;

        if (orient == 'H') {
            hOwner[r][c] = playerId;

            if (r > 0) closed += tryCloseBox(r - 1, c, playerId);
            if (r < rows) closed += tryCloseBox(r, c, playerId);

        } else {
            vOwner[r][c] = playerId;

            if (c > 0) closed += tryCloseBox(r, c - 1, playerId);
            if (c < columns) closed += tryCloseBox(r, c, playerId);
        }
        return closed;
    }

    private int tryCloseBox(int br, int bc, int playerId) {
        Tile[][] tiles = board.getPieces();

        if (br < 0 || br >= rows || bc < 0 || bc >= columns) return 0;
        if (tiles[br][bc].boxOwner != -1) return 0; // Check Tile's boxOwner

        boolean top = hOwner[br][bc] != -1;
        boolean bottom = hOwner[br + 1][bc] != -1;
        boolean left = vOwner[br][bc] != -1;
        boolean right = vOwner[br][bc + 1] != -1;

        if (top && bottom && left && right) {
            tiles[br][bc].boxOwner = playerId; // SET Tile's boxOwner
            return 1;
        }
        return 0;
    }
    
    /** Keeps track of the score for the users to know the score at each stage of their game */
    private void displayScore() {
        String[] messages = {
                "\nScores -> " +
                        player1.getName() + ": " + player1.getScore() + " | " +
                        player2.getName() + ": " + player2.getScore(),
        };

        menu.displayMessages(messages);
    }

    public void displayVictory() {
        String name = (player1.getScore() > player2.getScore()) ? player1.getName() : player2.getName();
        menu.displayMessage("Congratulations " + name + ", you win!");
    }

    public void displaySummary() {
        int score1 = player1.getScore();
        int score2 = player2.getScore();

        String winner = (score1 > score2) ? player1.getName() + " wins!" :
                (score2 > score1) ? player2.getName() + " wins!" :
                        "It's a tie!";

        String summary = "Final score: " + player1.getName() + ": " + score1 +
                " | " + player2.getName() + ": " + score2 +
                "\n" + winner;

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

    private static char initial(String name) {
        if (name == null) return '?';
        for (int i = 0; i < name.length(); i++) {
            char c = Character.toUpperCase(name.charAt(i));
            if (c >= 'A' && c <= 'Z') {
                return c;
            }
        }
        return '?';
    }

    private static final class Move {
        final char orient;
        final int r0;
        final int c0;

        Move(char o, int r0, int c0) {
            this.orient = o;
            this.r0 = r0;
            this.c0 = c0;
        }
    }
    
    /** Parse the user input to get the move the user is trying to play. */
    private Move parseOneIndexed(String input, int rowsBoxes, int colsBoxes) {
        String[] toks = input.trim().split("\\s+");
        if (toks.length != 3) return null;
        char o = Character.toUpperCase(toks[0].charAt(0));
        try {
            int r1 = Integer.parseInt(toks[1]);
            int c1 = Integer.parseInt(toks[2]);

            if (o == 'H') {
                if (r1 < 1 || r1 > rowsBoxes + 1 || c1 < 1 || c1 > colsBoxes) return null;
                return new Move('H', r1 - 1, c1 - 1);
            } else if (o == 'V') {
                if (r1 < 1 || r1 > rowsBoxes || c1 < 1 || c1 > colsBoxes + 1) return null;
                return new Move('V', r1 - 1, c1 - 1);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
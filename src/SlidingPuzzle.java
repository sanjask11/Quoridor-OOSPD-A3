import java.util.*;

public class SlidingPuzzle extends Game {
    private Player player;
    private int rows;
    private int columns;
    private int[] emptySpace = new int[2];

    // Board class inherited from abstract Game class
    public SlidingPuzzle(Menu menu, InputHandler inputHandler) {
        super(menu, inputHandler);
    }
    
    /** Initializes the board and makes sure that the board is solvable given the rules of the game */
    public void initializeGame() {
        int[] dimensions = {-1, -1};

        while (dimensions[0] == -1 || dimensions[1] == -1) {
            dimensions = inputHandler.getBoardDimensions();
        }

        this.rows = dimensions[0];
        this.columns = dimensions[1];

        board = new Board(rows, columns);

        Tile[] tiles = generateTiles(rows, columns);
        Tile[][] initialPieces = new Tile[rows][columns]; 

        do {
            Tile[] shuffledTiles = shuffleTiles(tiles);
            fillBoard(initialPieces, shuffledTiles); 
            board.setPieces(initialPieces); 
        } while (!isSolvable() || isGameWon());
        
        this.emptySpace = findTileInBoard("0");

        setGameActive(true);
    }
    
    /** Fills the provided board array with shuffled tiles */
    private void fillBoard(Tile[][] targetBoard, Tile[] tiles) {
        int index = 0;
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                targetBoard[x][y] = tiles[index++];
            }
        }
    }
    
    private Tile[] generateTiles(int rows, int columns) {
        Tile[] tiles = new Tile[rows * columns];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(String.valueOf(i)); 
        }
        return tiles;
    }

    private static Tile[] shuffleTiles(Tile[] tiles) {
        Tile[] shuffledTiles = Arrays.copyOf(tiles, tiles.length);
        Random random = new Random();
        for (int i = shuffledTiles.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Tile temp = shuffledTiles[i];
            shuffledTiles[i] = shuffledTiles[j];
            shuffledTiles[j] = temp;
        }
        return shuffledTiles;
    }
    
    /** Builds the DisplayInfo grid for the Board's printer. */
    public Tile.DisplayInfo[][] getDisplayGrid() {
        Tile[][] pieces = board.getPieces(); 
        Tile.DisplayInfo[][] grid = new Tile.DisplayInfo[rows][columns];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                grid[r][c] = pieces[r][c].getDisplayInfo(null); 
            }
        }
        return grid;
    }

    public String[][] getBoardDisplay() {
        return board.getBoardDisplay(getDisplayGrid());
    }
    
    /** Finds and returns the position of a tile based off of its value */
    private int[] findTileInBoard(String value) {
        Tile[][] pieces = board.getPieces(); 
        int[] position = new int[2];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                if (pieces[x][y].value.equals(value)) {
                    position[0] = x;
                    position[1] = y;
                    return position;
                }
            }
        }
        return position;
    }
    
    /** swaps the tiles on the board */
    private void swapPieces(int[] tile1, int[] tile2) {
        Tile[][] pieces = board.getPieces(); 
        Tile temp = pieces[tile1[0]][tile1[1]];
        pieces[tile1[0]][tile1[1]] = pieces[tile2[0]][tile2[1]];
        pieces[tile2[0]][tile2[1]] = temp;
    }

    private List<int[]> getAdjacentPieces(int row, int column) {
        List<int[]> adjacent = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = column + direction[1];

            if (board.isValidPosition(newRow, newCol)) {
                adjacent.add(new int[]{newRow, newCol});
            }
        }
        return adjacent;
    }
    
    /** Checks if the board is solvable or not */
    public boolean isSolvable() {
        Tile[][] pieces = board.getPieces(); 
        List<Integer> tiles = new ArrayList<>();
        int emptyRowFromBottom = 0;
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                Tile tile = pieces[x][y];

                if (tile.value.equals("0")) {
                    emptyRowFromBottom = rows - x;
                } else {
                    tiles.add(Integer.parseInt(tile.value));
                }
            }
        }

        int inversions = 0;
        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                if (tiles.get(i) > tiles.get(j)) {
                    inversions++;
                }
            }
        }

        if (columns % 2 == 1) {
            return inversions % 2 == 0;
        } else {
            return (inversions + emptyRowFromBottom) % 2 == 1;
        }
    }
    
    /** Checks if the user has achieved the task of the game by arranging the tiles in the correct order*/
    public boolean isGameWon() {
        Tile[][] pieces = board.getPieces(); 
        int expectedValue = 1;
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                if (x == rows - 1 && y == columns - 1) {
                    return Integer.parseInt(pieces[x][y].value) == 0;
                } else {
                    if (Integer.parseInt(pieces[x][y].value) != expectedValue++) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isActive(int flag) {
        return flag == 1;
    }

    public boolean isValidMove(int pieceValue) {
        if (pieceValue <= 0 || pieceValue > rows * columns) {
            return false;
        }

        List<int[]> emptyAdjacentPieces = getAdjacentPieces(emptySpace[0], emptySpace[1]);
        Tile[][] pieces = board.getPieces(); 

        for (int[] piece : emptyAdjacentPieces) {
            if (pieces[piece[0]][piece[1]].value.equals(String.valueOf(pieceValue))) {
                return true;
            }
        }
        return false;
    }


    public int makeMove() {
        String input = inputHandler.getMove(player, ", which tile do you want to slide to the empty space? ");
        if (input.equalsIgnoreCase("q")) return -1;
        try {
            int tileValue = Integer.parseInt(input);
            return makeMoveWithValue(tileValue);
        } catch (NumberFormatException e) {
            menu.displayError("Please enter a valid piece!");
            return 1;
        }
    }
    private int makeMoveWithValue(int validMove) {
        if (!isValidMove(validMove)) {
            menu.displayError("Please enter a valid piece");
            return 1;
        }

        List<int[]> adjacentPieces = getAdjacentPieces(emptySpace[0], emptySpace[1]);
        Tile[][] pieces = board.getPieces();
        int[] movePiece = new int[2];

        for (int[] adjacentPiece : adjacentPieces) {
            if (pieces[adjacentPiece[0]][adjacentPiece[1]].value.equals(String.valueOf(validMove))) {
                movePiece = adjacentPiece;
            }
        }

        int[] temp = movePiece;
        swapPieces(emptySpace, movePiece);
        emptySpace = temp;

        System.out.println();
        return 1;
    }

    public void displayVictory() {
        menu.displayVictory(player);
    }

    public void displaySummary() {
        String summary;

        if (isGameWon()) {
            summary = "Puzzle completed successfully!\nCongratulations " + player.getName() + "!";
        } else {
            summary = "Game ended prematurely.\nThe puzzle was not completed.\nBetter luck next time, " + player.getName() + "!";
        }

        menu.displayMessage(summary);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    protected void setGameActive(boolean var1) {
        this.gameActive = var1;
    }

    @Override
    public int makeMoveFromInput(String command) {
        if (command.equalsIgnoreCase("p") || command.equalsIgnoreCase("pause")) {
            return 1; // Pause handled by GameEngine
        }
        try {
            int tileValue = Integer.parseInt(command);
            return makeMoveWithValue(tileValue); // We'll refactor your logic slightly
        } catch (NumberFormatException e) {
            menu.displayMessage("Invalid input. Please enter a number or 'p' to pause.");
            return 1;
        }
    }

}

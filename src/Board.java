/**
 * This is a resuable base for all the rectangular board. It validates the inputs and stores the values.
 *Provides bound checking for the values.
 */
public class Board {
    private int rows;
    private int columns;
    private final int MAX_SIZE = 10;
    
    private Tile[][] pieces; 

    public Board(int var1, int var2) {
        this.validateDimensions(var1, var2);
        this.rows = var1;
        this.columns = var2;
        this.pieces = new Tile[rows][columns];
    }

    private void validateDimensions(int var1, int var2) {
        if (var1 > 0 && var2 > 0) {
            if (var1 > 10 || var2 > 10) {
                throw new IllegalArgumentException("Rows and columns must be smaller than 10");
            }
        } else {
            throw new IllegalArgumentException("Rows and columns must be greater than 0");
        }
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public boolean isValidPosition(int var1, int var2) {
        return var1 >= 0 && var1 < this.rows && var2 >= 0 && var2 < this.columns;
    }
   
    public Tile[][] getPieces() {
        return this.pieces;
    }
   
    public void setPieces(Tile[][] pieces) {
        if (pieces.length != this.rows || pieces[0].length != this.columns) {
             throw new IllegalArgumentException("Pieces array size must match board dimensions.");
        }
        this.pieces = pieces;
    }

    /**
     * The generalized board display method that interprets the provided 
     * Tile.DisplayInfo grid to build the printable board string array.
     */
    public String[][] getBoardDisplay(Tile.DisplayInfo[][] displayGrid) {
        if (displayGrid == null || displayGrid.length != rows || displayGrid[0].length != columns) {
            return new String[][]{{"Error: Invalid display grid provided to Board."}};
        }

        // Use a fixed display width for content (e.g., " A ")
        final int contentWidth = 3; 

        int displayHeight = rows * 2 + 1;
        String[][] display = new String[displayHeight][1];

        for (int r = 0; r < rows; r++) {
            // --- Build Horizontal Border Line (r * 2) ---
            StringBuilder borderLine = new StringBuilder();
            borderLine.append("+");
            for (int c = 0; c < columns; c++) {
                // The edge is based on the top edge of the current cell
                String edge = displayGrid[r][c].topEdge ? "-".repeat(contentWidth) : " ".repeat(contentWidth);
                borderLine.append(edge).append("+");
            }
            display[r * 2][0] = borderLine.toString();
            
            // --- Build Content Line (r * 2 + 1) ---
            StringBuilder contentLine = new StringBuilder();
            for (int c = 0; c < columns; c++) {
                Tile.DisplayInfo info = displayGrid[r][c];
                
                // Left edge of the current cell
                contentLine.append(info.leftEdge ? "|" : " ");
                
                // Center content (pad to fit contentWidth)
                String content = info.centerContent;
                contentLine.append(" ").append(content).append(" "); 
            }
            // Add the right edge of the last cell
            contentLine.append(displayGrid[r][columns - 1].rightEdge ? "|" : " ");
            display[r * 2 + 1][0] = contentLine.toString();
        }

        // --- Final Horizontal Border Line (rows * 2) ---
        StringBuilder finalBorder = new StringBuilder();
        finalBorder.append("+");
        for (int c = 0; c < columns; c++) {
            // The final border is the bottom edge of the last row
            String edge = displayGrid[rows - 1][c].bottomEdge ? "-".repeat(contentWidth) : " ".repeat(contentWidth);
            finalBorder.append(edge).append("+");
        }
        display[rows * 2][0] = finalBorder.toString();

        return display;
    }
}
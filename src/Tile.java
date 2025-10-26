/**
 * This java class represents a single numbered tile in sliding puzzle
 * It inherits from the Piece class and stores the tiles values
 */
public class Tile {

    // For Sliding Puzzle
    public String value; 
    
    // For Dots and Boxes, here this tile represents one box
    public int boxOwner = -1; 
    
    // For Visual State Container, replaces the cell display
    public static class DisplayInfo {
        public boolean topEdge = false;
        public boolean bottomEdge = false;
        public boolean leftEdge = false;
        public boolean rightEdge = false;
        public String centerContent = " ";
    }

    // Constructor for Sliding Puzzle, here the value is the tile number
    public Tile(String value) {
        this.value = value;
    }

    // Constructor for Dots and Boxes, it initializes the box state
    public Tile() {
        this.value = "Box"; // Differentiator for game logic
    }

    /**
     * Generates the visual information for the Board's universal printer,
     * relying on external game logic (in DotsAndBoxes) for edge state.
     */
    public DisplayInfo getDisplayInfo(char[] labels) {
        DisplayInfo info = new DisplayInfo();
        
        if (this.value.equals("Box")) {
            // Logic for Dots and Boxes
            
            // Center Content: The box owner label
            if (boxOwner != -1) {
                // Determine label (copied from DotsAndBoxes.labelFor logic)
                char ch = (labels != null && boxOwner >= 0 && boxOwner < labels.length) ? labels[boxOwner] : (boxOwner == 0 ? 'A' : 'B');
                info.centerContent = String.valueOf(ch);
            }
            // Edge state must be set by the DotsAndBoxes game class.
            
        } else {
            // Logic for Sliding Puzzle
            
            // Edges are always present
            info.topEdge = true;
            info.bottomEdge = true;
            info.leftEdge = true;
            info.rightEdge = true;
            
            // Center Content: The tile value
            info.centerContent = value.equals("0") ? " " : value;
        }
        return info;
    }
}

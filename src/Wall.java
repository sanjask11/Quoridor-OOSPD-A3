public class Wall extends Piece {
    // True if a wall segment is present in this slot
    private boolean isPresent; 

    // 'H' for horizontal segment, 'V' for vertical segment. 
    private final char orientation; 
    
    // 0 = No Owner, 1 = Player 1, 2 = Player 2
    private int owner; 

    // Constructor for a single wall segment slot
    public Wall(char orientation) {
        super("WALL_SLOT"); 
        this.orientation = orientation;
        this.isPresent = false;
        this.owner = 0;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void placeWall(int owner) {
        this.isPresent = true;
        this.owner = owner;
    }

    public int getOwner() {
        return owner;
    }
}
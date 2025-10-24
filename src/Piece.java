import java.util.Arrays;
import java.util.Random;

/**
 * This java class serves as a generic base for all the game elements.
 * It allows flexibility for reuse in different games
 */

abstract class Piece {
    protected String value;

    public Piece(String value){
        this.value = value;
    }
}

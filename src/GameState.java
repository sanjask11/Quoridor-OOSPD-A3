import java.io.Serializable;

public class GameState implements Serializable {
    public Object boardData;
    public int currentTurn;
    public int player1Score;
    public int player2Score;
    public boolean gameActive;
}

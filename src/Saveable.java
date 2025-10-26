import java.io.Serializable;

public interface Saveable extends Serializable {
    GameState saveState();
    void loadState(GameState state);
}

/**
 *  This class is responsible for launching the entire terminal arcade by creating an instance of the GameEngine and starting it.
 *  Entry point of the game
 */
public class App {
    public static void main(String[] args) throws Exception {
        GameEngine engine = new GameEngine();
        engine.start();
    }
}

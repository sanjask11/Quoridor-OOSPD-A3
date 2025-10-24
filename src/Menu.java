/**
 * Handles all the terminal facing messages and prompts which the user gets to see
 * It displays error messages, game winners, gameplay updates, etc
 */
public class Menu {

    public void displayWelcome(){
        System.out.println("\n=== Welcome to Terminal Game Arcade !!!! ===");
        System.out.println("Please follow the instructions in order to proceed! Press q to exit.");
    }

    public void displayError(String error){
        System.out.println("\nError: " + error +"\n");
    }

    public void displayMessage(String message){
        System.out.println(message);
    }

    public void displayMessages(String[] messages){
        for(int i = 0; i < messages.length; i++){
            System.out.println(messages[i]);
        }
    }

    public void displayVictory(Player player) {
        System.out.println("\nCongratulations, " + player.getName() +
                "! You solved the puzzle!");
    }

    public void printBoard(String[][] boardDisplay) {
        if (boardDisplay == null) return;
        for (int i = 0; i < boardDisplay.length; i++) {
            System.out.println(boardDisplay[i][0]);
        }
    }


}

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This java class deals with handling user inputs like player name, board dimensions, moves etc.
 * It validates user inputs and ensures a smooth experience.
 */
public class InputHandler {
    private final Scanner scanner;

    public InputHandler(){
        this.scanner = new Scanner(System.in);
    }

    public String getPlayerName(boolean multiplayer, String player) {
        if(multiplayer){
            System.out.print("What is " + player + "'s name?: ");
            return scanner.nextLine();
        }else{
            System.out.print("What is your name?: ");
            return scanner.nextLine();
        }
    }

    public int[] getBoardDimensions(){
        int[]choices = {-1, -1};
        System.out.println("\nChoose your board dimensions M x N");
        System.out.println("(Each dimension has to be greater than 0 and at most 10)");

        System.out.print("Rows: ");
        try{
            int rows = Integer.parseInt(scanner.nextLine());
            if(rows <= 0|| rows > 100){
                System.out.println("\nError: invalid input\n");
                return choices;
            }
            choices[0] = rows;
        } catch (NumberFormatException e){
            System.out.println("\nError: invalid input\n");
            return choices;
        }


        System.out.print("Columns: ");
        try{
            int columns = Integer.parseInt(scanner.nextLine());
            if(columns <= 0|| columns > 100){
                System.out.println("\nError: invalid input\n");
                return choices;
            }
            choices[1] = columns;
        } catch (NumberFormatException e){
            System.out.println("\nError: invalid input\n");
            return choices;
        }

        return choices;
    }
    //Gets user input that cannot be empty
    public String getInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    //Gets user input for the move the user wishes to make
    public String getMove(Player player, String message){
        System.out.print(player.getName() + message);

        return scanner.nextLine();
    }
    //Gets the users choice for which game they wish to play
    public int getGameChoice(){
        System.out.println("\nWhich game would you like to play?");
        System.out.println("0. Sliding Puzzle");
        System.out.println("1. Dots And Boxes");
        System.out.println("2. Quoridor");

        System.out.print("\nYour choice: ");
        try{
            int input = Integer.parseInt(scanner.nextLine().trim());
            //change
            if(input == 0 || input == 1 || input == 2){
                return input;
            }
            return -2;
        } catch (NumberFormatException e){
            return -1;
        }
    }

    public boolean askYesNo(String question) {
        System.out.print(question + " (y/n): ");
        String response = scanner.nextLine();

        return response.equals("y") || response.equals("yes");
    }

    public void close(){
        scanner.close();
    }


}

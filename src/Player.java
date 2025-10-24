/**
 * This java class represents a player in the game
 * It stores player details like player name , score and updates the score when needed.
 */
public class Player {
    private String name;
    private String team;
    private int score;

    public Player(String name){
        this.name = name;
    }

    public Player(String name, String team){
        this.name = name;
        this.team = team;
    }

    public String getName(){
        return name;
    }

    public String getTeam(){
        return this.team;
    }

    public int getScore(){
        return score;
    }

    public void addScore(int delta){
        score += delta;
    }

}

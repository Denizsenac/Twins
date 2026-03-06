package Game;
public class Player {

    private int x;
    private int y;
    private char symbol;
    private String name;
    private int score;

    public Player(int x, int y, char symbol, String name) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.name = name;
        this.score = 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public char getSymbol() { return symbol; }
    public String getName() { return name; }
    public int getScore() { return score; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public void addScore(int points) {
        score += points;
    }

    public boolean isAt(int tx, int ty) {
        return this.x == tx && this.y == ty;
    }
}

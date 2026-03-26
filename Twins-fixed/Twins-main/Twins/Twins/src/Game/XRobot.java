package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;
import java.util.Random;

public class XRobot {
    public int x;
    public int y;
    public int lifePoints = 1000;
    private int currentDir;
    private Random rnd = new Random();

    public XRobot(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.currentDir = rnd.nextInt(4);
    }

    public XRobot(int startX, int startY, int life) {
        this.x = startX;
        this.y = startY;
        this.lifePoints = life;
        this.currentDir = rnd.nextInt(4);
    }

    public void move(GameBoard board, EnemyManager enemies, int px, int py, BCharacter twin) {
        if (rnd.nextInt(100) < 25) {
            currentDir = rnd.nextInt(4);
        }

        int nextX = x;
        int nextY = y;

        if (currentDir == 0) nextY--;
        else if (currentDir == 1) nextX++;
        else if (currentDir == 2) nextY++;
        else if (currentDir == 3) nextX--;

        CollisionControl cc = new CollisionControl();

        if (cc.canRobotMove(board.getMap(), nextX, nextY, enemies, px, py, twin)) {
            x = nextX;
            y = nextY;
        } else {
            currentDir = rnd.nextInt(4);
        }
    }

    public void draw(Console cn) {
        TextAttributes robotColor = new TextAttributes(new Color(100, 180, 255), Color.BLACK);
        cn.getTextWindow().output((x * 2) + 4, y + 2, 'X', robotColor);
    }
}
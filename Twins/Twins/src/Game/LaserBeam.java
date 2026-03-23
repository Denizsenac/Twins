package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class LaserBeam {
    private int x;
    private int y;
    private int dirX;
    private int dirY;
    private boolean active;
    private int spawnTick;

    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 2;

    public LaserBeam(int startX, int startY, int dirX, int dirY, int spawnTick) {
        this.x = startX;
        this.y = startY;
        this.dirX = dirX;
        this.dirY = dirY;
        this.active = true;
        this.spawnTick = spawnTick;
    }

    public boolean advance(char[][] map, EnemyManager enemies) {
        if (!active) return false;

        int nextX = x + dirX;
        int nextY = y + dirY;

        if (nextY < 0 || nextY >= map.length || nextX < 0 || nextX >= map[0].length) {
            active = false;
            return false;
        }

        if (map[nextY][nextX] == '#') {
            active = false;
            return false;
        }

        x = nextX;
        y = nextY;

        if (enemies.isRobotAt(x, y)) {
            active = false;
            return true;
        }

        return false;
    }

    public void draw(Console cn) {
        if (!active) return;
        TextAttributes laserColor = new TextAttributes(Color.RED, Color.BLACK);
        int screenX = (x * 2) + OFFSET_X;
        int screenY = y + OFFSET_Y;
        char symbol = (dirX != 0) ? '-' : '|';
        cn.getTextWindow().output(screenX, screenY, symbol, laserColor);
    }

    public void erase(Console cn) {
        int screenX = (x * 2) + OFFSET_X;
        int screenY = y + OFFSET_Y;
        cn.getTextWindow().output(screenX, screenY, ' ');
    }

    public boolean isActive() { return active; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpawnTick() { return spawnTick; }
    public void deactivate() { active = false; }
}

package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class LaserBeam {
    private int x;
    private int y;
    private boolean active;
    private int spawnTick;

    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 2;

    public LaserBeam(int x, int y, int spawnTick) {
        this.x = x;
        this.y = y;
        this.active = true;
        this.spawnTick = spawnTick;
    }

    public void draw(Console cn) {
        if (!active) return;
        TextAttributes laserColor = new TextAttributes(Color.RED, Color.BLACK);
        int screenX = (x * 2) + OFFSET_X;
        int screenY = y + OFFSET_Y;
        cn.getTextWindow().output(screenX, screenY, '+', laserColor);
    }

    public void erase(Console cn) {
        int screenX = (x * 2) + OFFSET_X;
        int screenY = y + OFFSET_Y;
        cn.getTextWindow().output(screenX, screenY, ' ');
    }

    public boolean isExpired(int currentTick) {
        return currentTick - spawnTick >= 100;
    }

    public boolean isActive() { return active; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpawnTick() { return spawnTick; }
    public void deactivate() { active = false; }
}

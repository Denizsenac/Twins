package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class PackedLaser {
    private int x;
    private int y;
    private boolean collected;

    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 2;

    public PackedLaser(int x, int y) {
        this.x = x;
        this.y = y;
        this.collected = false;
    }

    public void draw(Console cn) {
        if (collected) return;
        TextAttributes color = new TextAttributes(new Color(0, 255, 255), Color.BLACK);
        int screenX = (x * 2) + OFFSET_X;
        int screenY = y + OFFSET_Y;
        cn.getTextWindow().output(screenX, screenY, '@', color);
    }

    public void erase(Console cn) {
        int screenX = (x * 2) + OFFSET_X;
        int screenY = y + OFFSET_Y;
        cn.getTextWindow().output(screenX, screenY, ' ');
    }

    public boolean checkPickup(int playerX, int playerY) {
        if (!collected && x == playerX && y == playerY) {
            collected = true;
            return true;
        }
        return false;
    }

    public boolean isCollected() { return collected; }
    public int getX() { return x; }
    public int getY() { return y; }
}

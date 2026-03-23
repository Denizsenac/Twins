package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class ScoreManager {

    private int score;
    private int health;
    private int maxHealth;

    private static final int HUD_X = 120;
    private static final int HUD_Y = 2;

    public ScoreManager() {
        this.score = 0;
        this.health = 100;
        this.maxHealth = 100;
    }

    public void addScore(int points) {
        score += points;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) health = maxHealth;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void drawHUD(Console cn, int ammo) {
        TextAttributes hudColor = new TextAttributes(Color.CYAN, Color.BLACK);
        TextAttributes scoreColor = new TextAttributes(Color.GREEN, Color.BLACK);
        TextAttributes healthColor;

        if (health > 60) {
            healthColor = new TextAttributes(Color.GREEN, Color.BLACK);
        } else if (health > 30) {
            healthColor = new TextAttributes(Color.YELLOW, Color.BLACK);
        } else {
            healthColor = new TextAttributes(Color.RED, Color.BLACK);
        }

        TextAttributes ammoColor = new TextAttributes(Color.YELLOW, Color.BLACK);
        TextAttributes labelColor = new TextAttributes(Color.WHITE, Color.BLACK);

        clearHUDArea(cn);

        drawString(cn, HUD_X, HUD_Y, "SCORE: ", labelColor);
        drawString(cn, HUD_X + 7, HUD_Y, "" + score, scoreColor);

        drawString(cn, HUD_X, HUD_Y + 2, "HEALTH: ", labelColor);
        drawString(cn, HUD_X + 8, HUD_Y + 2, "" + health + "/" + maxHealth, healthColor);

        drawHealthBar(cn, HUD_X, HUD_Y + 3, healthColor);

        drawString(cn, HUD_X, HUD_Y + 5, "AMMO: ", labelColor);
        drawString(cn, HUD_X + 6, HUD_Y + 5, "" + ammo, ammoColor);

        drawString(cn, HUD_X, HUD_Y + 8, "[SPACE] Shoot", hudColor);
        drawString(cn, HUD_X, HUD_Y + 9, "[M] Toggle Mode", hudColor);
        drawString(cn, HUD_X, HUD_Y + 10, "[ESC] Save/Quit", hudColor);
        drawString(cn, HUD_X, HUD_Y + 12, "* = Laser Pack", ammoColor);
    }

    private void drawHealthBar(Console cn, int x, int y, TextAttributes color) {
        TextAttributes emptyColor = new TextAttributes(Color.DARK_GRAY, Color.BLACK);
        int barLength = 20;
        int filled = (health * barLength) / maxHealth;

        drawString(cn, x, y, "[", new TextAttributes(Color.WHITE, Color.BLACK));
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                drawChar(cn, x + 1 + i, y, '=', color);
            } else {
                drawChar(cn, x + 1 + i, y, '.', emptyColor);
            }
        }
        drawString(cn, x + 1 + barLength, y, "]", new TextAttributes(Color.WHITE, Color.BLACK));
    }

    public void drawGameOver(Console cn) {
        TextAttributes gameOverColor = new TextAttributes(Color.RED, Color.BLACK);
        TextAttributes scoreColor = new TextAttributes(Color.YELLOW, Color.BLACK);

        drawString(cn, HUD_X, HUD_Y + 15, "GAME OVER!", gameOverColor);
        drawString(cn, HUD_X, HUD_Y + 16, "Final Score: " + score, scoreColor);
        drawString(cn, HUD_X, HUD_Y + 18, "Press ESC to exit", new TextAttributes(Color.WHITE, Color.BLACK));
    }

    private void clearHUDArea(Console cn) {
        for (int row = HUD_Y; row < HUD_Y + 20; row++) {
            for (int col = HUD_X; col < HUD_X + 30; col++) {
                cn.getTextWindow().output(col, row, ' ');
            }
        }
    }

    private void drawString(Console cn, int x, int y, String text, TextAttributes attr) {
        for (int i = 0; i < text.length(); i++) {
            cn.getTextWindow().output(x + i, y, text.charAt(i), attr);
        }
    }

    private void drawChar(Console cn, int x, int y, char c, TextAttributes attr) {
        cn.getTextWindow().output(x, y, c, attr);
    }

    public int getScore() { return score; }
    public int getHealth() { return health; }
}

package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class ScoreManager {

    private int score;
    private int health;
    private int maxHealth;
    private int computerScore;  // FIX: track computer score

    private static final int HUD_X = 120;
    private static final int HUD_Y = 2;

    public ScoreManager() {
        this.score = 0;
        this.health = 1000;
        this.maxHealth = 1000;
        this.computerScore = 0;
    }

    public void addScore(int points) {
        score += points;
    }

    // FIX: method to accumulate computer score
    public void addComputerScore(int points) {
        computerScore += points;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public boolean isAlive() {
        return health > 0;
    }

    // FIX: drawHUD now accepts time in ticks, cCount, xCount
    public void drawHUD(Console cn, int ammo, int currentTick, int cCount, int xCount) {
        TextAttributes hudColor    = new TextAttributes(new Color(0, 255, 255), Color.BLACK);
        TextAttributes scoreColor  = new TextAttributes(new Color(57, 255, 20), Color.BLACK);
        TextAttributes cscoreColor = new TextAttributes(new Color(255, 80, 80), Color.BLACK);
        TextAttributes ammoColor   = new TextAttributes(new Color(255, 255, 0), Color.BLACK);
        TextAttributes labelColor  = new TextAttributes(new Color(0, 255, 255), Color.BLACK);
        TextAttributes timeColor   = new TextAttributes(new Color(200, 200, 255), Color.BLACK);
        TextAttributes healthColor;

        if (health > 600) {
            healthColor = new TextAttributes(new Color(57, 255, 20), Color.BLACK);
        } else if (health > 300) {
            healthColor = new TextAttributes(new Color(255, 255, 0), Color.BLACK);
        } else {
            healthColor = new TextAttributes(new Color(255, 50, 50), Color.BLACK);
        }

        clearHUDArea(cn);

        // FIX: Time in seconds (20 ticks = 1 second)
        int seconds = currentTick / 20;
        drawString(cn, HUD_X,      HUD_Y,     "Time   : ", labelColor);
        drawString(cn, HUD_X + 9,  HUD_Y,     "" + seconds, timeColor);

        drawString(cn, HUD_X,      HUD_Y + 2, "P.Score: ", labelColor);
        drawString(cn, HUD_X + 9,  HUD_Y + 2, "" + score, scoreColor);

        drawString(cn, HUD_X,      HUD_Y + 3, "P.Life : ", labelColor);
        drawString(cn, HUD_X + 9,  HUD_Y + 3, "" + health + "/" + maxHealth, healthColor);

        drawHealthBar(cn, HUD_X, HUD_Y + 4, healthColor);

        drawString(cn, HUD_X,      HUD_Y + 6, "P.Laser: ", labelColor);
        drawString(cn, HUD_X + 9,  HUD_Y + 6, "" + ammo, ammoColor);

        // FIX: Computer score and robot counts
        drawString(cn, HUD_X,      HUD_Y + 9,  "C.Score : ", labelColor);
        drawString(cn, HUD_X + 10, HUD_Y + 9,  "" + computerScore, cscoreColor);

        drawString(cn, HUD_X,      HUD_Y + 10, "C-Robots: ", labelColor);
        drawString(cn, HUD_X + 10, HUD_Y + 10, "" + cCount, cscoreColor);

        drawString(cn, HUD_X,      HUD_Y + 11, "X-Robots: ", labelColor);
        drawString(cn, HUD_X + 10, HUD_Y + 11, "" + xCount, cscoreColor);

        drawString(cn, HUD_X, HUD_Y + 14, "[ARROWS] Move",       hudColor);
        drawString(cn, HUD_X, HUD_Y + 15, "[SPACE]  Shoot",      hudColor);
        drawString(cn, HUD_X, HUD_Y + 16, "[M]      Toggle Mode",hudColor);
        drawString(cn, HUD_X, HUD_Y + 17, "[ESC]    Save/Quit",  hudColor);
        drawString(cn, HUD_X, HUD_Y + 18, "@ = Laser Pack",
                new TextAttributes(new Color(0, 255, 255), Color.BLACK));
    }

    private void drawHealthBar(Console cn, int x, int y, TextAttributes color) {
        TextAttributes emptyColor = new TextAttributes(new Color(60, 60, 60), Color.BLACK);
        int barLength = 20;
        int filled = (health * barLength) / maxHealth;

        drawString(cn, x, y, "[", new TextAttributes(new Color(0, 255, 255), Color.BLACK));
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                drawChar(cn, x + 1 + i, y, '=', color);
            } else {
                drawChar(cn, x + 1 + i, y, '.', emptyColor);
            }
        }
        drawString(cn, x + 1 + barLength, y, "]", new TextAttributes(new Color(0, 255, 255), Color.BLACK));
    }

    public void drawGameOver(Console cn) {
        TextAttributes bgColor = new TextAttributes(Color.BLACK, Color.BLACK);
        for (int row = 0; row < 50; row++) {
            for (int col = 0; col < 200; col++) {
                cn.getTextWindow().output(col, row, ' ', bgColor);
            }
        }

        TextAttributes redColor   = new TextAttributes(new Color(255, 50, 50),  Color.BLACK);
        TextAttributes scoreColor = new TextAttributes(new Color(255, 255, 0),  Color.BLACK);
        TextAttributes hintColor  = new TextAttributes(new Color(0, 255, 255),  Color.BLACK);
        TextAttributes lineColor  = new TextAttributes(new Color(100, 100, 100),Color.BLACK);

        String[] gameOverArt = {
            "  ____    _    __  __ _____    _____     _______ ____  ",
            " / ___|  / \\  |  \\/  | ____|  / _ \\ \\   / / ____|  _ \\ ",
            "| |  _  / _ \\ | |\\/| |  _|   | | | \\ \\ / /|  _| | |_) |",
            "| |_| |/ ___ \\| |  | | |___  | |_| |\\ V / | |___|  _ < ",
            " \\____/_/   \\_\\_|  |_|_____|  \\___/  \\_/  |_____|_| \\_\\"
        };

        int artX = 72;
        int artY = 12;
        for (int i = 0; i < gameOverArt.length; i++) {
            drawString(cn, artX, artY + i, gameOverArt[i], redColor);
        }

        String scoreLine = "P.SCORE: " + score + "     C.SCORE: " + computerScore;
        int scoreX = 100 - (scoreLine.length() / 2);
        drawString(cn, scoreX, artY + 8, scoreLine, scoreColor);

        String divider = "- - - - - - - - - - - - - - - - - -";
        int divX = 100 - (divider.length() / 2);
        drawString(cn, divX, artY + 6,  divider, lineColor);
        drawString(cn, divX, artY + 10, divider, lineColor);

        String hint = "Press ESC to return to menu";
        int hintX = 100 - (hint.length() / 2);
        drawString(cn, hintX, artY + 13, hint, hintColor);
    }

    private void clearHUDArea(Console cn) {
        for (int row = HUD_Y; row < HUD_Y + 22; row++) {
            for (int col = HUD_X; col < HUD_X + 35; col++) {
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

    public int getScore()         { return score; }
    public int getComputerScore() { return computerScore; }
    public int getHealth()        { return health; }

    // FIX: setters needed for save/load restore
    public void setScore(int s)         { this.score = s; }
    public void setComputerScore(int s) { this.computerScore = s; }
    public void setHealth(int h)        { this.health = h; }
}

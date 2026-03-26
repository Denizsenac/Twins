package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class ModeMenuUI {

    private void drawText(Console cn, int x, int y, String text, TextAttributes attr) {
        for (int i = 0; i < text.length(); i++) {
            cn.getTextWindow().output(x + i, y, text.charAt(i), attr);
        }
    }

    public void drawMenu(Console cn, int selectedOption, int startX, int startY) {
        TextAttributes cyanColor  = new TextAttributes(new Color(57, 255, 20), Color.BLACK);
        TextAttributes grayColor  = new TextAttributes(new Color(100, 100, 100), Color.BLACK);
        TextAttributes whiteColor = new TextAttributes(new Color(0, 255, 255), Color.BLACK);

        drawText(cn, startX, startY,     "   --- GAME MODE SELECTION ---   ", cyanColor);

        drawText(cn, startX, startY + 3, selectedOption == 1  ? "       >> Randomized Map <<       " : "          Randomized Map          ", selectedOption == 1  ? cyanColor : grayColor);
        drawText(cn, startX, startY + 5, selectedOption == 2  ? "        >>  Export Map  <<        " : "            Export Map            ", selectedOption == 2  ? cyanColor : grayColor);

        drawText(cn, startX - 5, startY + 8,  "---------------------------------------", whiteColor);
        drawText(cn, startX - 5, startY + 9,  " [UP/DOWN] to navigate, [ENTER] to select ", whiteColor);
    }
}

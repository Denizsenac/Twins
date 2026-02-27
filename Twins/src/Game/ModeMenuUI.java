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
    
    public void drawMenu(Console cn, int selectedOption) {
        
        TextAttributes cyanColor = new TextAttributes(Color.CYAN, Color.BLACK);
        TextAttributes whiteColor = new TextAttributes(Color.WHITE, Color.BLACK);
        TextAttributes grayColor = new TextAttributes(Color.LIGHT_GRAY, Color.BLACK);
        
        drawText(cn, 0, 4, "                     ================================", cyanColor);
        drawText(cn, 0, 5, "                       TWINS - GAME MODE SELECTION   ", cyanColor);
        drawText(cn, 0, 6, "                     ================================", cyanColor);
        
        if (selectedOption == 1) {
            drawText(cn, 0, 9,  "             >> MODE  1 : Your twin moves in the SAME direction <<", cyanColor);
            drawText(cn, 0, 11, "                MODE -1 : Your twin moves in the OPPOSITE direction   ", grayColor);
        } else {
            drawText(cn, 0, 9,  "                MODE  1 : Your twin moves in the SAME direction   ", grayColor);
            drawText(cn, 0, 11, "             >> MODE -1 : Your twin moves in the OPPOSITE direction <<", cyanColor);
        }
        
        drawText(cn, 0, 14, "          ----------------------------------------------------------", whiteColor);
        drawText(cn, 0, 15, "           Use [UP/DOWN] arrow keys to navigate, [ENTER] to select", whiteColor);
    }
}
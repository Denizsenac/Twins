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
        
        TextAttributes cyanColor = new TextAttributes(Color.CYAN, Color.BLACK);
        TextAttributes whiteColor = new TextAttributes(Color.WHITE, Color.BLACK);
        TextAttributes grayColor = new TextAttributes(Color.LIGHT_GRAY, Color.BLACK);
        
        drawText(cn, startX, startY,     "   --- GAME MODE SELECTION ---   ", cyanColor);
        
        if (selectedOption == 1) {
            drawText(cn, startX, startY + 3, "       >> Randomized Map <<       ", cyanColor);
            drawText(cn, startX, startY + 5,   "            Export Map             ", grayColor);
        } else {
            drawText(cn, startX, startY + 3, "          Randomized Map        ", grayColor);
            drawText(cn, startX, startY + 5, "        >> Export Map <<          ", cyanColor);
        }
        
        drawText(cn, startX - 5, startY + 8, "---------------------------------------", whiteColor);
        drawText(cn, startX - 5, startY + 9, " [UP/DOWN] to navigate, [ENTER] to select ", whiteColor);
    }
}
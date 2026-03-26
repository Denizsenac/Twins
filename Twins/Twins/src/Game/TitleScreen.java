package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class TitleScreen {
    
    private final String[] logoLines = {
        " ___________   __          __   _____   ____  _____     _______ ",
        "|           | |  |        |  | |     | |    \\|     |   |       |",
        "|____   ____| |  |   __   |  | |     | |           |   |   ____|",
        "     | |      |  |  |  |  |  | |     | |           |   |  |___  ",
        "     | |      |  |__|  |__|  | |     | |     |\\    |   |___   | ",
        "     | |      |              | |     | |     | \\   |    ___|  | ",
        "     |_|      |______________| |_____| |_____|  \\__|   |______| "
    };

    public void drawLogo(Console cn, int startX, int startY) {
        TextAttributes color = new TextAttributes(Color.CYAN, Color.BLACK);
        
        for (int i = 0; i < logoLines.length; i++) {
            for (int j = 0; j < logoLines[i].length(); j++) {
                cn.getTextWindow().output(startX + j, startY + i, logoLines[i].charAt(j), color);
            }
        }
    }
}
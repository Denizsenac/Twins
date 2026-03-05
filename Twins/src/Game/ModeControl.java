package Game;

import enigma.console.Console;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ModeControl {
    
    private int menuKeypr = 0;
    private int menuRkey = 0;

    public int selectMode(Console cn) throws InterruptedException {
        ModeMenuUI ui = new ModeMenuUI();
        TitleScreen logo = new TitleScreen(); 
        int currentSelection = 1; 
        
        logo.drawLogo(cn, 67, 10);
        ui.drawMenu(cn, currentSelection, 80, 20);

        KeyListener menuListener = new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                if(menuKeypr == 0) {
                    menuKeypr = 1;
                    menuRkey = e.getKeyCode();
                }
            }
            public void keyReleased(KeyEvent e) {}
        };

        cn.getTextWindow().addKeyListener(menuListener);

        int finalMode = 1;
        boolean isSelecting = true;

        while (isSelecting) {
            if (menuKeypr == 1) {
                
                if (menuRkey == KeyEvent.VK_UP || menuRkey == KeyEvent.VK_DOWN) {
                    currentSelection = (currentSelection == 1) ? -1 : 1;
                    ui.drawMenu(cn, currentSelection, 80, 20); 
                } 
                
                else if (menuRkey == KeyEvent.VK_ENTER) {
                    finalMode = currentSelection;
                    isSelecting = false;
                }
                menuKeypr = 0; 
            }
            Thread.sleep(20); 
        }

        cn.getTextWindow().removeKeyListener(menuListener);
        
        for (int y = 0; y < 50; y++) {
            for (int x = 0; x < 200; x++) {
                cn.getTextWindow().output(x, y, ' ');
            }
        }

        return finalMode;
    }
}
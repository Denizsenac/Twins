package Game;

import enigma.console.Console;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ModeControl {
    
    
    private int menuKeypr = 0;
    private int menuRkey = 0;

    public int selectMode(Console cn) throws InterruptedException {
        ModeMenuUI ui = new ModeMenuUI();
        int currentSelection = 1; 
        
        
        ui.drawMenu(cn, currentSelection);

        
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
                    ui.drawMenu(cn, currentSelection); 
                } 
                
                else if (menuRkey == KeyEvent.VK_ENTER) {
                    finalMode = currentSelection;
                    isSelecting = false;
                }
                menuKeypr = 0; 
            }
            Thread.sleep(20); 
        }

        // ÇOK ÖNEMLİ: Seçim bittiği için bu dinleyiciyi kaldırıyoruz ki oyunun kendi hareket kontrollerini bozmasın!
        cn.getTextWindow().removeKeyListener(menuListener);
        
        
        cn.getTextWindow().setCursorPosition(0, 0);
        for(int i=0; i<15; i++) {
            System.out.println("                                                                        ");
        }
        cn.getTextWindow().setCursorPosition(0, 0);

        return finalMode;
    }
}
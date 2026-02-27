package Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.console.Console;

public class GameControls {
    public int rkey = 0;
    public int keypr = 0;

    public GameControls(Console cn) {
        cn.getTextWindow().addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                if (keypr == 0) {
                    keypr = 1;
                    rkey = e.getKeyCode();
                }
            }
            public void keyReleased(KeyEvent e) {}
        });
    }

    public int consumeKey() {
        if (keypr == 1) {
            int k = rkey;
            keypr = 0;
            return k;
        }
        return 0;
    }
}
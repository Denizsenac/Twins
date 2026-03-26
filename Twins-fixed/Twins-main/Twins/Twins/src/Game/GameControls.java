package Game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import enigma.console.Console;
public class GameControls {

    public Console cn;
    public KeyListener klis;

    public int keypr;
    public int rkey;

    GameControls(Console cn) throws Exception {
        this.cn = cn;

        klis = new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                if (keypr == 0) {
                    keypr = 1;
                    rkey = e.getKeyCode();
                }
            }
            public void keyReleased(KeyEvent e) {}
        };
        cn.getTextWindow().addKeyListener(klis);
    }

    public int consumeKey() {
        if (keypr == 1) {
            keypr = 0;
            return rkey;
        }
        return 0;
    }
}
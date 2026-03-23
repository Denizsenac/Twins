package Game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import enigma.console.Console;
import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

/*
 * GameControls — written by Buğra Akış
 *
 * Added two-player support so both twins can race through the maze at the same time.
 *   Player 1 (P) — WASD keys
 *   Player 2 (Q) — Arrow keys
 * When the two players meet on the same tile the round ends and scores are shown.
 */
public class GameControls {

    public Console cn;
    public TextMouseListener tmlis;
    public KeyListener klis;

    public int keypr;
    public int rkey;

    // Offset constants matching GameBoard.printBoard()
    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 2;

    // Simple constructor for GameEngine — only sets up key listening
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

    // Two-player constructor — runs its own game loop
    GameControls(GameBoard board) throws Exception {
        this.cn = Enigma.getConsole("Twins - Maze Game", 200, 50, 12);

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

        board.printBoard(cn);

        // Spawn both players at different random positions
        RandomSpawner spawner = new RandomSpawner();
        int[] spawn1 = spawner.getSpawnPoint(board.getMap());
        int[] spawn2 = spawner.getSpawnPoint(board.getMap());

        // Make sure they don't start on the same tile
        while (spawn2[0] == spawn1[0] && spawn2[1] == spawn1[1]) {
            spawn2 = spawner.getSpawnPoint(board.getMap());
        }

        Player p1 = new Player(spawn1[0], spawn1[1], 'P', "Player 1");
        Player p2 = new Player(spawn2[0], spawn2[1], 'Q', "Player 2");

        drawPlayer(p1);
        drawPlayer(p2);

        drawScoreboard(p1, p2);

        CollisionControl collision = new CollisionControl();

        while (true) {
            if (keypr == 1) {
                // --- Player 1: WASD ---
                int n1x = p1.getX();
                int n1y = p1.getY();
                if (rkey == KeyEvent.VK_W) n1y--;
                if (rkey == KeyEvent.VK_S) n1y++;
                if (rkey == KeyEvent.VK_A) n1x--;
                if (rkey == KeyEvent.VK_D) n1x++;

                if (collision.canMove(board.getMap(), n1x, n1y)) {
                    erasePlayer(p1);
                    p1.setX(n1x);
                    p1.setY(n1y);
                    drawPlayer(p1);
                }

                // --- Player 2: Arrow keys ---
                int n2x = p2.getX();
                int n2y = p2.getY();
                if (rkey == KeyEvent.VK_UP)    n2y--;
                if (rkey == KeyEvent.VK_DOWN)  n2y++;
                if (rkey == KeyEvent.VK_LEFT)  n2x--;
                if (rkey == KeyEvent.VK_RIGHT) n2x++;

                if (collision.canMove(board.getMap(), n2x, n2y)) {
                    erasePlayer(p2);
                    p2.setX(n2x);
                    p2.setY(n2y);
                    drawPlayer(p2);
                }

                // --- Check if the twins have met ---
                if (p1.isAt(p2.getX(), p2.getY())) {
                    p1.addScore(1);
                    p2.addScore(1);
                    drawScoreboard(p1, p2);
                    showMeetMessage();

                    // Respawn both players for the next round
                    erasePlayer(p1);
                    erasePlayer(p2);
                    spawn1 = spawner.getSpawnPoint(board.getMap());
                    spawn2 = spawner.getSpawnPoint(board.getMap());
                    while (spawn2[0] == spawn1[0] && spawn2[1] == spawn1[1])
                        spawn2 = spawner.getSpawnPoint(board.getMap());
                    p1.setX(spawn1[0]); p1.setY(spawn1[1]);
                    p2.setX(spawn2[0]); p2.setY(spawn2[1]);
                    drawPlayer(p1);
                    drawPlayer(p2);
                }

                keypr = 0;
            }
            Thread.sleep(20);
        }
    }

    // Consume the latest key press; returns 0 if no key is waiting
    public int consumeKey() {
        if (keypr == 1) {
            keypr = 0;
            return rkey;
        }
        return 0;
    }

    private void drawPlayer(Player p) {
        int sx = (p.getX() * 2) + OFFSET_X;
        int sy = p.getY() + OFFSET_Y;
        cn.getTextWindow().output(sx, sy, p.getSymbol());
    }

    private void erasePlayer(Player p) {
        int sx = (p.getX() * 2) + OFFSET_X;
        int sy = p.getY() + OFFSET_Y;
        cn.getTextWindow().output(sx, sy, ' ');
    }

    private void drawScoreboard(Player p1, Player p2) {
        cn.getTextWindow().setCursorPosition(0, 0);
        cn.getTextWindow().output(
            p1.getName() + " [WASD]: " + p1.getScore() + "   " +
            p2.getName() + " [Arrows]: " + p2.getScore() + "   "
        );
    }

    private void showMeetMessage() {
        cn.getTextWindow().setCursorPosition(0, 1);
        cn.getTextWindow().output("The twins met! Respawning...");
    }
}
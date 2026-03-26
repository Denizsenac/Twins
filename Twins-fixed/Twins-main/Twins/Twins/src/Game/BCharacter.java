package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;

public class BCharacter {
    private int x;
    private int y;

    public BCharacter(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void move(MoveTrack tracker, GameBoard board, int currentMode, TrailManager tm, int currentTick, EnemyManager enemyManager) {
        int nextX = this.x;
        int nextY = this.y;

        if (currentMode == 1) {
            nextX += tracker.getSameDirX();
            nextY += tracker.getSameDirY();
        } else if (currentMode == -1) {
            nextX += tracker.getOppositeDirX();
            nextY += tracker.getOppositeDirY();
        }

        CollisionControl collision = new CollisionControl();
        if (collision.canPlayerMove(board.getMap(), nextX, nextY, enemyManager)) {
            tm.addTrail(this.x, this.y, currentTick);
            this.x = nextX;
            this.y = nextY;
        }
    }

    public void draw(Console cn, int playerX, int playerY, TextAttributes color) {
        if (this.x != playerX || this.y != playerY) {
            cn.getTextWindow().output((this.x * 2) + 4, this.y + 2, 'B', color);
        }
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
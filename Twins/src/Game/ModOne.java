package Game;

import enigma.console.Console;

public class ModOne {
    private int x;
    private int y;

    
    public ModOne(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void move(MoveTrack tracker, GameBoard board) {
        int nextX = this.x + tracker.getSameDirX();
        int nextY = this.y + tracker.getSameDirY();

        CollisionControl collision = new CollisionControl();
        if (collision.canMove(board.getMap(), nextX, nextY)) {
            this.x = nextX;
            this.y = nextY;
        }
    }

    public void draw(Console cn, int playerX, int playerY) {
        if (this.x != playerX || this.y != playerY) {
            cn.getTextWindow().output((this.x * 2) + 4, this.y + 2, 'B'); 
        }
    }
}
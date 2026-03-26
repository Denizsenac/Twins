package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class CRobot {
    public int x;
    public int y;
    public int lifePoints = 1000;

    public CRobot(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public CRobot(int startX, int startY, int life) {
        this.x = startX;
        this.y = startY;
        this.lifePoints = life;
    }

    public void move(GameBoard board, EnemyManager enemies, int px, int py, BCharacter twin,
                     int[] tX, int[] tY, boolean[] tActive, int tCount) {
        // Find nearest treasure by Manhattan distance
        int bestDist = Integer.MAX_VALUE;
        int targetX = x;
        int targetY = y;
        for (int i = 0; i < tCount; i++) {
            if (!tActive[i]) continue;
            int dist = Math.abs(tX[i] - x) + Math.abs(tY[i] - y);
            if (dist < bestDist) {
                bestDist = dist;
                targetX = tX[i];
                targetY = tY[i];
            }
        }

        if (bestDist == Integer.MAX_VALUE) return;

        // Try to move toward treasure - prefer larger axis difference
        int dx = targetX - x;
        int dy = targetY - y;
        CollisionControl cc = new CollisionControl();

        // Try primary direction first, then secondary
        int moveX1 = 0, moveY1 = 0;
        int moveX2 = 0, moveY2 = 0;

        if (Math.abs(dx) >= Math.abs(dy)) {
            moveX1 = (dx > 0) ? 1 : (dx < 0) ? -1 : 0;
            moveY2 = (dy > 0) ? 1 : (dy < 0) ? -1 : 0;
        } else {
            moveY1 = (dy > 0) ? 1 : (dy < 0) ? -1 : 0;
            moveX2 = (dx > 0) ? 1 : (dx < 0) ? -1 : 0;
        }

        if (moveX1 != 0 || moveY1 != 0) {
            if (cc.canRobotMove(board.getMap(), x + moveX1, y + moveY1, enemies, px, py, twin)) {
                x += moveX1;
                y += moveY1;
                return;
            }
        }

        if (moveX2 != 0 || moveY2 != 0) {
            if (cc.canRobotMove(board.getMap(), x + moveX2, y + moveY2, enemies, px, py, twin)) {
                x += moveX2;
                y += moveY2;
                return;
            }
        }
        // Stuck on obstacle - do nothing
    }

    public void draw(Console cn) {
        TextAttributes robotColor = new TextAttributes(new Color(255, 80, 80), Color.BLACK);
        cn.getTextWindow().output((x * 2) + 4, y + 2, 'C', robotColor);
    }
}

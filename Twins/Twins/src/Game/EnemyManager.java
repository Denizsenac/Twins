package Game;

import enigma.console.Console;

public class EnemyManager {

    private XRobot[] xRobots = new XRobot[100];
    private int xCount = 0;

    private CRobot[] cRobots = new CRobot[100];
    private int cCount = 0;

    public void addXRobot(int x, int y) {
        if (xCount < xRobots.length) {
            xRobots[xCount] = new XRobot(x, y);
            xCount++;
        }
    }

    public void addXRobot(int x, int y, int life) {
        if (xCount < xRobots.length) {
            xRobots[xCount] = new XRobot(x, y, life);
            xCount++;
        }
    }

    public void addCRobot(int x, int y, int life) {
        if (cCount < cRobots.length) {
            cRobots[cCount] = new CRobot(x, y, life);
            cCount++;
        }
    }

    public int getRobotCount() {
        return xCount + cCount;
    }

    public int getXCount() { return xCount; }
    public int getCCount() { return cCount; }

    public int getRobotX(int i) {
        if (i < xCount) return xRobots[i].x;
        return cRobots[i - xCount].x;
    }

    public int getRobotY(int i) {
        if (i < xCount) return xRobots[i].y;
        return cRobots[i - xCount].y;
    }

    public int getRobotLife(int i) {
        if (i < xCount) return xRobots[i].lifePoints;
        return cRobots[i - xCount].lifePoints;
    }

    public void moveRobots(GameBoard board, TrailManager tm, int currentTick, int playerX, int playerY, BCharacter twin) {
        for (int i = 0; i < xCount; i++) {
            tm.addTrail(xRobots[i].x, xRobots[i].y, currentTick);
            xRobots[i].move(board, this, playerX, playerY, twin);
        }
    }

    public void moveCRobots(GameBoard board, TrailManager tm, int currentTick, int playerX, int playerY, BCharacter twin,
                            int[] tX, int[] tY, boolean[] tActive, int tCount) {
        for (int i = 0; i < cCount; i++) {
            tm.addTrail(cRobots[i].x, cRobots[i].y, currentTick);
            cRobots[i].move(board, this, playerX, playerY, twin, tX, tY, tActive, tCount);
        }
    }

    public void drawRobots(Console cn) {
        for (int i = 0; i < xCount; i++) {
            xRobots[i].draw(cn);
        }
        for (int i = 0; i < cCount; i++) {
            cRobots[i].draw(cn);
        }
    }

    public boolean isRobotAt(int targetX, int targetY) {
        for (int i = 0; i < xCount; i++) {
            if (xRobots[i].x == targetX && xRobots[i].y == targetY) return true;
        }
        for (int i = 0; i < cCount; i++) {
            if (cRobots[i].x == targetX && cRobots[i].y == targetY) return true;
        }
        return false;
    }

    public int damageRobotAt(int targetX, int targetY, int damage, Console cn) {
        for (int i = 0; i < xCount; i++) {
            if (xRobots[i].x == targetX && xRobots[i].y == targetY) {
                xRobots[i].lifePoints -= damage;
                if (xRobots[i].lifePoints <= 0) {
                    cn.getTextWindow().output((xRobots[i].x * 2) + 4, xRobots[i].y + 2, ' ');
                    for (int j = i; j < xCount - 1; j++) {
                        xRobots[j] = xRobots[j + 1];
                    }
                    xRobots[xCount - 1] = null;
                    xCount--;
                    return 1;
                }
                return 0;
            }
        }
        for (int i = 0; i < cCount; i++) {
            if (cRobots[i].x == targetX && cRobots[i].y == targetY) {
                cRobots[i].lifePoints -= damage;
                if (cRobots[i].lifePoints <= 0) {
                    cn.getTextWindow().output((cRobots[i].x * 2) + 4, cRobots[i].y + 2, ' ');
                    for (int j = i; j < cCount - 1; j++) {
                        cRobots[j] = cRobots[j + 1];
                    }
                    cRobots[cCount - 1] = null;
                    cCount--;
                    return 1;
                }
                return 0;
            }
        }
        return 0;
    }

    public boolean isAdjacentToPlayer(int playerX, int playerY) {
        return countAdjacentToPlayer(playerX, playerY) > 0;
    }

    public int countAdjacentToPlayer(int playerX, int playerY) {
        int count = 0;
        for (int i = 0; i < xCount; i++) {
            int dx = Math.abs(xRobots[i].x - playerX);
            int dy = Math.abs(xRobots[i].y - playerY);
            if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) count++;
        }
        for (int i = 0; i < cCount; i++) {
            int dx = Math.abs(cRobots[i].x - playerX);
            int dy = Math.abs(cRobots[i].y - playerY);
            if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) count++;
        }
        return count;
    }
}

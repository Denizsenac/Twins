package Game;

import enigma.console.Console;

public class EnemyManager {

    private XRobot[] xRobots = new XRobot[100];
    private int robotCount = 0;

    public void addXRobot(int x, int y) {
        if (robotCount < xRobots.length) {
            xRobots[robotCount] = new XRobot(x, y);
            robotCount++;
        }
    }

    public void addXRobot(int x, int y, int life) {
        if (robotCount < xRobots.length) {
            xRobots[robotCount] = new XRobot(x, y, life);
            robotCount++;
        }
    }

    public int getRobotCount() {
        return robotCount;
    }

    public int getRobotX(int i) {
        return xRobots[i].x;
    }

    public int getRobotY(int i) {
        return xRobots[i].y;
    }

    public int getRobotLife(int i) {
        return xRobots[i].lifePoints;
    }

    public void moveRobots(GameBoard board, TrailManager tm, int currentTick, int playerX, int playerY, BCharacter twin) {
        for (int i = 0; i < robotCount; i++) {
            tm.addTrail(xRobots[i].x, xRobots[i].y, currentTick);
            xRobots[i].move(board, this, playerX, playerY, twin);
        }
    }

    public void drawRobots(Console cn) {
        for (int i = 0; i < robotCount; i++) {
            xRobots[i].draw(cn);
        }
    }

    public boolean isRobotAt(int targetX, int targetY) {
        for (int i = 0; i < robotCount; i++) {
            if (xRobots[i].x == targetX && xRobots[i].y == targetY) {
                return true;
            }
        }
        return false;
    }

    public int damageRobotAt(int targetX, int targetY, int damage, Console cn) {
        for (int i = 0; i < robotCount; i++) {
            if (xRobots[i].x == targetX && xRobots[i].y == targetY) {
                xRobots[i].lifePoints -= damage;
                if (xRobots[i].lifePoints <= 0) {
                    cn.getTextWindow().output((xRobots[i].x * 2) + 4, xRobots[i].y + 2, ' ');
                    for (int j = i; j < robotCount - 1; j++) {
                        xRobots[j] = xRobots[j + 1];
                    }
                    xRobots[robotCount - 1] = null;
                    robotCount--;
                    return 1;
                }
                return 0;
            }
        }
        return 0;
    }

    public boolean isAdjacentToPlayer(int playerX, int playerY) {
        for (int i = 0; i < robotCount; i++) {
            int dx = xRobots[i].x - playerX;
            int dy = xRobots[i].y - playerY;
            if (dx >= -1 && dx <= 1 && dy >= -1 && dy <= 1) {
                return true;
            }
        }
        return false;
    }
}
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
    
}
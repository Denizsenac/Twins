package Game;

import enigma.console.Console;

public class EnemyManager {

    // XRobot dizisi (rastgele hareket eden)
    private XRobot[] xRobots = new XRobot[100];
    private int xRobotCount = 0;

    // CRobot dizisi (en yakın hazineye giden)
    private CRobot[] cRobots = new CRobot[100];
    private int cRobotCount = 0;

    // -------------------------------------------------------
    // XROBOT EKLEME
    // -------------------------------------------------------
    public void addXRobot(int x, int y) {
        if (xRobotCount < xRobots.length) {
            xRobots[xRobotCount] = new XRobot(x, y);
            xRobotCount++;
        }
    }

    public void addXRobot(int x, int y, int life) {
        if (xRobotCount < xRobots.length) {
            xRobots[xRobotCount] = new XRobot(x, y, life);
            xRobotCount++;
        }
    }

    // -------------------------------------------------------
    // CROBOT EKLEME
    // -------------------------------------------------------
    public void addCRobot(int x, int y) {
        if (cRobotCount < cRobots.length) {
            cRobots[cRobotCount] = new CRobot(x, y);
            cRobotCount++;
        }
    }

    public void addCRobot(int x, int y, int life) {
        if (cRobotCount < cRobots.length) {
            cRobots[cRobotCount] = new CRobot(x, y, life);
            cRobotCount++;
        }
    }

    // -------------------------------------------------------
    // GETTER'LAR - GameEngine ve SaveLoad için
    // -------------------------------------------------------
    public int getXRobotCount() { return xRobotCount; }
    public int getXRobotX(int i) { return xRobots[i].x; }
    public int getXRobotY(int i) { return xRobots[i].y; }
    public int getXRobotLife(int i) { return xRobots[i].lifePoints; }

    public int getCRobotCount() { return cRobotCount; }
    public int getCRobotX(int i) { return cRobots[i].x; }
    public int getCRobotY(int i) { return cRobots[i].y; }
    public int getCRobotLife(int i) { return cRobots[i].lifePoints; }

    // Eski kod uyumluluğu için (GameEngine'de getRobotCount() kullanılıyor)
    public int getRobotCount()      { return xRobotCount; }
    public int getRobotX(int i)     { return xRobots[i].x; }
    public int getRobotY(int i)     { return xRobots[i].y; }
    public int getRobotLife(int i)  { return xRobots[i].lifePoints; }

    // -------------------------------------------------------
    // HAREKET
    // XRobotlar her çağrıda hareket eder
    // CRobotlar da her çağrıda hareket eder
    // Her ikisi de zaten GameEngine'de isRobotTurn() kontrolüyle
    // her 4 tick'te bir çağrılıyor
    // -------------------------------------------------------
    public void moveRobots(GameBoard board, TrailManager tm, int currentTick, int playerX, int playerY, BCharacter twin) {

        // XRobotları hareket ettir
        for (int i = 0; i < xRobotCount; i++) {
            tm.addTrail(xRobots[i].x, xRobots[i].y, currentTick);
            xRobots[i].move(board, this, playerX, playerY, twin);
        }

        // CRobotları hareket ettir
        for (int i = 0; i < cRobotCount; i++) {
            tm.addTrail(cRobots[i].x, cRobots[i].y, currentTick);
            cRobots[i].move(board, this, playerX, playerY, twin);
        }
    }

    // -------------------------------------------------------
    // ÇİZİM
    // -------------------------------------------------------
    public void drawRobots(Console cn) {
        for (int i = 0; i < xRobotCount; i++) {
            xRobots[i].draw(cn);
        }
        for (int i = 0; i < cRobotCount; i++) {
            cRobots[i].draw(cn);
        }
    }

    // -------------------------------------------------------
    // ÇAKIŞMA KONTROLÜ
    // Hem X hem C robotların konumlarını kontrol ediyoruz
    // -------------------------------------------------------
    public boolean isRobotAt(int targetX, int targetY) {
        for (int i = 0; i < xRobotCount; i++) {
            if (xRobots[i].x == targetX && xRobots[i].y == targetY) {
                return true;
            }
        }
        for (int i = 0; i < cRobotCount; i++) {
            if (cRobots[i].x == targetX && cRobots[i].y == targetY) {
                return true;
            }
        }
        return false;
    }
}

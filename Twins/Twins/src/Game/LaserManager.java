package Game;

import enigma.console.Console;

public class LaserManager {

    private LaserBeam[] lasers = new LaserBeam[200];
    private int laserCount = 0;

    private PackedLaser[] packs = new PackedLaser[20];
    private int packCount = 0;

    private int ammo = 0;

    // Spread path for active laser fire
    private int[][] spreadPath = new int[200][2];
    private int spreadLength = 0;
    private int spreadIndex = 0;
    private boolean spreading = false;

    public void fireLaser(int ax, int ay, int bx, int by, int tick) {
        if (ammo <= 0) return;
        if (ax == bx && ay == by) return;
        if (spreading) return;

        // Compute path from A to B using Bresenham line
        spreadLength = 0;
        int x0 = ax, y0 = ay, x1 = bx, y1 = by;
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x0 += sx; }
            if (e2 < dx) { err += dx; y0 += sy; }

            if (x0 == x1 && y0 == y1) break;

            if (spreadLength < spreadPath.length) {
                spreadPath[spreadLength][0] = x0;
                spreadPath[spreadLength][1] = y0;
                spreadLength++;
            }
        }

        if (spreadLength > 0) {
            spreading = true;
            spreadIndex = 0;
            ammo--;
        }
    }

    public void updateSpread(char[][] map, int currentTick) {
        if (!spreading) return;

        if (spreadIndex < spreadLength) {
            int x = spreadPath[spreadIndex][0];
            int y = spreadPath[spreadIndex][1];
            // Only place on empty squares
            if (y >= 0 && y < map.length && x >= 0 && x < map[0].length && map[y][x] != '#') {
                if (laserCount < lasers.length) {
                    lasers[laserCount] = new LaserBeam(x, y, currentTick);
                    laserCount++;
                }
            }
            spreadIndex++;
        }

        if (spreadIndex >= spreadLength) {
            spreading = false;
        }
    }

    public int neighborHarm(EnemyManager enemies, Console cn) {
        int kills = 0;
        int robotCount = enemies.getRobotCount();
        for (int r = robotCount - 1; r >= 0; r--) {
            int rx = enemies.getRobotX(r);
            int ry = enemies.getRobotY(r);
            int adjacentCount = 0;
            for (int i = 0; i < laserCount; i++) {
                if (!lasers[i].isActive()) continue;
                int lx = lasers[i].getX();
                int ly = lasers[i].getY();
                int diffX = Math.abs(lx - rx);
                int diffY = Math.abs(ly - ry);
                if ((diffX == 1 && diffY == 0) || (diffX == 0 && diffY == 1)) {
                    adjacentCount++;
                }
            }
            if (adjacentCount > 0) {
                kills += enemies.damageRobotAt(rx, ry, adjacentCount * 50, cn);
            }
        }
        return kills;
    }

    public void cleanExpiredLasers(Console cn, int currentTick) {
        int writeIdx = 0;
        for (int i = 0; i < laserCount; i++) {
            if (lasers[i].isActive() && !lasers[i].isExpired(currentTick)) {
                lasers[writeIdx] = lasers[i];
                writeIdx++;
            } else {
                lasers[i].erase(cn);
            }
        }
        laserCount = writeIdx;
    }

    public void drawLasers(Console cn) {
        for (int i = 0; i < laserCount; i++) {
            if (lasers[i].isActive()) {
                lasers[i].draw(cn);
            }
        }
    }

    public void spawnPackedLaser(char[][] map, RandomSpawner spawner) {
        if (packCount < packs.length) {
            int[] pos = spawner.getSpawnPoint(map);
            packs[packCount] = new PackedLaser(pos[0], pos[1]);
            packCount++;
        }
    }

    public int checkPickups(int playerX, int playerY, Console cn) {
        int picked = 0;
        for (int i = 0; i < packCount; i++) {
            if (!packs[i].isCollected() && packs[i].checkPickup(playerX, playerY)) {
                packs[i].erase(cn);
                ammo++;
                picked++;
            }
        }
        return picked;
    }

    public void drawPacks(Console cn) {
        for (int i = 0; i < packCount; i++) {
            if (!packs[i].isCollected()) {
                packs[i].draw(cn);
            }
        }
    }

    public int getAmmo() { return ammo; }
    public int getPackCount() { return packCount; }
}

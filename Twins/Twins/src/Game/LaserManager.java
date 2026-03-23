package Game;

import enigma.console.Console;

public class LaserManager {

    private LaserBeam[] lasers = new LaserBeam[50];
    private int laserCount = 0;

    private PackedLaser[] packs = new PackedLaser[20];
    private int packCount = 0;

    private int ammo = 3;

    public void fireLaser(int startX, int startY, int dirX, int dirY, int tick) {
        if (ammo <= 0) return;
        if (dirX == 0 && dirY == 0) return;
        if (laserCount < lasers.length) {
            lasers[laserCount] = new LaserBeam(startX, startY, dirX, dirY, tick);
            laserCount++;
            ammo--;
        }
    }

    public int updateLasers(char[][] map, EnemyManager enemies, Console cn) {
        int hits = 0;
        for (int i = 0; i < laserCount; i++) {
            if (lasers[i].isActive()) {
                lasers[i].erase(cn);
                boolean hit = lasers[i].advance(map, enemies);
                if (hit) {
                    int rx = lasers[i].getX();
                    int ry = lasers[i].getY();
                    hits += enemies.damageRobotAt(rx, ry, 500, cn);
                }
            }
        }
        return hits;
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
                ammo += 2;
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

    public void cleanInactiveLasers(Console cn) {
        int writeIdx = 0;
        for (int i = 0; i < laserCount; i++) {
            if (lasers[i].isActive()) {
                lasers[writeIdx] = lasers[i];
                writeIdx++;
            }
        }
        laserCount = writeIdx;
    }

    public int getAmmo() { return ammo; }
    public int getPackCount() { return packCount; }
}

package Game;

import enigma.console.Console;

public class TrailManager {
    
    private class Trail {
        int x, y, spawnTick;
        Trail(int x, int y, int tick) {
            this.x = x;
            this.y = y;
            this.spawnTick = tick;
        }
    }
    
    
    private Trail[] trails = new Trail[2000]; 
    private int trailCount = 0; 

    public void addTrail(int x, int y, int currentTick) {
        if (trailCount < trails.length) {
            trails[trailCount] = new Trail(x, y, currentTick);
            trailCount++;
        }
    }

    public void clearOldTrails(Console cn, int currentTick) {
        for (int i = 0; i < trailCount; i++) {
            if (currentTick - trails[i].spawnTick >= 2) { 
                
                cn.getTextWindow().output((trails[i].x * 2) + 4, trails[i].y + 2, ' ');
                
                
                trails[i] = trails[trailCount - 1];
                trails[trailCount - 1] = null; 
                trailCount--; 
                
                i--; 
            }
        }
    }
}
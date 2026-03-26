package Game;

public class Timer {
    private final static long TICK_INTERVAL = 50;
    private long lastTick;
    private long now;
    private int tickCount;

    Timer(){
        tickCount = 0;
        lastTick = System.currentTimeMillis();
    }
    public void Play(){
        do{
            now = System.currentTimeMillis();
        } while(now - lastTick < TICK_INTERVAL);
        lastTick = now;
        tickCount++;
    }
    public boolean isRobotTurn() {
        return tickCount % 4 == 0;
    }
    public int getTicks() {
        return tickCount;
    }
    public void setTicks(int ticks) {
        tickCount = ticks;
    }

}

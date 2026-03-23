package Game;

public class TimeManager {
    private int ticks = 0;

    public void tick() {
        ticks++;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public boolean isRobotTurn() {
        return ticks % 4 == 0;
    }
}
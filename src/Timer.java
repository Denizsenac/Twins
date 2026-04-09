import java.util.Scanner;

public class Timer {
    private final static long TICK_INTERVAL = 50;
    private long lastTick;
    private long now;
    private int tickCount;

    Timer(){
        tickCount = 0;
        lastTick = System.currentTimeMillis();
    }
    public void Time(){
        do{
            now = System.currentTimeMillis();
        } while(now - lastTick < TICK_INTERVAL);
        lastTick = now;
        tickCount++;

        //Her zaman ünitesinde çalışması gereken metotlar buraya eklenecek

        if (tickCount % 2 == 0) {
            //Her 2 zaman ünitesinde bir çalışması gereken metotlar buraya eklenecek
        }
        if (tickCount % 4 == 0) {
            //Her 4 zaman ünitesinde bir çalışması gereken metotlar buraya eklenecek
        }
        if (tickCount % 20 == 0) {
            //Her 20 zaman ünitesinde bir çalışması gereken metotlar buraya eklenecek
        }
    }

}

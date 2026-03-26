package Game;

public class Main {
    public static void main(String[] args) {
        try {
            GameEngine engine = new GameEngine();
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
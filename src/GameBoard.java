import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class GameBoard {

    private char[][] map;
    private int mapWidth;
    private int mapHeight;
    private Timer timer;

    public GameBoard(int width, int height) {
        mapWidth = width;
        mapHeight = height;
        map = new char[height][width];
        initializeMap();
    }
    private void initializeMap() {
        Timer timer = new Timer();
        MazeGenerator mazer = new MazeGenerator(map);
        map = mazer.initializeMap();
    }


    public void printBoard(Console console) {
        TextAttributes wallColor = new TextAttributes(Color.WHITE, Color.WHITE);
        TextAttributes emptyColor = new TextAttributes(Color.BLACK, Color.BLACK);

        // KAYDIRMA MİKTARLARI (MARGIN)
        int offsetX = 4; // Soldan 4 birim boşluk
        int offsetY = 2; // Yukarıdan 2 satır boşluk

        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                
                // Çizim koordinatlarına boşluklarımızı ekliyoruz
                int screenX = (j * 2) + offsetX; 
                int screenY = i + offsetY;
                
                if (map[i][j] == '#') {
                    console.getTextWindow().output(screenX, screenY, ' ', wallColor);
                    console.getTextWindow().output(screenX + 1, screenY, ' ', wallColor);
                } else {
                    console.getTextWindow().output(screenX, screenY, ' ', emptyColor);
                    console.getTextWindow().output(screenX + 1, screenY, ' ', emptyColor);
                }
                
            }
        }
    }

    public char[][] getMap() {
        return map;
    }
}
package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;

public class GameBoard {

    private char[][] map;
    private int mapWidth;
    private int mapHeight;

    public GameBoard(int width, int height) {
        mapWidth = width;
        mapHeight = height;
        map = new char[height][width];
        initializeMap();
    }

    public GameBoard(char[][] loadedMap) {
        this.mapHeight = loadedMap.length;
        this.mapWidth  = loadedMap[0].length;
        this.map       = loadedMap;
    }

    private void initializeMap() {
        MazeGenerator mazer = new MazeGenerator(map);
        map = mazer.initializeMap();
    }

    public void printBoard(Console console) {
        TextAttributes wallColor  = new TextAttributes(Color.WHITE, Color.WHITE);
        TextAttributes emptyColor = new TextAttributes(Color.BLACK, Color.BLACK);

        int offsetX = 4;
        int offsetY = 2;

        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
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
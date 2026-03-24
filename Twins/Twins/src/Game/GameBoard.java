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

    public void addWalls(int wallcount, int walllength) {
        int randomx, randomy, randomdir;
        boolean canplace = true;
        while (wallcount > 0) {
            randomx = (int) ((Math.random() * 23) + 1);
            randomy = (int) ((Math.random() * 53) + 1);
            if (map[randomx][randomy] != '#') {
                randomdir = (int) (Math.random() * 4);
                if (randomdir == 0 && randomx >= walllength) {
                    for (int i = 1; i < walllength; i++) if (map[randomx - i][randomy] == '#') { canplace = false; break; }
                    if (!canplace) { wallcount++; canplace = true; }
                    else for (int i = 0; i < walllength; i++) map[randomx - i][randomy] = '#';
                } else if (randomdir == 1 && (53 - randomy) >= (walllength - 1)) {
                    for (int i = 1; i < walllength; i++) if (map[randomx][randomy + i] == '#') { canplace = false; break; }
                    if (!canplace) { wallcount++; canplace = true; }
                    else for (int i = 0; i < walllength; i++) map[randomx][randomy + i] = '#';
                } else if (randomdir == 2 && 23 - randomx >= walllength - 1) {
                    for (int i = 1; i < walllength; i++) if (map[randomx + i][randomy] == '#') { canplace = false; break; }
                    if (!canplace) { wallcount++; canplace = true; }
                    else for (int i = 0; i < walllength; i++) map[randomx + i][randomy] = '#';
                } else if (randomy >= walllength) {
                    for (int i = 1; i < walllength; i++) if (map[randomx][randomy - i] == '#') { canplace = false; break; }
                    if (!canplace) { wallcount++; canplace = true; }
                    else for (int i = 0; i < walllength; i++) map[randomx][randomy - i] = '#';
                } else wallcount++;
            }
            wallcount--;
        }
    }

    private boolean checkWalls(int checkbox, int checksize) {
        return checkWalls(checkbox, checkbox, checksize);
    }

    private boolean checkWalls(int checkwidth, int checkheight, int checksize) {
        if (checksize > checkwidth * checkheight) return false;
        int wallcount = 0;
        int startx = 1;
        int starty = 1;
        while (starty + checkheight <= 25) {
            while (startx + checkwidth <= 55) {
                for (int j = startx; j < startx + checkwidth; j++)
                    for (int i = starty; i < starty + checkheight; i++) {
                        if (map[i][j] == '#') wallcount++;
                        if (wallcount > checksize) return false;
                    }
                wallcount = 0;
                startx++;
            }
            startx = 1;
            starty++;
        }
        return true;
    }

    private boolean checkConnected() {
        int randomx, randomy;
        char[][] connectionmap = new char[map.length][map[0].length];
        for (int i = 0; i < connectionmap.length; i++)
            for (int j = 0; j < connectionmap[0].length; j++)
                connectionmap[i][j] = map[i][j];
        while (true) {
            randomx = (int) (Math.random() * 23 + 1);
            randomy = (int) (Math.random() * 53 + 1);
            if (map[randomx][randomy] == ' ') {
                connectionmap[randomx][randomy] = '+';
                connectionmap = searhConnection(randomx, randomy, connectionmap);
                break;
            }
        }
        for (int i = 0; i < connectionmap.length; i++)
            for (int j = 0; j < connectionmap[0].length; j++)
                if (connectionmap[i][j] == ' ') return false;
        return true;
    }

    private char[][] searhConnection(int x, int y, char[][] connectionmap) {
        if (x < 24 && connectionmap[x + 1][y] == ' ') { connectionmap[x + 1][y] = '+'; connectionmap = searhConnection(x + 1, y, connectionmap); }
        if (y < 54 && connectionmap[x][y + 1] == ' ') { connectionmap[x][y + 1] = '+'; connectionmap = searhConnection(x, y + 1, connectionmap); }
        if (x > 0  && connectionmap[x - 1][y] == ' ') { connectionmap[x - 1][y] = '+'; connectionmap = searhConnection(x - 1, y, connectionmap); }
        if (y > 0  && connectionmap[x][y - 1] == ' ') { connectionmap[x][y - 1] = '+'; connectionmap = searhConnection(x, y - 1, connectionmap); }
        return connectionmap;
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
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

    private void initializeMap() {
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                
                if (i == 0 || i == mapHeight - 1 || j == 0 || j == mapWidth - 1) {
                    map[i][j] = '#'; 
                } else {
                    map[i][j] = ' '; 
                }

            }
        }
        addWalls(4,8);
        addWalls(6,6);
        addWalls(20,4);
        addWalls(5,3);
    }
    public void addWalls(int wallcount,int walllength) {
        int randomx,randomy,randomdir;
        boolean canplace = true;
        while(wallcount > 0)
        {
            randomx = (int) ((Math.random() * 23) + 1);
            randomy = (int) ((Math.random() * 53) + 1);
            if(map[randomx][randomy] != '#')
            {
                randomdir = (int) (Math.random() * 4);
                if(randomdir == 0 && randomx >= walllength)
                {
                    for (int i = 1; i < walllength;i++) if (map[randomx - i][randomy] == '#')
                    {
                        canplace = false;
                        break;
                    }
                    if (!canplace)
                    {
                        wallcount++;
                        canplace = true;
                    }
                    else for (int i = 0; i < walllength; i++) map[randomx - i][randomy] = '#';
                }
                else if (randomdir == 1 && (53 - randomy) >= (walllength - 1))
                {
                    for (int i = 1; i < walllength;i++) if (map[randomx][randomy + i] == '#')
                    {
                        canplace = false;
                        break;
                    }
                    if (!canplace)
                    {
                        wallcount++;
                        canplace = true;
                    }
                    else for (int i = 0; i < walllength; i++) map[randomx][randomy + i] = '#';
                }
                else if (randomdir == 2 && 23 - randomx >= walllength - 1)
                {
                    for (int i = 1; i < walllength;i++) if (map[randomx + i][randomy] == '#')
                    {
                        canplace = false;
                        break;
                    }
                    if (!canplace)
                    {
                        wallcount++;
                        canplace = true;
                    }
                    else for (int i = 0; i < walllength; i++) map[randomx + i][randomy] = '#';
                }
                else if (randomy >= walllength)
                {
                    for (int i = 1; i < walllength;i++) if (map[randomx][randomy - i] == '#')
                    {
                        canplace = false;
                        break;
                    }
                    if (!canplace)
                    {
                        wallcount++;
                        canplace = true;
                    }
                    else for (int i = 0; i < walllength; i++) map[randomx][randomy - i] = '#';
                }
                else wallcount++;
            }
            wallcount--;
        }
        return;
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
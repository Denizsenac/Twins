package Game;

public class MazeGenerator {
    private char[][] map;
    private char[][] mapcopy;
    private int mapHeight;
    private int mapWidth;

    MazeGenerator(char[][] map){
        mapHeight = map.length;
        mapWidth = map[0].length;
        mapcopy = new char[mapHeight][mapWidth];
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (i == 0 || i == mapHeight - 1 || j == 0 || j == mapWidth - 1) {
                    map[i][j] = '#';
                } else {
                    map[i][j] = ' ';
                }
                mapcopy[i][j] = map[i][j];
            }
        }
        this.map = map;
    }

    public char[][] initializeMap() {
        int wallcount = 0;
        int walllength = 0;
        int stepCount = 1;
        while (stepCount < 5)
        {
            if (stepCount == 1) {
                wallcount = 4;
                walllength = 8;
            }
            else if (stepCount == 2) {
                wallcount = 6;
                walllength = 6;
            }
            else if (stepCount == 3) {
                wallcount = 20;
                walllength = 4;
            }
            else if (stepCount == 4) {
                wallcount = 5;
                walllength = 3;
            }
            for (int i = 0; i < wallcount;i++){
                do {
                    resetCopy();
                    addWalls(1,walllength);
                } while (!checkCopy());
                pasteCopy();
            }
            stepCount++;
        }
        return map;
    }

    private boolean checkCopy() {
        if (!checkWalls(2,3)) return false;
        if (!checkWalls(3,5)) return false;
        if (!checkWalls(4,7)) return false;
        if (!checkWalls(6,15)) return false;
        if (!checkConnected()) return false;
        return true;
    }

    private void resetCopy(){
        for(int i = 0; i < mapHeight; i++)
            for(int j = 0; j < mapWidth; j++)
                mapcopy[i][j] = map[i][j];
    }

    private void pasteCopy(){
        for(int i = 0; i < mapHeight; i++)
            for(int j = 0; j < mapWidth; j++)
                map[i][j] = mapcopy[i][j];
    }

    public void addWalls(int wallcount,int walllength) {
        int randomx,randomy,randomdir;
        boolean canplace = true;
        while(wallcount > 0)
        {
            randomx = (int) ((Math.random() * (mapHeight - 2)) + 1);
            randomy = (int) ((Math.random() * (mapWidth - 2)) + 1);
            if(mapcopy[randomx][randomy] != '#')
            {
                randomdir = (int) (Math.random() * 4);
                if(randomdir == 0 && randomx >= walllength)
                {
                    for (int i = 1; i < walllength;i++) if (mapcopy[randomx - i][randomy] == '#')
                    {
                        canplace = false;
                        break;
                    }
                    if (!canplace)
                    {
                        wallcount++;
                        canplace = true;
                    }
                    else for (int i = 0; i < walllength; i++) mapcopy[randomx - i][randomy] = '#';
                }
                else if (randomdir == 1 && (mapHeight - randomy) > walllength)
                {
                    for (int i = 1; i < walllength;i++) if (mapcopy[randomx][randomy + i] == '#')
                    {
                        canplace = false;
                        break;
                    }
                    if (!canplace)
                    {
                        wallcount++;
                        canplace = true;
                    }
                    else for (int i = 0; i < walllength; i++) mapcopy[randomx][randomy + i] = '#';
                }
                else if (randomdir == 2 && (mapWidth - randomx) > walllength)
                {
                    for (int i = 1; i < walllength;i++) if (mapcopy[randomx + i][randomy] == '#')
                    {
                        canplace = false;
                        break;
                    }
                    if (!canplace)
                    {
                        wallcount++;
                        canplace = true;
                    }
                    else for (int i = 0; i < walllength; i++) mapcopy[randomx + i][randomy] = '#';
                }
                else if (randomy >= walllength)
                {
                    for (int i = 1; i < walllength;i++) if (mapcopy[randomx][randomy - i] == '#')
                    {
                        canplace = false;
                        break;
                    }
                    if (!canplace)
                    {
                        wallcount++;
                        canplace = true;
                    }
                    else for (int i = 0; i < walllength; i++) mapcopy[randomx][randomy - i] = '#';
                }
                else wallcount++;
            }
            wallcount--;
        }
    }

    private boolean checkWalls(int checkbox, int checksize) {
        return checkWalls(checkbox,checkbox,checksize);
    }

    private boolean checkWalls(int checkwidth,int checkheight,int checksize) {
        if (checksize > checkwidth * checkheight) return false;
        int wallcount = 0;
        int startx = 1;
        int starty = 1;
        while(starty + checkheight < mapWidth)
        {
            while (startx + checkwidth < mapHeight)
            {
                for (int j = startx; j < startx + checkwidth; j++ )
                    for (int i = starty; i < starty + checkheight; i++)
                    {
                        if(mapcopy[j][i] == '#')
                            wallcount++;
                        if(wallcount > checksize)
                            return false;
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
        int randomx,randomy;
        char[][] connectionmap = new char[mapHeight][mapWidth];
        for (int i = 0; i < connectionmap.length; i++)
            for (int j = 0; j < connectionmap[0].length; j++)
                connectionmap[i][j] = mapcopy[i][j];
        while (true)
        {
            randomx = (int) ((Math.random() * (mapHeight - 2)) + 1);
            randomy = (int) ((Math.random() * (mapWidth - 2)) + 1);
            if (mapcopy[randomx][randomy] == ' ') {
                connectionmap[randomx][randomy] = '+';
                connectionmap = searhConnection(randomx,randomy,connectionmap);
                break;
            }
        }
        for (int i = 0; i < connectionmap.length; i++)
            for (int j = 0; j < connectionmap[0].length; j++)
                if (connectionmap[i][j] == ' ') return false;
        return true;
    }

    private char[][] searhConnection(int x, int y,char[][] connectionmap){

        if (x < mapWidth - 1 && connectionmap[x + 1][y] == ' ') {
            connectionmap[x + 1][y] = '+';
            connectionmap = searhConnection(x + 1,y,connectionmap);
        }
        if (y < mapHeight - 1 && connectionmap[x][y + 1] == ' ') {
            connectionmap[x][y + 1] = '+';
            connectionmap = searhConnection(x,y + 1,connectionmap);
        }
        if (x > 0 && connectionmap[x - 1][y] == ' ') {
            connectionmap[x - 1][y] = '+';
            connectionmap = searhConnection(x - 1, y, connectionmap);
        }
        if (y > 0 && connectionmap[x][y - 1] == ' ') {
            connectionmap[x][y - 1] = '+';
            connectionmap = searhConnection(x, y - 1, connectionmap);
        }
        return connectionmap;
    }
}

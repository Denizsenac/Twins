package Game;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

public class SaveLoad {

    static String SAVE_FILE = "savegame.txt";

    private static int toInt(String s) {
        int result = 0;
        int i = 0;
        boolean negative = false;
        if (s.charAt(0) == '-') {
            negative = true;
            i = 1;
        }
        for (; i < s.length(); i++) {
            result = result * 10 + (s.charAt(i) - '0');
        }
        return negative ? -result : result;
    }

    // FIX: extended save — now persists C-robots, treasures, laser packs, and scores
    public static void saveGame(
            int playerX, int playerY,
            int twinX,   int twinY, int twinMode,
            int gameTicks,
            int playerScore, int computerScore, int playerHealth,
            int[] xRobotX, int[] xRobotY, int[] xRobotLife, int xRobotCount,
            int[] cRobotX, int[] cRobotY, int[] cRobotLife, int cRobotCount,
            int[] tX, int[] tY, int[] tVal, boolean[] tActive, int tCount,
            int[] packX, int[] packY, boolean[] packCollected, int packCount,
            char[][] map) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(SAVE_FILE));

        bw.write("PLAYER_X:"    + playerX);       bw.newLine();
        bw.write("PLAYER_Y:"    + playerY);       bw.newLine();
        bw.write("TWIN_X:"      + twinX);         bw.newLine();
        bw.write("TWIN_Y:"      + twinY);         bw.newLine();
        bw.write("TWIN_MODE:"   + twinMode);      bw.newLine();
        bw.write("GAME_TICKS:"  + gameTicks);     bw.newLine();
        bw.write("PLAYER_SCORE:"+ playerScore);   bw.newLine();
        bw.write("COMP_SCORE:"  + computerScore); bw.newLine();
        bw.write("PLAYER_HEALTH:"+ playerHealth); bw.newLine();

        bw.write("MAP_ROWS:" + map.length);      bw.newLine();
        bw.write("MAP_COLS:" + map[0].length);   bw.newLine();
        for (int i = 0; i < map.length; i++) {
            bw.write("MAP_ROW:" + new String(map[i])); bw.newLine();
        }

        // X-Robots
        bw.write("X_ROBOT_COUNT:" + xRobotCount); bw.newLine();
        for (int i = 0; i < xRobotCount; i++) {
            bw.write("XROBOT:" + xRobotX[i] + "," + xRobotY[i] + "," + xRobotLife[i]);
            bw.newLine();
        }

        // FIX: C-Robots
        bw.write("C_ROBOT_COUNT:" + cRobotCount); bw.newLine();
        for (int i = 0; i < cRobotCount; i++) {
            bw.write("CROBOT:" + cRobotX[i] + "," + cRobotY[i] + "," + cRobotLife[i]);
            bw.newLine();
        }

        // FIX: Treasures
        bw.write("TREASURE_COUNT:" + tCount); bw.newLine();
        for (int i = 0; i < tCount; i++) {
            if (tActive[i]) {
                bw.write("TREASURE:" + tX[i] + "," + tY[i] + "," + tVal[i]);
                bw.newLine();
            }
        }

        // FIX: Packed lasers
        bw.write("PACK_COUNT:" + packCount); bw.newLine();
        for (int i = 0; i < packCount; i++) {
            if (!packCollected[i]) {
                bw.write("PACK:" + packX[i] + "," + packY[i]);
                bw.newLine();
            }
        }

        bw.close();
    }

    // FIX: extended load result array:
    //   result[0]=playerX, [1]=playerY, [2]=twinX, [3]=twinY, [4]=twinMode,
    //   result[5]=gameTicks, [6]=xRobotCount, [7]=cRobotCount,
    //   result[8]=playerScore, [9]=computerScore, [10]=playerHealth,
    //   result[11]=treasureCount, [12]=packCount
    public static boolean loadGame(
            int[]    result,
            int[]    xRobotX, int[] xRobotY, int[] xRobotLife,
            int[]    cRobotX, int[] cRobotY, int[] cRobotLife,
            int[]    tX, int[] tY, int[] tVal,
            int[]    packX, int[] packY,
            char[][] map) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(SAVE_FILE));
        String line;
        int xIdx = 0, cIdx = 0, mIdx = 0, tIdx = 0, pIdx = 0;

        while ((line = br.readLine()) != null) {
            if      (line.startsWith("PLAYER_X:"))      result[0]  = toInt(line.split(":")[1]);
            else if (line.startsWith("PLAYER_Y:"))      result[1]  = toInt(line.split(":")[1]);
            else if (line.startsWith("TWIN_X:"))        result[2]  = toInt(line.split(":")[1]);
            else if (line.startsWith("TWIN_Y:"))        result[3]  = toInt(line.split(":")[1]);
            else if (line.startsWith("TWIN_MODE:"))     result[4]  = toInt(line.split(":")[1]);
            else if (line.startsWith("GAME_TICKS:"))    result[5]  = toInt(line.split(":")[1]);
            else if (line.startsWith("X_ROBOT_COUNT:")) result[6]  = toInt(line.split(":")[1]);
            else if (line.startsWith("C_ROBOT_COUNT:")) result[7]  = toInt(line.split(":")[1]);
            else if (line.startsWith("PLAYER_SCORE:"))  result[8]  = toInt(line.split(":")[1]);
            else if (line.startsWith("COMP_SCORE:"))    result[9]  = toInt(line.split(":")[1]);
            else if (line.startsWith("PLAYER_HEALTH:")) result[10] = toInt(line.split(":")[1]);
            else if (line.startsWith("TREASURE_COUNT:"))result[11] = toInt(line.split(":")[1]);
            else if (line.startsWith("PACK_COUNT:"))    result[12] = toInt(line.split(":")[1]);

            else if (line.startsWith("MAP_ROW:")) {
                map[mIdx] = line.substring(8).toCharArray();
                mIdx++;
            }
            else if (line.startsWith("XROBOT:")) {
                String[] p = line.split(":")[1].split(",");
                xRobotX[xIdx]    = toInt(p[0]);
                xRobotY[xIdx]    = toInt(p[1]);
                xRobotLife[xIdx] = toInt(p[2]);
                xIdx++;
            }
            // FIX: load C-robots
            else if (line.startsWith("CROBOT:")) {
                String[] p = line.split(":")[1].split(",");
                cRobotX[cIdx]    = toInt(p[0]);
                cRobotY[cIdx]    = toInt(p[1]);
                cRobotLife[cIdx] = toInt(p[2]);
                cIdx++;
            }
            // FIX: load treasures
            else if (line.startsWith("TREASURE:")) {
                String[] p = line.split(":")[1].split(",");
                tX[tIdx]  = toInt(p[0]);
                tY[tIdx]  = toInt(p[1]);
                tVal[tIdx]= toInt(p[2]);
                tIdx++;
            }
            // FIX: load packed lasers
            else if (line.startsWith("PACK:")) {
                String[] p = line.split(":")[1].split(",");
                packX[pIdx] = toInt(p[0]);
                packY[pIdx] = toInt(p[1]);
                pIdx++;
            }
        }

        br.close();
        return true;
    }

    public static boolean saveExists() {
        return new java.io.File(SAVE_FILE).exists();
    }
}

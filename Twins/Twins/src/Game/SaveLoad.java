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

    public static void saveGame(
            int playerX, int playerY,
            int twinX, int twinY, int twinMode,
            int gameTicks,
            int[] xRobotX, int[] xRobotY, int[] xRobotLife, int xRobotCount,
            char[][] map) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(SAVE_FILE));

        bw.write("PLAYER_X:" + playerX);     bw.newLine();
        bw.write("PLAYER_Y:" + playerY);     bw.newLine();
        bw.write("TWIN_X:" + twinX);         bw.newLine();
        bw.write("TWIN_Y:" + twinY);         bw.newLine();
        bw.write("TWIN_MODE:" + twinMode);   bw.newLine();
        bw.write("GAME_TICKS:" + gameTicks); bw.newLine();

        bw.write("MAP_ROWS:" + map.length);    bw.newLine();
        bw.write("MAP_COLS:" + map[0].length); bw.newLine();
        for (int i = 0; i < map.length; i++) {
            bw.write("MAP_ROW:" + new String(map[i])); bw.newLine();
        }

        bw.write("X_ROBOT_COUNT:" + xRobotCount); bw.newLine();
        for (int i = 0; i < xRobotCount; i++) {
            bw.write("XROBOT:" + xRobotX[i] + "," + xRobotY[i] + "," + xRobotLife[i]);
            bw.newLine();
        }

        bw.close();
    }

    public static boolean loadGame(
            int[] result,
            int[] xRobotX, int[] xRobotY, int[] xRobotLife,
            char[][] map) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(SAVE_FILE));
        String line;
        int xIdx = 0;
        int mIdx = 0;

        while ((line = br.readLine()) != null) {
            if      (line.startsWith("PLAYER_X:"))      result[0] = toInt(line.split(":")[1]);
            else if (line.startsWith("PLAYER_Y:"))      result[1] = toInt(line.split(":")[1]);
            else if (line.startsWith("TWIN_X:"))        result[2] = toInt(line.split(":")[1]);
            else if (line.startsWith("TWIN_Y:"))        result[3] = toInt(line.split(":")[1]);
            else if (line.startsWith("TWIN_MODE:"))     result[4] = toInt(line.split(":")[1]);
            else if (line.startsWith("GAME_TICKS:"))    result[5] = toInt(line.split(":")[1]);
            else if (line.startsWith("X_ROBOT_COUNT:")) result[6] = toInt(line.split(":")[1]);

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
        }

        br.close();
        return true;
    }

    public static boolean saveExists() {
        return new java.io.File(SAVE_FILE).exists();
    }
}
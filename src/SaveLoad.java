import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

public class SaveLoad {

    static String SAVE_FILE = "savegame.txt";

    // -------------------------------------------------------
    // KAYDETME
    // Oyunun şu anki bilgilerini savegame.txt'ye yazar
    // -------------------------------------------------------
    public static void saveGame(
            int playerScore, int playerLife, int playerLaser,
            int playerX, int playerY,
            int twinX, int twinY, int twinMode,
            int gameTime, int computerScore,
            int[] cRobotX, int[] cRobotY, int[] cRobotLife, int cRobotCount,
            int[] xRobotX, int[] xRobotY, int[] xRobotLife, int xRobotCount,
            char[][] map) {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(SAVE_FILE));

            // Oyuncu bilgileri
            bw.write("PLAYER_SCORE:" + playerScore); bw.newLine();
            bw.write("PLAYER_LIFE:"  + playerLife);  bw.newLine();
            bw.write("PLAYER_LASER:" + playerLaser); bw.newLine();
            bw.write("PLAYER_X:"     + playerX);     bw.newLine();
            bw.write("PLAYER_Y:"     + playerY);     bw.newLine();
            bw.write("TWIN_X:"       + twinX);       bw.newLine();
            bw.write("TWIN_Y:"       + twinY);       bw.newLine();
            bw.write("TWIN_MODE:"    + twinMode);    bw.newLine();

            // Zaman ve bilgisayar skoru
            bw.write("GAME_TIME:"       + gameTime);       bw.newLine();
            bw.write("COMPUTER_SCORE:"  + computerScore);  bw.newLine();

            // Haritayı satır satır yaz
            bw.write("MAP_ROWS:" + map.length);       bw.newLine();
            bw.write("MAP_COLS:" + map[0].length);    bw.newLine();
            for (int i = 0; i < map.length; i++) {
                bw.write("MAP_ROW:" + new String(map[i])); bw.newLine();
            }

            // C-Robot bilgileri
            bw.write("C_ROBOT_COUNT:" + cRobotCount); bw.newLine();
            for (int i = 0; i < cRobotCount; i++) {
                bw.write("CROBOT:" + cRobotX[i] + "," + cRobotY[i] + "," + cRobotLife[i]);
                bw.newLine();
            }

            // X-Robot bilgileri
            bw.write("X_ROBOT_COUNT:" + xRobotCount); bw.newLine();
            for (int i = 0; i < xRobotCount; i++) {
                bw.write("XROBOT:" + xRobotX[i] + "," + xRobotY[i] + "," + xRobotLife[i]);
                bw.newLine();
            }

            bw.close();
            System.out.println("Oyun kaydedildi.");

        } catch (IOException e) {
            System.out.println("Kaydetme hatasi: " + e.getMessage());
        }
    }


    // -------------------------------------------------------
    // YÜKLEME
    // savegame.txt'yi okur, okunan değerleri result[] dizisine koyar.
    //
    // result dizisinin anlamı (indeks sırası):
    //   result[0]  = playerScore
    //   result[1]  = playerLife
    //   result[2]  = playerLaser
    //   result[3]  = playerX
    //   result[4]  = playerY
    //   result[5]  = twinX
    //   result[6]  = twinY
    //   result[7]  = twinMode
    //   result[8]  = gameTime
    //   result[9]  = computerScore
    //   result[10] = cRobotCount
    //   result[11] = xRobotCount
    //
    // Robot ve harita verileri ayrı dizi parametreleri olarak doldurulur.
    // Metod true döndürürse yükleme başarılıdır.
    // -------------------------------------------------------
    public static boolean loadGame(
            int[] result,
            int[] cRobotX, int[] cRobotY, int[] cRobotLife,
            int[] xRobotX, int[] xRobotY, int[] xRobotLife,
            char[][] map) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(SAVE_FILE));
            String line;

            int cIdx = 0; // C robotlarını kaçıncısına yazıyoruz
            int xIdx = 0; // X robotlarını kaçıncısına yazıyoruz
            int mIdx = 0; // haritanın kaçıncı satırını yazıyoruz

            while ((line = br.readLine()) != null) {

                if      (line.startsWith("PLAYER_SCORE:"))  result[0]  = Integer.parseInt(line.split(":")[1]);
                else if (line.startsWith("PLAYER_LIFE:"))   result[1]  = Integer.parseInt(line.split(":")[1]);
                else if (line.startsWith("PLAYER_LASER:"))  result[2]  = Integer.parseInt(line.split(":")[1]);
                else if (line.startsWith("PLAYER_X:"))      result[3]  = Integer.parseInt(line.split(":")[1]);
                else if (line.startsWith("PLAYER_Y:"))      result[4]  = Integer.parseInt(line.split(":")[1]);
                else if (line.startsWith("TWIN_X:"))        result[5]  = Integer.parseInt(line.split(":")[1]);
                else if (line.startsWith("TWIN_Y:"))        result[6]  = Integer.parseInt(line.split(":")[1]);
                else if (line.startsWith("TWIN_MODE:"))     result[7]  = Integer.parseInt(line.split(":")[1]);
                else if (line.startsWith("GAME_TIME:"))     result[8]  = Integer.parseInt(line.split(":")[1]);
                else if (line.startsWith("COMPUTER_SCORE:"))result[9]  = Integer.parseInt(line.split(":")[1]);
                else if (line.startsWith("C_ROBOT_COUNT:")) result[10] = Integer.parseInt(line.split(":")[1]);
                else if (line.startsWith("X_ROBOT_COUNT:")) result[11] = Integer.parseInt(line.split(":")[1]);

                else if (line.startsWith("MAP_ROW:")) {
                    // "MAP_ROW:" 8 karakter, geri kalanı harita satırı
                    map[mIdx] = line.substring(8).toCharArray();
                    mIdx++;
                }

                else if (line.startsWith("CROBOT:")) {
                    // "CROBOT:10,5,1000" -> split(":")[1] -> "10,5,1000" -> split(",") -> ["10","5","1000"]
                    String[] p = line.split(":")[1].split(",");
                    cRobotX[cIdx]    = Integer.parseInt(p[0]);
                    cRobotY[cIdx]    = Integer.parseInt(p[1]);
                    cRobotLife[cIdx] = Integer.parseInt(p[2]);
                    cIdx++;
                }

                else if (line.startsWith("XROBOT:")) {
                    String[] p = line.split(":")[1].split(",");
                    xRobotX[xIdx]    = Integer.parseInt(p[0]);
                    xRobotY[xIdx]    = Integer.parseInt(p[1]);
                    xRobotLife[xIdx] = Integer.parseInt(p[2]);
                    xIdx++;
                }
            }

            br.close();
            System.out.println("Oyun yuklendi.");
            return true;

        } catch (IOException e) {
            System.out.println("Yukleme hatasi: " + e.getMessage());
            return false;
        }
    }


    // Kayıt dosyası var mı? (Menüde "devam et" seçeneği göstermek için)
    public static boolean saveExists() {
        return new java.io.File(SAVE_FILE).exists();
    }
}
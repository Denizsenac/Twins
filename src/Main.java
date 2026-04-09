//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
//public class Main {
//    public static void main(String[] args) throws Exception {
//        GameBoard board = new GameBoard(55,25);
//        GameControls game = new GameControls(board);
//    }
//}


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

        GameBoard board = new GameBoard(55,25);
        GameControls game = new GameControls(board);

        // --- Örnek oyun değişkenleri ---
        int playerScore = 173, playerLife = 950, playerLaser = 6;
        int playerX = 8, playerY = 7;
        int twinX = 8, twinY = 11, twinMode = 1;
        int gameTime = 27, computerScore = 297;

        int cRobotCount = 2;
        int[] cRobotX    = {10, 30};
        int[] cRobotY    = {5, 12};
        int[] cRobotLife = {1000, 750};

        int xRobotCount = 3;
        int[] xRobotX    = {5, 20, 40};
        int[] xRobotY    = {3, 8, 15};
        int[] xRobotLife = {1000, 500, 800};

        char[][] map = {
                "##########".toCharArray(),
                "#   A  B #".toCharArray(),
                "# 1    2 #".toCharArray(),
                "##########".toCharArray()
        };

        // --- KAYDET ---
        SaveLoad.saveGame(
                playerScore, playerLife, playerLaser,
                playerX, playerY,
                twinX, twinY, twinMode,
                gameTime, computerScore,
                cRobotX, cRobotY, cRobotLife, cRobotCount,
                xRobotX, xRobotY, xRobotLife, xRobotCount,
                map
        );

        // --- YÜKLE ---
        int[] result      = new int[12];
        int[] lcRobotX    = new int[20], lcRobotY    = new int[20], lcRobotLife = new int[20];
        int[] lxRobotX    = new int[20], lxRobotY    = new int[20], lxRobotLife = new int[20];
        char[][] loadedMap = new char[4][10];

        boolean ok = SaveLoad.loadGame(
                result,
                lcRobotX, lcRobotY, lcRobotLife,
                lxRobotX, lxRobotY, lxRobotLife,
                loadedMap
        );

        if (ok) {
            System.out.println("playerScore  = " + result[0]);
            System.out.println("playerLife   = " + result[1]);
            System.out.println("playerLaser  = " + result[2]);
            System.out.println("playerX/Y    = " + result[3] + "/" + result[4]);
            System.out.println("twinX/Y      = " + result[5] + "/" + result[6]);
            System.out.println("twinMode     = " + result[7]);
            System.out.println("gameTime     = " + result[8]);
            System.out.println("computerScore= " + result[9]);
            System.out.println("cRobotCount  = " + result[10]);
            System.out.println("xRobotCount  = " + result[11]);
            System.out.println("1.C-Robot    = (" + lcRobotX[0] + "," + lcRobotY[0] + ") can:" + lcRobotLife[0]);
            System.out.println("Harita[1]    = " + new String(loadedMap[1]));
        }

    }
}
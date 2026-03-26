package Game;

import enigma.console.Console;
import enigma.console.TextAttributes;
import enigma.core.Enigma;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GameEngine {
    private Console cn;
    private GameBoard board;
    private GameControls controls;
    private Timer timer;
    private TrailManager trailManager;
    private EnemyManager enemyManager;

    private BModeManager modeManager;
    private BCharacter twin;
    private MoveTrack tracker;

    private ModeMenuUI modeMenu;
    private TitleScreen gameLogo;

    private LaserManager laserManager;
    private ScoreManager scoreManager;

    // Treasure system
    private int[] tX      = new int[200];
    private int[] tY      = new int[200];
    private int[] tVal    = new int[200];
    private boolean[] tActive = new boolean[200];
    private int tCount    = 0;

    private int px, py;
    private int selectedModeOption = 1;

    public GameEngine() throws Exception {
        cn = Enigma.getConsole("Twins - Maze Game", 200, 50, 12);
        controls     = new GameControls(cn);
        timer        = new Timer();
        trailManager = new TrailManager();
        enemyManager = new EnemyManager();

        modeManager  = new BModeManager();
        tracker      = new MoveTrack();

        modeMenu  = new ModeMenuUI();
        gameLogo  = new TitleScreen();

        laserManager  = new LaserManager();
        scoreManager  = new ScoreManager();
    }

    private void clearScreen() {
        for (int y = 0; y < 50; y++)
            for (int x = 0; x < 200; x++)
                cn.getTextWindow().output(x, y, ' ');
    }

    private void drawText(int x, int y, String text) {
        for (int i = 0; i < text.length(); i++)
            cn.getTextWindow().output(x + i, y, text.charAt(i));
    }

    private void drawText(int x, int y, String text, TextAttributes attr) {
        for (int i = 0; i < text.length(); i++)
            cn.getTextWindow().output(x + i, y, text.charAt(i), attr);
    }

    // -------------------------------------------------------------------------
    // Treasure helpers
    // -------------------------------------------------------------------------

    private void spawnTreasure(int value, RandomSpawner spawner) {
        if (tCount >= tX.length) return;
        int[] pos  = spawner.getSpawnPoint(board.getMap());
        tX[tCount]     = pos[0];
        tY[tCount]     = pos[1];
        tVal[tCount]   = value;
        tActive[tCount]= true;
        tCount++;
    }

    private void drawTreasures() {
        TextAttributes t1Color = new TextAttributes(new Color(255, 255, 0),   Color.BLACK);
        TextAttributes t2Color = new TextAttributes(new Color(255, 165, 0),   Color.BLACK);
        TextAttributes t3Color = new TextAttributes(new Color(255, 100, 255), Color.BLACK);
        for (int i = 0; i < tCount; i++) {
            if (!tActive[i]) continue;
            int screenX = (tX[i] * 2) + 4;
            int screenY = tY[i] + 2;
            TextAttributes c = (tVal[i] == 1) ? t1Color : (tVal[i] == 2) ? t2Color : t3Color;
            cn.getTextWindow().output(screenX, screenY, (char)('0' + tVal[i]), c);
        }
    }

    // Returns player score points for collecting treasure at (x,y)
    private int collectTreasure(int x, int y) {
        for (int i = 0; i < tCount; i++) {
            if (tActive[i] && tX[i] == x && tY[i] == y) {
                tActive[i] = false;
                if (tVal[i] == 1) return 3;
                if (tVal[i] == 2) return 10;
                if (tVal[i] == 3) return 30;
            }
        }
        return 0;
    }

    // FIX: computer earns 3x treasure value (9/30/90)
    private int collectTreasureForComputer(int x, int y) {
        for (int i = 0; i < tCount; i++) {
            if (tActive[i] && tX[i] == x && tY[i] == y) {
                tActive[i] = false;
                if (tVal[i] == 1) return 9;
                if (tVal[i] == 2) return 30;
                if (tVal[i] == 3) return 90;
            }
        }
        return 0;
    }

    private void spawnGameInput(RandomSpawner spawner, java.util.Random rnd) {
        int roll = rnd.nextInt(11);
        if      (roll <= 1) { spawnTreasure(1, spawner); }
        else if (roll <= 3) { spawnTreasure(2, spawner); }
        else if (roll <= 5) { spawnTreasure(3, spawner); }
        else if (roll <= 8) { laserManager.spawnPackedLaser(board.getMap(), spawner); }
        else if (roll == 9) {
            int[] pos = spawner.getSpawnPoint(board.getMap());
            enemyManager.addCRobot(pos[0], pos[1], 1000);
        } else {
            int[] pos = spawner.getSpawnPoint(board.getMap());
            enemyManager.addXRobot(pos[0], pos[1], 1000);
        }
    }

    // -------------------------------------------------------------------------
    // Main entry
    // -------------------------------------------------------------------------

    public void start() throws InterruptedException, IOException {

        TextAttributes hintColor = new TextAttributes(new Color(0, 255, 255), Color.BLACK);
        TextAttributes dimColor  = new TextAttributes(new Color(100, 100, 100), Color.BLACK);

        drawText(68, 22, "Please resize your window", hintColor);
        drawText(66, 23, "to fit the game area properly.", hintColor);
        drawText(65, 27, "Press ENTER when you are ready...", dimColor);

        boolean waiting = true;
        while (waiting) {
            int key = controls.consumeKey();
            if (key == KeyEvent.VK_ENTER) waiting = false;
            Thread.sleep(50);
        }
        clearScreen();

        while (true) {
            boolean inMenu = true;
            int lastOption = 0;
            selectedModeOption = 1;

            while (inMenu) {
                if (selectedModeOption != lastOption) {
                    gameLogo.drawLogo(cn, 67, 10);
                    modeMenu.drawMenu(cn, selectedModeOption, 80, 20);
                    lastOption = selectedModeOption;
                }
                int key = controls.consumeKey();
                if (key != 0) {
                    if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
                        selectedModeOption = (selectedModeOption == 1) ? 2 : 1;
                    } else if (key == KeyEvent.VK_ENTER) {
                        inMenu = false;
                    }
                }
                Thread.sleep(50);
            }

            clearScreen();

            laserManager = new LaserManager();
            scoreManager = new ScoreManager();
            tCount = 0;
            RandomSpawner spawner = new RandomSpawner();
            java.util.Random rnd  = new java.util.Random();

            if (selectedModeOption == 1) {
                // ---- New random game ----
                board = new GameBoard(55, 25);

                int[] playerSpawn = spawner.getSpawnPoint(board.getMap());
                px   = playerSpawn[0];
                py   = playerSpawn[1];
                twin = new BCharacter(px, py);

                for (int i = 0; i < 10; i++) spawnGameInput(spawner, rnd);

            } else {
                // ---- Load saved game ----
                if (!SaveLoad.saveExists()) {
                    drawText(70, 22, "No saved game found. Please change option.");
                    drawText(70, 24, "Press ESC");
                    boolean waitEsc = true;
                    while (waitEsc) {
                        int key = controls.consumeKey();
                        if (key == KeyEvent.VK_ESCAPE) waitEsc = false;
                        Thread.sleep(50);
                    }
                    clearScreen();
                    enemyManager = new EnemyManager();
                    timer        = new Timer();
                    trailManager = new TrailManager();
                    modeManager  = new BModeManager();
                    tracker      = new MoveTrack();
                    continue;
                }

                // FIX: extended result array (indices 0-12)
                int[]    result       = new int[13];
                int[]    xRobotX      = new int[100]; int[] xRobotY    = new int[100]; int[] xRobotLife = new int[100];
                int[]    cRobotX      = new int[100]; int[] cRobotY    = new int[100]; int[] cRobotLife = new int[100];
                int[]    loadedTX     = new int[200]; int[] loadedTY   = new int[200]; int[] loadedTVal = new int[200];
                int[]    loadedPackX  = new int[50];  int[] loadedPackY = new int[50];
                char[][] loadedMap    = new char[25][55];

                SaveLoad.loadGame(result,
                        xRobotX, xRobotY, xRobotLife,
                        cRobotX, cRobotY, cRobotLife,
                        loadedTX, loadedTY, loadedTVal,
                        loadedPackX, loadedPackY,
                        loadedMap);

                px   = result[0];
                py   = result[1];
                twin = new BCharacter(result[2], result[3]);
                modeManager.setMode(result[4]);
                timer.setTicks(result[5]);
                int xRobotCount = result[6];
                int cRobotCount = result[7];

                // FIX: restore scores and health
                scoreManager.setScore(result[8]);
                scoreManager.setComputerScore(result[9]);
                scoreManager.setHealth(result[10]);

                int savedTreasureCount = result[11];
                int savedPackCount     = result[12];

                board = new GameBoard(loadedMap);
                enemyManager = new EnemyManager();

                for (int i = 0; i < xRobotCount; i++)
                    enemyManager.addXRobot(xRobotX[i], xRobotY[i], xRobotLife[i]);

                // FIX: restore C-robots
                for (int i = 0; i < cRobotCount; i++)
                    enemyManager.addCRobot(cRobotX[i], cRobotY[i], cRobotLife[i]);

                // FIX: restore treasures
                for (int i = 0; i < savedTreasureCount; i++) {
                    if (tCount < tX.length) {
                        tX[tCount]      = loadedTX[i];
                        tY[tCount]      = loadedTY[i];
                        tVal[tCount]    = loadedTVal[i];
                        tActive[tCount] = true;
                        tCount++;
                    }
                }

                // FIX: restore packed lasers
                for (int i = 0; i < savedPackCount; i++)
                    laserManager.spawnPackedLaserAt(loadedPackX[i], loadedPackY[i]);
            }

            board.printBoard(cn);
            drawTreasures();

            TextAttributes initColor = new TextAttributes(new Color(57, 255, 20), Color.BLACK);
            cn.getTextWindow().output((px * 2) + 4, py + 2, 'A', initColor);
            twin.draw(cn, px, py, initColor);
            enemyManager.drawRobots(cn);
            laserManager.drawPacks(cn);

            // FIX: pass tick, cCount, xCount to HUD
            scoreManager.drawHUD(cn, laserManager.getAmmo(),
                    timer.getTicks(),
                    enemyManager.getCCount(),
                    enemyManager.getXCount());

            boolean gameOver = false;

            while (true) {
                timer.Play();
                int currentTick = timer.getTicks();

                int key = controls.consumeKey();
                int nextX = px, nextY = py;
                int moveX = 0,  moveY = 0;
                boolean playerMoved = false;

                if (key != 0) {

                    if (key == KeyEvent.VK_ESCAPE) {
                        if (!gameOver) {
                            // FIX: save extended state
                            int xCount = enemyManager.getXCount();
                            int cCount = enemyManager.getCCount();
                            int[] rxArr = new int[xCount], ryArr = new int[xCount], rLifeArr = new int[xCount];
                            for (int i = 0; i < xCount; i++) {
                                rxArr[i]    = enemyManager.getRobotX(i);
                                ryArr[i]    = enemyManager.getRobotY(i);
                                rLifeArr[i] = enemyManager.getRobotLife(i);
                            }
                            int[] cxArr = new int[cCount], cyArr = new int[cCount], cLifeArr = new int[cCount];
                            for (int i = 0; i < cCount; i++) {
                                cxArr[i]    = enemyManager.getCRobotX(i);
                                cyArr[i]    = enemyManager.getCRobotY(i);
                                cLifeArr[i] = enemyManager.getCRobotLife(i);
                            }

                            // collect active treasures for save
                            int activeTCount = 0;
                            int[] saveTX = new int[200], saveTY = new int[200], saveTVal = new int[200];
                            for (int i = 0; i < tCount; i++) {
                                if (tActive[i]) {
                                    saveTX[activeTCount]   = tX[i];
                                    saveTY[activeTCount]   = tY[i];
                                    saveTVal[activeTCount] = tVal[i];
                                    activeTCount++;
                                }
                            }

                            SaveLoad.saveGame(
                                    px, py,
                                    twin.getX(), twin.getY(), modeManager.getMode(),
                                    currentTick,
                                    scoreManager.getScore(), scoreManager.getComputerScore(), scoreManager.getHealth(),
                                    rxArr, ryArr, rLifeArr, xCount,
                                    cxArr, cyArr, cLifeArr, cCount,
                                    saveTX, saveTY, saveTVal, new boolean[activeTCount], activeTCount,
                                    laserManager.getPackXArray(), laserManager.getPackYArray(),
                                    laserManager.getPackCollectedArray(), laserManager.getPackCount(),
                                    board.getMap()
                            );
                        }
                        enemyManager = new EnemyManager();
                        timer        = new Timer();
                        trailManager = new TrailManager();
                        modeManager  = new BModeManager();
                        tracker      = new MoveTrack();
                        clearScreen();
                        break;
                    }

                    if (gameOver) continue;

                    if (key == KeyEvent.VK_M || key == 'm' || key == 'M') {
                        modeManager.toggleMode();
                    }

                    if (key == KeyEvent.VK_SPACE) {
                        laserManager.fireLaser(px, py, twin.getX(), twin.getY(), currentTick);
                    }

                    if      (key == KeyEvent.VK_LEFT)  { nextX--; moveX = -1; playerMoved = true; }
                    else if (key == KeyEvent.VK_RIGHT)  { nextX++; moveX =  1; playerMoved = true; }
                    else if (key == KeyEvent.VK_UP)     { nextY--; moveY = -1; playerMoved = true; }
                    else if (key == KeyEvent.VK_DOWN)   { nextY++; moveY =  1; playerMoved = true; }

                    tracker.setLastMove(moveX, moveY);

                    CollisionControl cd = new CollisionControl();
                    if (playerMoved && cd.canPlayerMove(board.getMap(), nextX, nextY, enemyManager)) {
                        trailManager.addTrail(px, py, currentTick);
                        px = nextX;
                        py = nextY;
                    }

                    if (playerMoved && (moveX != 0 || moveY != 0)) {
                        twin.move(tracker, board, modeManager.getMode(), trailManager, currentTick, enemyManager);
                    }
                }

                if (!gameOver) {

                    // FIX: A and B collect lasers and treasures every tick (not just on key press)
                    laserManager.checkPickups(px, py, cn);
                    laserManager.checkPickups(twin.getX(), twin.getY(), cn);

                    scoreManager.addScore(collectTreasure(px, py));
                    scoreManager.addScore(collectTreasure(twin.getX(), twin.getY()));

                    // Update laser
                    laserManager.updateSpread(board.getMap(), currentTick);

                    int kills = laserManager.neighborHarm(enemyManager, cn);
                    if (kills > 0) scoreManager.addScore(100 * kills);

                    laserManager.cleanExpiredLasers(cn, currentTick);

                    if (timer.isRobotTurn()) {
                        enemyManager.moveRobots(board, trailManager, currentTick, px, py, twin);
                        enemyManager.moveCRobots(board, trailManager, currentTick, px, py, twin,
                                tX, tY, tActive, tCount);
                        for (int i = 0; i < enemyManager.getRobotCount(); i++) {
                            int pts = collectTreasureForComputer(
                                    enemyManager.getRobotX(i), enemyManager.getRobotY(i));
                            if (pts > 0) scoreManager.addComputerScore(pts);
                        }
                    }

                    int adjacentRobots = enemyManager.countAdjacentToPlayer(px, py);
                    if (adjacentRobots > 0) scoreManager.takeDamage(adjacentRobots * 50);

                    if (!scoreManager.isAlive()) {
                        gameOver = true;
                        scoreManager.drawGameOver(cn);
                    }

                    if (currentTick % 20 == 0 && currentTick > 0) {
                        spawnGameInput(spawner, rnd);
                    }
                }

                if (!gameOver) {
                    trailManager.clearOldTrails(cn, currentTick);

                    drawTreasures();
                    laserManager.drawPacks(cn);
                    laserManager.drawLasers(cn);
                    enemyManager.drawRobots(cn);

                    TextAttributes playerColor = (modeManager.getMode() == 1)
                            ? new TextAttributes(new Color(57, 255, 20),   Color.BLACK)
                            : new TextAttributes(new Color(255, 100, 255), Color.BLACK);

                    cn.getTextWindow().output((px * 2) + 4, py + 2, 'A', playerColor);
                    twin.draw(cn, px, py, playerColor);

                    // FIX: full HUD with time, computer score, robot counts
                    scoreManager.drawHUD(cn, laserManager.getAmmo(),
                            currentTick,
                            enemyManager.getCCount(),
                            enemyManager.getXCount());
                }
            }
        }
    }
}

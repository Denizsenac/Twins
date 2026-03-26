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
    private int[] tX = new int[100];
    private int[] tY = new int[100];
    private int[] tVal = new int[100];
    private boolean[] tActive = new boolean[100];
    private int tCount = 0;

    private int px, py;
    private int selectedModeOption = 1;

    public GameEngine() throws Exception {
        cn = Enigma.getConsole("Twins - Maze Game", 200, 50, 12);
        controls = new GameControls(cn);
        timer = new Timer();
        trailManager = new TrailManager();
        enemyManager = new EnemyManager();

        modeManager = new BModeManager();
        tracker = new MoveTrack();

        modeMenu = new ModeMenuUI();
        gameLogo = new TitleScreen();

        laserManager = new LaserManager();
        scoreManager = new ScoreManager();
    }

    private void clearScreen() {
        for (int y = 0; y < 50; y++) {
            for (int x = 0; x < 200; x++) {
                cn.getTextWindow().output(x, y, ' ');
            }
        }
    }

    private void drawText(int x, int y, String text) {
        for (int i = 0; i < text.length(); i++) {
            cn.getTextWindow().output(x + i, y, text.charAt(i));
        }
    }

    private void drawText(int x, int y, String text, TextAttributes attr) {
        for (int i = 0; i < text.length(); i++) {
            cn.getTextWindow().output(x + i, y, text.charAt(i), attr);
        }
    }

    private void spawnTreasure(int value, RandomSpawner spawner) {
        if (tCount >= tX.length) return;
        int[] pos = spawner.getSpawnPoint(board.getMap());
        tX[tCount] = pos[0];
        tY[tCount] = pos[1];
        tVal[tCount] = value;
        tActive[tCount] = true;
        tCount++;
    }

    private void drawTreasures() {
        TextAttributes t1Color = new TextAttributes(new Color(255, 255, 0), Color.BLACK);
        TextAttributes t2Color = new TextAttributes(new Color(255, 165, 0), Color.BLACK);
        TextAttributes t3Color = new TextAttributes(new Color(255, 100, 255), Color.BLACK);
        for (int i = 0; i < tCount; i++) {
            if (!tActive[i]) continue;
            int screenX = (tX[i] * 2) + 4;
            int screenY = tY[i] + 2;
            TextAttributes c = (tVal[i] == 1) ? t1Color : (tVal[i] == 2) ? t2Color : t3Color;
            cn.getTextWindow().output(screenX, screenY, (char)('0' + tVal[i]), c);
        }
    }

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

    private void spawnGameInput(RandomSpawner spawner, java.util.Random rnd) {
        int roll = rnd.nextInt(11);
        if (roll <= 1) {
            spawnTreasure(1, spawner);
        } else if (roll <= 3) {
            spawnTreasure(2, spawner);
        } else if (roll <= 5) {
            spawnTreasure(3, spawner);
        } else if (roll <= 8) {
            laserManager.spawnPackedLaser(board.getMap(), spawner);
        } else if (roll == 9) {
            int[] pos = spawner.getSpawnPoint(board.getMap());
            enemyManager.addCRobot(pos[0], pos[1], 1000);
        } else {
            int[] pos = spawner.getSpawnPoint(board.getMap());
            enemyManager.addXRobot(pos[0], pos[1], 1000);
        }
    }

    public void start() throws InterruptedException, IOException {

        // Confirmation screen - resize window first
        TextAttributes hintColor = new TextAttributes(new Color(0, 255, 255), Color.BLACK);
        TextAttributes dimColor = new TextAttributes(new Color(100, 100, 100), Color.BLACK);

        drawText(68, 22, "Please resize your window", hintColor);
        drawText(66, 23, "to fit the game area properly.", hintColor);
        drawText(65, 27, "Press ENTER when you are ready...", dimColor);

        boolean waiting = true;
        while (waiting) {
            int key = controls.consumeKey();
            if (key == KeyEvent.VK_ENTER) {
                waiting = false;
            }
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
            java.util.Random rnd = new java.util.Random();

            if (selectedModeOption == 1) {
                board = new GameBoard(55, 25);

                int[] playerSpawn = spawner.getSpawnPoint(board.getMap());
                px = playerSpawn[0];
                py = playerSpawn[1];
                twin = new BCharacter(px, py);

                // First 10 elements of the game input system
                for (int i = 0; i < 10; i++) {
                    spawnGameInput(spawner, rnd);
                }

            } else {
                if (!SaveLoad.saveExists()) {
                    drawText(70, 22, "No old maps please change your option");
                    drawText(70, 24, "Press ESC");

                    boolean waitEsc = true;
                    while (waitEsc) {
                        int key = controls.consumeKey();
                        if (key == KeyEvent.VK_ESCAPE) {
                            waitEsc = false;
                        }
                        Thread.sleep(50);
                    }

                    clearScreen();
                    enemyManager = new EnemyManager();
                    timer  = new Timer();
                    trailManager = new TrailManager();
                    modeManager  = new BModeManager();
                    tracker      = new MoveTrack();
                    continue;
                }

                int[] result       = new int[7];
                int[] xRobotX      = new int[100];
                int[] xRobotY      = new int[100];
                int[] xRobotLife   = new int[100];
                char[][] loadedMap = new char[25][55];

                SaveLoad.loadGame(result, xRobotX, xRobotY, xRobotLife, loadedMap);

                px = result[0];
                py = result[1];
                twin = new BCharacter(result[2], result[3]);
                modeManager.setMode(result[4]);
                timer.setTicks(result[5]);
                int robotCount = result[6];

                board = new GameBoard(loadedMap);

                for (int i = 0; i < robotCount; i++) {
                    enemyManager.addXRobot(xRobotX[i], xRobotY[i], xRobotLife[i]);
                }

                // First 10 elements of the game input system
                for (int i = 0; i < 10; i++) {
                    spawnGameInput(spawner, rnd);
                }
            }

            board.printBoard(cn);
            drawTreasures();
            TextAttributes initColor = new TextAttributes(new Color(57, 255, 20), Color.BLACK);
            cn.getTextWindow().output((px * 2) + 4, py + 2, 'A', initColor);
            twin.draw(cn, px, py, initColor);
            enemyManager.drawRobots(cn);
            laserManager.drawPacks(cn);
            scoreManager.drawHUD(cn, laserManager.getAmmo());

            boolean gameOver = false;

            while (true) {
                timer.Play();
                int currentTick = timer.getTicks();

                int key = controls.consumeKey();
                int nextX = px;
                int nextY = py;
                int moveX = 0;
                int moveY = 0;
                boolean playerMoved = false;

                if (key != 0) {

                    if (key == KeyEvent.VK_ESCAPE) {
                        if (!gameOver) {
                            int robotCount = enemyManager.getRobotCount();
                            int[] rxArr    = new int[robotCount];
                            int[] ryArr    = new int[robotCount];
                            int[] rLifeArr = new int[robotCount];
                            for (int i = 0; i < robotCount; i++) {
                                rxArr[i]    = enemyManager.getRobotX(i);
                                ryArr[i]    = enemyManager.getRobotY(i);
                                rLifeArr[i] = enemyManager.getRobotLife(i);
                            }
                            SaveLoad.saveGame(
                                    px, py,
                                    twin.getX(), twin.getY(), modeManager.getMode(),
                                    currentTick,
                                    rxArr, ryArr, rLifeArr, robotCount,
                                    board.getMap()
                            );
                        }
                        enemyManager = new EnemyManager();
                        timer = new Timer();
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

                    // SPACE to fire laser from A to B
                    if (key == KeyEvent.VK_SPACE) {
                        laserManager.fireLaser(px, py, twin.getX(), twin.getY(), currentTick);
                    }

                    if (key == KeyEvent.VK_LEFT)      { nextX--; moveX = -1; playerMoved = true; }
                    else if (key == KeyEvent.VK_RIGHT) { nextX++; moveX =  1; playerMoved = true; }
                    else if (key == KeyEvent.VK_UP)    { nextY--; moveY = -1; playerMoved = true; }
                    else if (key == KeyEvent.VK_DOWN)  { nextY++; moveY =  1; playerMoved = true; }

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

                    // Check laser pack pickups for A and B
                    laserManager.checkPickups(px, py, cn);
                    laserManager.checkPickups(twin.getX(), twin.getY(), cn);

                    // Check treasure pickups for A and B
                    scoreManager.addScore(collectTreasure(px, py));
                    scoreManager.addScore(collectTreasure(twin.getX(), twin.getY()));
                }

                if (!gameOver) {
                    // Update laser spread
                    laserManager.updateSpread(board.getMap(), currentTick);

                    // Laser neighbor harming
                    int kills = laserManager.neighborHarm(enemyManager, cn);
                    if (kills > 0) {
                        scoreManager.addScore(100 * kills);
                    }

                    // Clean expired laser blocks
                    laserManager.cleanExpiredLasers(cn, currentTick);

                    if (timer.isRobotTurn()) {
                        enemyManager.moveRobots(board, trailManager, currentTick, px, py, twin);
                        enemyManager.moveCRobots(board, trailManager, currentTick, px, py, twin, tX, tY, tActive, tCount);
                        // Robots collect treasures (treasures disappear)
                        for (int i = 0; i < enemyManager.getRobotCount(); i++) {
                            collectTreasure(enemyManager.getRobotX(i), enemyManager.getRobotY(i));
                        }
                    }

                    // Check if robots are adjacent to player A - take damage (50 per adjacent robot)
                    int adjacentRobots = enemyManager.countAdjacentToPlayer(px, py);
                    if (adjacentRobots > 0) {
                        scoreManager.takeDamage(adjacentRobots * 50);
                    }

                    // Check if player is dead
                    if (!scoreManager.isAlive()) {
                        gameOver = true;
                        scoreManager.drawGameOver(cn);
                    }

                    // Game input system: spawn one element every second (20 ticks)
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
                        ? new TextAttributes(new Color(57, 255, 20), Color.BLACK)
                        : new TextAttributes(new Color(255, 100, 255), Color.BLACK);
                    cn.getTextWindow().output((px * 2) + 4, py + 2, 'A', playerColor);
                    twin.draw(cn, px, py, playerColor);
                    scoreManager.drawHUD(cn, laserManager.getAmmo());
                }
                
            }
        }
    }
}

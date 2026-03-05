
package Game;

import enigma.console.Console;
import enigma.core.Enigma;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GameEngine {
    private Console cn;
    private GameBoard board;
    private GameControls controls;
    private TimeManager timeManager;
    private TrailManager trailManager;
    private EnemyManager enemyManager;

    private BModeManager modeManager;
    private BCharacter twin;
    private MoveTrack tracker;

    private ModeMenuUI modeMenu;
    private TitleScreen gameLogo;

    private int px, py;
    private int selectedModeOption = 1;

    public GameEngine() throws Exception {
        cn = Enigma.getConsole("Twins - Maze Game", 200, 50, 12);
        controls = new GameControls(cn);
        timeManager = new TimeManager();
        trailManager = new TrailManager();
        enemyManager = new EnemyManager();

        modeManager = new BModeManager();
        tracker = new MoveTrack();

        modeMenu = new ModeMenuUI();
        gameLogo = new TitleScreen();
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

    public void start() throws InterruptedException, IOException {

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
                        selectedModeOption = (selectedModeOption == 1) ? -1 : 1;
                    } else if (key == KeyEvent.VK_ENTER) {
                        inMenu = false;
                    }
                }
                Thread.sleep(50);
            }

            clearScreen();

            if (selectedModeOption == 1) {
                board = new GameBoard(55, 25);
                RandomSpawner spawner = new RandomSpawner();

                int[] playerSpawn = spawner.getSpawnPoint(board.getMap());
                px = playerSpawn[0];
                py = playerSpawn[1];
                twin = new BCharacter(px, py);

                int[] robotSpawn = spawner.getSpawnPoint(board.getMap());
                enemyManager.addXRobot(robotSpawn[0], robotSpawn[1]);

            } else {
                if (!SaveLoad.saveExists()) {
                    drawText(70, 22, "No old maps please change your option");
                    drawText(70, 24, "Press ESC");

                    boolean waiting = true;
                    while (waiting) {
                        int key = controls.consumeKey();
                        if (key == KeyEvent.VK_ESCAPE) {
                            waiting = false;
                        }
                        Thread.sleep(50);
                    }

                    clearScreen();
                    enemyManager = new EnemyManager();
                    timeManager  = new TimeManager();
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
                timeManager.setTicks(result[5]);
                int robotCount = result[6];

                board = new GameBoard(loadedMap);

                for (int i = 0; i < robotCount; i++) {
                    enemyManager.addXRobot(xRobotX[i], xRobotY[i], xRobotLife[i]);
                }
            }

            board.printBoard(cn);
            cn.getTextWindow().output((px * 2) + 4, py + 2, 'A');
            twin.draw(cn, px, py);
            enemyManager.drawRobots(cn);

            while (true) {
                timeManager.tick();
                int currentTick = timeManager.getTicks();

                int key = controls.consumeKey();
                int nextX = px;
                int nextY = py;
                int moveX = 0;
                int moveY = 0;
                boolean playerMoved = false;

                if (key != 0) {

                    if (key == KeyEvent.VK_ESCAPE) {
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
                        enemyManager = new EnemyManager();
                        timeManager  = new TimeManager();
                        trailManager = new TrailManager();
                        modeManager  = new BModeManager();
                        tracker      = new MoveTrack();
                        clearScreen();
                        break;
                    }

                    if (key == KeyEvent.VK_M || key == 'm' || key == 'M') {
                        modeManager.toggleMode();
                        cn.getTextWindow().setCursorPosition(0, 0);
                        cn.getTextWindow().output("ACTIVE MOD: " + modeManager.getMode() + "    ");
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
                        twin.move(tracker, board, modeManager.getMode(), trailManager, currentTick);
                    }
                }

                if (timeManager.isRobotTurn()) {
                    enemyManager.moveRobots(board, trailManager, currentTick, px, py, twin);
                }

                trailManager.clearOldTrails(cn, currentTick);

                cn.getTextWindow().output((px * 2) + 4, py + 2, 'A');
                twin.draw(cn, px, py);
                enemyManager.drawRobots(cn);

                Thread.sleep(50);
            }
        }
    }
}
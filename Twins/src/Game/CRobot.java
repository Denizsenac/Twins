package Game;

import enigma.console.Console;

public class CRobot {

    public int x;
    public int y;
    public int lifePoints = 1000;

    public CRobot(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public CRobot(int startX, int startY, int life) {
        this.x = startX;
        this.y = startY;
        this.lifePoints = life;
    }

    // -------------------------------------------------------
    // Haritada en yakın hazineyi bulur (Manhattan distance ile)
    // Hazine karakterleri: '1', '2', '3'
    // Bulamazsa {-1, -1} döner
    // -------------------------------------------------------
    private int[] findNearestTreasure(char[][] map) {
        int bestX = -1;
        int bestY = -1;
        int bestDist = Integer.MAX_VALUE;

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                char cell = map[row][col];
                if (cell == '1' || cell == '2' || cell == '3') {
                    // Manhattan distance: yatay + dikey fark
                    int dist = Math.abs(col - this.x) + Math.abs(row - this.y);
                    if (dist < bestDist) {
                        bestDist = dist;
                        bestX = col;
                        bestY = row;
                    }
                }
            }
        }

        return new int[]{bestX, bestY};
    }

    // -------------------------------------------------------
    // Robotu hareket ettirir
    // Önce en yakın hazineyi bulur
    // Sonra o yöne doğru bir adım atar
    // Engel varsa duvar gibi davranır (takılır)
    // -------------------------------------------------------
    public void move(GameBoard board, EnemyManager enemies, int px, int py, BCharacter twin) {
        char[][] map = board.getMap();

        int[] target = findNearestTreasure(map);

        // Haritada hiç hazine yoksa hareket etme
        if (target[0] == -1) {
            return;
        }

        int targetX = target[0];
        int targetY = target[1];

        // Hedefe göre hangi yönde gideceğimizi hesaplıyoruz
        // Önce yatay farkı, sonra dikey farkı kontrol ediyoruz
        // Hangisi daha büyükse önce o yönde gitmeye çalışıyoruz
        int nextX = this.x;
        int nextY = this.y;

        int diffX = targetX - this.x; // yatay fark
        int diffY = targetY - this.y; // dikey fark

        if (Math.abs(diffX) >= Math.abs(diffY)) {
            // Yatay fark daha büyük, yatay git
            if (diffX > 0) nextX++;
            else if (diffX < 0) nextX--;
        } else {
            // Dikey fark daha büyük, dikey git
            if (diffY > 0) nextY++;
            else if (diffY < 0) nextY--;
        }

        // Hareket edebilir mi kontrol et
        CollisionControl cc = new CollisionControl();
        if (cc.canRobotMove(map, nextX, nextY, enemies, px, py, twin)) {
            this.x = nextX;
            this.y = nextY;
        }
        // Engel varsa hiç hareket etme (proje: "they are stuck on obstacles")
    }

    public void draw(Console cn) {
        cn.getTextWindow().output((x * 2) + 4, y + 2, 'C');
    }
}

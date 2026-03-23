package Game;

import enigma.console.Console;
import java.util.Random;

public class TreasureManager {

    private Random rnd = new Random();

    // Haritaya hazine ekler, her 20 tick'te bir çağrılacak
    // Rastgele boş bir kareye '1', '2' veya '3' koyar
    public void spawnTreasure(char[][] map) {
        // Hangi hazineyi koyacağımızı seçiyoruz
        // Proje dökümanındaki olasılıklara göre:
        // '1' en sık, '3' en nadir
        int roll = rnd.nextInt(10);
        char treasure;
        if (roll < 5)      treasure = '1'; // %50
        else if (roll < 8) treasure = '2'; // %30
        else               treasure = '3'; // %20

        // Boş bir kare bulana kadar rastgele konum seçiyoruz
        int tries = 0;
        while (tries < 200) {
            int x = rnd.nextInt(map[0].length - 2) + 1;
            int y = rnd.nextInt(map.length - 2) + 1;
            if (map[y][x] == ' ') {
                map[y][x] = treasure;
                return;
            }
            tries++;
        }
    }

    // Haritadaki tüm hazineleri ekrana çizer
    public void drawTreasures(Console cn, char[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char c = map[y][x];
                if (c == '1' || c == '2' || c == '3') {
                    cn.getTextWindow().output((x * 2) + 4, y + 2, c);
                }
            }
        }
    }

    // Oyuncu (A veya B) hazine üstüne geldi mi kontrol et
    // Geldiyse haritadan sil ve kazanılan skoru döndür
    // Hazine yoksa 0 döner
    public int checkPlayerCollect(char[][] map, int x, int y) {
        char c = map[y][x];
        if (c == '1' || c == '2' || c == '3') {
            map[y][x] = ' '; // haritadan sil
            // ekranda da temizle (boşluk yaz)
            return treasureValue(c);
        }
        return 0;
    }

    // C-Robot hazine üstüne geldi mi kontrol et
    // C-Robot için değer 3 kat fazla (proje dökümanı)
    public int checkRobotCollect(char[][] map, int x, int y) {
        char c = map[y][x];
        if (c == '1' || c == '2' || c == '3') {
            map[y][x] = ' ';
            return treasureValue(c) * 3; // robotlar için 3 kat
        }
        return 0;
    }

    // Hazine değerlerini döndürür
    private int treasureValue(char c) {
        if (c == '1') return 3;
        if (c == '2') return 10;
        if (c == '3') return 30;
        return 0;
    }
}

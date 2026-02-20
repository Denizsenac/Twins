import java.util.Random;

public class RandomSpawner {
    
    public int[] getSpawnPoint(char[][] map) {
        Random rnd = new Random();
        int x, y;
        
        do {
            y = rnd.nextInt(map.length);
            x = rnd.nextInt(map[0].length);
        } while (map[y][x] != ' '); 
        
        return new int[]{x, y};
    }
}
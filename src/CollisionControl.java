public class CollisionControl {
    
    public boolean canMove(char[][] map, int targetX, int targetY) {
        
        
        if (targetY < 0 || targetY >= map.length || targetX < 0 || targetX >= map[0].length) {
            return false; 
        }
        
        if (map[targetY][targetX] == ' ') {
            return true;
        }
        
        return false;
    }
}
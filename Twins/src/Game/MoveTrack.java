package Game;

public class MoveTrack {
    private int dx = 0; 
    private int dy = 0; 

    
    public void setLastMove(int x, int y) {
        this.dx = x;
        this.dy = y;
    }

    public int getSameDirX() {
        return dx;
    }
    public int getSameDirY() {
        return dy;
    }
    public int getOppositeDirX() {
        return -dx; 
    }
    public int getOppositeDirY() {
        return -dy;
    }
}
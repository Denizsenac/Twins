package Game;

public class BModeManager {
    
    private int currentMode = 1; 

    
    public void toggleMode() {
        currentMode = currentMode * -1;
    }

    
    public int getMode() {
        return currentMode;
    }
}
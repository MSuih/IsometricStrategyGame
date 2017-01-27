package game;

public class Game {
    private Level level;
    public boolean squareExists(int x, int y) {
        if (x < 0 || y < 0) return false;
        return level.insideBounds(x, y);
    }
}

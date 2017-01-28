package game;

import window.Point;

public class Game {
    private Level level = new Level();
    public boolean squareExists(int x, int y) {
        if (x < 0 || y < 0) return false;
        return level.insideBounds(x, y);
    }
    public boolean squareExists(Point p) {
        return squareExists(p.x, p.y);
    }
}

package window;

public class IsometricUtilities {

    private IsometricUtilities() {}

    public static Point screen2coord(int x, int y) {
        //TODO: needs tweaking
        /*final int px = y + x;
        final int py = y - x;*/
        int px = x;
        int py = y;
        
        return new Point(px, py);
    }
    public static Point screen2coord(Point p) {
        return screen2coord(p.x, p.y);
    }
    public static Point coord2screen(int x, int y) {
        //Todo: needs tweaking
        final int px = x - y;
        final int py = x + y;
        return new Point(px, py);
    }
    public static Point coord2screen(Point p) {
        return coord2screen(p.x, p.y);
    }
}

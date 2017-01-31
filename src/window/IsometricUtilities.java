package window;

public class IsometricUtilities {

    private IsometricUtilities() {}

    public static Point iso2coord(int x, int y) {
        //TODO: needs testing
        final int px = (y + x);
        final int py = (y - x) / 2;
        return new Point(px, py);
    }
    public static Point iso2coord(Point p) {
        return iso2coord(p.x, p.y);
    }
    public static Point coord2iso(int x, int y) {
        final int px = x - y;
        final int py = x + y;
        return new Point(px, py);
    }
    public static Point coord2iso(Point p) {
        return coord2iso(p.x, p.y);
    }
}

package game;

public class MapSquare {
    private boolean wallAtTop;
    private boolean wallAtLeft;
    private byte height;

    public MapSquare() {
        this(false, false, (byte) 0);
    }

    public MapSquare(boolean wallAtTop, boolean wallAtLeft, byte height) {
        this.wallAtTop = wallAtTop;
        this.wallAtLeft = wallAtLeft;
        this.height = height;
    }

    public boolean isWallAtTop() {
        return wallAtTop;
    }

    public boolean isWallAtLeft() {
        return wallAtLeft;
    }

    public byte getHeight() {
        return height;
    }

    public void toggleWallAtTop() {
        wallAtLeft = !wallAtLeft;
    }

    public void setWallAtTop(boolean wallAtTop) {
        this.wallAtTop = wallAtTop;
    }

    public void toggleWallAtLeft() {
        wallAtLeft = !wallAtLeft;
    }

    public void setWallAtLeft(boolean wallAtLeft) {
        this.wallAtLeft = wallAtLeft;
    }

    public void setHeight(byte height) {
        this.height = height;
    }
}
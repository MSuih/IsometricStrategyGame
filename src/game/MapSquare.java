package game;

public class MapSquare {
    /*
    To avoid overhead, we'll be using bit-level stuff
    0000 0000
    first two bits are top & left wall
    1100 0000
    last 6 bits are wall height
    0011 1111
    */
    private static final byte wallTopBit = (byte) 0b10000000;
    private static final byte wallLeftBit = 0b01000000;
    private static final byte wallHeightMask = 0b00111111;

    private byte data;

    public MapSquare() {
        this(false, false, (byte) 0);
    }

    public MapSquare(boolean wallAtTop, boolean wallAtLeft, int height) {
        data = 0;
        if (wallAtTop) data = (byte) (data ^ wallTopBit);
        if (wallAtLeft) data = (byte) (data ^ wallLeftBit);
        height = height & wallHeightMask;
        data = (byte) (data ^ height);
    }

    public boolean isWallAtTop() {
        return (data & wallTopBit) == wallTopBit;
    }

    public boolean isWallAtLeft() {
        return (data & wallLeftBit) == wallLeftBit;
    }

    public int getHeight() {
        return data & wallHeightMask;
    }

    public void toggleWallAtTop() {
        data = (byte) (data ^ wallTopBit);
    }

    public void toggleWallAtLeft() {
        data = (byte) (data ^ wallLeftBit);
    }

    public void setHeight(int height) {
        height = height & wallHeightMask;
        data = (byte) (data & height);
    }
}
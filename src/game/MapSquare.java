package game;

public class MapSquare {
    /*
    To avoid overhead, we'll be using bit-level stuff
    0000 0000
    first two bits are top & left wall
    1100 0000
    second two bits tell the type of this square
    0011 0000
    last 4 bits are square height
    0000 1111
    */
    private static final byte wallTopBit = (byte) 0b10000000;
    private static final byte wallLeftBit = 0b01000000;
    private static final byte squareTypeMask = 0b00110000;
    private static final byte typeWalkable = 0b00000000;
    private static final byte typeViewable = 0b00010000;
    private static final byte typeSolid = 0b00110000;
    //Reserved for future use
    //private static final byte typeExtra = 0b00100000;
    private static final byte heightMask = 0b00001111;

    private byte data;

    public MapSquare() {
        this(false, false, 0);
    }

    public MapSquare(boolean wallAtTop, boolean wallAtLeft, int height) {
        this(wallAtTop, wallAtLeft, height, SquareType.WALKABLE);
    }

    public MapSquare(boolean wallAtTop, boolean wallAtLeft, int height, SquareType type) {
        data = 0;

        if (wallAtTop) data = (byte) (data ^ wallTopBit);
        if (wallAtLeft) data = (byte) (data ^ wallLeftBit);

        height = height & heightMask;
        data = (byte) (data ^ height);

        data = (byte) (data ^ type.getAsBits());
    }

    public MapSquare(byte data) {
        this.data = data;
    }

    public boolean isWallAtTop() {
        return (data & wallTopBit) == wallTopBit;
    }

    public boolean isWallAtLeft() {
        return (data & wallLeftBit) == wallLeftBit;
    }

    public int getHeight() {
        return data & heightMask;
    }

    public void toggleWallAtTop() {
        data = (byte) (data ^ wallTopBit);
    }

    public void toggleWallAtLeft() {
        data = (byte) (data ^ wallLeftBit);
    }

    public void setHeight(int height) {
        height = height & heightMask;
        data = (byte) (data & height);
    }

    public SquareType getType() {
        return SquareType.bitsToType(data);
    }

    public void setType(SquareType type) {
        data = (byte) (data ^ type.getAsBits());
    }

    public static enum SquareType {
        WALKABLE, VIEWABLE, SOLID; //space for one more

        public byte getAsBits() {
            switch (this) {
                case WALKABLE:
                    return typeWalkable;
                case SOLID:
                    return typeSolid;
                case VIEWABLE:
                    return typeViewable;
                default:
                    throw new AssertionError("SquareType does not have a mask");
            }
        }

        public static SquareType bitsToType(byte bits) {
            bits = (byte) (bits & squareTypeMask);
            switch (bits) {
                case typeWalkable:
                    return WALKABLE;
                case typeSolid:
                    return SOLID;
                case typeViewable:
                    return VIEWABLE;
                default:
                    throw new AssertionError("No squaretype for these bits: " + Integer.toBinaryString(bits));
            }
        }
    }
}
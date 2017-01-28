package game;

public class MapSquare {
    /*
    To avoid overhead, we'll be using bit-level stuff
    0000 0000
    first two bits are top & left wall
    1100 0000
    second two bits tell if the square is walkable and/or obaque
    0011 0000
    last 4 bits are wall height
    0000 1111
    */
    private static final byte wallTopBit = (byte) 0b10000000;
    private static final byte wallLeftBit = 0b01000000;
    //TODO: Are there more states than walkable & solid? We could store 4 different states here
    private static final byte wallTypeMask = 0b00110000;
    private static final byte isWalkableBit = 0b00100000;
    private static final byte isSolidBit = 0b00010000;
    private static final byte wallHeightMask = 0b00001111;

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

        height = height & wallHeightMask;
        data = (byte) (data ^ height);

        data = (byte) (data ^ type.getAsBits());
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

    public SquareType getType() {
        return SquareType.bitsToType(data);
    }

    public void setType(SquareType type) {
        data = (byte) (data ^ type.getAsBits());
    }

    public static enum SquareType {
        WALKABLE, SOLID; //space for two more

        public byte getAsBits() {
            switch (this) {
                case WALKABLE:
                    return isWalkableBit;
                case SOLID:
                    return isSolidBit;
                default:
                    throw new AssertionError("SquareType does not have a mask");
            }
        }
        
        public static SquareType bitsToType(byte bits) {
            bits = (byte) (bits & wallTypeMask);
            switch (bits) {
                case isWalkableBit:
                    return WALKABLE;
                case isSolidBit:
                    return SOLID;
                default:
                    throw new AssertionError("No squaretype for these bits: " + Integer.toBinaryString(bits));
            }
        }
    }
}
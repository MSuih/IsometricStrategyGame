package objects;

public interface Drawable {
    default public double getXOffset() {
        return 0;
    }
    default public double getYOffset() {
        return 0;
    }
    public int getX();
    public int getY();
    public String getSprite();
}

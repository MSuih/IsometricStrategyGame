package objects;

public abstract class Unit implements Drawable {
    private int x, y;

    public Unit() {
        this(0, 0);
    }

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public abstract double getHealth();
    public abstract double getHealthPercent();
    public abstract boolean isDead();
    public abstract void damage(int value);
    public abstract void heal(int value);
}

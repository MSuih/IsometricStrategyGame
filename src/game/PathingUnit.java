package game;

public class PathingUnit {
    public final PathingUnit parent;
    public final int x, y;

    public PathingUnit(int x, int y) {
        this(null, x, y);
    }

    public PathingUnit(PathingUnit parent, int x, int y) {
        this.parent = parent;
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PathingUnit)) return false;
        
        final PathingUnit other = (PathingUnit) obj;
        return this.x == other.x && this.y == other.y;
    }
}

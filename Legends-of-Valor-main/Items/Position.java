package Items;

/**
 * Immutable value object for (row, col) coordinates.
 */
public final class Position {
    public final int row;
    public final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position translate(int dr, int dc) {
        return new Position(row + dr, col + dc);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position))
            return false;
        Position other = (Position) o;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return (row * 31) ^ col;
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}

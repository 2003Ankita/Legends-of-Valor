package Board;

/**
 * Logical lane in the Legends of Valor 8x8 board.
 * Each lane corresponds to two adjacent columns.
 */
public enum Lane {
    TOP(0, 1),
    MIDDLE(3, 4),
    BOTTOM(6, 7);

    private final int leftColumn;
    private final int rightColumn;

    Lane(int leftColumn, int rightColumn) {
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
    }

    public int getLeftColumn() {
        return leftColumn;
    }

    public int getRightColumn() {
        return rightColumn;
    }

    /** @return true if column index belongs to this lane. */
    public boolean containsColumn(int col) {
        return col == leftColumn || col == rightColumn;
    }
}

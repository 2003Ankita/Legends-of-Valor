package Board;

import java.util.Random;

/**
 * Represents the game board containing a grid of tiles.
 * Handles tile generation, start tile setup,
 * and tracking visible "main monsters".
 */
public class Board {
    private final int size;
    private final Tile[][] grid;
    private final int startRow;
    private final int startCol;

    // Tile generation rates
    private final double inaccessibleRate;
    private final double marketRate;
    private final double bushRate = 0.10;   // NEW
    private final double koulouRate = 0.10; // NEW
    private final double commonRate;

    private int mainMonstersRemaining = 0;

    public Board(int size) {
        this(size, 0.20, 0.30, 0.50);
    }

    public Board(int size, double inaccessibleRate, double marketRate, double commonRate) {
        if (size <= 0) {
            throw new IllegalArgumentException("Board size must be positive.");
        }
        this.size = size;

        this.inaccessibleRate = inaccessibleRate;
        this.marketRate = marketRate;
        this.commonRate = commonRate;

        this.grid = new Tile[size][size];
        generate();

        this.startRow = 0;
        this.startCol = 0;

        grid[startRow][startCol].setType(TileType.COMMON);
        grid[startRow][startCol].setMonster(false);
    }

    /** Generates grid including new tile types (BUSH, KOULOU). */
    private void generate() {
        Random random = new Random();

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {

                double p = random.nextDouble();
                TileType type;

                if (p < inaccessibleRate) {
                    type = TileType.INACCESSIBLE;

                } else if (p < inaccessibleRate + marketRate) {
                    type = TileType.MARKET;

                } else if (p < inaccessibleRate + marketRate + bushRate) {
                    type = TileType.BUSH;

                } else if (p < inaccessibleRate + marketRate + bushRate + koulouRate) {
                    type = TileType.KOULOU;

                } else {
                    type = TileType.COMMON;
                }

                grid[r][c] = new Tile(type);
            }
        }
    }

    public boolean inBounds(int r, int c) {
        return r >= 0 && r < size && c >= 0 && c < size;
    }

    public Tile getTile(int r, int c) {
        return grid[r][c];
    }

    public int getSize() { return size; }
    public int getStartRow() { return startRow; }
    public int getStartCol() { return startCol; }

    /** Registers a monster on a COMMON tile (MAIN monster). */
    public boolean registerMainMonsterAt(int r, int c) {
        Tile t = grid[r][c];

        if (t.getType() == TileType.COMMON && !t.hasMonster()) {
            t.setMonster(true);
            mainMonstersRemaining++;
            return true;
        }
        return false;
    }

    public void mainMonsterDefeated() {
        if (mainMonstersRemaining > 0)
            mainMonstersRemaining--;
    }

    public int getMonstersRemaining() {
        return mainMonstersRemaining;
    }

    public boolean allMonstersCleared() {
        return mainMonstersRemaining == 0;
    }
}

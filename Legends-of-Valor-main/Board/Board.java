package Board;

import java.util.Random;

/**
 * Represents the game board containing a grid of tiles.
 * Handles tile generation, start tile setup, and tracking of all
 * "main monsters" placed visibly on the map.
 */
public class Board {
    private final int size;
    private final Tile[][] grid;
    private final int startRow;
    private final int startCol;

    private final double inaccessibleRate;
    private final double marketRate;
    private final double commonRate;

    private int mainMonstersRemaining = 0;

    /**
     * Creates a board using the default PDF probabilities:
     * 20% inaccessible, 30% market, 50% common.
     * 
     * @param size dimension of the square grid
     */
    public Board(int size) {
        this(size, 0.20, 0.30, 0.50); // 20% inaccessible, 30% market, 50% common
    }

    /**
     * Creates a board with fully customizable tile generation probabilities.
     * Initializes the grid, generates all tiles, and sets the (0,0) start tile
     * as a safe COMMON tile with no monster.
     * 
     * @param size             board dimension
     * @param inaccessibleRate probability of INACCESSIBLE tiles
     * @param marketRate       probability of MARKET tiles
     * @param commonRate       probability of COMMON tiles
     */
    public Board(int size, double inaccessibleRate, double marketRate, double commonRate) {
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

    /**
     * Randomly generates each tile of the board based on the configured
     * probability rates for INACCESSIBLE, MARKET, and COMMON tiles.
     */
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
                } else {
                    type = TileType.COMMON;
                }

                grid[r][c] = new Tile(type);
            }
        }
    }

    /**
     * Checks if the given coordinates are within the board boundaries.
     * 
     * @param r row index
     * @param c column index
     * @return true if (r,c) is inside the grid, false otherwise
     */
    public boolean inBounds(int r, int c) {
        return r >= 0 && r < size && c >= 0 && c < size;
    }

    /**
     * Returns the tile at the given coordinates.
     * 
     * @param r row index
     * @param c column index
     * @return tile at (r,c)
     */
    public Tile getTile(int r, int c) {
        return grid[r][c];
    }

    /** @return board size (dimension of grid) */
    public int getSize() {
        return size;
    }

    /** @return starting row coordinate for the party */
    public int getStartRow() {
        return startRow;
    }

    /** @return starting column coordinate for the party */
    public int getStartCol() {
        return startCol;
    }

    /**
     * Registers a visible “main monster” at the given tile.
     * Only valid on COMMON tiles without an existing monster.
     * 
     * @param r row index
     * @param c column index
     * @return true if registration succeeded, false otherwise
     */
    public boolean registerMainMonsterAt(int r, int c) {
        Tile t = grid[r][c];

        // must be common tile & empty
        if (t.getType() == TileType.COMMON && !t.hasMonster()) {
            t.setMonster(true);
            mainMonstersRemaining++;
            return true;
        }
        return false;
    }

    /**
     * Registers a main monster without placing it on the map.
     * Used when monsters are generated implicitly and not tied to a tile.
     * Increments only the counter.
     */
    public void registerMainMonster() {
        mainMonstersRemaining++;
    }

    /**
     * Called when a main monster is defeated.
     * Decrements the total visible monster count but never drops below 0.
     */
    public void mainMonsterDefeated() {
        if (mainMonstersRemaining > 0) {
            mainMonstersRemaining--;
        }
    }

    /**
     * @return number of remaining main monsters on the map
     */
    public int getMonstersRemaining() {
        return mainMonstersRemaining;
    }

    /**
     * @return true only when all registered main monsters are cleared
     */
    public boolean allMonstersCleared() {
        return mainMonstersRemaining == 0;
    }
}

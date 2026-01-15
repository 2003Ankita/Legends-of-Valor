
package Board;

import general.Market;

/**
 * Represents a single tile on the game board.
 * A tile has:
 * - A TileType (COMMON, MARKET, INACCESSIBLE)
 * - An optional Market (only on MARKET tiles)
 * - A flag indicating whether a visible main monster occupies the tile
 * Used by the Board and GameController to manage movement,
 * markets, and monster encounters.
 */
public class Tile {
    private TileType type;
    private Market market;
    private boolean hasMonster; // visible 👹 on this tile?

    /**
     * Creates a tile of a specific type.
     * By default, a tile has no monster and no market.
     * 
     * @param type the tile type (COMMON, MARKET, INACCESSIBLE)
     */
    public Tile(TileType type) {
        this.type = type;
        this.hasMonster = false;

    }

    /**
     * @return the tile's type
     */
    public TileType getType() {
        return type;
    }

    /**
     * Updates the tile type.
     * Used when the starting tile is forced to become COMMON.
     * 
     * @param type the new TileType
     */
    public void setType(TileType type) {
        this.type = type;
    }

    /**
     * Returns the market associated with this tile (if any).
     * Only MARKET tiles should have a market.
     * 
     * @return the market on this tile or null
     */
    public Market getMarket() {
        return market;
    }

    /**
     * Assigns a market to this tile.
     * Markets appear only on MARKET tiles.
     * 
     * @param market the market instance
     */
    public void setMarket(Market market) {
        this.market = market;
    }

    /**
     * @return true if the tile currently has a visible monster
     */
    public boolean hasMonster() {
        return hasMonster;
    }

    /**
     * Sets whether this tile currently holds a visible monster.
     * Main monsters are placed at game start; random battles can
     * temporarily set this flag as well.
     * 
     * @param hasMonster true if a monster occupies the tile
     */
    public void setMonster(boolean hasMonster) {
        this.hasMonster = hasMonster;
    }
}

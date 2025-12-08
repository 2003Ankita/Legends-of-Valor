package Board;

/**
 * Represents the different types of tiles that can appear on the game board.
 * INACCESSIBLE — Cannot be entered by the party
 * MARKET — Allows heroes to buy and sell items
 * COMMON — Normal tile; may trigger random battles or hold main monsters
 */
public enum TileType {
    INACCESSIBLE, MARKET, COMMON
}

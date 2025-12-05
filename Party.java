import java.util.List;

/**
 * Represents the player's party of heroes.
 * Tracks:
 * - The list of heroes
 * - The party's position on the board
 * - Team-wide statistics such as total gold and highest level
 */
public class Party {
    private final List<Hero> heroes;
    private int row;
    private int col;

    /**
     * Creates a party with the specified list of heroes.
     * Party starts at position (0,0) by default.
     * @param heroes list of selected heroes
     */
    public Party(List<Hero> heroes) {
        this.heroes = heroes;
        this.row = 0;
        this.col = 0;
    }
    /** @return the list of heroes in the party */
    public List<Hero> getHeroes() {
        return heroes;
    }
    /** @return the party's current row on the board */
    public int getRow() {
        return row;
    }
    /** @return the party's current column on the board */
    public int getCol() {
        return col;
    }
    /**
     * Sets the party's position on the board.
     * @param r new row
     * @param c new column
     */
    public void setPosition(int r, int c) {
        this.row = r;
        this.col = c;
    }
    /**
     * Finds the highest level among all heroes in the party.
     * @return highest hero level (minimum is always 1)
     */
    public int getHighestLevel() {
        int max = 1;
        for (Hero h : heroes) {
            if (h.getLevel() > max) max = h.getLevel();
        }
        return max;
    }
    /**
     * Computes the total gold held by all heroes in the party.
     * @return sum of all hero gold
     */
    public double getTotalGold() {
        double sum = 0;
        for (Hero h : heroes) {
            sum += h.getGold();
        }
        return sum;
    }
    /**
     * Checks whether all heroes have fainted.
     * If even one hero is alive, the party is still active.
     * @return true if every hero has 0 HP, false otherwise
     */
    public boolean allFainted() {
        for (Hero h : heroes) {
            if (h.isAlive()) return false;
        }
        return true;
    }
    /**
     * Returns a concise summary string showing each hero's HP/MP,
     * plus overall party level and total gold.
     * Used by the UI after each movement step.
     * @return formatted one-line party summary
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            if (i > 0) sb.append(" | ");
            sb.append(h.getName())
                    .append(" HP ").append((int) h.getHp())
                    .append(" MP ").append((int) h.getMana());
        }
        sb.append(" | Party level ").append(getHighestLevel());
        sb.append(" | Gold ").append((int) getTotalGold());
        return sb.toString();
    }
}

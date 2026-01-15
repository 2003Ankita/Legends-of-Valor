package Heroes;

import java.util.List;

/**
 * Represents the player's party of heroes.
 * Tracks:
 *  - Party's shared position on the board
 *  - List of heroes
 *  - Highest level
 *  - Gold total
 *  - Alive/fainted status
 */
public class Party {

    private final List<Hero> heroes;
    private int row;
    private int col;

    public Party(List<Hero> heroes) {
        this.heroes = heroes;
        this.row = 0;
        this.col = 0;
    }

    /* ================= POSITION ================= */
    public int getRow() { return row; }
    public int getCol() { return col; }

    public void setPosition(int r, int c) {
        this.row = r;
        this.col = c;
    }

    /* ================= HERO LIST ================= */
    public List<Hero> getHeroes() {
        return heroes;
    }

    /* ================= PARTY STATS ================= */
    public int getHighestLevel() {
        int max = 1;
        for (Hero h : heroes) {
            max = Math.max(max, h.getLevel());
        }
        return max;
    }

    public double getTotalGold() {
        double sum = 0;
        for (Hero h : heroes)
            sum += h.getGold();
        return sum;
    }

    public boolean allFainted() {
        for (Hero h : heroes)
            if (h.isAlive())
                return false;
        return true;
    }

    public String getSummary() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            if (i > 0) sb.append(" | ");
            sb.append("🧙 ").append(h.getName())
                    .append(" | ❤️ HP ").append((int) h.getHp())
                    .append(" | 🔮 MP ").append((int) h.getMana());
        }

        sb.append(" | ⭐ Party Level ").append(getHighestLevel());
        sb.append(" | 💰 Total Gold ").append((int) getTotalGold());

        return sb.toString();
    }
}

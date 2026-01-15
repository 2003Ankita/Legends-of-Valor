package Heroes;

/**
 * Represents a Paladin hero.
 * Paladins gain balanced attribute increases when leveling up,
 * but receive an additional +5% bonus to Strength and Dexterity
 * (their favored stats).
 */
public class Paladin extends Hero {

    /**
     * Constructs a Paladin with the given attributes.
     *
     * @param name      hero name
     * @param level     starting level
     * @param mana      starting mana
     * @param strength  base strength value
     * @param dexterity base dexterity value
     * @param agility   base agility value
     * @param gold      starting gold
     */
    public Paladin(String name, int level, double mana,
            double strength, double dexterity, double agility,
            double gold) {
        super(name, level, mana, strength, dexterity, agility, gold);
    }

    /**
     * Levels up the Paladin.
     * Stat scaling rules:
     * - Base +5% to Strength, Dexterity, Agility
     * - Additional +5% to Strength and Dexterity (favored stats)
     * - HP resets to (level × 100)
     * - Mana increases by 10%
     */
    @Override
    public void levelUp() {
        level++;

        strength *= 1.05;
        dexterity *= 1.05;
        agility *= 1.05;

        // Favored +5%
        strength *= 1.05;
        dexterity *= 1.05;

        hp = level * 100;
        mana *= 1.1;
    }

    /**
     * Creates a deep copy of this Paladin.
     * Used in hero selection to avoid shared references.
     * 
     * @return a new Paladin with identical stats
     */
    @Override
    protected Hero copyInternal() {
        return new Paladin(name, level, mana, strength, dexterity, agility, gold);
    }
}

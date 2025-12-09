package Heroes;

/**
 * Represents a Warrior hero.
 * Warriors specialize in physical combat and gain extra bonuses
 * to Strength and Agility when leveling up.
 */
public class Warrior extends Hero {

    /**
     * Creates a Warrior with the specified attributes.
     * 
     * @param name      hero name
     * @param level     starting level
     * @param mana      starting mana
     * @param strength  base strength value
     * @param dexterity base dexterity value
     * @param agility   base agility value
     * @param gold      starting gold
     */
    public Warrior(String name, int level, double mana,
            double strength, double dexterity, double agility,
            double gold) {
        super(name, level, mana, strength, dexterity, agility, gold);
    }

    /**
     * Levels up the Warrior.
     * Stat scaling rules:
     * - Base +5% to Strength, Dexterity, Agility
     * - Additional +5% to Strength and Agility (favored stats)
     * - HP resets to (level × 100)
     * - Mana increases by 10%
     */
    @Override
    public void levelUp() {
        level++;

        // Increase all skills by 5%
        strength *= 1.05;
        dexterity *= 1.05;
        agility *= 1.05;

        // Favored skills +5% extra
        strength *= 1.05;
        agility *= 1.05;

        // Reset HP and MP as per PDF
        hp = level * 100;
        mana *= 1.1;
    }

    /**
     * Produces a deep copy of this Warrior.
     * Ensures party selection does not share references.
     * 
     * @return a new Warrior with identical stats
     */
    @Override
    protected Hero copyInternal() {
        return new Warrior(name, level, mana, strength, dexterity, agility, gold);
    }
}

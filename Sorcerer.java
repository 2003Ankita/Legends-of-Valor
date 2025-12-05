/**
 * Represents a Sorcerer hero.
 * Sorcerers excel in spellcasting and receive extra bonuses
 * to Dexterity and Agility when leveling up.
 */
public class Sorcerer extends Hero {


    /**
     * Creates a Sorcerer with the given attributes.
     *
     * @param name      hero name
     * @param level     starting level
     * @param mana      starting mana
     * @param strength  base strength value
     * @param dexterity base dexterity value
     * @param agility   base agility value
     * @param gold      starting gold
     */
    public Sorcerer(String name, int level, double mana,
                    double strength, double dexterity, double agility,
                    double gold) {
        super(name, level, mana, strength, dexterity, agility, gold);
    }

    /**
     * Levels up the Sorcerer.
     * Stat scaling rules:
     * - Base +5% to Strength, Dexterity, Agility
     * - Additional +5% to Dexterity & Agility (favored stats)
     * - HP resets to (level × 100)
     * - Mana increases by 10%
     */
    @Override
    public void levelUp() {
        level++;

        // Base +5%
        strength *= 1.05;
        dexterity *= 1.05;
        agility *= 1.05;

        // Favored +5%: Only Dexterity & Agility
        dexterity *= 1.05;
        agility *= 1.05;

        hp = level * 100;
        mana *= 1.1;
    }


    /**
     * Creates a deep copy of this Sorcerer.
     * Used when forming a party so no hero instance is shared.
     * @return a new Sorcerer with identical stats
     */
    @Override
    protected Hero copyInternal() {
        return new Sorcerer(name, level, mana, strength, dexterity, agility, gold);
    }
}

/**
 * Represents a Spirit-type monster.
 * Spirits typically excel in dodge chance and agility-based evasion.
 * Inherits all basic monster attributes and behaviors from Monster.
 */
public class Spirit extends Monster {

    /**
     * Creates a Spirit with the given stats.
     * @param name         monster name
     * @param level        monster level
     * @param damage       base damage output
     * @param defense      monster defense value
     * @param dodgeChance  dodge percentage from the PDF (converted internally)
     */
    public Spirit(String name, int level,
                  double damage, double defense, double dodgeChance) {
        super(name, level, damage, defense, dodgeChance);
    }

    /**
     * Produces a deep copy of this Spirit instance.
     * @return a new Spirit object with identical stats
     */
    @Override
    public Monster copy() {
        return new Spirit(name, level, damage, defense, dodgeChance);
    }
}

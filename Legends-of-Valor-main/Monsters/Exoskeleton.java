package Monsters;

/**
 * Represents an Exoskeleton monster type.
 * Exoskeletons are high-defense monsters and inherit base stats
 * and behaviors from the Monster class. This class exists to
 * allow type-specific effects or extensions in the future.
 */
public class Exoskeleton extends Monster {

    /**
     * Creates a new Exoskeleton monster.
     * The last parameter {@code v} appears unused but is kept
     * for compatibility with the data tables.
     * 
     * @param name        monster name
     * @param level       monster level
     * @param damage      base damage output
     * @param defense     defense value that reduces incoming damage
     * @param dodgeChance probability to dodge attacks (0–1)
     * @param v           unused attribute included for data-source consistency
     */
    public Exoskeleton(String name, int level, double damage,
            double defense, double dodgeChance, double v) {
        super(name, level, damage, defense, dodgeChance);
    }

    /**
     * Creates and returns an exact duplicate of this Exoskeleton instance.
     * The final parameter {@code 0.20} is used as a placeholder for the
     * unused constructor argument.
     * 
     * @return a new Exoskeleton object with identical stats
     */
    @Override
    public Monster copy() {
        return new Exoskeleton(name, level, damage, defense, dodgeChance, 0.20);
    }
}

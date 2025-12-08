package Monsters;

/**
 * Represents a Dragon monster type.
 * Dragons inherit all general Monster stats and behavior,
 * but exist as a separate class for classification or
 * type-specific future extensions.
 */
public class Dragon extends Monster {

    /**
     * Creates a new Dragon with the given attributes.
     * 
     * @param name        dragon's name
     * @param level       monster level
     * @param damage      base damage dealt by the dragon
     * @param defense     defense value reducing incoming damage
     * @param dodgeChance probability of dodging attacks (0–1)
     */
    public Dragon(String name, int level,
            double damage, double defense, double dodgeChance) {
        super(name, level, damage, defense, dodgeChance);
    }

    /**
     * Creates and returns a deep copy of this Dragon instance.
     * 
     * @return a new Dragon with identical stats
     */
    @Override
    public Monster copy() {
        return new Dragon(name, level, damage, defense, dodgeChance);
    }
}

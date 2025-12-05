/**
 * Represents an armor item that provides damage reduction to the player.
 * Extends the base Item class to include armor-specific attributes.
 */
public class Armor extends Item {

    /** Percentage or flat value of damage reduction provided by this armor. */
    private double damageReduction;

    /**
     * Constructs an Armor object with the given attributes.
     * @param name           the name of the armor
     * @param price          the cost of the armor
     * @param levelRequired  the minimum level required to equip the armor
     * @param damageReduction the damage reduction this armor provides
     */
    public Armor(String name, int price, int levelRequired, double damageReduction) {
        super(name, price, levelRequired);
        this.damageReduction = damageReduction;
    }

    /**
     * Returns the amount of damage reduction provided by this armor.
     * @return the damage reduction value
     */
    public double getDamageReduction() {
        return damageReduction;
    }

    /**
     * Returns a formatted string containing armor information, including
     * the base item info and its damage reduction.
     * @return a string describing this armor
     */
    @Override
    public String info() {
        return super.info() + " reduction " + (int)damageReduction;
    }

    /**
     * Creates and returns a deep copy of this Armor instance.
     * @return a new Armor object with the same properties
     */
    @Override
    public Item copy() {
        return new Armor(name, price, levelRequired, damageReduction);
    }
}

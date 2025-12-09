package Items;

/**
 * Represents a weapon item that a hero can equip.
 * Weapons increase a hero's physical attack damage and may require
 * one or two hands to wield.
 * Extends the base Item class.
 */
public class Weapon extends Item {
    private double damage;
    private int handsRequired;

    /**
     * Creates a Weapon with the given attributes.
     * 
     * @param name          weapon name
     * @param price         cost of the weapon
     * @param levelRequired minimum hero level required to equip
     * @param damage        base physical damage provided by the weapon
     * @param handsRequired number of hands needed (1 or 2)
     */
    public Weapon(String name, int price, int levelRequired, double damage, int handsRequired) {
        super(name, price, levelRequired);
        this.damage = damage;
        this.handsRequired = handsRequired;
    }

    /** @return the base damage this weapon adds to a hero's attack */
    public double getDamage() {
        return damage;
    }

    /** @return number of hands required to use this weapon (1 or 2) */
    public int getHandsRequired() {
        return handsRequired;
    }

    public int getLevelRequirement() {
        return levelRequired;
    }

    /**
     * Returns formatted weapon information including:
     * - name, price, level requirement (from Item)
     * - weapon damage
     * - hands required to wield
     * 
     * @return formatted string describing the weapon
     */
    @Override
    public String info() {
        return super.info() + " dmg " + (int) damage + " hands " + handsRequired;
    }

    /**
     * Produces a deep copy of this weapon.
     * 
     * @return identical Weapon object
     */
    @Override
    public Item copy() {
        return new Weapon(name, price, levelRequired, damage, handsRequired);
    }
}

package Items;

/**
 * Represents a magical spell item that heroes can cast in battle.
 * Each spell has:
 * - A spell type (ICE, FIRE, LIGHTNING)
 * - Base damage
 * - Mana cost required to cast
 *
 * Extends the base Item class.
 */

public class Spell extends Item {
    private SpellType type;
    private double damage;
    private double manaCost;

    /**
     * Creates a Spell with the given attributes.
     *
     * @param name          spell name
     * @param price         purchase cost
     * @param levelRequired minimum hero level required to buy/use the spell
     * @param type          the spell's elemental type
     * @param damage        base damage dealt by the spell
     * @param manaCost      mana required to cast the spell
     */
    public Spell(String name, int price, int levelRequired, SpellType type, double damage, double manaCost) {
        super(name, price, levelRequired);
        this.type = type;
        this.damage = damage;
        this.manaCost = manaCost;
    }

    /** @return the elemental type of the spell */
    public SpellType getType() {
        return type;
    }

    /** @return base spell damage */
    public double getDamage() {
        return damage;
    }

    /** @return mana consumption required to cast this spell */
    public double getManaCost() {
        return manaCost;
    }

    /**
     * Returns formatted spell information including:
     * - name/price/level from Item
     * - spell type
     * - spell damage
     * - mana cost
     * 
     * @return formatted string describing the spell
     */
    @Override
    public String info() {
        return super.info()
                + " | 🔮 Type " + type
                + " | 🔥 Dmg " + (int) damage
                + " | 🧬 Mana " + (int) manaCost;
    }

    /**
     * Produces a deep copy of this spell.
     * 
     * @return identical Spell object
     */
    @Override
    public Item copy() {
        return new Spell(name, price, levelRequired, type, damage, manaCost);
    }
}

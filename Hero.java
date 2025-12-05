/**
 * Abstract base class representing all hero types (Warrior, Sorcerer, Paladin, etc.).
 * Extends Character and adds hero-specific stats such as mana, experience,
 * attributes (strength, dexterity, agility), inventory, equipment,
 * and level-up mechanics.
 */
public abstract class Hero extends Character {
    protected double mana;
    protected int experience;
    protected double strength;
    protected double dexterity;
    protected double agility;
    protected double gold;
    protected Inventory inventory;
    protected Weapon weapon;
    protected Armor armor;

    /**
     * Creates a new hero with the given base stats.
     * HP is automatically set to (level * 100).
     * @param name      hero name
     * @param level     starting level
     * @param mana      starting mana
     * @param strength  base strength stat
     * @param dexterity base dexterity stat (spell/accuracy related)
     * @param agility   base agility stat (dodge related)
     * @param gold      starting gold
     */

    public Hero(String name, int level, double mana,
                double strength, double dexterity, double agility,
                double gold) {
        super(name, level, level * 100);
        this.mana = mana;
        this.strength = strength;
        this.dexterity = dexterity;
        this.agility = agility;
        this.gold = gold;
        this.inventory = new Inventory();
    }

    public double getMana() {
        return mana;
    }

    /**
     * Reduces the hero's mana by a given amount.
     * Mana cannot fall below zero.
     * @param amount mana consumed
     */
    public void reduceMana(double amount) {
        mana -= amount;
        if (mana < 0) mana = 0;
    }
    /** @return hero's strength stat */
    public double getStrength() {
        return strength;
    }
    /** @return hero's dexterity stat */
    public double getDexterity() {
        return dexterity;
    }
    /** @return hero's agility stat */
    public double getAgility() {
        return agility;
    }

    /** @return hero's current gold */
    public double getGold() {
        return gold;
    }

    /**
     * Adds gold to the hero's total.
     * @param amount gold gained
     */
    public void addGold(double amount) {
        gold += amount;
    }


    /**
     * Removes gold from the hero.
     * Cannot reduce gold below zero.
     *
     * @param amount gold spent
     */
    public void removeGold(double amount) {
        gold -= amount;
        if (gold < 0) gold = 0;
    }


    /**
     * @return the hero's dodge chance based on agility
     */
    public double getDodgeChance() {
        return agility * 0.002;
    }

    /** @return the hero’s inventory */
    public Inventory getInventory() {
        return inventory;
    }
    /** @return currently equipped weapon (may be null) */
    public Weapon getWeapon() {
        return weapon;
    }
    /** @return currently equipped armor (may be null) */
    public Armor getArmor() {
        return armor;
    }
    /**
     * Equips a weapon.
     *
     * @param w weapon to equip
     */
    public void equipWeapon(Weapon w) {
        this.weapon = w;
    }
    /**
     * Equips armor.
     *
     * @param a armor to equip
     */
    public void equipArmor(Armor a) {
        this.armor = a;
    }

    /**
     * Increases the hero's experience and handles automatic leveling.
     * When experience exceeds (level * 10), the hero levels up and
     * excess exp carries over.
     *
     * @param exp gained experience
     */
    public void gainExperience(int exp) {
        experience += exp;
        while (experience >= level * 10) {
            experience -= level * 10;
            levelUp();
        }
    }


    /**
     * Each hero type (Warrior, Sorcerer, Paladin) defines its own
     * stat-scaling rules inside this method.
     */
    public abstract void levelUp();

    /**
     * Returns concise hero information for menus or selection screens.
     * @return formatted short info string
     */
    public String shortInfo() {
        return name + " (lvl " + level + ", HP " + (int)hp + ", MP " + (int)mana + ", gold " + (int)gold + ")";
    }

    /**
     * Returns full hero stats including attributes.
     * @return detailed hero stat summary
     */
    public String fullInfo() {
        return name + " lvl " + level +
                " HP " + (int)hp +
                " MP " + (int)mana +
                " STR " + (int)strength +
                " DEX " + (int)dexterity +
                " AGI " + (int)agility +
                " Gold " + (int)gold;
    }

    /**
     * Returns minimal battle info (HP/MP only).
     * @return battle summary string
     */
    public String battleInfo() {
        return name + " HP " + (int)hp + " MP " + (int)mana;
    }

    /**
     * Creates a deep copy of this hero using the subclass-specific
     * copyInternal() method.
     * @return a duplicated hero object
     */
    public Hero copy() {
        Hero h = copyInternal();
        return h;
    }
    /**
     * Implemented by each subclass to clone the specific hero type.
     * @return a new instance with the same stats
     */
    protected abstract Hero copyInternal();
}

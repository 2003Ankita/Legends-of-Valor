package Items;

import Heroes.HeroStat;

/**
 * Represents a consumable potion item that boosts a specific hero stat
 * such as HP, Mana, Strength, Dexterity, or Agility.
 * Extends the base Item class.
 */
public class Potion extends Item {
    private HeroStat stat;
    private double amount;

    /**
     * Creates a Potion with the given attributes.
     * 
     * @param name          potion name
     * @param price         purchase cost
     * @param levelRequired minimum hero level required to use the potion
     * @param stat          which hero stat this potion increases
     * @param amount        the amount of stat increase provided
     */
    public Potion(String name, int price, int levelRequired, HeroStat stat, double amount) {
        super(name, price, levelRequired);
        this.stat = stat;
        this.amount = amount;
    }

    /**
     * @return the stat that this potion increases
     */
    public HeroStat getStat() {
        return stat;
    }

    /**
     * @return the numerical amount of stat increase
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns text describing the potion, including its stat bonus.
     * 
     * @return formatted potion info string
     */
    @Override
    public String info() {
        return super.info() + " +" + (int) amount + " " + stat;
    }

    /**
     * Produces a deep copy of this potion.
     * 
     * @return a new Potion instance with identical data
     */
    @Override
    public Item copy() {
        return new Potion(name, price, levelRequired, stat, amount);
    }
}

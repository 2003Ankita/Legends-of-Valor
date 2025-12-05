/**
 * Abstract base class for all game items such as Weapons, Armor,
 * Potions, and Spells.
 * Each item has:
 * - A name
 * - A price (used for buying/selling)
 * - A minimum hero level required to use/purchase
 * Subclasses must implement a copy() method to produce
 * independent item duplicates.
 */
public abstract class Item {
    protected String name;
    protected int price;
    protected int levelRequired;


    /**
     * Constructs a basic item with name, price, and level requirement.
     * @param name           item name
     * @param price          cost of the item
     * @param levelRequired  minimum hero level needed to use/buy
     */
    public Item(String name, int price, int levelRequired) {
        this.name = name;
        this.price = price;
        this.levelRequired = levelRequired;
    }


    /** @return the name of the item */
    public String getName() {
        return name;
    }
    /** @return the gold price of the item */
    public int getPrice() {
        return price;
    }
    /** @return the minimum hero level required to equip/use the item */
    public int getLevelRequired() {
        return levelRequired;
    }

    /**
     * Returns simple, formatted details about the item including:
     * - name
     * - level required
     * - price
     * @return formatted item info string
     */
    public String info() {
        return name + " (lvl " + levelRequired + ", price " + price + ")";
    }
    /**
     * Creates a deep copy of the item.
     * Implemented individually by each concrete item subclass.
     * @return an independent duplicate of the item
     */
    public abstract Item copy();
}


import java.util.ArrayList;
import java.util.List;

/**
 * Represents a market located on a tile. A market holds a list of items
 * that heroes can buy or sell. Handles buying/selling logic including
 * level requirements and gold checks.
 */
public class Market {
    private List<Item> stock;

    /**
     * Creates a market with an initial list of items.
     * A defensive copy of the list is stored internally.
     * @param stock list of items available for sale
     */
    public Market(List<Item> stock) {
        this.stock = new ArrayList<>(stock);
    }

    /**
     * Returns the list of all items currently in the market.
     * @return list of market stock items
     */
    public List<Item> getStock() {
        return stock;
    }

    /**
     * Processes a purchase attempt by a hero.
     * A hero can buy the item only if:
     * - The item is still in stock
     * - The hero meets the required level
     * - The hero has enough gold
     * On success:
     * - Gold is deducted
     * - Item is added to hero inventory
     * - Item is removed from market
     * @param hero the hero attempting the purchase
     * @param item the item to buy
     * @return true if purchase successful, false otherwise
     */
    public boolean buy(Hero hero, Item item) {
        if (!stock.contains(item)) return false;
        if (hero.getLevel() < item.getLevelRequired()) return false;
        if (hero.getGold() < item.getPrice()) return false;
        hero.removeGold(item.getPrice());
        hero.getInventory().add(item);
        stock.remove(item);
        return true;
    }

    /**
     * Processes selling an item from a hero to the market.
     * A hero can sell only items that exist in their inventory.
     * On success:
     * - Item is removed from hero inventory
     * - Hero receives 50% of item's price
     * - Item is added to market stock
     * @param hero the hero selling the item
     * @param item the item to sell
     * @return true if sold successfully, false otherwise
     */
    public boolean sell(Hero hero, Item item) {
        if (!hero.getInventory().getAllItems().contains(item)) return false;
        hero.getInventory().remove(item);
        hero.addGold(item.getPrice() / 2.0);
        stock.add(item);
        return true;
    }
}

package general;

import java.util.ArrayList;
import java.util.List;

import Items.Armor;
import Items.Item;
import Items.Potion;
import Items.Spell;
import Items.Weapon;

/**
 * Represents a hero's inventory, which stores all owned items.
 * Supports adding/removing items and retrieving items by specific type
 * (Weapons, Armors, Potions, Spells).
 */
public class Inventory {
    private List<Item> items = new ArrayList<>();

    /**
     * Adds an item to the inventory.
     * 
     * @param item the item to add
     */
    public void add(Item item) {
        items.add(item);
    }

    /**
     * Removes an item from the inventory.
     * If the item is not present, nothing happens.
     * 
     * @param item the item to remove
     */
    public void remove(Item item) {
        items.remove(item);
    }

    /**
     * Returns a defensive copy of all items in the inventory.
     * 
     * @return list of all items owned by the hero
     */
    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    /**
     * Retrieves all Weapon-type items from the inventory.
     * 
     * @return list of weapons
     */
    public List<Weapon> getWeapons() {
        List<Weapon> res = new ArrayList<>();
        for (Item i : items)
            if (i instanceof Weapon)
                res.add((Weapon) i);
        return res;
    }

    /**
     * Retrieves all Armor-type items from the inventory.
     * 
     * @return list of armors
     */
    public List<Armor> getArmors() {
        List<Armor> res = new ArrayList<>();
        for (Item i : items)
            if (i instanceof Armor)
                res.add((Armor) i);
        return res;
    }

    /**
     * Retrieves all Potion-type items from the inventory.
     * 
     * @return list of potions
     */
    public List<Potion> getPotions() {
        List<Potion> res = new ArrayList<>();
        for (Item i : items)
            if (i instanceof Potion)
                res.add((Potion) i);
        return res;
    }

    /**
     * Retrieves all Spell-type items from the inventory.
     * 
     * @return list of spells
     */
    public List<Spell> getSpells() {
        List<Spell> res = new ArrayList<>();
        for (Item i : items)
            if (i instanceof Spell)
                res.add((Spell) i);
        return res;
    }
}

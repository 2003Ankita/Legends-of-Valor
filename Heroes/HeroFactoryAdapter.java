package Heroes;

import java.util.ArrayList;
import java.util.List;

import Items.Item;
import Monsters.Monster;
import general.DataLoader;

/**
 * Small adapter that exposes a single method to obtain the
 * full hero and monster pools from the existing DataLoader
 * helper class of Monsters & Heroes.
 */
public final class HeroFactoryAdapter {

    private HeroFactoryAdapter() {
    }
    private static final String SPELL_FILE = "spells.txt";
    /**
     * Loads and aggregates all hero types into a single collection.
     * Includes warriors, paladins, and sorcerers from their respective loaders.
     *
     * @return a list containing all heroes available in the game
     */
    public static List<Hero> loadAllHeroes() {
        List<Hero> result = new ArrayList<>();
        result.addAll(DataLoader.loadWarriors(null));
        result.addAll(DataLoader.loadPaladins(null));
        result.addAll(DataLoader.loadSorcerers(null));
        return result;
    }
    /**
     * Loads and aggregates all monster types into a single collection.
     * Includes dragons, exoskeletons, and spirits from their respective loaders.
     *
     * @return a list containing all monsters available in the game
     */
    public static List<Monster> loadAllMonsters() {
        List<Monster> result = new ArrayList<>();
        result.addAll(DataLoader.loadDragons(null));
        result.addAll(DataLoader.loadExoskeletons(null));
        result.addAll(DataLoader.loadSpirits(null));
        return result;
    }
    /**
     * Loads and aggregates all game items into a single collection.
     * Combines weapons, armors, potions, and spells from their respective loaders.
     * @return a list containing all available items in the game
     */
    public static List<Item> loadAllItems() {
        List<Item> result = new ArrayList<>();
        result.addAll(DataLoader.loadWeapons(null));
        result.addAll(DataLoader.loadArmors(null));
        result.addAll(DataLoader.loadPotions(null));
        result.addAll(DataLoader.loadSpells(SPELL_FILE));

        return result;
    }
}

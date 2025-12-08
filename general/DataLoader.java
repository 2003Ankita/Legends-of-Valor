package general;

import java.util.ArrayList;
import java.util.List;

import Heros.Hero;
import Heros.HeroStat;
import Heros.Paladin;
import Heros.Sorcerer;
import Heros.Warrior;
import Items.Armor;
import Items.Item;
import Items.Potion;
import Items.Spell;
import Items.SpellType;
import Items.Weapon;
import Monsters.Dragon;
import Monsters.Exoskeleton;
import Monsters.Monster;
import Monsters.Spirit;

/**
 * Utility class responsible for loading all game data:
 * heroes, monsters, weapons, armor, potions, and spells.
 * All methods return pre-constructed lists of objects
 * (acting like static "database tables" for the game).
 * This class cannot be instantiated.
 */
public final class DataLoader {

    private DataLoader() {
    }

    // ===================== HEROES =====================

    /**
     * Loads all Warrior heroes from hard-coded data.
     * 
     * @param ignored unused parameter (kept for API compatibility)
     * @return list of Warrior objects
     */
    public static List<Hero> loadWarriors(String ignored) {
        List<Hero> list = new ArrayList<>();

        list.add(new Warrior("Gaerdal_Ironhand", 1, 100, 700, 600, 500, 1354));
        list.add(new Warrior("Sehanine_Moonbow", 1, 600, 700, 500, 800, 2500));
        list.add(new Warrior("Muamman_Duathall", 1, 300, 900, 750, 500, 2546));
        list.add(new Warrior("Flandal_Steelskin", 1, 200, 750, 700, 650, 2500));
        list.add(new Warrior("Undefeated_Yoj", 1, 400, 800, 700, 400, 2500));
        list.add(new Warrior("Eunoia_Cyn", 1, 400, 700, 600, 800, 2500));

        return list;
    }

    /**
     * Loads all Sorcerer heroes.
     * 
     * @param ignored unused parameter
     * @return list of Sorcerer objects
     */
    public static List<Hero> loadSorcerers(String ignored) {
        List<Hero> list = new ArrayList<>();

        list.add(new Sorcerer("Rillifane_Rallathil", 1, 1300, 750, 500, 450, 2500));
        list.add(new Sorcerer("Segojan_Earthcaller", 1, 900, 800, 650, 500, 2500));
        list.add(new Sorcerer("Reign_Havoc", 1, 800, 800, 800, 800, 2500));
        list.add(new Sorcerer("Reverie_Ashels", 1, 900, 800, 400, 700, 2500));
        list.add(new Sorcerer("Kalabar", 1, 800, 850, 600, 400, 2500));
        list.add(new Sorcerer("Skye_Soar", 1, 1000, 700, 500, 400, 2500));

        return list;
    }

    /**
     * Loads all Paladin heroes.
     * 
     * @param ignored unused parameter
     * @return list of Paladin objects
     */
    public static List<Hero> loadPaladins(String ignored) {
        List<Hero> list = new ArrayList<>();

        list.add(new Paladin("Parzival", 1, 300, 750, 700, 650, 2500));
        list.add(new Paladin("Sehanine_Moonbow", 1, 300, 750, 700, 700, 2500));
        list.add(new Paladin("Skoraeus_Stonebones", 1, 250, 650, 350, 600, 2500));
        list.add(new Paladin("Garl_Glittergold", 1, 100, 600, 400, 500, 2500));
        list.add(new Paladin("Amaryllis_Astra", 1, 500, 500, 500, 500, 2500));
        list.add(new Paladin("Caliber_Heist", 1, 400, 400, 400, 400, 2500));

        return list;
    }

    // ===================== MONSTERS =====================

    /**
     * Loads all Dragon monsters.
     * 
     * @param ignored unused parameter
     * @return list of Dragon objects
     */
    public static List<Monster> loadDragons(String ignored) {
        List<Monster> list = new ArrayList<>();

        list.add(new Dragon("Desghidorrah", 3, 300, 400, 0.35));
        list.add(new Dragon("Chrysophylax", 2, 200, 500, 0.20));
        list.add(new Dragon("BunsenBurner", 4, 400, 500, 0.45));
        list.add(new Dragon("Natsunomeryu", 1, 100, 200, 0.10));
        list.add(new Dragon("TheScaleless", 7, 700, 600, 0.75));
        list.add(new Dragon("Kas-Ethelinh", 5, 600, 500, 0.60));
        list.add(new Dragon("Alexstraszan", 10, 1000, 900, 0.55));
        list.add(new Dragon("Phaarthurnax", 6, 600, 700, 0.60));
        list.add(new Dragon("D-Maleficent", 9, 900, 950, 0.85));
        list.add(new Dragon("TheWeatherbe", 8, 800, 900, 0.80));
        list.add(new Dragon("Igneel", 6, 600, 400, 0.60));
        list.add(new Dragon("BlueEyesWhite", 9, 900, 600, 0.75));

        return list;
    }

    /**
     * Loads all Spirit monsters.
     * 
     * @param ignored unused parameter
     * @return list of Spirit objects
     */
    public static List<Monster> loadSpirits(String ignored) {
        List<Monster> list = new ArrayList<>();

        list.add(new Spirit("Andrealphus", 2, 600, 500, 0.40));
        list.add(new Spirit("Blinky", 1, 450, 350, 0.35));
        list.add(new Spirit("Andromalius", 3, 550, 450, 0.25));
        list.add(new Spirit("Chiang-shih", 4, 700, 600, 0.40));
        list.add(new Spirit("FallenAngel", 5, 800, 700, 0.50));
        list.add(new Spirit("Ereshkigall", 6, 950, 450, 0.35));
        list.add(new Spirit("Melchiresas", 7, 350, 150, 0.75));
        list.add(new Spirit("Jormunngand", 8, 600, 900, 0.20));
        list.add(new Spirit("Rakkshasass", 9, 550, 600, 0.35));
        list.add(new Spirit("Taltecuhtli", 10, 300, 200, 0.50));
        list.add(new Spirit("Casper", 1, 100, 100, 0.50));

        return list;
    }

    /**
     * Loads all Exoskeleton monsters.
     * 
     * @param ignored unused parameter
     * @return list of Exoskeleton objects
     */
    public static List<Monster> loadExoskeletons(String ignored) {
        List<Monster> list = new ArrayList<>();

        list.add(new Exoskeleton("Cyrrollalee", 7, 700, 520, 700, 0.25));
        list.add(new Exoskeleton("Brandobaris", 3, 300, 250, 400, 0.20));
        list.add(new Exoskeleton("BigBad-Wolf", 1, 100, 150, 250, 0.15));
        list.add(new Exoskeleton("WickedWitch", 9, 900, 950, 900, 0.35));
        list.add(new Exoskeleton("Aasterinian", 5, 500, 350, 450, 0.20));
        list.add(new Exoskeleton("Chronepsish", 6, 600, 450, 550, 0.30));

        return list;
    }

    // ===================== WEAPONS =====================

    /**
     * Loads all Weapon-type items.
     * 
     * @param ignored unused parameter
     * @return list of Weapon items
     */
    public static List<Item> loadWeapons(String ignored) {
        List<Item> list = new ArrayList<>();

        list.add(new Weapon("Sword", 500, 1, 800, 1));
        list.add(new Weapon("Bow", 300, 2, 500, 2));
        list.add(new Weapon("Scythe", 1000, 6, 1100, 2));
        list.add(new Weapon("Axe", 550, 5, 850, 1));
        list.add(new Weapon("TSwords", 1400, 8, 1600, 2));
        list.add(new Weapon("Dagger", 200, 1, 250, 1));

        return list;
    }

    // ===================== ARMOR =====================

    /**
     * Loads all Armor-type items.
     * 
     * @param ignored unused parameter
     * @return list of Armor items
     */
    public static List<Item> loadArmors(String ignored) {
        List<Item> list = new ArrayList<>();

        list.add(new Armor("Platinum_Shield", 150, 1, 200));
        list.add(new Armor("Breastplate", 350, 3, 600));
        list.add(new Armor("Full_Body_Armor", 1000, 8, 1100));
        list.add(new Armor("Wizard_Shield", 1200, 10, 1500));
        list.add(new Armor("Guardian_Angel", 1000, 10, 1000));

        return list;
    }

    // ===================== POTIONS =====================
    /**
     * Loads all Potion items.
     * 
     * @param ignored unused parameter
     * @return list of Potion items
     */
    public static List<Item> loadPotions(String ignored) {
        List<Item> list = new ArrayList<>();

        list.add(new Potion("Healing_Potion", 250, 1, HeroStat.HP, 100));
        list.add(new Potion("Strength_Potion", 200, 1, HeroStat.STRENGTH, 75));
        list.add(new Potion("Magic_Potion", 350, 2, HeroStat.MANA, 100));
        list.add(new Potion("Luck_Elixir", 500, 4, HeroStat.AGILITY, 65));
        list.add(new Potion("Mermaid_Tears", 850, 5, HeroStat.HP, 100));
        list.add(new Potion("Ambrosia", 1000, 8, HeroStat.HP, 150));

        return list;
    }

    // ===================== SPELLS =====================

    /**
     * Loads spells based on the supplied filename.
     * If the filename contains "ice", "fire", or "lightning",
     * returns the appropriate spell list.
     * 
     * @param fileName name of the spell file
     * @return list of spells matching the category
     */
    public static List<Item> loadSpells(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.contains("ice"))
            return loadIceSpells();
        if (lower.contains("fire"))
            return loadFireSpells();
        if (lower.contains("lightning"))
            return loadLightningSpells();
        return new ArrayList<>();
    }

    /**
     * Loads Ice spells only.
     * 
     * @return list of ICE-type spells
     */
    private static List<Item> loadIceSpells() {
        List<Item> list = new ArrayList<>();

        list.add(new Spell("Snow_Cannon", 500, 2, SpellType.ICE, 650, 250));
        list.add(new Spell("Ice_Blade", 250, 1, SpellType.ICE, 450, 100));
        list.add(new Spell("Frost_Blizzard", 750, 5, SpellType.ICE, 850, 350));
        list.add(new Spell("Arctic_Storm", 700, 6, SpellType.ICE, 800, 300));

        return list;
    }

    /**
     * Loads Fire spells only.
     * 
     * @return list of FIRE-type spells
     */
    private static List<Item> loadFireSpells() {
        List<Item> list = new ArrayList<>();

        list.add(new Spell("Flame_Tornado", 700, 4, SpellType.FIRE, 850, 300));
        list.add(new Spell("Breath_of_Fire", 350, 1, SpellType.FIRE, 450, 100));
        list.add(new Spell("Heat_Wave", 450, 2, SpellType.FIRE, 600, 150));
        list.add(new Spell("Lava_Comet", 800, 7, SpellType.FIRE, 1000, 550));
        list.add(new Spell("Hell_Storm", 600, 3, SpellType.FIRE, 950, 600));

        return list;
    }

    /**
     * Loads Lightning spells only.
     * 
     * @return list of LIGHTNING-type spells
     */
    private static List<Item> loadLightningSpells() {
        List<Item> list = new ArrayList<>();

        list.add(new Spell("Lightning_Dagger", 400, 1, SpellType.LIGHTNING, 500, 150));
        list.add(new Spell("Thunder_Blast", 750, 4, SpellType.LIGHTNING, 950, 400));
        list.add(new Spell("Electric_Arrows", 550, 5, SpellType.LIGHTNING, 650, 200));
        list.add(new Spell("Spark_Needles", 500, 2, SpellType.LIGHTNING, 600, 200));

        return list;
    }

}

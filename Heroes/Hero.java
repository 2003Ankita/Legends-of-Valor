package Heroes;

import java.util.List;

import Items.Armor;
import Items.Spell;
import Items.Weapon;
import general.Character;
import general.Inventory;

/**
 * Base Hero class with support for:
 * - Tile bonuses (Bush/Koulou)
 * - Clean stat resets when leaving tiles
 * - Proper spell list usage
 */
public abstract class Hero extends Character {

    protected double mana;
    protected int experience;

    protected double strength;
    protected double dexterity;
    protected double agility;
    protected double gold;

    // base stats (for resetting after tile bonuses)
    public double baseStrength;
    public double baseDexterity;
    public double baseAgility;

    protected Inventory inventory;
    protected Weapon weapon;
    protected Armor armor;

    public Hero(String name, int level, double mana,
                double strength, double dexterity, double agility,
                double gold) {

        super(name, level, level * 100);

        this.mana = mana;
        this.strength = strength;
        this.dexterity = dexterity;
        this.agility = agility;

        this.baseStrength = strength;
        this.baseDexterity = dexterity;
        this.baseAgility = agility;

        this.gold = gold;
        this.inventory = new Inventory();
    }

    /* ---------------- Position removed ------------------
       Party holds the team's position, not individual heroes.
       ----------------------------------------------------- */

    public void setMana(double m) { this.mana = m; }
    public void setHP(double HP) { this.hp = HP; }
    public void setStrength(double s) { this.strength = s; }
    public void setDexterity(double d) { this.dexterity = d; }
    public void setAgility(double agility) { this.agility = agility; }

    public double getMana() { return mana; }
    public double getHP() { return hp; }

    public double getStrength() { return strength; }
    public double getDexterity() { return dexterity; }
    public double getAgility() { return agility; }
    public double getGold() { return gold; }

    public void addGold(double amount) { gold += amount; }
    public void removeGold(double amount) { gold = Math.max(0, gold - amount); }

    public double getDodgeChance() {
        return agility * 0.002;
    }

    public Inventory getInventory() { return inventory; }
    public Weapon getWeapon() { return weapon; }
    public Armor getArmor() { return armor; }

    public void equipWeapon(Weapon w) { this.weapon = w; }
    public void equipArmor(Armor a) { this.armor = a; }

    /* ---------------- Tile Effects ---------------- */

    // called when stepping on a Bush tile
    public void applyBushBonus() {
        this.dexterity = baseDexterity * 1.1;  // +10%
    }

    // called when stepping on a Koulou tile
    public void applyKoulouBonus() {
        this.strength = baseStrength * 1.1;    // +10%
    }

    // reset bonuses when leaving a tile
    public void resetTileBonuses() {
        this.strength = baseStrength;
        this.dexterity = baseDexterity;
        this.agility = baseAgility;
    }

    /* ---------------- Spells ---------------- */

    public List<Spell> getSpells() {
        return inventory.getSpells();  // FIX: use inventory spells
    }

    /* ---------------- Experience / Level Up ---------------- */

    public void gainExperience(int exp) {
        experience += exp;
        while (experience >= level * 10) {
            experience -= level * 10;
            levelUp();
        }
    }

    public abstract void levelUp();

    /* ---------------- Info ---------------- */

    public String shortInfo() {
        return "🧙 " + name + " | ⭐ Lvl " + level +
                " | ❤️ HP " + (int) hp +
                " | 🔮 MP " + (int) mana +
                " | 💰 Gold " + (int) gold;
    }

    public String fullInfo() {
        return "🧙 " + name +
                " | ⭐ Lvl " + level +
                " | ❤️ HP " + (int) hp +
                " | 🔮 MP " + (int) mana +
                " | ⚔️ STR " + (int) strength +
                " | 🏹 DEX " + (int) dexterity +
                " | 🤸 AGI " + (int) agility +
                " | 💰 Gold " + (int) gold;
    }

    public String battleInfo() {
        return "⚔️ " + name + " | ❤️ HP " + (int) hp + " | 🔮 MP " + (int) mana;
    }

    /* ---------------- Copy ---------------- */

    public Hero copy() {
        Hero h = copyInternal();
        return h;
    }
    public void reduceMana(double amount) {
        mana -= amount;
        if (mana < 0)
            mana = 0;
    }


    protected abstract Hero copyInternal();
}

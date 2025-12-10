
package Monsters;

import general.Character;

/**
 * Abstract base class representing all monster types (Dragons, Spirits,
 * Exoskeletons, etc.).
 * Extends Character and adds monster-specific stats:
 * damage, defense, and dodge chance.
 * Monsters follow PDF rules:
 * - HP = level × 100
 * - dodgeChance = input × 0.01
 */
public abstract class Monster extends Character {

    protected double damage;
    protected double defense;
    protected double dodgeChance;
    protected boolean isBoss = false;

    /**
     * Creates a monster with the given stats.
     * 
     * @param name        monster name
     * @param level       monster level
     * @param damage      base damage dealt
     * @param defense     defense that reduces incoming damage
     * @param dodgeChance dodge percentage from PDF (0–100), internally converted to
     *                    probability
     */
    public Monster(String name, int level, double damage, double defense, double dodgeChance) {
        super(name, level, level * 100); // HP = level × 100 (PDF rule)
        this.damage = damage;
        this.defense = defense;
        this.dodgeChance = dodgeChance * 0.01; // PDF: monster dodge = dodgeChance × .01
    }

    /** @return monster’s base damage */
    public double getDamage() {
        return damage;
    }

    /** @return monster’s defense value */
    public double getDefense() {

        return defense;
    }

    /** @return monster’s dodge probability (0–1) */
    public double getDodgeChance() {
        return dodgeChance;
    }

    /**
     * Reduces the monster’s damage by a percentage factor.
     * Example: factor = 0.10 means damage reduced by 10%.
     * 
     * @param factor percentage to reduce (0–1)
     */
    public void reduceDamage(double factor) {
        damage *= (1 - factor);
    }

    /**
     * Reduces the monster’s defense by a given factor.
     * 
     * @param factor percentage to reduce (0–1)
     */
    public void reduceDefense(double factor) {
        defense *= (1 - factor);
    }

    /**
     * Reduces the monster’s dodge chance by a given factor.
     * 
     * @param factor percentage to reduce (0–1)
     */
    public void reduceDodge(double factor) {
        dodgeChance *= (1 - factor);
    }

    /**
     * Returns a concise string showing monster stats for menus and summaries.
     * 
     * @return basic formatted monster info
     */
    public String info() {
        return name + " lvl " + level +
                " HP " + (int) hp +
                " DMG " + (int) damage +
                " DEF " + (int) defense +
                " DODGE " + (int) (dodgeChance * 100);
    }

    /**
     * Shows only battle-relevant details (HP).
     * 
     * @return battle HP summary
     */
    public String battleInfo() {
        return name + " HP " + (int) hp;
    }

    /**
     * Short, single-line summary used in monster selection menus.
     * 
     * @return formatted monster description
     */
    public String shortInfo() {
        return String.format(
                "%s (Lvl %d, HP %.0f, DMG %.0f, DEF %.0f, Dodge %.0f%%)",
                name, level, hp, damage, defense, dodgeChance * 100);
    }

    /**
     * Returns a full multi-line summary of all monster attributes.
     * 
     * @return detailed monster stats
     */
    public String fullInfo() {
        return "Name: " + name +
                "\nLevel: " + level +
                "\nHP: " + hp +
                "\nDamage: " + damage +
                "\nDefense: " + defense +
                "\nDodge Chance: " + (dodgeChance * 100) + "%" +
                "\n-----------------------";
    }

    public void SetDamage(double damage) {
        this.damage = damage;
    }

    public void SetHP(double HP) {
        this.hp = HP;
    }

    public void SetDefense(double defense) {
        this.defense = defense;
    }

    public void SetDodgeChance(double dodgeChance) {
        this.dodgeChance = dodgeChance;
    }

    /**
     * Produces a deep copy of the monster.
     * Implemented individually by each concrete monster type.
     * 
     * @return a duplicated monster instance
     */
    public abstract Monster copy();

    public boolean isBoss() {
        return isBoss;
    }

    public void setBoss(boolean boss) {
        this.isBoss = boss;
    }
}

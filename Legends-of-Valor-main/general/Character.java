package general;

/**
 * Base abstract class representing any character in the game
 * (heroes, monsters, NPCs, etc.). Provides shared attributes such
 * as name, level, HP, and common combat-related behaviors.
 */
public abstract class Character {
    protected String name;
    protected int level;
    protected double hp;

    /**
     * Constructs a character with the given name, level, and HP.
     * 
     * @param name  character's display name
     * @param level character level
     * @param hp    initial health points
     */
    public Character(String name, int level, double hp) {
        this.name = name;
        this.level = level;
        this.hp = hp;
    }

    /**
     * @return the character's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the character's current level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return the character's current HP
     */
    public double getHp() {
        return hp;
    }

    /**
     * Updates the character's level and resets HP accordingly.
     * HP is always set to (level * 100).
     * 
     * @param level new level value
     */
    public void setLevel(int level) {
        this.level = level;
        this.hp = level * 100;
    }

    /**
     * Lowers the character's HP by a specified amount.
     * HP cannot fall below 0.
     * 
     * @param dmg amount of damage taken
     */
    public void takeDamage(double dmg) {
        hp -= dmg;
        if (hp < 0)
            hp = 0;
    }

    /**
     * @return true if HP is greater than 0, otherwise character is fainted/dead
     */
    public boolean isAlive() {
        return hp > 0;
    }

    /**
     * Converts a lowercase ASCII letter to uppercase.
     * If the character is not a lowercase letter, returns it unchanged.
     * 
     * @param c character to convert
     * @return uppercase version if applicable, otherwise original character
     */
    public static char toUpperCase(char c) {
        if (c >= 'a' && c <= 'z') {
            return (char) (c - ('a' - 'A'));
        }
        return c;
    }
}

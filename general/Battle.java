package general;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import Board.Weather;
import Heroes.*;
import Items.*;
import Monsters.Monster;

/**
 * Handles the full turn-based battle system between a party of heroes and a
 * list of monsters.
 * Manages turn order, actions, damage calculation, rewards, and win/lose
 * conditions.
 */
public class Battle {
    private Party party;
    private List<Monster> monsters;
    private GameView view;
    private Scanner scanner;
    private Random random;
    private Weather currentWeather;
    private boolean isDay;
    private String playerName;

    public Battle(Party party, List<Monster> monsters,
            GameView view, Scanner scanner, Random random,
            Weather currentWeather, boolean isDay, String playerName) {

        this.party = party;
        this.monsters = monsters;
        this.view = view;
        this.scanner = scanner;
        this.random = random;
        this.currentWeather = currentWeather;
        this.isDay = isDay;
        this.playerName = playerName;

    }

    /**
     * Starts the battle loop. Alternates between hero turns and monster turns
     * until either all heroes faint or all monsters are defeated.
     * Shows battle status after every full round.
     * On victory, grants EXP and gold. On defeat, ends the game.
     */
    public void start() {
        while (!party.allFainted() && !allMonstersDead()) {
            heroesTurn();
            if (allMonstersDead())
                break;
            monstersTurn();
            // regenHeroes();
            view.showBattleStatus(party, monsters);
        }
        if (allMonstersDead()) {
            view.showMessage("Victory! Your heroes have defeated every enemy on the battlefield!");
            rewardHeroes();
        } else {
            view.showMessage("💀 " + playerName + ", your heroes have fallen in battle. The journey ends here.");
            System.exit(0);

        }
    }

    /**
     * Controls the full turn sequence for all heroes.
     * Each hero gets to act if alive and may choose between:
     * Attack, Cast Spell, Use Potion, Change Equipment, or View Info.
     * Ensures hero keeps choosing until a valid action is taken.
     */
    private void heroesTurn() {

        for (Hero h : party.getHeroes()) {
            if (!h.isAlive())
                continue;
            boolean done = false;
            while (!done) {
                view.showMessage("\\n⚔" + h.getName() + "'s turn — choose your action wisely.");
                view.showMessage("1) Attack an enemy");
                view.showMessage("2)Cast a powerful spell");
                view.showMessage("3) Use a potion");
                view.showMessage("4) Change Equipment");
                view.showMessage("5) Info : View battle information");
                view.showMessage("Choose an option (1–5):");

                int choice = readIntInRange(1, 5);
                switch (choice) {
                    case 1:
                        heroAttack(h);
                        done = true;
                        break;
                    case 2:
                        if (heroCastSpell(h))
                            done = true;
                        break;
                    case 3:
                        if (heroUsePotion(h))
                            done = true;
                        break;
                    case 4:
                        heroEquip(h);
                        break;
                    case 5:
                        view.showInfo(party, monsters);
                        break;
                }
            }
        }
    }

    /**
     * Executes a basic physical attack for a hero.
     * Damage depends on hero strength + weapon, reduced by monster defense.
     * Monster may dodge based on dodge chance.
     * 
     * @param h the acting hero
     */
    private void heroAttack(Hero h) {
        Monster m = chooseMonster();
        if (m == null)
            return;
        double base = (h.getStrength() + (h.getWeapon() != null ? h.getWeapon().getDamage() : 0)) * 0.05;
        if (random.nextDouble() < m.getDodgeChance()) {
            view.showMessage("💨 " + m.getName() + " swiftly dodged the attack!");
            return;
        }
        double dmg = Math.max(0, base - m.getDefense());
        m.takeDamage(dmg);
        view.showMessage("⚔️ " + h.getName() + " struck " + m.getName() + " for " + (int) dmg + " damage!");
    }

    /**
     * Allows the hero to cast a spell.
     * Validates mana, selects spell, selects target, applies damage,
     * and applies secondary spell effects (ice/fire/lightning debuffs).
     * 
     * @param h the acting hero
     * @return true if the hero successfully acted, false if hero must choose again
     */
    private boolean heroCastSpell(Hero h) {
        List<Spell> spells = h.getInventory().getSpells();
        if (spells.isEmpty()) {
            view.showMessage("No spells available. Choose a different action");
            return false;
        }

        view.showMessage("✨ Choose a spell to cast:");
        view.showMessage("Enter your choice:");

        for (int i = 0; i < spells.size(); i++) {
            view.showMessage((i + 1) + ") " + spells.get(i).info());
        }
        int choice = readIntInRange(1, spells.size());
        Spell spell = spells.get(choice - 1);
        if (h.getMana() < spell.getManaCost()) {
            view.showMessage("🔮 Not enough mana to cast that spell.");
            return false;
        }
        Monster m = chooseMonster();
        if (m == null)
            return false;
        if (random.nextDouble() < m.getDodgeChance()) {
            view.showMessage(m.getName() + " dodged the spell!");
            return true;
        }
        double weatherBonus = 1.0;

        switch (currentWeather) {
            case RAINY:
                if (spell.getType() == SpellType.LIGHTNING)
                    weatherBonus = 1.25;
                break;
            case SUNNY:
                if (spell.getType() == SpellType.FIRE)
                    weatherBonus = 1.25;
                break;
            case SNOWY:
                if (spell.getType() == SpellType.ICE)
                    weatherBonus = 1.25;
                break;
            case STORM:
                m.reduceDodge(0.1); // monsters slightly worse at dodging
                break;
        }

        double dmg = spell.getDamage() * weatherBonus * (1 + h.getDexterity() / 10000.0);

        dmg = Math.max(0, dmg - m.getDefense());
        m.takeDamage(dmg);
        h.reduceMana(spell.getManaCost());
        h.getInventory().remove(spell);
        switch (spell.getType()) {
            case ICE:
                m.reduceDamage(0.1);
                break;
            case FIRE:
                m.reduceDefense(0.1);
                break;
            case LIGHTNING:
                m.reduceDodge(0.1);
                break;
        }
        view.showMessage(
                h.getName() + " cast " + spell.getName() + " on " + m.getName() + " for " + (int) dmg + " damage.");
        return true;
    }

    /**
     * Hero uses a potion to recover HP/MP or buff a stat.
     * Removes potion from inventory after use.
     * 
     * @param h the acting hero
     * @return true if the potion was used successfully, false if must choose again
     */
    private boolean heroUsePotion(Hero h) {
        List<Potion> potions = h.getInventory().getPotions();
        if (potions.isEmpty()) {
            view.showMessage("🧪 No potions left. Choose another action.");
            return false;
        }
        view.showMessage("🧪 Choose a potion to use:");

        view.showMessage("Enter choice: "); // ADD

        for (int i = 0; i < potions.size(); i++) {
            view.showMessage((i + 1) + ") " + potions.get(i).info());
        }
        int choice = readIntInRange(1, potions.size());
        Potion p = potions.get(choice - 1);
        switch (p.getStat()) {
            case HP:
                h.setHP(h.getHp() + p.getAmount());
                break;
            case MP:
                h.setMana(h.getMana() + p.getAmount());
                break;
            case STRENGTH:
                h.setStrength(h.getStrength() + p.getAmount());
                break;
            case DEXTERITY:
                h.setDexterity(h.getDexterity() + p.getAmount());
                break;
            case AGILITY:
                h.setAgility(h.getAgility() + p.getAmount());
                break;
        }
        h.getInventory().remove(p);
        view.showMessage("✨ " + h.getName() + " used " + p.getName() + " and feels refreshed!");
        return true;
    }

    /**
     * Allows the hero to equip a weapon or armor.
     * Displays inventory items and updates equipment accordingly.
     * 
     * @param h the acting hero
     */
    private void heroEquip(Hero h) {
        view.showMessage("1) Equip weapon");
        view.showMessage("2) Equip armor");
        int choice = readIntInRange(1, 2);
        if (choice == 1) {
            List<Weapon> weapons = h.getInventory().getWeapons();
            if (weapons.isEmpty()) {
                view.showMessage("⚠️ No weapons available. Choose another option.");
                return;
            }
            view.showMessage("⚔️ Choose a weapon to equip:");
            for (int i = 0; i < weapons.size(); i++) {
                view.showMessage((i + 1) + ") " + weapons.get(i).info());
            }
            view.showMessage("Enter choice: "); // ADD
            int idx = readIntInRange(1, weapons.size()) - 1;
            h.equipWeapon(weapons.get(idx));
            view.showMessage("🗡️ Equipped " + weapons.get(idx).getName() + " successfully.");

        } else {
            List<Armor> armors = h.getInventory().getArmors();
            if (armors.isEmpty()) {
                view.showMessage("🛡️ No armor available. Choose another option.");
                return;
            }
            view.showMessage("🛡️ Choose armor to equip:");

            for (int i = 0; i < armors.size(); i++) {
                view.showMessage((i + 1) + ") " + armors.get(i).info());
            }
            view.showMessage("Enter choice: "); // ADD

            int idx = readIntInRange(1, armors.size()) - 1;
            h.equipArmor(armors.get(idx));
            view.showMessage("🛡️ Equipped " + armors.get(idx).getName() + " successfully.");
        }
    }

    /**
     * Executes all monster attacks for the round.
     * Each alive monster chooses a random living hero.
     * Hero may dodge based on agility. Armor reduces incoming damage.
     */
    private void monstersTurn() {

        for (Monster m : monsters) {
            if (!m.isAlive())
                continue;
            Hero target = randomLivingHero();
            if (target == null)
                return;
            double dodge = target.getAgility() * 0.002;
            if (!isDay)
                dodge *= 1.1;
            if (random.nextDouble() < dodge) {
                view.showMessage("💨 " + target.getName() + " dodged the attack from " + m.getName() + "!");

                continue;
            }
            double dmg = m.getDamage();
            if (target.getArmor() != null) {
                dmg -= target.getArmor().getDamageReduction();
            }
            if (dmg < 0)
                dmg = 0;
            target.takeDamage(dmg);
            view.showMessage("💥 " + m.getName() + " struck " + target.getName() + " for " + (int) dmg + " damage!");

        }
    }

    /**
     * Regenerates HP and mana for all alive heroes after each battle round.
     * HP is capped at (level * 100). Mana increases by 10%.
     */
    private void regenHeroes() {
        double hpRegen = 1.1;
        double mpRegen = 1.1;

        if (isDay) {
            hpRegen = 1.2; // stronger regen
            mpRegen = 1.2;
        }

        for (Hero h : party.getHeroes()) {
            if (!h.isAlive())
                continue;
            h.hp = Math.min(h.getLevel() * 100, h.hp * hpRegen);
            h.setMana(h.getMana() * mpRegen);

        }
    }

    /**
     * Grants experience and gold to heroes after a victory.
     * Alive heroes receive full rewards; fainted heroes revive at half HP/Mana.
     */
    private void rewardHeroes() {
        int aliveCount = 0;
        for (Hero h : party.getHeroes())
            if (h.isAlive())
                aliveCount++;
        int exp = monsters.size() * 2;
        for (Hero h : party.getHeroes()) {
            if (h.isAlive()) {
                h.gainExperience(exp);
                double goldGain = 0;
                for (Monster m : monsters)
                    goldGain += m.getLevel() * 100;
                goldGain /= party.getHeroes().size();
                h.addGold(goldGain);
            } else {
                h.hp = (h.level * 100) * 0.5;
                h.setMana(h.getMana() * 0.5);
            }
        }
    }

    /**
     * Checks whether all monsters have been defeated.
     * 
     * @return true if every monster is dead, false otherwise
     */
    private boolean allMonstersDead() {
        for (Monster m : monsters) {
            if (m.isAlive())
                return false;
        }
        return true;
    }

    /**
     * Asks the player to choose a target monster.
     * Shows only alive monsters and maps user choice to the correct index.
     * 
     * @return the selected Monster, or null if none are alive
     */
    private Monster chooseMonster() {
        int aliveCount = 0;
        for (Monster m : monsters)
            if (m.isAlive())
                aliveCount++;
        if (aliveCount == 0)
            return null;
        view.showMessage("🎯 Choose your target:");
        int idxShown = 1;
        int[] map = new int[monsters.size()];
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            if (!m.isAlive())
                continue;
            view.showMessage(idxShown + ") " + m.battleInfo());
            map[idxShown - 1] = i;
            idxShown++;
        }
        int choice = readIntInRange(1, idxShown - 1);
        return monsters.get(map[choice - 1]);
    }

    /**
     * Selects a random living hero for a monster to attack.
     * 
     * @return a random living hero, or null if none are alive
     */
    private Hero randomLivingHero() {
        java.util.List<Hero> alive = new java.util.ArrayList<>();
        for (Hero h : party.getHeroes())
            if (h.isAlive())
                alive.add(h);
        if (alive.isEmpty())
            return null;
        return alive.get(random.nextInt(alive.size()));
    }

    /**
     * Safely reads an integer from input within a specified range.
     * Re-prompts until valid.
     * 
     * @param min the minimum acceptable integer
     * @param max the maximum acceptable integer
     * @return a validated integer within [min, max]
     */
    private int readIntInRange(int min, int max) {
        while (true) {
            try {
                String line = scanner.nextLine().trim();
                int v = Integer.parseInt(line);
                if (v < min || v > max) {
                    view.showMessage("⚠️ Please enter a number between " + min + " and " + max + ".");

                    continue;
                }
                return v;
            } catch (Exception e) {
                view.showMessage("❌ Invalid input. Please enter a valid number:");

            }
        }
    }

}

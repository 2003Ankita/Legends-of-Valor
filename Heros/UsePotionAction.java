package Heros;

import java.util.List;
import java.util.Scanner;

import Items.*;
import general.*;

public class UsePotionAction implements HeroAction {

    @Override
    public void execute(LegendsOfValorGame game, HeroUnit unit, Scanner in) {
        Hero hero = unit.getHero();
        Inventory inv = hero.getInventory();

        List<Potion> potions = inv.getPotions();
        if (potions.isEmpty()) {
            System.out.println("No potions available.");
            return;
        }

        System.out.println("\nChoose a potion to use:");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            System.out.printf("%d) %s (effect: %s)\n",
                    i + 1,
                    p.getName(),
                    p.getStat());
        }

        System.out.println("0) Cancel");
        int choice = game.readInt(in, 0, potions.size());
        if (choice == 0) {
            System.out.println("Cancel using potion.");
            return;
        }

        Potion selected = potions.get(choice - 1);

        applyPotionEffect(selected, hero);

        inv.remove(selected);

        System.out.println(hero.getName() + " used " + selected.getName() + ".");
    }

    private void applyPotionEffect(Potion p, Hero hero) {
        String name = p.getName().toLowerCase();
        double amount = p.getAmount();

        // HP potion
        if (name.contains("health") || name.contains("hp")) {
            hero.SetHP(hero.getHP() + amount);
            System.out.println("HP increased by " + amount);
        }

        // Mana potion
        if (name.contains("mana") || name.contains("mp")) {
            hero.SetMana(hero.getMana() + amount);
            System.out.println("Mana increased by " + amount);
        }

        // Strength potion
        if (name.contains("strength")) {
            hero.SetStrength(hero.getStrength() + amount);
            System.out.println("Strength increased by " + amount);
        }

        // Dexterity potion
        if (name.contains("dexterity") || name.contains("dex")) {
            hero.SetDexterity(hero.getDexterity() + amount);
            System.out.println("Dexterity increased by " + amount);
        }

        // Agility potion
        if (name.contains("agility") || name.contains("agi")) {
            hero.SetAgility(hero.getAgility() + amount);
            System.out.println("Agility increased by " + amount);
        }

        System.out.println("Updated stats: " + hero.fullInfo());
    }
}

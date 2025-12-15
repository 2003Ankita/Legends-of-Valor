package Heroes;

import java.util.List;
import java.util.Scanner;

import Items.*;
import general.*;

public class EquipAction implements HeroAction {

    @Override
    public boolean execute(LegendsOfValorGame game, HeroUnit unit, Scanner in) {
        Hero hero = unit.getHero();
        Inventory inv = hero.getInventory();

        System.out.println("\n🛡️ What would you like to equip?");

        System.out.println("1) ⚔️ Weapon");

        System.out.println("2) 🛡️ Armor");
        System.out.println("0) ↩️ Cancel");

        int choice = game.readInt(in, 0, 2);
        if (choice == 0) {
            System.out.println("↩️ Equipment selection cancelled.");
            return false;
        }

        if (choice == 1) {
            List<Weapon> weapons = inv.getWeapons();
            if (weapons.isEmpty()) {
                System.out.println("⚠️ No weapons available to equip.");
                return false;
            }

            System.out.println("\n⚔️ Choose a weapon to equip:");
            for (int i = 0; i < weapons.size(); i++) {
                Weapon w = weapons.get(i);
                System.out.printf("%d) %s | ⚔️ Dmg %.1f | 🔓 Lvl %d%n",
                        i + 1, w.getName(), w.getDamage(), w.getLevelRequirement());
            }

            int idx = game.readInt(in, 1, weapons.size());
            hero.equipWeapon(weapons.get(idx - 1));
            System.out.println("✅ " + hero.getName() + " equipped " + weapons.get(idx - 1).getName() + ".");

            return true;
        }

        else if (choice == 2) {
            List<Armor> armors = inv.getArmors();
            if (armors.isEmpty()) {
                System.out.println("⚠️ No armor available to equip.");

                return false;
            }

            System.out.println("\n🛡️ Choose armor to equip:");
            for (int i = 0; i < armors.size(); i++) {
                Armor a = armors.get(i);
                System.out.printf("%d) %s | 🛡️ Reduction %.1f | 🔓 Lvl %d%n",
                        i + 1, a.getName(), a.getDamageReduction(), a.getLevelRequirement());
            }

            int idx = game.readInt(in, 1, armors.size());
            hero.equipArmor(armors.get(idx - 1));
            System.out.println("✅ " + hero.getName() + " equipped " + armors.get(idx - 1).getName() + ".");
            return true;
        }
        return true;
    }
}

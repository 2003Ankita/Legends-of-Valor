package Heroes;

import java.util.List;
import java.util.Scanner;

import Monsters.Monster;
import Monsters.MonsterUnit;
import general.LegendsOfValorGame;

/**
 * Hero physical attack on a monster within range 1.
 */
public class AttackAction implements HeroAction {

    @Override
    public boolean execute(LegendsOfValorGame game, HeroUnit unit, Scanner in) {
        List<MonsterUnit> targets = game.getMonstersInRange(unit.getPosition(), 1);
        if (targets.isEmpty()) {
            System.out.println("⚠️ No monsters are within attack range.");
            return false;
        }

        System.out.println("🎯 Choose a monster to attack:");
        for (int i = 0; i < targets.size(); i++) {
            MonsterUnit mu = targets.get(i);
            Monster m = mu.getMonster();
            System.out.printf("%d) %s | ❤️ HP %.1f | ⭐ Lvl %d | 📍 Pos %s%n",
                    i + 1, m.getName(), m.getHp(), m.getLevel(), mu.getPosition());
        }
        int choice = game.readInt(in, 1, targets.size());
        MonsterUnit target = targets.get(choice - 1);
        game.heroAttack(unit, target);
        return true;
    }
}

package Heros;

import java.util.List;
import java.util.Scanner;

import Board.LegendsOfValorGame;
import Monsters.Monster;
import Monsters.MonsterUnit;

/**
 * Hero physical attack on a monster within range 1.
 */
public class AttackAction implements HeroAction {

    @Override
    public void execute(LegendsOfValorGame game, HeroUnit unit, Scanner in) {
        List<MonsterUnit> targets = game.getMonstersInRange(unit.getPosition(), 1);
        if (targets.isEmpty()) {
            System.out.println("No monsters in attack range.");
            return;
        }

        System.out.println("Choose target to attack:");
        for (int i = 0; i < targets.size(); i++) {
            MonsterUnit mu = targets.get(i);
            Monster m = mu.getMonster();
            System.out.printf("%d) %s (HP %.1f, lvl %d) at %s%n",
                    i + 1, m.getName(), m.getHp(), m.getLevel(), mu.getPosition());
        }
        int choice = game.readInt(in, 1, targets.size());
        MonsterUnit target = targets.get(choice - 1);
        game.heroAttack(unit, target);
    }
}

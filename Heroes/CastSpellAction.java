package Heroes;

import java.util.List;
import java.util.Scanner;

import Board.Position;
import Items.*;
import general.*;
import Monsters.*;
import Board.*;

public class CastSpellAction implements HeroAction {

    @Override
    public boolean execute(LegendsOfValorGame game, HeroUnit unit, Scanner in) {
        Hero hero = unit.getHero();
        List<Spell> spells = hero.getSpells();

        if (spells == null || spells.isEmpty()) {
            System.out.println("No spells available.");
            return false;
        }

        System.out.println("\nAvailable spells:");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            System.out.printf("%d) %s (dmg %.1f, mana %.1f, type %s)%n",
                    i + 1, s.getName(), s.getDamage(), s.getManaCost(), s.getType());
        }

        System.out.println("Choose a spell (0 to cancel):");
        int idx = game.readInt(in, 0, spells.size());
        if (idx == 0) {
            System.out.println("Cancel casting spell.");
            return false;
        }
        Spell spell = spells.get(idx - 1);

        if (hero.getMana() < spell.getManaCost()) {
            System.out.println("Not enough MP.");
            return false;
        }

        Position pos = unit.getPosition();
        List<MonsterUnit> targets = game.getMonstersInRange(pos, 1); // 用改成曼哈顿距离后的方法
        if (targets.isEmpty()) {
            System.out.println("No monsters in range to cast spell on.");
            return false;
        }

        System.out.println("Choose target monster:");
        for (int i = 0; i < targets.size(); i++) {
            Monster m = targets.get(i).getMonster();
            System.out.printf("%d) %s (HP %.1f, lvl %d)%n",
                    i + 1, m.getName(), m.getHp(), m.getLevel());
        }

        int tIdx = game.readInt(in, 1, targets.size());
        MonsterUnit target = targets.get(tIdx - 1);

        hero.SetMana(hero.getMana() - spell.getManaCost());

        LegendsTile tile = game.getBoard().getTile(pos);
        double dmg = game.getDamageCalculator().heroCastsSpell(
                hero, target.getMonster(), spell, tile.getTerrainType());

        target.getMonster().takeDamage(dmg);
        System.out.printf("%s casts %s on %s for %.1f damage%n",
                hero.getName(), spell.getName(), target.getMonster().getName(), dmg);

        if (!target.isAlive()) {
            System.out.println(target.getMonster().getName() + " is defeated!");
            game.getBoard().getTile(target.getPosition()).removeMonster();
            game.getMonstersOnBoard().remove(target);
            game.rewardHeroesForKill(target.getMonster());
        }
        return true;
    }
}

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
            System.out.println("📜 You have no spells available to cast.");
            return false;
        }

        System.out.println("\n🔮 Available Spells:");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            System.out.printf("%d) %s | 🔥 Dmg %.1f | 🔮 Mana %.1f | 🧬 Type %s%n",
                    i + 1, s.getName(), s.getDamage(), s.getManaCost(), s.getType());
        }

        System.out.println("👉 Choose a spell to cast (0 to cancel):");
        int idx = game.readInt(in, 0, spells.size());
        if (idx == 0) {
            System.out.println("↩️ Spell casting cancelled.");
            return false;
        }
        Spell spell = spells.get(idx - 1);

        if (hero.getMana() < spell.getManaCost()) {
            System.out.println("❌ Not enough mana to cast this spell.");
            return false;
        }

        Position pos = unit.getPosition();
        List<MonsterUnit> targets = game.getMonstersInRange(pos, 1);
        if (targets.isEmpty()) {
            System.out.println("⚠️ No monsters are within spell range.");
            return false;
        }

        System.out.println("🎯 Choose a target monster:");
        for (int i = 0; i < targets.size(); i++) {
            Monster m = targets.get(i).getMonster();
            System.out.printf("%d) %s | ❤️ HP %.1f | ⭐ Lvl %d%n",
                    i + 1, m.getName(), m.getHp(), m.getLevel());
        }

        int tIdx = game.readInt(in, 1, targets.size());
        MonsterUnit target = targets.get(tIdx - 1);

        hero.setMana(hero.getMana() - spell.getManaCost());

        LegendsTile tile = game.getBoard().getTile(pos);
        double dmg = game.getDamageCalculator().heroCastsSpell(
                hero, target.getMonster(), spell, tile.getTerrainType());

        target.getMonster().takeDamage(dmg);
        System.out.printf("✨ %s casts %s on %s, dealing %.1f damage!%n",
                hero.getName(), spell.getName(), target.getMonster().getName(), dmg);

        if (!target.isAlive()) {
            System.out.println("🏆 " + target.getMonster().getName() + " has been defeated!");
            game.getBoard().getTile(target.getPosition()).removeMonster();
            game.getMonstersOnBoard().remove(target);
            game.rewardHeroesForKill(unit, target.getMonster());
        }
        return true;
    }
}

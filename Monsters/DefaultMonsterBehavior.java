package Monsters;

import java.util.List;

import Heros.HeroUnit;
import Items.Position;
import general.LegendsOfValorGame;

/**
 * Default monster AI:
 * - If a hero is in range-1, attack one of them.
 * - Otherwise, move one step SOUTH (towards heroes' Nexus) if possible.
 */
public class DefaultMonsterBehavior implements MonsterBehavior {

    @Override
    public void takeTurn(MonsterUnit unit, LegendsOfValorGame game) {
        if (!unit.isAlive())
            return;

        Position pos = unit.getPosition();

        // 1) Look for heroes in range 1 (cross pattern)
        List<HeroUnit> targets = game.getHeroesInRange(pos, 1);
        if (!targets.isEmpty()) {
            HeroUnit target = targets.get(0);
            game.monsterAttack(unit, target);
            return;
        }

        // 2) Move south if legal
        Position south = new Position(pos.row + 1, pos.col);
        if (game.canMonsterMoveTo(unit, south)) {
            game.moveMonster(unit, south);
        }
    }
}

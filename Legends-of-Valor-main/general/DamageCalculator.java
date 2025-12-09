package general;

import Board.TerrainType;
import Heros.Hero;
import Items.Armor;
import Items.Weapon;
import Monsters.Monster;

/**
 * Strategy interface for computing damage between heroes and monsters.
 */
public interface DamageCalculator {

        double heroAttacksMonster(Hero hero, Monster monster,
                        TerrainType terrainType, Weapon weapon);

        double monsterAttacksHero(Monster monster, Hero hero,
                        TerrainType terrainType, Armor armor);
}

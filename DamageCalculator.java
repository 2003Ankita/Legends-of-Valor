/**
 * Strategy interface for computing damage between heroes and monsters.
 */
public interface DamageCalculator {

    double heroAttacksMonster(Hero hero, Monster monster,
                              TerrainType terrainType, Weapon weapon);

    double monsterAttacksHero(Monster monster, Hero hero,
                              TerrainType terrainType, Armor armor);
}

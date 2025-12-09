package Board;

import Heros.Hero;
import Monsters.Monster;

/**
 * Extended tile for Legends of Valor.
 *
 * This class ADAPTS the existing Tile class to the richer
 * terrain model required by LoV:
 * - A tile remembers its TerrainType
 * - It can simultaneously hold one hero and one monster
 *
 * We keep using TileType from the base game so that existing
 * market/board logic can still understand the tile in a generic way.
 */
public class LegendsTile extends Tile {

    private TerrainType terrainType;
    private Hero hero; // at most one hero
    private Monster monster; // at most one monster

    public LegendsTile(TerrainType terrainType) {
        // Map terrain to a basic TileType used by the old code.
        super(mapToBaseTileType(terrainType));
        this.terrainType = terrainType;
    }

    private static TileType mapToBaseTileType(TerrainType t) {
        switch (t) {
            case INACCESSIBLE:
                return TileType.INACCESSIBLE;
            case HERO_NEXUS:
                // Hero Nexus works as a market
                return TileType.MARKET;
            default:
                // MONSTER_NEXUS and all lane tiles are "COMMON"
                return TileType.COMMON;
        }
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    public Hero getHero() {
        return hero;
    }

    public Monster getMonster() {
        return monster;
    }

    public boolean isEmptyForHero() {
        return hero == null
                && terrainType != TerrainType.INACCESSIBLE
                && terrainType != TerrainType.OBSTACLE;
    }

    public boolean isEmptyForMonster() {
        return monster == null
                && terrainType != TerrainType.INACCESSIBLE
                && terrainType != TerrainType.OBSTACLE;
    }

    public void placeHero(Hero h) {
        this.hero = h;
    }

    public void removeHero() {
        this.hero = null;
    }

    public void placeMonster(Monster m) {
        this.monster = m;
        // Maintain base hasMonster flag for compatibility.
        setMonster(m != null);
    }

    public void removeMonster() {
        this.monster = null;
        setMonster(false);
    }

    public boolean isHeroNexus() {
        return terrainType == TerrainType.HERO_NEXUS;
    }

    public boolean isMonsterNexus() {
        return terrainType == TerrainType.MONSTER_NEXUS;
    }
}
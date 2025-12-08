import java.util.EnumMap;
import java.util.Map;

/**
 * Factory and cache for TerrainEffect flyweights.
 * Each TerrainType is mapped to exactly one TerrainEffect instance.
 */
public final class TerrainEffectFactory {

    private static final Map<TerrainType, TerrainEffect> CACHE =
            new EnumMap<>(TerrainType.class);

    static {
        // Default: no bonus
        CACHE.put(TerrainType.PLAIN,         new TerrainEffect(1.0, 1.0, 1.0));
        CACHE.put(TerrainType.OBSTACLE,      new TerrainEffect(1.0, 1.0, 1.0));
        CACHE.put(TerrainType.INACCESSIBLE,  new TerrainEffect(1.0, 1.0, 1.0));
        CACHE.put(TerrainType.HERO_NEXUS,    new TerrainEffect(1.0, 1.0, 1.0));
        CACHE.put(TerrainType.MONSTER_NEXUS, new TerrainEffect(1.0, 1.0, 1.0));

        // Suggested in PDF: special tiles buff hero stats (e.g. +10%). :contentReference[oaicite:1]{index=1}
        CACHE.put(TerrainType.BUSH,   new TerrainEffect(1.0, 1.10, 1.0)); // dexterity
        CACHE.put(TerrainType.CAVE,   new TerrainEffect(1.0, 1.0,  1.10)); // agility
        CACHE.put(TerrainType.KOULOU, new TerrainEffect(1.10, 1.0, 1.0));  // strength
    }

    private TerrainEffectFactory() { }

    public static TerrainEffect forTerrain(TerrainType type) {
        TerrainEffect effect = CACHE.get(type);
        if (effect == null) {
            effect = new TerrainEffect(1.0, 1.0, 1.0);
            CACHE.put(type, effect);
        }
        return effect;
    }
}

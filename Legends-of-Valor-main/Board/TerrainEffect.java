package Board;

/**
 * Immutable flyweight that represents the stat modifiers given
 * by a particular TerrainType. Multiple tiles share the same
 * TerrainEffect instance through TerrainEffectFactory.
 */
public final class TerrainEffect {
    private final double strengthMultiplier;
    private final double dexterityMultiplier;
    private final double agilityMultiplier;

    TerrainEffect(double strengthMultiplier,
            double dexterityMultiplier,
            double agilityMultiplier) {
        this.strengthMultiplier = strengthMultiplier;
        this.dexterityMultiplier = dexterityMultiplier;
        this.agilityMultiplier = agilityMultiplier;
    }

    public double getStrengthMultiplier() {
        return strengthMultiplier;
    }

    public double getDexterityMultiplier() {
        return dexterityMultiplier;
    }

    public double getAgilityMultiplier() {
        return agilityMultiplier;
    }
}

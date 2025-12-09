package general;

import Board.TerrainEffect;
import Board.TerrainEffectFactory;
import Board.TerrainType;
import Heroes.Hero;
import Items.Armor;
import Items.Spell;
import Items.Weapon;
import Monsters.Monster;

/**
 * Default implementation of DamageCalculator.
 * Uses hero strength + weapon damage and monster defense, with
 * terrain buffs applied through TerrainEffect.
 */
public class DefaultDamageCalculator implements DamageCalculator {

    @Override
    public double heroAttacksMonster(Hero hero, Monster monster,
            TerrainType terrainType, Weapon weapon) {

        TerrainEffect effect = TerrainEffectFactory.forTerrain(terrainType);

        double effectiveStrength = hero.getStrength() * effect.getStrengthMultiplier();

        double weaponDamage = (weapon != null) ? weapon.getDamage() : 0.0;

        double rawDamage = (effectiveStrength + weaponDamage) * 0.05;
        double mitigated = rawDamage * (1.0 - monster.getDefense() / 100.0);
        return Math.max(0.0, mitigated);
    }

    @Override
    public double monsterAttacksHero(Monster monster, Hero hero,
            TerrainType terrainType, Armor armor) {

        TerrainEffect effect = TerrainEffectFactory.forTerrain(terrainType);

        double effectiveAgility = hero.getAgility() * effect.getAgilityMultiplier();

        double baseDamage = monster.getDamage();
        double armorReduction = (armor != null) ? armor.getDamageReduction() : 0.0;

        // Simple dodge chance using agility
        double dodgeChance = Math.min(0.5, effectiveAgility * 0.002);

        if (Math.random() < dodgeChance) {
            return 0.0; // hero dodged
        }

        double mitigated = baseDamage * (1.0 - armorReduction / 100.0);
        return Math.max(0.0, mitigated);
    }

    @Override
    public double heroCastsSpell(Hero hero, Monster monster,
            Spell spell, TerrainType terrainType) {
        TerrainEffect effect = TerrainEffectFactory.forTerrain(terrainType);

        double effectiveDexterity = hero.getDexterity() * effect.getDexterityMultiplier();

        double base = spell.getDamage() * (1 + effectiveDexterity / 10000.0);

        double mitigated = base * (1.0 - monster.getDefense() / 100.0);
        return Math.max(0.0, mitigated);
    }
}

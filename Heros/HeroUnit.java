package Heros;

import Board.Lane;
import Board.Position;

/**
 * Represents a hero piece on the Legends of Valor board.
 * Keeps track of the underlying Hero object, its lane,
 * current position, and Nexus position (for Recall).
 */
public class HeroUnit {
    private final Hero hero;
    private final Lane lane;
    private final Position nexusPosition; // where Recall sends the hero
    private Position position;

    public HeroUnit(Hero hero, Lane lane, Position nexusPosition) {
        this.hero = hero;
        this.lane = lane;
        this.nexusPosition = nexusPosition;
        this.position = nexusPosition;
    }

    public Hero getHero() {
        return hero;
    }

    public Lane getLane() {
        return lane;
    }

    public Position getPosition() {
        return position;
    }

    public Position getNexusPosition() {
        return nexusPosition;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isAlive() {
        return hero.getHp() > 0;
    }
}

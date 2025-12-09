package Monsters;

import Board.Lane;
import Items.Position;

/**
 * Represents a monster piece on the Legends of Valor board.
 */
public class MonsterUnit {
    private final Monster monster;
    private final Lane lane;
    private Position position;

    public MonsterUnit(Monster monster, Lane lane, Position spawnPosition) {
        this.monster = monster;
        this.lane = lane;
        this.position = spawnPosition;
    }

    public Monster getMonster() {
        return monster;
    }

    public Lane getLane() {
        return lane;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isAlive() {
        return monster.getHp() > 0;
    }
}

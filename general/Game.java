package general;

import java.util.List;
import Heroes.HeroUnit;
import Monsters.MonsterUnit;

/**
 * Generic base game class for all RPG-style games.
 */
public abstract class Game {

    protected List<HeroUnit> heroes;
    protected List<MonsterUnit> monsters;

    protected int roundNumber = 1;
    protected boolean gameOver = false;

    // DO NOT override this
    public final void startGame() {
        initializeGame();
        while (!gameOver) {
            playRound();
            roundNumber++;
        }
        endGame();
    }

    protected void playRound() {
        heroesTurn();
        monstersTurn();
        checkWinCondition();
    }

    // Hooks for specific games
    protected abstract void initializeGame();
    protected abstract void heroesTurn();
    protected abstract void monstersTurn();
    protected abstract void checkWinCondition();
    protected abstract void endGame();
}

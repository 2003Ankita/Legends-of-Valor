package Heroes;

import java.util.Scanner;

import general.LegendsOfValorGame;

/**
 * Strategy representing a single hero action (Move, Attack, Teleport, etc.)
 * to be performed during the hero's turn.
 */
public interface HeroAction {
    /**
     * @return true if the action was performed and the turn should end,
     *         false if the player should choose another action.
     */
    boolean execute(LegendsOfValorGame game, HeroUnit heroUnit, Scanner in);
}

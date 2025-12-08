import java.util.Scanner;

/**
 * Strategy representing a single hero action (Move, Attack, Teleport, etc.)
 * to be performed during the hero's turn.
 */
public interface HeroAction {
    void execute(LegendsOfValorGame game, HeroUnit heroUnit, Scanner in);
}

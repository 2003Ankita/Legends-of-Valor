package Heros;

import java.util.Scanner;

import general.LegendsOfValorGame;

/**
 * Strategy representing a single hero action (Move, Attack, Teleport, etc.)
 * to be performed during the hero's turn.
 */
public interface HeroAction {
    void execute(LegendsOfValorGame game, HeroUnit heroUnit, Scanner in);
}

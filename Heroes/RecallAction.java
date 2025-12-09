package Heroes;

import java.util.Scanner;

import general.LegendsOfValorGame;

/**
 * Recall: return hero to their Nexus tile.
 */
public class RecallAction implements HeroAction {

    @Override
    public void execute(LegendsOfValorGame game, HeroUnit heroUnit, Scanner in) {
        game.recallHero(heroUnit);
        System.out.println(heroUnit.getHero().getName() + " recalled to Nexus.");
    }
}

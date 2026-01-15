package Heroes;

import java.util.Scanner;

import general.LegendsOfValorGame;

/**
 * Recall: return hero to their Nexus tile.
 */
public class RecallAction implements HeroAction {

    @Override
    public boolean execute(LegendsOfValorGame game, HeroUnit heroUnit, Scanner in) {
        game.recallHero(heroUnit);
        System.out.println("🔙 " + heroUnit.getHero().getName() + " has returned safely to the Hero Nexus.");
        return true;
    }
}

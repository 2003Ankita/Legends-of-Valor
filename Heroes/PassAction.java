package Heroes;

import java.util.Scanner;

import general.LegendsOfValorGame;

/**
 * Optional "do nothing" action for a hero turn.
 */
public class PassAction implements HeroAction {
    @Override
    public boolean execute(LegendsOfValorGame game, HeroUnit heroUnit, Scanner in) {
        System.out.println("⏭️ " + heroUnit.getHero().getName() + " waits and passes the turn.");
        return true;
    }
}

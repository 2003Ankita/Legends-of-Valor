import java.util.Scanner;

/**
 * Optional "do nothing" action for a hero turn.
 */
public class PassAction implements HeroAction {
    @Override
    public void execute(LegendsOfValorGame game, HeroUnit heroUnit, Scanner in) {
        System.out.println(heroUnit.getHero().getName() + " passes the turn.");
    }
}

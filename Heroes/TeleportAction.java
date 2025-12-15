package Heroes;

import java.util.List;
import java.util.Scanner;

import Board.Position;
import general.LegendsOfValorGame;

/**
 * Teleport: move hero to a tile adjacent to a target hero in another lane,
 * respecting the assignment rules (no teleport ahead of target hero, etc.).
 */
public class TeleportAction implements HeroAction {

    @Override
    public boolean execute(LegendsOfValorGame game, HeroUnit unit, Scanner in) {
        List<HeroUnit> others = game.getOtherHeroes(unit);
        if (others.isEmpty()) {
            System.out.println("⚠️ No other heroes available for teleportation.");
            return false;
        }
        System.out.println("✨ Choose a hero to teleport near:");

        for (int i = 0; i < others.size(); i++) {
            HeroUnit h = others.get(i);
            System.out.printf("%d) %s | 📍 %s | 🛣️ Lane %s%n",

                    i + 1, h.getHero().getName(), h.getPosition(), h.getLane());
        }
        int choice = game.readInt(in, 1, others.size());
        HeroUnit target = others.get(choice - 1);

        List<Position> candidates = game.validTeleportDestinations(unit, target);
        if (candidates.isEmpty()) {
            System.out.println("❌ No valid teleport destinations near the selected hero.");

            return false;
        }
        System.out.println("📍 Choose a teleport destination:");
        for (int i = 0; i < candidates.size(); i++) {
            System.out.printf("%d) 📍 %s%n",
                    i + 1, candidates.get(i));
        }
        int destIdx = game.readInt(in, 1, candidates.size());
        Position dest = candidates.get(destIdx - 1);

        game.moveHero(unit, dest);
        return true;
    }
}

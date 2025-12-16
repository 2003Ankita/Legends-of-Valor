package Heroes;

import java.util.Scanner;

import Board.*;
import general.*;

/**
 * Moves hero one tile in cardinal directions, enforcing LoV rules.
 */
public class MoveAction implements HeroAction {

    @Override
    public boolean execute(LegendsOfValorGame game, HeroUnit unit, Scanner in) {
        int dr = 0, dc = 0;
        while (true) {
            System.out.println("Move (W/A/S/D):W =move up, A = move left, S = move down, D = move right ");
            System.out.println("Choose a Move:");
            String line = in.nextLine().trim().toUpperCase();
            if (line.isEmpty()) {
                System.out.println("❌ Invalid input. Please enter W, A, S, or D.");
                continue;
            }
            char ch = line.charAt(0);
            switch (ch) {
                case 'W':
                    dr = -1;
                    break; // north
                case 'S':
                    dr = 1;
                    break; // south
                case 'A':
                    dc = -1;
                    break; // west
                case 'D':
                    dc = 1;
                    break; // east
                default:
                    System.out.println("❌ Invalid direction. Use W, A, S, or D to move.");
                    continue;
            }
            break;
        }
        Position current = unit.getPosition();
        Position dest = current.translate(dr, dc);
        if (!game.getBoard().inBounds(dest)) {
            System.out.println("🚫 You cannot move outside the battlefield. Choose another direction.");
            return false;
        }

        LegendsTile destTile = game.getBoard().getTile(dest);
        if (destTile.getTerrainType() == TerrainType.OBSTACLE) {
            System.out.println("🧱 An obstacle blocks your path. Spend one turn to clear it? (y/n): ");

            String ans = in.nextLine().trim().toLowerCase();


            if (!ans.isEmpty() && ans.charAt(0) == 'y') {
                // Change terrain from OBSTACLE to PLAIN
                destTile.setTerrainType(TerrainType.PLAIN);
                System.out.println("✅ Obstacle cleared successfully. Your turn ends.");

            } else {
                System.out.println("↩️ Obstacle remains. Choose a different action or move.");

                return false; // don't consume turn if they decline
            }
            return true; // consumes turn only when cleared

        }

        if (game.canHeroMoveTo(unit, dest)) {
            game.moveHero(unit, dest);
            return true; // valid move consumes the turn
        } else {
            System.out.println("❌ That move is not allowed in Legends of Valor. Try again.");

            return false; // IMPORTANT: retry; do NOT end turn
        }

    }
}

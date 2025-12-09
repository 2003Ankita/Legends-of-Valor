package Heros;

import java.util.Scanner;

import Board.LegendsOfValorGame;
import Board.LegendsTile;
import Board.TerrainType;
import Items.Position;

/**
 * Moves hero one tile in cardinal directions, enforcing LoV rules.
 */
public class MoveAction implements HeroAction {

    @Override
    public void execute(LegendsOfValorGame game, HeroUnit unit, Scanner in) {
        System.out.print("Move (W/A/S/D): ");
        String line = in.nextLine().trim().toUpperCase();
        if (line.isEmpty())
            return;
        char ch = line.charAt(0);

        int dr = 0, dc = 0;
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
                System.out.println("Invalid direction.");
                return;
        }

        Position current = unit.getPosition();
        Position dest = current.translate(dr, dc);
        if (!game.getBoard().inBounds(dest)) {
            System.out.println("You cannot move outside the board boundaries.");
            return;
        }

        LegendsTile destTile = game.getBoard().getTile(dest);
        if (destTile.getTerrainType() == TerrainType.OBSTACLE) {
            System.out.print("There is an obstacle ahead. "
            + "Do you want to spend one turn to clear this tile? (y/n) ");
            String ans = in.nextLine().trim().toLowerCase();

            if (!ans.isEmpty() && ans.charAt(0) == 'y') {
        // Change terrain from OBSTACLE to PLAIN
                destTile.setTerrainType(TerrainType.PLAIN);
                System.out.println("You cleared the obstacle tile. Your turn ends.");
            } else {
                System.out.println("You chose not to clear the obstacle. Your turn ends.");
            }
    // In either case, the hero does not move this turn
            return;
        }

        if (game.canHeroMoveTo(unit, dest)) {
            game.moveHero(unit, dest);
        } else {
            System.out.println("Illegal move for Legends of Valor.");
        }
    }
}

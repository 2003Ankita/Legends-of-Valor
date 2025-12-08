package general;

import java.util.List;
import java.util.Scanner;

import Board.Board;
import Board.Tile;
import Board.TileType;
import Heros.Hero;
import Heros.Party;
import Monsters.Monster;

/**
 * Console-based implementation of the GameView interface.
 * Responsible for rendering the board, displaying hero/monster details,
 * showing battle updates, and printing general messages to the console.
 */
public class ConsoleGameView implements GameView {
    private final Scanner scanner;

    /**
     * Creates a ConsoleGameView using the given Scanner for user input.
     * 
     * @param scanner input source (usually System.in)
     */
    public ConsoleGameView(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Renders the full game board to the console.
     * Displays tile types, hero position, visible monsters, party stats,
     * and remaining monster tiles.
     * 
     * @param board the current game board
     * @param party the player's party whose position and stats are shown
     */
    @Override
    public void render(Board board, Party party) {
        int size = board.getSize();

        System.out.println("Legend: H=Heroes  M=Market  X=Blocked  .=Common  👹=Monster\n");

        // column header
        System.out.print("  ");
        for (int c = 0; c < size; c++) {
            System.out.print(" " + c);
        }
        System.out.println();
        System.out.println("  " + repeat("-", size * 2 + 1));

        for (int r = 0; r < size; r++) {
            StringBuilder sb = new StringBuilder();
            sb.append(r).append(" |");
            for (int c = 0; c < size; c++) {
                if (party.getRow() == r && party.getCol() == c) {
                    sb.append('H');
                } else {
                    Tile t = board.getTile(r, c);
                    if (t.hasMonster() && t.getType() == TileType.COMMON) {
                        sb.append("👹");
                    } else {
                        char ch;
                        switch (t.getType()) {
                            case INACCESSIBLE:
                                ch = 'X';
                                break;
                            case MARKET:
                                ch = 'M';
                                break;
                            case COMMON:
                            default:
                                ch = '.';
                                break;
                        }
                        sb.append(ch);
                    }
                }
                sb.append(' ');
            }
            System.out.println(sb.toString());
        }

        System.out.println();
        System.out.println("Party position: (" + party.getRow() + ", " + party.getCol() + ")");
        System.out.println("Party highest level: " + party.getHighestLevel());
        System.out.println("Party total gold: " + (int) party.getTotalGold());
        System.out.println("Monster tiles remaining: " + board.getMonstersRemaining());
        System.out.println();
    }

    /**
     * Displays detailed information about all heroes in the party and,
     * if provided, all monsters currently in battle.
     * 
     * @param party   the player's party
     * @param enemies optional list of monsters (null outside battle mode)
     */
    @Override
    public void showInfo(Party party, List<Monster> enemies) {
        System.out.println("\n--- HERO DETAILS ---");
        for (Hero h : party.getHeroes()) {
            System.out.println(h.fullInfo());
        }

        if (enemies != null) {
            System.out.println("\n--- MONSTER DETAILS ---");
            for (Monster m : enemies) {
                System.out.println(m.fullInfo());
            }
        }
    }

    /**
     * Shows the current state of the battle, including HP/mana/damage info
     * for all heroes and monsters.
     * 
     * @param party    the player's party
     * @param monsters the list of active monsters in the battle
     */
    @Override
    public void showBattleStatus(Party party, List<Monster> monsters) {
        System.out.println("\nBattle status:");
        for (Hero h : party.getHeroes()) {
            System.out.println(h.battleInfo());
        }
        System.out.println();
        for (Monster m : monsters) {
            System.out.println(m.battleInfo());
        }
    }

    @Override
    public void showMessage(String msg) {
        System.out.println(msg);
    }

    /**
     * Simple helper method to replicate a short string a given number of times.
     * 
     * @param s     the string to repeat
     * @param count how many times to append it
     * @return resulting repeated string
     */
    private String repeat(String s, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++)
            sb.append(s);
        return sb.toString();
    }
}

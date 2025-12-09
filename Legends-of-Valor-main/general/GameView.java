package general;

import Board.Board;
import Heros.Party;
import Monsters.Monster;

/**
 * Defines the UI abstraction layer for the game.
 * Implementations of this interface (such as ConsoleGameView)
 * are responsible for how information is displayed to the player.
 * This allows the game logic to remain independent of the UI.
 */
public interface GameView {
    /**
     * Renders the current state of the board and the party's position.
     * 
     * @param board the game board to display
     * @param party the player's party
     */
    void render(Board board, Party party);

    /**
     * Displays detailed information about the party and (optionally)
     * the list of monsters — used primarily in battle or player requests.
     * 
     * @param party    the player's party
     * @param monsters optional list of monsters; may be null outside battle
     */
    void showInfo(Party party, java.util.List<Monster> monsters);

    /**
     * Shows the battle status summary, usually including
     * the HP/MP of heroes and the HP of all monsters in the fight.
     * 
     * @param party    the player's party
     * @param monsters monsters currently involved in the battle
     */
    void showBattleStatus(Party party, java.util.List<Monster> monsters);

    /**
     * Displays a single message to the player (text only).
     * Implementations decide the formatting/output medium.
     * 
     * @param msg the message to display
     */
    void showMessage(String msg);
}

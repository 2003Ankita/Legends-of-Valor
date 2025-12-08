/**
 * Strategy interface that encapsulates how a monster behaves
 * on its turn in Legends of Valor.
 */
public interface MonsterBehavior {

    /**
     * Executes the monster's turn.
     * @param unit monster wrapper with position
     * @param game game facade for querying heroes/board
     */
    void takeTurn(MonsterUnit unit, LegendsOfValorGame game);
}

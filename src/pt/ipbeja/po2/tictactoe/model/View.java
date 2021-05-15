package pt.ipbeja.po2.tictactoe.model;

import java.util.List;

/**
 * @author Diogo Pina Manique
 * @version 13/03/2021
 */
public interface View {

    /**
     * Notifies that the Place at the given Position has changed
     * @param place The Place, representing a player
     * @param position The Position of the Place
     */
    void onBoardMarkChanged(Mark place, Position position);

    /**
     * Notifies that a new game board has been created
     * @param board The new board
     */
    void onBoardCreated(Mark[][] board);

    /**
     * Notifies that the existing board has changed
     * @param board The existing board
     */
    void onBoardChanged(Mark[][] board);

    /**
     * Notifies that the game has ended with a win
     * @param place The Place representing the winning player
     */
    void onGameWon(Player place);

    /**
     * Notifies that the game has ended with a draw
     */
    void onGameDraw();
}

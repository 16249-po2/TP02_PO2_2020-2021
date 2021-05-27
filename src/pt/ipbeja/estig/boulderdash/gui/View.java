package pt.ipbeja.estig.boulderdash.gui;

import pt.ipbeja.estig.boulderdash.model.Move;

/**
 * The boulderdash puzzle view
 *
 * @author João Paulo Barros
 * @version 2021/05/18
 */
public interface View {
    void notifyView(Move move, Boolean winning, int tValue);
}

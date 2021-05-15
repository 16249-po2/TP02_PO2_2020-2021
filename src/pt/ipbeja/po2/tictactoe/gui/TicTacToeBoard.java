package pt.ipbeja.po2.tictactoe.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import pt.ipbeja.po2.tictactoe.model.*;

import java.util.List;

/**
 * @author Diogo Pina Manique
 * @version 12/03/2021
 */

public class TicTacToeBoard extends GridPane implements View {

    private final TicTacToeGame game;
    private TicTacToeButton[][] buttons;

    public TicTacToeBoard(TicTacToeGame game) {
        this.game = game;
        this.game.setGameView(this);
        this.game.startGame();
    }

    @Override
    public void onBoardMarkChanged(Mark place, Position position) {
        int row = position.getRow();
        int col = position.getCol();
        TicTacToeButton button = buttons[row][col];
        button.setPlace(place);
    }

    @Override
    public void onBoardCreated(Mark[][] board) {
        int rows = board.length;
        int cols = board[0].length;
        getChildren().clear(); // redraw buttons
        ButtonHandler handler = new ButtonHandler();
        this.buttons = new TicTacToeButton[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                TicTacToeButton button = new TicTacToeButton();
                button.setOnAction(handler);
                add(button, col, row);
                buttons[row][col] = button;
            }
        }
    }

    @Override
    public void onBoardChanged(Mark[][] board) {
        // ignorado aqui, mas útil na text user interface
    }


    @Override
    public void onGameWon(Player player) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Player " + player + " has won!");
        alert.showAndWait();
        game.startGame();
    }

    @Override
    public void onGameDraw() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Stalemate.");
        alert.showAndWait();
        game.startGame();
    }

    private void setButtonsHighlighted(List<Position> selectedPositions, boolean highlighted) {
        for (Position position : selectedPositions) {
            TicTacToeButton button = buttons[position.getRow()][position.getCol()];
            button.setHighlighted(highlighted);
        }
    }

    /**
     * Get node posiiotn in gridpane
     * @param node node to get position
     * @return node position
     */
    private Position getPosition(Node node) {
        // ou então guardar as coordenadas no TicTacToeButton
        Integer row = GridPane.getRowIndex(node);
        Integer col = GridPane.getColumnIndex(node);
        return new Position(row, col);
    }

    class ButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            TicTacToeButton source = (TicTacToeButton) event.getSource();
            Position position = getPosition(source);
            TicTacToeBoard.this.game.positionSelected(position);
            // ou apenas game.placeClicked(position);
        }
    }
}

















package pt.ipbeja.po2.tictactoe.tui;

import pt.ipbeja.po2.tictactoe.model.*;

import java.util.Locale;
import java.util.Scanner;

/**
 * @author Diogo Pina Manique
 * @version 13/03/2021
 */

public class TicTacToeTUI implements View {

    private static final Scanner SCANNER = new Scanner(System.in);
    private final TicTacToeGame game;


    public static void main(String[] args) {
        TicTacToeGame game = new TicTacToeGame();
        TicTacToeTUI ticTacToeTui = new TicTacToeTUI(game);
        ticTacToeTui.play();
    }

    public TicTacToeTUI(TicTacToeGame game) {
        this.game = game;
        this.game.setGameView(this);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void play() {
        this.game.startGame();
        while (true) {
            Position position = readPlayInput();
            game.positionSelected(position);
        }
    }

    private Position readPlayInput() {
        System.out.println("Insert play (row,col) -> 0,2");
        String s = SCANNER.nextLine();
        String[] split = s.split(",");
        return new Position(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    @Override
    public void onBoardMarkChanged(Mark place, Position position) {
        // unused here
    }

    @Override
    public void onBoardCreated(Mark[][] board) {
        drawBoard(board);
    }

    @Override
    public void onBoardChanged(Mark[][] board) {
        drawBoard(board);
    }

    private void drawBoard(Mark[][] board) {
        for (Mark[] places : board) {
            for (Mark place : places) {
                switch (place) {
                    case X_MARK:
                        System.out.print("X");
                        break;
                    case O_MARK:
                        System.out.print("O");
                        break;
                    case EMPTY:
                        System.out.print("_");
                        break;
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    @Override
    public void onGameWon(Player place) {
        askPlayAgain("Player " + place + " won!");
    }

    @Override
    public void onGameDraw() {
        askPlayAgain("Game ended in a draw");
    }

    private void askPlayAgain(String gameOverMessage) {
        System.out.println(gameOverMessage);
        System.out.println("Would you like to play again? (y/n)");
        String answer = SCANNER.nextLine();
        if(answer.equalsIgnoreCase("n")) {
            System.exit(0);
        }
        else {
            game.startGame();
        }
    }


}

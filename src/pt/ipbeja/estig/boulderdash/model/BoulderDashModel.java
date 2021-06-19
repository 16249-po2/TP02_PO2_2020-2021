package pt.ipbeja.estig.boulderdash.model;

import pt.ipbeja.estig.boulderdash.gui.View;

import java.util.*;

/**
 * The boulderdash puzzle model
 *
 * @author João Paulo Barros
 * @version 2014/05/19 - 2016/04/03 - 2017/04/19 - 2019/05/06 - 2021/05/18 - 2021/05/21
 */
public class BoulderDashModel {
    private static GetMap getMap;
    private Board board;

    public static final int N_LINES = getN_LINES();
    public static final int N_COLS = getN_COLS();

    private final static Random RAND = new Random();
    private final static int[][] NEIGHBORS = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};


    private Deque<Move> moves;

    private Timer timer;
    private int timerValue;

    private View view;

    public static int getN_LINES(){
        return getMap.mapDimensions()[0];
    }

    public static int getN_COLS(){
        return getMap.mapDimensions()[1];
    }


    /**
     * Creates board in winning position
     */
    public BoulderDashModel(View view) {
        this.moves = new ArrayDeque<>();
        this.board = new Board(N_LINES, N_COLS);
        this.board.resetBoard();
        this.timer = new Timer();
        this.view = view;
    }

    /**
     * Creates a random mixed board starting from a winning position
     *
     * @param minIter        minimum number of iterations to mix board
     * @param additionalIter maximum number of additional iterations to mix board
     */
    public BoulderDashModel(View view, int minIter, int additionalIter) {
        this(view); // call default constructor Fifteen()
        this.mix(minIter, additionalIter);
        this.resetTimer();
        this.startTimer();
    }


    /**
     * mixes the puzzle with random moves
     *
     * @param minMoves minimum of moves
     * @param maxMoves maximum of moves
     */
    public void mix(int minMoves, int maxMoves) {
        assert (minMoves <= maxMoves);
        // see http://docs.oracle.com/javase/8/docs/api/java/util/Deque.html
        AbstractPosition empty = new AbstractPosition(N_LINES - 1, N_COLS - 1, 'L');
        int nMoves = minMoves + RAND.nextInt(maxMoves - minMoves + 1);

        for (int i = 0; i < nMoves; i++) {
            AbstractPosition pieceToMove = this.randomlySelectNeighborOf(empty);
            Move m = new Move(pieceToMove, empty); // occupy empty space
            this.board.applyMove(m);
            empty = pieceToMove; // moved piece position is now the empty
            // position
            this.moves.addFirst(m); // add at head (begin) of deque
        }
    }

    /**
     * Solve the puzzle using the stored positions
     */
    public void solve() {
        this.unmix(500);
    }

    /**
     * rewinds the puzzle with given moves and applies the reverse of each move
     *
     * @param sleepTime time between each move
     */
    public void unmix(int sleepTime) {
//        Runnable task = () -> {
//            Move m;
//            while ((m = moves.poll()) != null) {
//                Move mr = m.getReversed();
//                board.applyMove(mr);
//                BoulderDashModel.sleep(sleepTime);
//
//                notifyViews(mr, timerValue);
//
//            }
//        };
//        Thread threadToUnmix = new Thread(task);
//        threadToUnmix.start();
    }





    /**
     * Tries to move a piece at abstractPosition If moved notifies views
     *
     * @param abstractPosition abstractPosition of piece to move
     */
    private void movePieceAt(AbstractPosition abstractPosition) {
        if (abstractPosition.isInside()) {
            AbstractPosition emptyPos = this.board.getEmptyInNeighborhood(abstractPosition);
            if (emptyPos != null) {
                Move newMove = new Move(abstractPosition, emptyPos);
                this.board.applyMove(newMove);
                this.moves.addFirst(newMove); // add at head (begin) of deque
                this.notifyViews(newMove, timerValue);
            }
        }
    }

    /**
     * Notify observers using methods inherited from from class Observable
     *
     * @param move    the executed move
     * @param tValue  current time count
     */
    private void notifyViews(Move move, int tValue) {

        this.view.notifyView(move, tValue);
    }

    /**
     * Gets last executed move
     *
     * @return the last move
     */
    public Move getLastMove() {
        return this.moves.getFirst();
    }


    /**
     * Randomly selects position that can be moved to the empty position
     *
     * @param empty the empty position
     * @return the selected neighbor position
     */
    private AbstractPosition randomlySelectNeighborOf(AbstractPosition empty) {
        int line = 0;
        int col = 0;
        do {
            int[] pos = NEIGHBORS[RAND.nextInt(NEIGHBORS.length)];
            line = empty.getLine() + pos[0];
            col = empty.getCol() + pos[1];
        } while (AbstractPosition.isInside(line, col) == false);
        return new AbstractPosition(line, col, 'L');
    }


    public void pieceSelected(AbstractPosition pos) {
        this.movePieceAt(pos);
    }

    public void keyPressed(Direction direction) {
        AbstractPosition pos = this.board.getPositionNextToEmpty(direction);
        this.movePieceAt(pos);
    }


    /**
     * Wait the specified time in milliseconds
     *
     * @param sleepTime time in miliseconds
     */
    public static void sleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            // nothing to do
        }
    }


    /**
     * Creates a new timer and sets the timer count to zero
     */
    public void resetTimer() {
        this.timerValue = -1;
        this.timer = new Timer();
    }

    /**
     * Starts timer
     */
    public void startTimer() {
        this.resetTimer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                timerValue++;
                notifyViews(null, timerValue);
            }
        };
        this.timer.schedule(timerTask, 0, 1000);
    }

    /**
     * Stops the current timer
     */
    public void stopTimer() {
        timer.cancel();
    }

    /**
     * Get current timer value
     *
     * @return time in seconds
     */
    public int getTimerValue() {
        return this.timerValue;
    }

}

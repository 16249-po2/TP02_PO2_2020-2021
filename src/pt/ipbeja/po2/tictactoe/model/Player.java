package pt.ipbeja.po2.tictactoe.model;

public enum Player {
    X(Mark.X_MARK), O(Mark.O_MARK);

    private final Mark mark;

    Player(Mark mark) {
        this.mark = mark;
    }

    public Mark getMark() {
        return mark;
    }
}

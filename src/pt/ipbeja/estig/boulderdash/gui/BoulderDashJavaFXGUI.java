package pt.ipbeja.estig.boulderdash.gui;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import pt.ipbeja.estig.boulderdash.model.*;

import java.util.HashMap;
import java.util.Map;

//transparent
//https://stackoverflow.com/questions/34033119/how-to-make-transparent-scene-and-stage-in-javafx

/**
 * The boulderdash main view
 *
 * @author João Paulo Barros
 * @version 2021/05/20
 */
public class BoulderDashJavaFXGUI extends Application implements View {
    private static final String ICON_FILE = "/resources/images/puzzle15.jpg";
    private final BoulderDashModel model;

    private final PositionImage[][] positionImages;
    private Button closeButton;
    private Button solveButton;
    private Label timeLabel;
    private Pane pane;

    private static final Map<KeyCode, Direction> directionMap = new HashMap<>();

    static {
        directionMap.put(KeyCode.UP, Direction.UP);
        directionMap.put(KeyCode.DOWN, Direction.DOWN);
        directionMap.put(KeyCode.LEFT, Direction.LEFT);
        directionMap.put(KeyCode.RIGHT, Direction.RIGHT);
    }

    /**
     * Start program
     *
     * @param args currently not used
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Create window with board
     */
    public BoulderDashJavaFXGUI() {
        this.model = new BoulderDashModel(this);
        this.model.mix(5, 10);
        this.positionImages = new PositionImage[BoulderDashModel.N_LINES][BoulderDashModel.N_COLS];
    }

    @Override
    public void start(Stage stage) {
        this.setAppIcon(stage, ICON_FILE);
        stage.setTitle("Fifteen Puzzle");

        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(this.createScene());
        stage.show();

        this.model.startTimer();
    }

    /**
     * Executed on exit to stop all threads
     */
    @Override
    public void stop() {
        System.out.println("END");
        System.exit(0);
    }

    private void setAppIcon(Stage stage, String filename) {
        try {
            Image ico = new Image(filename);
            stage.getIcons().add(ico);
        } catch (Exception ex) {
            System.err.println("Error setting icon");
        }
    }

    private Scene createScene() {
        VBox vbxMain = new VBox();
        vbxMain.setStyle("-fx-border-color: black;\n" + "-fx-background-color: transparent");

        this.closeButton = new Button("Quit");
        this.closeButton.setMaxWidth(Integer.MAX_VALUE);
        this.closeButton.setStyle("-fx-background-color: #FF9933; ");
        this.closeButton.setOnAction(event -> System.exit(0) );

        this.solveButton = new Button("Solve");
        this.solveButton.setMaxWidth(Integer.MAX_VALUE);
        this.solveButton.setStyle("-fx-background-color: #CCFFCC; ");
        this.solveButton.setOnAction(event -> {
            pane.setDisable(true);
            solveButton.setDisable(true);
            model.solve();
        });

        this.timeLabel = new Label(this.model.getTimerValue() + "");
        this.timeLabel.setMaxWidth(Integer.MAX_VALUE);
        this.timeLabel.setStyle("-fx-background-color: #33ff33; -fx-alignment:center; " +
                                "-fx-font-size: 1cm;");

        this.pane = new Pane();
        this.pane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

        this.createPieces();

        vbxMain.getChildren().addAll(
                this.closeButton,
                this.solveButton,
                this.timeLabel,
                this.pane);

        Scene scene = new Scene(vbxMain, Color.TRANSPARENT);
        this.setKeyHandle(scene);

        return scene;
    }

    /**
     * Defining image from processed text file to GUI tiles
     * 2021-06-10
     */
    private void createPieces() {
        int nLines = BoulderDashModel.N_LINES;
        int nCols = BoulderDashModel.N_COLS;
        char[][] arr = GetMap.mapTopography();

        // can be adapted to read the map from the file
        this.pane.getChildren().clear();
        for (int line = 0; line < nLines; line++) {
            for (int col = 0; col < nCols; col++) {
                char text = arr[line][col];
                AbstractPosition pos = new AbstractPosition(line, col, text);
                PositionImage pi = new PositionImage("" + text, pos);
                this.pane.getChildren().add(pi); // add to gui
                this.positionImages[line][col] = pi; // add to array
                pi.setOnMouseClicked(this::handle);
            }
        }
    }

    /**
     * Handle button press by asking the model to execute the respective actions
     * The model is then responsible to notify this (and other) views
     */
    public void handle(MouseEvent e) {
        PositionImage pi = (PositionImage) e.getSource();
        AbstractPosition pos = pi.getLineCol();
        model.pieceSelected(pos); // inform model
    }

    void setKeyHandle(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            Direction dir = BoulderDashJavaFXGUI.directionMap.get(keyEvent.getCode());
            if (dir != null) {
                model.keyPressed(dir);
            } else {
                // close stage (and program) with escape ou Q keys
                if (keyEvent.getCode() == KeyCode.ESCAPE || keyEvent.getCode() == KeyCode.Q) {
                    Stage stage = (Stage)scene.getWindow();
                    stage.close();
                }
            }
        });
    }

    /**
     * Updates the pieces content by asking the model. After a win
     */
    private void restart() {
        this.model.mix(5, 10);
        this.pane.setDisable(false);
        this.model.startTimer();
        this.createPieces();
        this.solveButton.setDisable(false);
    }

    public void updateLayoutAfterMove(Move lastMove) {
        if (lastMove != null) {
            int beginLine = lastMove.getBegin().getLine();
            int beginCol = lastMove.getBegin().getCol();
            char beginText = lastMove.getBegin().getText();
            int endLine = lastMove.getEnd().getLine();
            int endCol = lastMove.getEnd().getCol();
            char endText = lastMove.getEnd().getText();


            PositionImage imageToMove = this.positionImages[beginLine][beginCol];
            PositionImage imageToReplace = this.positionImages[endLine][endCol];

            TranslateTransition tt =
                    new TranslateTransition(Duration.millis(500), imageToMove);
            int dCol = endCol - beginCol;
            int dLine = endLine - beginLine;
            tt.setByX(dCol * PositionImage.SIZE);
            tt.setByY(dLine * PositionImage.SIZE);
            tt.play();

            imageToMove.updateLineCol(dCol, dLine);
            this.positionImages[endLine][endCol] = imageToMove;

            imageToReplace.setLineColAndXY(new AbstractPosition(beginLine, beginCol, beginText));
            this.positionImages[beginLine][beginCol] = imageToReplace;
        }
    }

    @Override
    public void notifyView(Move lastMove, Boolean wins, int timerValue) {
        Platform.runLater(() -> {
            if (lastMove != null) {
                this.updateLayoutAfterMove(lastMove);
            }
            if (wins) {
                this.model.stopTimer();
                new Alert(AlertType.INFORMATION, "You win! ").showAndWait();
                this.restart();
            }
            this.timeLabel.setText(timerValue + "");
        });
    }
}

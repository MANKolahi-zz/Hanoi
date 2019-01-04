package com.MANK.App.Hanoi;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.out;

public final class HanoiDiskController {


    @FXML
    private Rectangle disc0;
    @FXML
    private Rectangle disc1;
    @FXML
    private Rectangle disc2;
    @FXML
    private Rectangle disc3;
    @FXML
    private Rectangle disc4;
    @FXML
    private Rectangle disc5;
    @FXML
    private Rectangle disc6;
    @FXML
    private Rectangle disc7;
    @FXML
    private Rectangle disc8;
    @FXML
    private Rectangle disc9;
    @FXML
    private Rectangle disc10;


    @FXML
    private ToggleGroup DiscNum;
    @FXML
    private RadioMenuItem num1;
    @FXML
    private RadioMenuItem num2;
    @FXML
    private RadioMenuItem num3;
    @FXML
    private RadioMenuItem num4;
    @FXML
    private RadioMenuItem num5;
    @FXML
    private RadioMenuItem num6;
    @FXML
    private RadioMenuItem num7;
    @FXML
    private RadioMenuItem num8;
    @FXML
    private RadioMenuItem num9;
    @FXML
    private RadioMenuItem num10;
    @FXML
    private RadioMenuItem num11;
    @FXML
    private TextField DiscNumDisplay;

    private Rectangle[] discs;
    private int discNum = 1;
    private Disc[] vDisc;

    @FXML
    private Rectangle pivot1;
    private Pivot pivotA;
    @FXML
    private Rectangle pivot2;
    private Pivot pivotB;
    @FXML
    private Rectangle pivot3;
    private Pivot pivotC;

    @FXML
    private Button Button;
    @FXML
    private Button pauseButton;
    @FXML
    private Button resumeButton;
    @FXML
    private Pane pane;

    @FXML
    private ToggleGroup SpeedGroup;
    @FXML
    private RadioMenuItem S1;
    @FXML
    private RadioMenuItem S2;
    @FXML
    private RadioMenuItem S3;
    @FXML
    private RadioMenuItem S4;
    @FXML
    private RadioMenuItem S5;
    @FXML
    private RadioMenuItem S6;
    @FXML
    private TextField speedDisplay;

    private int animationTime = 100;

    private List<PathTransition> pathList;
    private AnimationPlayer<PathTransition> animationPlayer;
    private boolean animationRunning = false;
    private HanoiResolver hanoiResolver ;

    @FXML
    void startButtonPressed(ActionEvent event){
        if(!confirmation())return;
    	out.println("\nHanoiResolver started;");
    	animationPlayer = new AnimationPlayer<>();
    	double startTime = System.currentTimeMillis();
    	hanoiResolver  = new HanoiResolver(new Pivot[]{pivotA,pivotB,pivotC},discNum,animationPlayer,animationTime,discs,vDisc);
        ExecutorService executor = Executors.newSingleThreadExecutor();
    	executor.execute(hanoiResolver);
		animationPlay();
    	executor.shutdown();
    }

    @FXML
    void resumeButtonPressed(ActionEvent event){
        if(animationRunning) {
            animationPlayer.play();
            pauseButton.setVisible(true);
            resumeButton.setVisible(false);
        }
    }

    @FXML
    void pauseButtonPressed(ActionEvent event) {
        if(animationRunning) {
            animationPlayer.pause();
            pauseButton.setVisible(false);
            resumeButton.setVisible(true);
        }
    }

    @FXML
    void MenuItemSelect(ActionEvent event) {
        discNum = (int) DiscNum.getSelectedToggle().getUserData();
        DiscNumDisplay.setText(String.valueOf(discNum));
    }

    @FXML
    void speedSelect(ActionEvent event){
        animationTime = (int) SpeedGroup.getSelectedToggle().getUserData();
        speedDisplay.setText(String.format("%.3f s", (float)animationTime/1000));
    }

    private boolean confirmation(){
        if(animationRunning){
            animationPlayer.pause();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Do you want to stopAll animation and play another one ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                resumeButton.fire();
                /*
                * hanoiResolver must stop before animationPlayer;
                * hanoiResolver will stop and finish after animation.stop() clear the BlockingQueue and
                * realize the hanoiResolver LOCK in BlockingQueue;
                */
                /*1*/hanoiResolver.stop();
                /*2*/animationPlayer.stop();
                return true;
            }
            else{
                if(!resumeButton.isVisible())animationPlayer.play();
                return false;
            }
        }
        return true;
    }

    private void animationPlay(){
        animationPlayer.setOnFinished(event -> { animationRunning = false; });
        animationRunning = true;
        animationPlayer.play();
    }

    private void setDiscNumMenuItemData(){
        num1.setUserData(1);
        num2.setUserData(2);
        num3.setUserData(3);
        num4.setUserData(4);
        num5.setUserData(5);
        num6.setUserData(6);
        num7.setUserData(7);
        num8.setUserData(8);
        num9.setUserData(9);
        num10.setUserData(10);
        num11.setUserData(11);
    }

    private void setSpeedMenuItemData(){
        S1.setUserData(1);
        S2.setUserData(50);
        S3.setUserData(100);
        S4.setUserData(200);
        S5.setUserData(500);
        S6.setUserData(1000);
    }

    private void setDiscsArray(){
        discs = new Rectangle[11];
        discs[0] = disc0;
        discs[1] = disc1;
        discs[2] = disc2;
        discs[3] = disc3;
        discs[4] = disc4;
        discs[5] = disc5;
        discs[6] = disc6;
        discs[7] = disc7;
        discs[8] = disc8;
        discs[9] = disc9;
        discs[10] = disc10;
    }

    private void setVDisc(){
        vDisc = new Disc[11];
        Arrays.setAll(vDisc, i -> new Disc(0, 0, (int) discs[i].getWidth(), (int) discs[i].getHeight()));
    }

    private void InitializeDisplayFields(){
        DiscNumDisplay.setFocusTraversable(false);
        DiscNumDisplay.setEditable(false);
        speedDisplay.setEditable(false);
        speedDisplay.setFocusTraversable(false);
    }

    private void InitializePivots(){
        pivotA = new Pivot
                ((int) pivot1.getHeight(), (int) pivot1.getWidth(),
                        (int) pivot1.getLayoutY(), (int) pivot1.getLayoutX(),"pivotA");
        pivotB = new Pivot
                ((int) pivot2.getHeight(), (int) pivot2.getWidth(),
                        (int) pivot2.getLayoutY(), (int) pivot2.getLayoutX(),"pivotB");
        pivotC = new Pivot
                ((int) pivot3.getHeight(), (int) pivot3.getWidth(),
                        (int) pivot3.getLayoutY(), (int) pivot3.getLayoutX(),"pivotC");
    }

    private void setBackground(){
        try {
            Image bg = new Image("/paneBackground.jpg");
            BackgroundImage bgImg = new BackgroundImage(bg,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,
                            false, false, true, true));
            pane.setBackground(new Background(bgImg));
        }catch (Exception e){
            out.println("Background not loaded");
        }
    }

    public void initialize() {
        setDiscNumMenuItemData();
        setSpeedMenuItemData();
        setDiscsArray();
        setVDisc();
        InitializeDisplayFields();
        Platform.runLater(() -> {
            Button.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
                Button.fire();
            });
        });
        InitializePivots();
        InitializeDisplayFields();
        setBackground();
        Thread.currentThread().setName("Controller");
        hanoiResolver = new HanoiResolver(new Pivot[]{pivotA,pivotB,pivotC},discs,vDisc);
        resumeButton.setVisible(false);
        pauseButton.setVisible(true);
    }

}

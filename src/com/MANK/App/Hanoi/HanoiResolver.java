package com.MANK.App.Hanoi;

import javafx.animation.PathTransition;
import javafx.scene.shape.Rectangle;

import java.time.Duration;
import java.time.Instant;

public class HanoiResolver implements Runnable{

    private final Pivot pivots[];
    private final Disc virtualDisks[];
    private final Rectangle disks[];
    private int diskNumber;
    private AnimationPlayer<PathTransition> animationPlayer;
    private int animationTime;
    private boolean stop = false;

    public HanoiResolver(Pivot[] pivots,
                         int diskNumber, AnimationPlayer<PathTransition> animationPlayer, int animationTime,
                         Rectangle[] disks, Disc[] virtualDisks){
        this.pivots = pivots;
        this.diskNumber = diskNumber;
        this.animationPlayer = animationPlayer;
        this.animationTime = animationTime;
        this.disks = disks;
        this.virtualDisks = virtualDisks;
    }

    HanoiResolver(Pivot [] pivots, Rectangle[] disks, Disc[] virtualDisks ){
        this(pivots,-1,null,-1,disks,virtualDisks);
    }

    @Override
    public void  run() {
        Thread.currentThread().setName(String.format("HanoiResolver %d", System.currentTimeMillis()%100));
        if(animationTime == -1||diskNumber == -1 || animationPlayer == null){
            System.out.println(Thread.currentThread().getName() + " run failed");
            return;
        }
        System.out.println(Thread.currentThread().getName() + " Starts.");
        Instant startTime = Instant.now();
        setDisc();
        try {
            HanoiResolve(
                    pivots[0],
                    pivots[1],
                    pivots[2],
                    diskNumber
            );
        }catch (ArrayIndexOutOfBoundsException ex){
            System.out.printf("%nError in %s Pivots : %s%s%s%n",
                    Thread.currentThread().getName(), pivots[0] , pivots[1] , pivots[2]);
        }
        animationPlayer.animationEnd();
        Instant endTime = Instant.now();
        System.out.printf
                ("%s completed in : %d milliSeconds;%n",
                        Thread.currentThread().getName(), Duration.between(startTime,endTime).toMillis());
    }

    private void Translation(Pivot p1, Pivot p2) {
        int indexTemp = p1.removeFirstDisc();
        this.animationPlayer.addAnimation(
                p2.inputPath(
                        disks[indexTemp],
                        virtualDisks[indexTemp],
                        indexTemp,
                        animationTime
                ));
    }

    private void HanoiResolve(Pivot p1, Pivot p2, Pivot p3, int discNum) {
        if(stop)return;
        if(discNum == 1) Translation(p1,p3);
        else {
            HanoiResolve(p1, p3, p2, discNum - 1);
            Translation(p1,p3);
            HanoiResolve(p2, p1, p3, discNum - 1);
        }
    }

    public void stop(){
        stop = true;
    }

    private void setDisc() {
        pivots[0].reset();
        pivots[1].reset();
        pivots[2].reset();
        try {
            for (Rectangle rect : disks) {
                rect.setVisible(false);
            }
            for (int i = diskNumber - 1; i >= 0; i--) {
                try {
                    pivots[0].addDisc(disks[i], virtualDisks[i], i);
                    disks[i].setVisible(true);
                    disks[i].setTranslateX(0);
                    disks[i].setTranslateY(0);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("exception i : " + i);
                    return;
                }
            }
        }catch (Exception e){
            System.out.println("An unexpected exception occurred in setDisc() in " + Thread.currentThread().getName());
        }
        System.out.printf("%s Discs Set end: %s%s%s%n" ,Thread.currentThread().getName(), pivots[0] , pivots[1] , pivots[2]);
    }

    public void initialize(int animationTime,int diskNumber,AnimationPlayer<PathTransition> animationPlayer){
        stop = false;
        this.animationPlayer = animationPlayer;
        this.animationTime = animationTime;
        this.diskNumber = diskNumber;
    }

}

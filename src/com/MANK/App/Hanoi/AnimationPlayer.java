package com.MANK.App.Hanoi;

import javafx.animation.Animation;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.lang.System.out;


public class AnimationPlayer<AnimationType extends Animation> {

    private final BlockingQueue<AnimationType> animationBlockingQueue;
    private int delay = 1;
    private final static int MAXIMUM_RETRY = 50;
    private boolean pause;
    private boolean stop;
    private boolean animationMakerEnd = false;
    private AnimationType lastAnimation;


    private ObjectProperty<EventHandler<ActionEvent>> onFinished;
    private static final EventHandler<ActionEvent> DEFAULT_ON_FINISHED = null;

    public AnimationPlayer(){
        animationBlockingQueue = new ArrayBlockingQueue<>(1);
        pause = false;
        stop = false;
    }

    private void animationPlay() {
        if (stop || pause) return;
        if(animationBlockingQueue.isEmpty()){
            if(!retry(MAXIMUM_RETRY)){
                lastAnimation = null;
                onFinishedExecute();
                return;
            }
        }
        try {
            lastAnimation = animationBlockingQueue.take();
            lastAnimation.play();
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (NullPointerException ex) {
            stop();
        }
        lastAnimation.setOnFinished(event -> {animationPlay();});
    }

    private boolean retry(int retryCount){
        if(retryCount == 0) return false;
        else {
            try {
             Thread.sleep(1);
            }catch (InterruptedException ex){
                Thread.currentThread().interrupt();
            }
            if(animationBlockingQueue.isEmpty())
                return retry(retryCount - 1);
            else {
                out.println("animationPlayer retry counter : " + (MAXIMUM_RETRY - retryCount + 1));
                return true;
            }
        }
    }

    private void onFinishedExecute(){
        final EventHandler<ActionEvent> handler = onFinished.get();
        if (handler != null) {
            handler.handle(new ActionEvent(this, null));
        }
    }

    @SafeVarargs
    public final synchronized void addAnimation(AnimationType... animations){
        for (AnimationType animation :
               animations ) {
            try {
                animationBlockingQueue.put(animation);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void animationEnd() {
        this.animationMakerEnd = true;
    }

    public void pause(){
        pause = true;
        lastAnimation.pause();
        out.println("AnimationPlayer Pause.");
    }

    public void stop(){
        stop = true;
        animationBlockingQueue.clear();
        if(lastAnimation!=null)lastAnimation.stop();
        lastAnimation = null;
        onFinishedExecute();
        out.println("AnimationPlayer Stop.");
    }

    public void play() {
        stop = false;
        pause = false;
        if(lastAnimation == null) animationPlay();
        else {
            out.println("animationPlayer Resume.");
            lastAnimation.play();
        }
    }

    public final void setOnFinished(EventHandler<ActionEvent> value) {
        if ((onFinished != null) || (value != null /* DEFAULT_ON_FINISHED */)) {
            onFinishedProperty().set(value);
        }
    }

    private ObjectProperty<EventHandler<ActionEvent>> onFinishedProperty() {
        if (onFinished == null) {
            onFinished = new SimpleObjectProperty<EventHandler<ActionEvent>>(this, "onFinished", DEFAULT_ON_FINISHED);
        }
        return onFinished;
    }

}

package com.MANK.App.Hanoi;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.out;

class Pivot{
	private int height;
	private int width;
	private int layoutY;
	private int layoutX;
	private Stack<Integer> discsSpace;
	private final String pivotName;
	private final ReentrantLock lock = new ReentrantLock();

    Pivot(int height, int width, int layoutY, int layoutX, String pivotName) {
		this.height = height;
		this.width = width;
		this.layoutY = layoutY;
		this.layoutX = layoutX;
		this.pivotName = pivotName;
		this.discsSpace = new Stack<>();
    }

	void addDisc(Rectangle disc, Disc vDisc, int discIndex) {

		this.discsSpace.push(discIndex);
		disc.setY
				(getPivotFloor() - (discsSpace.size() * disc.getHeight()) );
		disc.setX
				(getPivotMiddleX() -(float) vDisc.getWidth()/2);
		vDisc.setY
				((int) (getPivotFloor() + vDisc.getHeight()/2 - (discsSpace.size() * disc.getHeight())));
		vDisc.setX
				((getPivotMiddleX()));
	}

	PathTransition inputPath(Rectangle disc, Disc vDisc, int discIndex, int animationTime) {
		this.discsSpace.push(discIndex);
		Path path = new Path(
				new MoveTo(vDisc.getX()  , vDisc.getY()),
				new LineTo(vDisc.getX(),this.layoutY - disc.getHeight()),
				new LineTo(getPivotMiddleX()  ,this.layoutY - vDisc.getHeight()),
				new LineTo(getPivotMiddleX(),
						getPivotFloor() + (float)vDisc.getHeight()/2 - (discsSpace.size() * vDisc.getHeight()))
		);
		vDisc.setY
				((int) (getPivotFloor() + vDisc.getHeight()/2 - (discsSpace.size() * disc.getHeight())));
		vDisc.setX
				((getPivotMiddleX() ));
		PathTransition animation = new PathTransition(
				Duration.millis(animationTime),
				path,
				disc
		);
		animation.setAutoReverse(true);
		animation.setInterpolator(Interpolator.EASE_BOTH);
		animation.setCycleCount(1);
		return animation;
	}

	int removeFirstDisc() {
		try {
			return this.discsSpace.pop();
		}catch (ArrayIndexOutOfBoundsException ex){
			out.printf("%nArray out of Bounds Exception in %s: %nDiscsNum = %d%nPivot name : %s%n",
					Thread.currentThread().getName(), discsSpace.size(),pivotName);
			throw ex;
		}
	}

	void reset() {
		discsSpace.removeAllElements();
	}

	private int getPivotMiddleX() {
		return layoutX + (width/2);
	}

	private int getPivotFloor() { return this.height + this.layoutY - 5 ; }

	public String getPivotName() {
		return pivotName;
	}

	public int getDiscsNum() {
		return discsSpace.size();
	}

	@Override
	public String toString(){
    	return String.format("%n%sDisksNum : %d",
		getPivotName(), getDiscsNum());
	}
}
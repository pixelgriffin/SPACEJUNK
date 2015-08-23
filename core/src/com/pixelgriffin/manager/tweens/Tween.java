package com.pixelgriffin.manager.tweens;

import com.pixelgriffin.manager.TweenManager.TweenType;

/**
 * 
 * @author Nathan
 *
 */
public abstract class Tween {
	
	private boolean paused;
	
	protected float time;
	protected float value;
	
	protected float b, c, d;
	
	protected TweenType t;
	
	public Tween(TweenType type, float beginning, float change, float duration) {
		time = 0f;
		value = beginning;
		
		b = beginning;
		c = change;
		d = duration;
		
		t = type;
		
		paused = false;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public void setPaused(boolean pause) {
		paused = pause;
	}
	
	public abstract void update(float dt);
	public abstract float getValue();
	public abstract boolean isDone();
}

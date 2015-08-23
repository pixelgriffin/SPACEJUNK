package com.pixelgriffin.manager.tweens;

import com.pixelgriffin.manager.TweenManager.TweenType;

/**
 * 
 * @author Nathan
 *
 */
public class LinearTween extends Tween {

	public LinearTween(float beginning, float change, float duration) {
		super(TweenType.IN, beginning, change, duration);
	}

	@Override
	public void update(float dt) {
		time += dt;
		if(time > d)
			time = d;
		
		value = ((c * time) / d) + b;
	}

	@Override
	public float getValue() {
		return value;
	}

	@Override
	public boolean isDone() {
		return (time == d);
	}
}

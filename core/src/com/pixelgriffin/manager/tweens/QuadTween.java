package com.pixelgriffin.manager.tweens;

import com.pixelgriffin.manager.TweenManager.TweenType;

/**
 * 
 * @author Nathan
 *
 */
public class QuadTween extends Tween {

	public QuadTween(TweenType type, float beginning, float change, float duration) {
		super(type, beginning, change, duration);
	}

	@Override
	public void update(float dt) {
		time += dt;
		if(time > d)
			time = d;
		
		float tt = time;
		
		if(t == TweenType.INOUT) {
			if((tt /= d / 2) < 1)
				value = c / 2 * tt * tt + b;
			else
				value = -c / 2 * ((--tt) * (tt - 2) - 1) + b;
		} else if(t == TweenType.IN) {
			value = c * (tt /= d) * tt + b;
		} else {
			value = -c * (tt /= d) * (tt - 2) + b;
		}
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

package com.pixelgriffin.manager.tweens;

import com.pixelgriffin.manager.TweenManager.TweenType;

/**
 * 
 * @author Nathan
 *
 */
public class BackTween extends Tween {

	public BackTween(TweenType type, float beginning, float change, float duration) {
		super(type, beginning, change, duration);
	}

	@Override
	public void update(float dt) {
		time += dt;
		if(time > d)
			time = d;
		
		float tt = time;
		
		if(t == TweenType.IN) {
			float s = 1.70158f;
			
			value = c * (tt /= d) * tt * ((s + 1) * tt - s) + b;
		} else if(t == TweenType.OUT) {
			float s = 1.70158f;
			
			value = c * ((tt = tt / d - 1) * tt * ((s + 1) * tt + s) + 1) + b;
		} else {
			float s = 1.70158f;
			
			if((tt /= d / 2) < 1)
				value = c / 2 * (tt * tt * (((s *= (1.525f)) + 1) * tt - s)) + b;
			else
				value = c / 2 * ((tt -= 2) * tt * (((s *= (1.525f)) + 1) * tt + s) + 2) + b;
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

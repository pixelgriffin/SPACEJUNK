package com.pixelgriffin.manager.tweens;

import com.pixelgriffin.manager.TweenManager.TweenType;

/**
 * 
 * @author Nathan
 *
 */
public class CircTween extends Tween {

	public CircTween(TweenType type, float beginning, float change, float duration) {
		super(type, beginning, change, duration);
	}

	@Override
	public void update(float dt) {
		time += dt;
		if(time > d)
			time = d;
		
		float tt = time;
		
		if(t == TweenType.IN) {
			value = -c * ((float)Math.sqrt(1 - (tt /= d) * tt) - 1) + b;
		} else if(t == TweenType.OUT) {
			value = c * (float)Math.sqrt(1 - (tt /= tt / d - 1) * tt) + b;
		} else {
			if((tt /= d / 2) < 1)
				value = -c / 2 * ((float)Math.sqrt(1 - tt* tt) - 1) + b;
			else
				value = c / 2 * ((float)Math.sqrt(1 - (tt -= 2) * tt) + 1) + b;
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

package com.pixelgriffin.manager.tweens;

import com.pixelgriffin.manager.TweenManager.TweenType;

/**
 * 
 * @author Nathan
 *
 */
public class SinTween extends Tween {

	public SinTween(TweenType type, float beginning, float change, float duration) {
		super(type, beginning, change, duration);
	}

	@Override
	public void update(float dt) {
		time += dt;
		if(time > d)
			time = d;
		
		if(t == TweenType.IN)
			value = -c * (float)Math.cos(time / d * (Math.PI / 2)) + c + b;
		else if(t == TweenType.INOUT)
			value = -c / 2 * ((float)Math.cos(Math.PI * time / d) - 1) + b;
		else
			value = c * (float)Math.sin(time / d * (Math.PI / 2)) + b;
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

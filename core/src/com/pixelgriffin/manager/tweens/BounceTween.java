package com.pixelgriffin.manager.tweens;

import com.pixelgriffin.manager.TweenManager.TweenType;

/**
 * 
 * @author Nathan
 *
 */
public class BounceTween extends Tween {

	public BounceTween(TweenType type, float beginning, float change, float duration) {
		super(type, beginning, change, duration);
	}

	@Override
	public void update(float dt) {
		time += dt;
		if(time > d)
			time = d;
		
		if(t == TweenType.IN) {
			value = c - out(d - time, 0, c, d) + b;
		} else if(t == TweenType.OUT) {
			value = out(time, b, c, d);
		} else {
			if(time < d / 2)
				value = c - out(d - time, 0, c, d) + b;
			else
				value = out(time * 2 - d, 0, c, d) * 0.5f + c * 0.5f + b;
		}
	}
	
	private float out(float _t, float _b, float _c, float _d) {
		if((_t /= d) < (1f / 2.75f)) {
			return _c * (7.5625f * _t * _t) + b;
		} else if(_t < (2f / 2.75f)) {
			return _c * (7.5625f * (_t -= (1.5f / 2.75f)) * _t + 0.75f) + b;
		} else if(_t < (2.5f / 2.75f)) {
			return _c * (7.5625f * (_t -= (2.25f / 2.75f)) * _t + 0.9375f) + b;
		} else {
			return _c * (7.5625f * (_t -= (2.625f / 2.75f)) * _t + 0.984375f) + b;
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

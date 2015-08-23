package com.pixelgriffin.manager.tweens;

import com.pixelgriffin.manager.TweenManager.TweenType;

/**
 * 
 * @author Nathan
 *
 */
public class ElasticTween extends Tween {

	public ElasticTween(TweenType type, float beginning, float change, float duration) {
		super(type, beginning, change, duration);
	}

	@Override
	public void update(float dt) {
		time += dt;
		if(time > d)
			time = d;
		
		float tt = time;
		
		if(t == TweenType.IN) {
			if(tt == 0) {
				value = b;
			} else if((tt /= d) == 1) {
				value = b + c;
			} else {
				float p = d * 0.3f;
				float a = c;
				float s = p / 4f;
				
				value = -(a * (float)Math.pow(2, 10 * (tt -= 1)) * (float)Math.sin((tt * d - s) * (2 * (float)Math.PI) / p)) + b;
			}
		} else if(t == TweenType.OUT) {
			if(tt == 0) {
				value = b;
			} else if((tt /= d) == 1) {
				value = b + c;
			} else {
				float p = d * 0.3f;
				float a = c;
				float s = p /4f;
				
				value = (a * (float)Math.pow(2, -10 * tt) * (float)Math.sin((tt * d - s) * (2 * (float)Math.PI) / p) + c + b);
			}
		} else {
			if(tt == 0) {
				value = b;
			} else if((tt /= d / 2) == 2) {
				value = b + c;
			} else {
				float p = d * (0.3f * 1.5f);
				float a = c;
				float s = p / 4f;
				
				if(tt < 1) {
					value = -0.5f * (a * (float)Math.pow(2, 10 * (tt -= 1)) * (float)Math.sin((tt * d - s) * (2 * (float)Math.PI) / p ));
				} else {
					value = a * (float)Math.pow(2, -10 * (tt -= 1)) * (float)Math.sin((tt * d - s) * (2 * (float)Math.PI) / p ) * 0.5f + c + b;
				}
			}
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

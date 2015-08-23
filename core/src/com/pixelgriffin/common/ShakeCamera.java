package com.pixelgriffin.common;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * 
 * @author Nathan
 *
 */
public class ShakeCamera extends OrthographicCamera {
	
	private float duration = 1f;
	private float speed = 10.0f;
	private float mag = 5f;
	
	private float elapsed = -1.0f;
	private float start = 0.0f;
	private Vector3 camPos;
	
	public ShakeCamera() {
		super();
		
		camPos = new Vector3(0, 0, 0);
	}
	
	public void setPosition(float x, float y) {
		this.position.set(x, y, 0);
		camPos.set(x, y, 0);
	}
	
	public void shake() {
		shake(1f, 10f, 10f);
	}
	
	public void shake(float _duration, float _speed, float _mag) {
		duration = _duration;
		speed = _speed;
		mag = _mag;
		
		elapsed = 0.0f;
		start = NumberUtility.randomInt(-1000,  1000);
		camPos.set(this.position);
	}
	
	public void update(float dt) {
		super.update();
		
		if(elapsed != -1.0f) {
			if(elapsed < duration) {
				elapsed += dt;
				
				float perc = elapsed / duration;
				float damp = 1.0f - NumberUtility.clampFloat(2.0f * perc - 1.0f,  0.0f, 1.0f);
				float change = start + speed * perc;
				
				float x = (float)ImprovedNoise.noise(change, 0.0f, 0.0f);
				float y = (float)ImprovedNoise.noise(0.0f, change, 0.0f);
				
				x *= mag * damp;
				y *= mag * damp;
				
				this.position.x = camPos.x + x;
				this.position.y = camPos.y + y;
			} else {
				this.position.set(camPos);
				elapsed = -1.0f;
			}
		}
	}
}

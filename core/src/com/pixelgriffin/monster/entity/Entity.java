package com.pixelgriffin.monster.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * @author Nathan
 *
 */
public abstract class Entity {
	protected float x, y, z;

	public boolean destroyed;
	
	protected Sprite sprite = null;
	
	public Entity() {
		destroyed = false;
		x = y = z = 0f;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
	}
	
	public void setX(float _x) {
		x = _x;
		
		if(sprite != null)
			sprite.setX(x - (sprite.getOriginX()));
	}
	
	public void setY(float _y) {
		y = _y;
		
		if(sprite != null)
			sprite.setY(y - (sprite.getOriginY()));
	}
	
	public void setZ(float _z) {
		z = _z;
	}
	
	public void setRotation(float angle) {
		this.sprite.setRotation(angle);
	}
	
	public float getRotation() {
		return this.sprite.getRotation();
	}
	
	public abstract void update(float dt);
	public abstract void draw(SpriteBatch batch);
}

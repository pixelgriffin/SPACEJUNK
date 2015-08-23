package com.pixelgriffin.monster.entity;

import java.util.HashSet;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.pixelgriffin.common.NumberUtility;
import com.pixelgriffin.monster.entity.parts.Eye;
import com.pixelgriffin.monster.entity.parts.Part;

/**
 * 
 * @author Nathan
 *
 */
public abstract class Core extends Entity {
	
	public float speed;
	public float armor;
	public int turretCount;
	public int laserCount;
	
	public HashSet<Part> parts;
	
	protected Vector2 velocity;
	
	public Core() {
		parts = new HashSet<Part>();
		
		velocity = new Vector2(0, 0);
	}
	
	@Override
	public void update(float dt) {
		for(Part p : parts) {
			p.update(dt);
		}
		
		if(Math.abs(velocity.x + velocity.y) > speed || Math.abs(velocity.x - velocity.y) > speed) {
			velocity.x *= 0.9071;
			velocity.y *= 0.9071;
		}
		
		velocity.x *= 0.9;
		velocity.y *= 0.9;
		
		setX(x + velocity.x);
		setY(y + velocity.y);
	}
	
	@Override
	public void setX(float _x) {
		float dx = _x - this.x;
		for(Part p : parts) {
			p.setX(p.x + dx);
		}
		
		this.x = _x;
		sprite.setX(x - (sprite.getOriginX()));
	}
	
	@Override
	public void setY(float _y) {
		float dy = _y - this.y;
		
		for(Part p : parts) {
			p.setY(p.y + dy);
		}
		
		this.y = _y;
		sprite.setY(y - (sprite.getOriginX()));
	}
	
	public void addVelocityX(float add) {
		velocity.x += add;
		
		if(velocity.x > speed)
			velocity.x = speed;
		else if(velocity.x < -speed)
			velocity.x = -speed;
	}
	
	public void addVelocityY(float add) {
		velocity.y += add;
		
		if(velocity.y > speed)
			velocity.y = speed;
		else if(velocity.y < -speed)
			velocity.y = -speed;
	}
	
	public void setRotation(float angle) {
		if(getRotation() == angle)
			return;
		
		this.sprite.rotate(angle - this.sprite.getRotation());
		System.out.println("core angle: " + this.getRotation());
		//this.sprite.rotate(0.1f);
		
		float dir = 0f;
		for(Part p : parts) {
			dir = angle;
			
			p.setX(x + NumberUtility.lengthdirX(p.distanceToParent, dir));
			p.setY(y + NumberUtility.lengthdirY(p.distanceToParent, dir));
			p.setRotation(angle);
		}
	}
	
	public float getRotation() {
		return this.sprite.getRotation();
	}
	
	public abstract void generate();
}

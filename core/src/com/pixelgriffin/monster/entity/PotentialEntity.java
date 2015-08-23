package com.pixelgriffin.monster.entity;

import com.badlogic.gdx.math.Circle;

/**
 * 
 * @author Nathan
 *
 */
public abstract class PotentialEntity extends Entity {
	public Circle bound = null;
	
	@Override
	public void update(float dt) {
		if(bound != null) {
			this.bound.x = this.x;
			this.bound.y = this.y;
		}
	}
	
	public abstract void collided(PotentialEntity other);
}

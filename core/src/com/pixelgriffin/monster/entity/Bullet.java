package com.pixelgriffin.monster.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.pixelgriffin.common.NumberUtility;
import com.pixelgriffin.manager.TweenManager.TweenType;
import com.pixelgriffin.manager.tweens.BounceTween;
import com.pixelgriffin.manager.tweens.SinTween;
import com.pixelgriffin.manager.tweens.Tween;
import com.pixelgriffin.monster.Game;
import com.pixelgriffin.monster.entity.parts.Part;

/**
 * 
 * @author Nathan
 *
 */
public class Bullet extends PotentialEntity {

	public boolean isPlayerOwned;
	
	private float time;
	private float dir;
	
	public Bullet(boolean isPlayer, boolean flip, float direction) {
		isPlayerOwned = isPlayer;
		dir = direction;
		
		time = 0f;
		
		bound = new Circle(this.x, this.y, 2);
		
		sprite = new Sprite(Game.gfxMgr.getTexture("test", "bullet.png"));
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setRotation(direction);
		
		sprite.setFlip(flip, false);
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		time += dt;
		if(time > 10 || ( this.x < -10 || this.x > 970 || this.y < -10 || this.y > 550)) {
			this.destroyed = true;
			Game.entMgr.removeEntity(this);
			
			return;
		}
		
		if(isPlayerOwned) {
			setX(this.x + NumberUtility.lengthdirX(150 * dt, dir));
			setY(this.y + NumberUtility.lengthdirY(150 * dt, dir));
		} else {
			setX(this.x + NumberUtility.lengthdirX(150 * dt, dir));
			setY(this.y + NumberUtility.lengthdirY(150 * dt, dir));
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	@Override
	public void collided(PotentialEntity other) {
		if(this.destroyed)
			return;
		
		if(!isPlayerOwned) {
			if(other instanceof Part) {
				Poof poof = new Poof();
				poof.setX(this.x);
				poof.setY(this.y);
				
				Game.entMgr.addEntity(poof);
				
				((Part)other).health -= 5;
				this.destroyed = true;
				Game.entMgr.removeEntity(this);
			}
		} else {
			if(other instanceof HumanShip) {
				Poof poof = new Poof();
				poof.setX(this.x);
				poof.setY(this.y);
				
				Game.entMgr.addEntity(poof);
				
				((HumanShip) other).health -= 15;
				this.destroyed = true;
				Game.entMgr.removeEntity(this);
			} else if(other instanceof Earth) {
				Poof poof = new Poof();
				poof.setX(this.x);
				poof.setY(this.y);
				
				Game.entMgr.addEntity(poof);
				
				((Earth) other).health -= 15;
				this.destroyed = true;
				Game.entMgr.removeEntity(this);
			}
		}
	}

}

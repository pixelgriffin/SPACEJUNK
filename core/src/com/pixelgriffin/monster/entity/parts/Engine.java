package com.pixelgriffin.monster.entity.parts;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.pixelgriffin.common.NumberUtility;
import com.pixelgriffin.monster.Game;
import com.pixelgriffin.monster.entity.Laser;
import com.pixelgriffin.monster.entity.PotentialEntity;

/**
 * 
 * @author Nathan
 *
 */
public class Engine extends Part {
	
	private ParticleEmitter emit;
	public Laser laser;
	
	private boolean chargingMyLaser;
	
	public Engine(boolean fallIn, float toX) {
		super(fallIn, toX);
		
		sprite = new Sprite(Game.gfxMgr.getTexture("test", "engine.png"));
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2);
		
		emit = new ParticleEmitter(Game.gfxMgr.getParticleEffect("test", "boss.p").getEmitters().get(0));
		emit.setPosition(this.x, this.y);
		
		laser = null;
		chargingMyLaser = false;
		
		this.bound = new Circle(this.x, this.y, 8);
	}
	
	public void fire() {
		if(emit.isComplete()) {
			emit.start();
			chargingMyLaser = true;
		}
	}
	
	public boolean isFiring() {
		return laser != null;
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		emit.setPosition(this.x + NumberUtility.lengthdirX(24, sprite.getRotation()), this.y + NumberUtility.lengthdirY(24, sprite.getRotation()));
		emit.update(dt * 1.25f);
		
		if(laser != null) {
			laser.setX(this.x + NumberUtility.lengthdirX(550, sprite.getRotation()));
			laser.setY(this.y + NumberUtility.lengthdirY(550, sprite.getRotation()));
			
			
			if(laser.destroyed) {
				laser = null;
			}
		}
		
		//if we were charging and we are now done
		if(chargingMyLaser && emit.isComplete()) {
			chargingMyLaser = false;
			
			laser = new Laser(false);
			laser.setX(this.x + NumberUtility.lengthdirX(550, sprite.getRotation()));
			laser.setY(this.y + NumberUtility.lengthdirY(550, sprite.getRotation()));
			laser.setRotation(sprite.getRotation());
			
			Game.entMgr.addEntity(laser);
			
			Game.gfxMgr.getCamera().shake(4f, 50f, 10f);
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
		
		emit.draw(batch);
	}

	@Override
	public void collided(PotentialEntity other) {
	}

	@Override
	public void destroyed() {
		if(laser != null) {
			laser.destroyed = true;
			Game.entMgr.removeEntity(laser);
			
			return;
		}
	}

}

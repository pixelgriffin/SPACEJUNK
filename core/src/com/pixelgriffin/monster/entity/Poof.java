package com.pixelgriffin.monster.entity;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pixelgriffin.monster.Game;

/**
 * 
 * @author Nathan
 *
 */
public class Poof extends Entity {

	private ParticleEmitter emit;
	
	public Poof() {
		emit = new ParticleEmitter(Game.gfxMgr.getParticleEffect("test", "boss.p").getEmitters().get(2));
		emit.start();
		
		Game.gfxMgr.getSound("test", "hit.wav").play(0.25f);
	}
	
	@Override
	public void update(float dt) {
		emit.setPosition(this.x, this.y);
		emit.update(dt);
		
		if(emit.isComplete()) {
			this.destroyed = true;
			Game.entMgr.removeEntity(this);
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		emit.draw(batch);
	}
}

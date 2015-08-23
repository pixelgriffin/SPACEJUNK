package com.pixelgriffin.monster.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
public class Laser extends Entity {

	private Tween tween;
	private Animation anim;
	private float time;
	
	private boolean turnRed;
	
	public Laser(boolean bad) {
		turnRed = bad;
		
		sprite = new Sprite(Game.gfxMgr.getTexture("test", "laser2_strip4.png"));
		sprite.setScale(0.25f, 7f);
		
		tween = Game.twnMgr.createTween(BounceTween.class, TweenType.OUT, 0.5f, -0.5f, 4f);
		
		TextureRegion[][] frames = TextureRegion.split(sprite.getTexture(), 16, 160);
		TextureRegion[] give = new TextureRegion[4];
		for(int i = 0; i < 4; i++) {
			give[i] = frames[0][i];
		}
		
		anim = new Animation(1/16f, give);
		anim.setPlayMode(PlayMode.LOOP);
		
		sprite.setRegion(anim.getKeyFrame(time));
	}
	
	@Override
	public void update(float dt) {
		if(tween.isDone()) {
			this.destroyed = true;
			Game.entMgr.removeEntity(this);
			
			return;
		}
		
		float lx = 0f;
		float ly = 0f;
		for(int i = -150; i < 70; i++) {
			lx = this.x + NumberUtility.lengthdirX(i * 4, sprite.getRotation());
			ly = this.y + NumberUtility.lengthdirY(i * 4, sprite.getRotation());
			
			
			for(Entity e : Game.entMgr.getEntities()) {
				if(!turnRed) {
					if(e instanceof HumanShip) {
						HumanShip s = (HumanShip)e;
						
						if(s.bound.contains(lx, ly)) {
							s.health -= 2;
							
							int num = NumberUtility.randomInt(0, 10);
							
							if(num == 1) {
								Game.gfxMgr.getSound("test", "hit.wav").play(0.5f);
								Poof poof = new Poof();
								poof.setX(e.getX());
								poof.setY(e.getY());
								
								Game.entMgr.addEntity(poof);
							}
						}
					} else if(e instanceof Earth) {
						Earth s = (Earth)e;
						
						if(s.bound.contains(lx, ly)) {
							s.health -= 2;
							
							int num = NumberUtility.randomInt(0, 10);
							
							if(num == 1) {
								Game.gfxMgr.getSound("test", "hit.wav").play(0.5f);
								Poof poof = new Poof();
								poof.setX(e.getX());
								poof.setY(e.getY());
								
								Game.entMgr.addEntity(poof);
							}
						}
					}
				} else {
					if(e instanceof Part) {
						Part p = (Part)e;
						
						if(p.bound != null) {
							if(p.bound.contains(lx, ly)) {
								p.health -= 2;
								
								int num = NumberUtility.randomInt(0, 10);
								
								if(num == 1) {
									Game.gfxMgr.getSound("test", "hit.wav").play(0.5f);
									Poof poof = new Poof();
									poof.setX(e.getX());
									poof.setY(e.getY());
									
									Game.entMgr.addEntity(poof);
								}
							}
						}
					}
				}
			}
		}
		
		time += dt;
		
		sprite.setRegion(anim.getKeyFrame(time));
		
		sprite.setScale(tween.getValue(), sprite.getScaleY());
		//sprite.setAlpha(tween.getValue());
	}

	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
}

package com.pixelgriffin.monster.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.pixelgriffin.common.NumberUtility;
import com.pixelgriffin.manager.TweenManager.TweenType;
import com.pixelgriffin.manager.tweens.QuadTween;
import com.pixelgriffin.manager.tweens.Tween;
import com.pixelgriffin.monster.Game;
import com.pixelgriffin.monster.scene.GameScene;

/**
 * 
 * @author Nathan
 *
 */
public class Earth extends PotentialEntity {

	public boolean entering;
	public boolean popped;
	
	public int health;
	
	private float change;
	private float shoot;
	private float shoot2;
	
	private Sprite glowSprite;
	
	private Tween moveTweenX;
	private Tween moveTweenY;
	
	private Laser[] laser;
	
	private float shoot2wait;
	private float shoot2cooldown;
	
	public Earth(Difficulty diff) {
		sprite = new Sprite(Game.gfxMgr.getTexture("test", "world.png"));
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		
		glowSprite = new Sprite(Game.gfxMgr.getTexture("test", "glow_red.png"));
		glowSprite.setOrigin(glowSprite.getWidth() / 2, glowSprite.getHeight() / 2);
		
		change = 0f;
		shoot = 0f;
		shoot2 = 0f;
		health = 10000;
		
		switch(diff) {
		case CASUAL:
			shoot2wait = 4f;
			shoot2cooldown = 20f;
			break;
		case NORMAL:
			shoot2wait = 3f;
			shoot2cooldown = 18f;
			
			health += 2500;
			break;
		case HARDCORE:
			shoot2wait = 15f;
			shoot2cooldown = 1f;
			
			health += 7500;
			break;
		}
		
		laser = new Laser[3];
		laser[0] = null;
		
		moveTweenX = null;
		moveTweenY = null;
		
		entering = false;
		popped = false;
		
		this.bound = new Circle(this.x, this.y, 32);
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		if(health <= 0) {
			this.destroyed = true;
			Game.entMgr.removeEntity(this);
			
			return;
		}
		
		glowSprite.setX(this.x - glowSprite.getWidth() / 2 - 1);
		glowSprite.setY(this.y - glowSprite.getHeight() / 2 + 3);
		
		change += dt * 3;
		glowSprite.setScale((float) (Math.sin(change) * 0.5f + 1f) * 2f);
		
		
		if(entering)
			return;
		
		if(shoot2 <= shoot2cooldown && (laser[0] == null || laser[1] == null || laser[2] == null)) {
			sprite.rotate(dt * 16);
			
			if(moveTweenX == null && moveTweenY == null) {
				float changeX = NumberUtility.randomFloat(48 - this.x, (960 - 48) - this.x);
				float changeY = NumberUtility.randomFloat(48 - this.y, (540 - 48) - this.y);
				
				moveTweenX = Game.twnMgr.createTween(QuadTween.class, TweenType.INOUT, this.x, changeX, NumberUtility.randomFloat(0.25f, 2.5f));
				moveTweenY = Game.twnMgr.createTween(QuadTween.class, TweenType.INOUT, this.y, changeY, NumberUtility.randomFloat(0.25f, 2.5f));
			} else {
				if(moveTweenX != null && moveTweenX.isDone()) {
					moveTweenX = null;
				}
				if(moveTweenY != null && moveTweenY.isDone()) {
					moveTweenY = null;
				}
				
				if(moveTweenX != null)
					setX(moveTweenX.getValue());
				if(moveTweenY != null)
					setY(moveTweenY.getValue());
			}
		}
		
		if(shoot2 > shoot2cooldown && shoot2 < (shoot2cooldown + (shoot2wait / 2f))) {
			GameScene scene = (GameScene)Game.entMgr.getGameInstance().getScreen();
			setRotation(NumberUtility.getAngle(x, y, scene.boss.x, scene.boss.y) - 90);
		}
		
		//shooting
		shoot += dt;
		if(shoot >= 1.5f) {
			shoot = 0f;
			
			for(int i = 1; i < 30; i++) {
				Bullet b = new Bullet(false, false, sprite.getRotation() + (i * 3));
				b.setX(this.x - 3);
				b.setY(this.y + (8 * i));
				
				Bullet b2 = new Bullet(false, true, sprite.getRotation() + (i * 3));
				b2.setX(this.x + 3);
				b2.setY(this.y + (8 * i));
				
				Game.entMgr.addEntity(b);
				Game.entMgr.addEntity(b2);
			}
		}
		
		shoot2 += dt;
		
		if(shoot2 >= (shoot2cooldown + shoot2wait)) {
			shoot2 = 0f;
			
			if(laser[0] == null) {
				for(int i = 0; i < 3; i++) {
					laser[i] = new Laser(true);
					laser[i].setX(this.x + NumberUtility.lengthdirX(550, this.getRotation() - 15 + (i * 15)));
					laser[i].setY(this.y + NumberUtility.lengthdirY(550, this.getRotation() - 15 + (i * 15)));
					laser[i].setRotation(this.getRotation() - 15 + (i * 15));
					
					Game.entMgr.addEntity(laser[i]);
				}
				
				Game.gfxMgr.getCamera().shake(4f, 50f, 10f);
			}
		}
		
		/*
		 * Handle laser
		 */
		if(laser[0] != null && laser[1] != null && laser[2] != null) {
			for(int i = 0; i < 3; i++) {
				if(laser[i].destroyed) {
					laser[i] = null;
				}
			}
		}
	}

	public void pop() {
		if(popped)
			return;
		
		popped = true;
		
		for(int i = 0; i < 20; i++) {
			Pilot pilot = new Pilot();
			pilot.setX(x);
			pilot.setY(y);
			
			Game.entMgr.addEntity(pilot);
			
			Game.gfxMgr.getSound("test", "explode.wav").play(0.6f);
		}
		
		Game.gfxMgr.getCamera().shake();
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		if(!destroyed)
			glowSprite.draw(batch);
		
		sprite.draw(batch);
	}

	@Override
	public void collided(PotentialEntity other) {
	}

}

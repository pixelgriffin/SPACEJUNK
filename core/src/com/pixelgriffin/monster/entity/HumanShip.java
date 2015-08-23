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
public class HumanShip extends PotentialEntity {

	public int health;
	
	private float change;
	private float shoot;
	
	private Sprite glowSprite;
	
	private Tween moveTweenX;
	private Tween moveTweenY;
	
	public boolean smart;
	
	private int bulletCount;
	
	public HumanShip(boolean beSmart, Difficulty diff) {
		sprite = new Sprite(Game.gfxMgr.getTexture("test", "human.png"));
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		
		glowSprite = new Sprite(Game.gfxMgr.getTexture("test", "glow_red.png"));
		glowSprite.setOrigin(glowSprite.getWidth() / 2, glowSprite.getHeight() / 2);
		
		smart = beSmart;
		
		change = 0f;
		shoot = 0f;
		
		switch(diff) {
		case CASUAL:
			health = 200;
			bulletCount = 4;
			break;
			
		case NORMAL:
			health = 225;
			bulletCount = 6;
			break;
		
		case HARDCORE:
			health = 270;
			bulletCount = 8;
			break;
		}
		
		moveTweenX = null;
		moveTweenY = null;
		
		this.bound = new Circle(this.x, this.y, 16);
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		if(health <= 0) {
			this.destroyed = true;
			Game.entMgr.removeEntity(this);
			
			Pilot pilot = new Pilot();
			pilot.setX(x);
			pilot.setY(y);
			
			Game.entMgr.addEntity(pilot);
			
			Game.gfxMgr.getSound("test", "explode.wav").play(0.4f);
			
			Game.gfxMgr.getCamera().shake();
			
			return;
		}
		
		glowSprite.setX(this.x - glowSprite.getWidth() / 2 - 1);
		glowSprite.setY(this.y - glowSprite.getHeight() / 2 + 3);
		
		change += dt * 3;
		glowSprite.setScale((float) (Math.sin(change) * 0.5f + 1f) / 4f);
		
		
		GameScene scene = (GameScene)Game.entMgr.getGameInstance().getScreen();
		
		sprite.setRotation(NumberUtility.getAngle(x, y, scene.boss.x, scene.boss.y) - 90);
		
		if(smart) {
			if(moveTweenX == null && moveTweenY == null) {
				float changeX = NumberUtility.randomFloat(48 - this.x, (960 - 48) - this.x);
				float changeY = NumberUtility.randomFloat(48 - this.y, (540 - 48) - this.y);
				
				moveTweenX = Game.twnMgr.createTween(QuadTween.class, TweenType.INOUT, this.x, changeX, NumberUtility.randomFloat(0.5f, 4f));
				moveTweenY = Game.twnMgr.createTween(QuadTween.class, TweenType.INOUT, this.y, changeY, NumberUtility.randomFloat(0.5f, 4f));
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
		
		//shooting
		shoot += dt;
		if(shoot >= 1.5f) {
			shoot = 0f;
			
			for(int i = 1; i < bulletCount; i++) {
				Bullet b = new Bullet(false, false, sprite.getRotation());
				b.setX(this.x - 3);
				b.setY(this.y + (8 * i));
				
				Bullet b2 = new Bullet(false, true, sprite.getRotation());
				b2.setX(this.x + 3);
				b2.setY(this.y + (8 * i));
				
				Game.entMgr.addEntity(b);
				Game.entMgr.addEntity(b2);
			}
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		glowSprite.draw(batch);
		sprite.draw(batch);
	}

	@Override
	public void collided(PotentialEntity other) {
	}

}

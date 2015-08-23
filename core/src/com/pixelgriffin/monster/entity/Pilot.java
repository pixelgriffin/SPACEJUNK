package com.pixelgriffin.monster.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pixelgriffin.common.NumberUtility;
import com.pixelgriffin.monster.Game;

/**
 * 
 * @author Nathan
 *
 */
public class Pilot extends Entity {
	
	private float angle;
	private float time;
	
	private String swear;
	
	public Pilot() {
		sprite = new Sprite(Game.gfxMgr.getTexture("test", "pilot.png"));
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		
		angle = NumberUtility.randomFloat(0f, 359f);
		sprite.setRotation(angle);
		
		time = 10f;
		
		int say = NumberUtility.randomInt(0, 10);
		if(say < 5) {
			switch(say) {
			case 0:
				swear = "lol";
				break;
			case 1:
				swear = "shit!";
				break;
			case 2:
				swear = "aaah!";
				break;
			case 3:
				swear = "dangit";
				break;
			case 4:
				swear = "radical!";
				break;
			default:
				swear = "";
				break;
			}
		} else {
			swear = "";
		}
	}
	
	@Override
	public void update(float dt) {
		sprite.rotate(dt * 50);
		
		time -= dt * 10;
		if(time <= 0 || ( this.x < -10 || this.x > 970 || this.y < -10 || this.y > 550)) {
			this.destroyed = true;
			Game.entMgr.removeEntity(this);
			
			return;
		}
		
		sprite.setAlpha(time / 10f);
		
		setX(x + NumberUtility.lengthdirX(1, angle));
		setY(y + NumberUtility.lengthdirY(1, angle));
	}

	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
		
		Game.gfxMgr.getDefaultFont().setColor(1f, 1f, 1f, time / 10f);
		Game.gfxMgr.getDefaultFont().draw(batch, swear, this.x - 6, this.y + 10);
		Game.gfxMgr.getDefaultFont().setColor(1f, 1f, 1f, 1f);
	}
}

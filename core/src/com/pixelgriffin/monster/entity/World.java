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
public class World extends Entity {
	
	public World() {
		sprite = new Sprite(Game.gfxMgr.getTexture("test", "world.png"));
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
	}
	
	@Override
	public void update(float dt) {
		sprite.rotate(dt * 50);
	}

	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
}

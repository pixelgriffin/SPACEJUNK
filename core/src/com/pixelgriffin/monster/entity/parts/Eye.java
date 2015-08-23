package com.pixelgriffin.monster.entity.parts;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.pixelgriffin.monster.Game;
import com.pixelgriffin.monster.entity.PotentialEntity;

/**
 * 
 * @author Nathan
 *
 */
public class Eye extends Part {
	
	public Eye(boolean fallIn, float toX) {
		super(fallIn, toX);
		
		sprite = new Sprite(Game.gfxMgr.getTexture("test", "eye.png"));
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2);
		
		this.bound = new Circle(this.x, this.y, 11);
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
	}

	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}

	@Override
	public void collided(PotentialEntity other) {
	}

	@Override
	public void destroyed() {
		// TODO Auto-generated method stub
		
	}

}

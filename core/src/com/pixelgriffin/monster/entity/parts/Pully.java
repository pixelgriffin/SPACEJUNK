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
public class Pully extends Part {
	
	public Pully(boolean fallIn, float toX) {
		super(fallIn, toX);
		
		sprite = new Sprite(Game.gfxMgr.getTexture("test", "pully.png"));
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2);
		
		this.bound = new Circle(this.x, this.y, 6);
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

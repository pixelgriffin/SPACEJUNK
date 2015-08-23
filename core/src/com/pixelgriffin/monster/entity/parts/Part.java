package com.pixelgriffin.monster.entity.parts;

import java.util.HashSet;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pixelgriffin.common.NumberUtility;
import com.pixelgriffin.manager.TweenManager.TweenType;
import com.pixelgriffin.manager.tweens.BackTween;
import com.pixelgriffin.manager.tweens.Tween;
import com.pixelgriffin.monster.Game;
import com.pixelgriffin.monster.entity.Entity;
import com.pixelgriffin.monster.entity.PotentialEntity;

/**
 * 
 * @author Nathan
 *
 */
public abstract class Part extends PotentialEntity {
	public HashSet<Part> parts;
	
	public float distanceToParent;
	public float angleOffset;
	
	public int health;
	
	private Tween inTween;
	
	public Part(boolean fallIn, float toX) {
		parts = new HashSet<Part>();
		
		health = 100;
		
		distanceToParent = 0f;
		angleOffset = 0f;
		
		if(fallIn) {
			inTween = Game.twnMgr.createTween(BackTween.class, TweenType.OUT, 600, -600 + toX, 2f);
		} else {
			inTween = null;
		}
	}
	
	public boolean inPlace() {
		return (inTween == null);
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		if(inTween == null)
			return;
		
		setY(inTween.getValue());
		
		if(inTween.isDone())
			inTween = null;
	}
	
	@Override
	public void setX(float _x) {
		float dx = _x - this.x;
		for(Part p : parts) {
			p.setX(p.x + dx);
		}
		
		this.x = _x;
		sprite.setX(x - (sprite.getOriginX()));
	}
	
	@Override
	public void setY(float _y) {
		float dy = _y - this.y;
		
		for(Part p : parts) {
			p.setY(p.y + dy);
		}
		
		this.y = _y;
		sprite.setY(y - (sprite.getOriginX()));
	}
	
	public void setRotation(float angle) {
		this.sprite.setRotation(angle);
	}
	
	public float getRotation() {
		return this.sprite.getRotation();
	}
	
	public abstract void destroyed();
}

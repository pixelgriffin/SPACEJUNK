package com.pixelgriffin.monster.entity;

import java.util.HashSet;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pixelgriffin.common.NumberUtility;
import com.pixelgriffin.monster.Game;
import com.pixelgriffin.monster.entity.parts.*;

/**
 * 
 * @author Nathan
 *
 */
public class PlayerCore extends Core {
	
	private float change = 0f;
	private float turretFireRate = 0f;
	
	private float vol;
	private float time;
	
	private Sound snd;
	private Sound turretSnd;
	
	public PlayerCore() {
		sprite = new Sprite(Game.gfxMgr.getTexture("test", "glow.png"));
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setColor(Color.CYAN);
		
		vol = 0.5f;
		time = 1f;
		
		setZ(10);
		
		snd = Game.gfxMgr.getSound("test", "rumble.wav");
		turretSnd = Game.gfxMgr.getSound("test", "shoot.wav");
	}
	
	public void fireLasers() {
		boolean willFire = false;
		for(Part p : parts) {
			if(p instanceof Engine) {
				((Engine)p).fire();
				
				willFire = true;
			}
		}
		
		if(willFire) {
			Game.gfxMgr.getSound("test", "charge.wav").play(0.75f);
			
			vol = 0.5f;
			time = 1.0f;
		}
	}
	
	public void fireTurrets() {
		if(turretFireRate < 0.05f)
			return;
		
		boolean willFire = false;
		for(Part p : parts) {
			if(p instanceof Cog) {
				Bullet b = new Bullet(true, false, p.sprite.getRotation());
				b.setX(p.x);
				b.setY(p.y);
				
				Game.entMgr.addEntity(b);
				
				willFire = true;
			}
		}
		
		if(willFire)
			turretSnd.play(0.25f);
		
		turretFireRate = 0.0f;
	}
	
	@Override
	public void generate() {
		speed = 1.0f;
		armor = 10.0f;
		turretCount = 0;
		laserCount = 0;
		
		for(Part p : parts) {
			p.destroyed = true;
			Game.entMgr.removeEntity(p);
		}
		parts.clear();
		
		Eye eye = new Eye(true, y);
		eye.setX(x);
		//eye.setY(y);
		eye.setRotation(NumberUtility.randomFloat(0f, 360f));
		
		parts.add(eye);
		Game.entMgr.addEntity(eye);
		
		int levels = NumberUtility.randomInt(75, 125);
		
		for(int xx = -100; xx < 0; xx += NumberUtility.randomInt(26, 32)) {
			for(int yy = 0; yy < levels; yy += NumberUtility.randomInt(26, 32)) {
				CommonPartType type = CommonPartType.values()[NumberUtility.randomInt(0, 5)];
				
				Part pL = null;
				Part pR = null;
				
				switch(type) {
				case COG:
					pL = new Cog(true, y + yy);
					pR = new Cog(true, y + yy);
					break;
					
				case ENGINE:
					pL = new Engine(true, y + yy);
					pR = new Engine(true, y + yy);
					break;
					
				case EYE:
					pL = new Eye(true, y + yy);
					pR = new Eye(true, y + yy);
					break;
					
				case PIPES:
					pL = new Pipes(true, y + yy);
					pR = new Pipes(true, y + yy);
					break;
					
				case PULLY:
					pL = new Pully(true, y + yy);
					pR = new Pully(true, y + yy);
					break;
					
				case SAD:
					pL = new Wire(true, y + yy);
					pR = new Wire(true, y + yy);
					break;
					
				case UGU:
					pL = new Engine(true, y + yy);
					pR = new Engine(true, y + yy);
					break;
					
				case WING:
					pL = new Wing(true, y + yy);
					pR = new Wing(true, y + yy);
					break;
					
				case WIRE:
					pL = new Wire(true, y + yy);
					pR = new Wire(true, y + yy);
					break;
					
				default:
					System.out.println("ERR: Tried to create non-existent boss piece");
					break;
				}
				
				if(pL != null && pR != null) {
					pL.setX(this.x + xx);
					pL.setY(this.y + yy);
					pL.setRotation(NumberUtility.randomFloat(0f, 360f));
					pL.sprite.setScale(1f + NumberUtility.randomFloat(0f, 1f), 1f + NumberUtility.randomFloat(0f,1f));
					
					pR.setX(this.x - xx);
					pR.setY(this.y + yy);
					pR.setRotation(-pL.getRotation());
					pR.sprite.flip(true, false);
					pR.sprite.setScale(pL.sprite.getScaleX(), pL.sprite.getScaleY());
					
					parts.add(pL);
					Game.entMgr.addEntity(pL);
					
					parts.add(pR);
					Game.entMgr.addEntity(pR);
				}
			}
		}
		
		for(Part p : parts) {
			if(p instanceof Pipes || p instanceof Wire) {
				speed += 0.25f;
			} else if(p instanceof Eye) {
				armor += 20;
			} else if(p instanceof Engine) {
				laserCount++;
			} else if(p instanceof Cog) {
				turretCount++;
			} else if(p instanceof Pully) {
				speed += 0.1;
				armor += 10;
			}
		}
		
		
		for(Part p : parts) {
			p.health = (int) armor;
		}
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		boolean isFiring = false;
		for(Part p : parts) {
			if(p instanceof Engine) {
				if(((Engine)p).isFiring()) {
					isFiring = true;
					break;
				}
			}
		}
		
		HashSet<Part> removeParts = new HashSet<Part>();
		for(Part p : parts) {
			if(p.health <= 0) {
				p.destroyed = true;
				p.destroyed();
				
				Game.entMgr.removeEntity(p);
				removeParts.add(p);
				
				Game.gfxMgr.getCamera().shake();
			}
		}
		
		for(Part p : removeParts) {
			parts.remove(p);
		}
		
		if(isFiring) {
			vol += dt;
			
			time += dt * 2;
			if(vol > 0.5f) {
				snd.play(1f / time);
				vol = 0f;
			}
		} else {
			snd.stop();
		}
		
		turretFireRate += dt;
		change += dt * 3;
		
		sprite.setScale((float) (Math.sin(change) * 0.5f + 1f));
	}

	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
}

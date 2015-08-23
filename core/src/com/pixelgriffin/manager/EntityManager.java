package com.pixelgriffin.manager;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.pixelgriffin.monster.Game;
import com.pixelgriffin.monster.entity.Entity;
import com.pixelgriffin.monster.entity.PlayerCore;
import com.pixelgriffin.monster.entity.PotentialEntity;

/**
 * 
 * @author Nathan
 *
 */
public class EntityManager implements Manager {

	public static boolean PAUSED = false;
	
	private HashSet<Entity> addQueue;
	private HashSet<Entity> removeQueue;
	private HashSet<Entity> ents;
	
	private Array<Entity> renderQueue;
	private Comparator<Entity> sorter;
	
	private Game inst;
	
	public PlayerCore core;
	
	public EntityManager(Game g) {
		inst = g;
		
		addQueue = new HashSet<Entity>();
		removeQueue = new HashSet<Entity>();
		ents = new HashSet<Entity>();
		
		renderQueue = new Array<Entity>();
		
		sorter = new Comparator<Entity>() {
			public int compare(Entity a, Entity b) {
				return (int)Math.signum(b.getZ() - a.getZ());
			}
		};
	}
	
	public HashSet<Entity> getEntities() {
		return ents;
	}
	
	public Game getGameInstance() {
		return inst;
	}
	
	public void addEntity(Entity e) {
		addQueue.add(e);
	}
	
	public void removeEntity(Entity e) {
		removeQueue.add(e);
	}
	
	public void clearEntities() {
		ents.clear();
	}
	
	@Override
	public void update(float dt) {
		if(PAUSED)
			return;
		
		//clear renderables in case we were asked to render the same thing before but it was never cleared
		if(renderQueue.size != 0)
			renderQueue.clear();
		
		for(Entity e : removeQueue) {
			ents.remove(e);
		}
		removeQueue.clear();
		
		for(Entity e : addQueue) {
			ents.add(e);
		}
		addQueue.clear();
		
		//update
		for(Entity e : ents) {
			if(!e.destroyed) {
				e.update(dt);
				
				//collisions
				if(e instanceof PotentialEntity) {
					PotentialEntity pe = (PotentialEntity)e;
					if(pe.bound != null) {
						for(Entity e2 : ents) {
							if(e == e2)
								continue;
							
							if(e2 instanceof PotentialEntity) {
								PotentialEntity pe2 = (PotentialEntity)e2;
								
								if(pe2.bound != null) {
									if(pe.bound.overlaps(pe2.bound)) {
										pe.collided(pe2);
										pe2.collided(pe);
									}
								}
							}
						}
					}
				}
				
				//render later
				renderQueue.add(e);
			}
		}
	}
	
	public void renderEntities(SpriteBatch batch) {
		//render
		renderQueue.sort(sorter);
		
		for(Entity e : renderQueue) {
			e.draw(batch);
		}
		
		if(!PAUSED)
			renderQueue.clear();
	}

	@Override
	public void dispose() {
	}
}

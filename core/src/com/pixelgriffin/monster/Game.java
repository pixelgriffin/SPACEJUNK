package com.pixelgriffin.monster;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.VisUI;
import com.pixelgriffin.manager.EntityManager;
import com.pixelgriffin.manager.GraphicsManager;
import com.pixelgriffin.manager.TweenManager;
import com.pixelgriffin.monster.scene.IntroScene;
import com.pixelgriffin.monster.scene.GameScene;

/**
 * 
 * @author Nathan
 *
 */
public class Game extends com.badlogic.gdx.Game {

	public static EntityManager entMgr;
	public static GraphicsManager gfxMgr;
	public static TweenManager twnMgr;
	
	@Override
	public void create() {
		//set up managers
		entMgr = new EntityManager(this);
		gfxMgr = new GraphicsManager();
		twnMgr = new TweenManager();
		
		//create required classes
		VisUI.load();
		gfxMgr.createCamera(960, 540);
	
		//load required data
		gfxMgr.createDictionary("test");
		gfxMgr.addTexture("test", "cog.png");
		gfxMgr.addTexture("test", "engine.png");
		gfxMgr.addTexture("test", "eye.png");
		gfxMgr.addTexture("test", "glow.png");
		gfxMgr.addTexture("test", "glow_red.png");
		gfxMgr.addTexture("test", "pipes.png");
		gfxMgr.addTexture("test", "pully.png");
		//gfxMgr.addTexture("test", "sad.png");
		//gfxMgr.addTexture("test", "ugu.png");
		//gfxMgr.addTexture("test", "wing.png");
		gfxMgr.addTexture("test", "wire.png");
		gfxMgr.addTexture("test", "human.png");
		gfxMgr.addTexture("test", "bullet.png");
		gfxMgr.addTexture("test", "pilot.png");
		gfxMgr.addTexture("test", "world.png");
		gfxMgr.addTexture("test", "laser2_strip4.png");
		gfxMgr.addTexture("test", "zx_keys.png");
		gfxMgr.addTexture("test", "directional_keys.png");
		gfxMgr.addTexture("test", "black.png");
		
		gfxMgr.addParticleEffect("test", "boss.p");
		
		gfxMgr.addSound("test", "rumble.wav");
		gfxMgr.addSound("test", "charge.wav");
		gfxMgr.addSound("test", "hit.wav");
		gfxMgr.addSound("test", "explode.wav");
		gfxMgr.addSound("test", "shoot.wav");
		gfxMgr.addSound("test", "drum.ogg");
		gfxMgr.addSound("test", "doot.wav");
		
		gfxMgr.addMusic("test", "theme.mp3");
		
		gfxMgr.addTTFFont("big", "default.ttf", 24);
		gfxMgr.addTTFFont("med", "default.ttf", 12);
		
		
		gfxMgr.loadAssetsFor("test");
		
		while(!gfxMgr.loadStep());
		
		setScreen(new IntroScene());
	}
	
	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		
		//always be ticking the managers
		entMgr.update(dt);
		twnMgr.update(dt);
		gfxMgr.update(dt);
		
		super.render();
	}
}

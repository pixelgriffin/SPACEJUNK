package com.pixelgriffin.monster.scene;

import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.util.dialog.OptionDialogAdapter;
import com.kotcrab.vis.ui.util.dialog.DialogUtils.OptionDialogType;
import com.pixelgriffin.common.NumberUtility;
import com.pixelgriffin.common.ShakeCamera;
import com.pixelgriffin.manager.EntityManager;
import com.pixelgriffin.manager.TweenManager.TweenType;
import com.pixelgriffin.manager.tweens.BackTween;
import com.pixelgriffin.manager.tweens.ElasticTween;
import com.pixelgriffin.manager.tweens.QuadTween;
import com.pixelgriffin.manager.tweens.Tween;
import com.pixelgriffin.monster.Game;
import com.pixelgriffin.monster.entity.Difficulty;
import com.pixelgriffin.monster.entity.Earth;
import com.pixelgriffin.monster.entity.Entity;
import com.pixelgriffin.monster.entity.HumanShip;
import com.pixelgriffin.monster.entity.PlayerCore;
import com.pixelgriffin.monster.entity.parts.Cog;
import com.pixelgriffin.monster.entity.parts.Engine;
import com.pixelgriffin.monster.entity.parts.Part;

/**
 * 
 * @author Nathan
 *
 */
public class GameScene implements Screen {

	private Sprite fadeSpriteBack;
	private Sprite fadeSpriteFront;
	
	private int humansLeft;
	
	private Tween t;
	private boolean showEarth = false;
	private float earthTime = 0.0f;
	private Texture earth;
	private String earthMessage;
	
	private Stage ui;
	
	private ShakeCamera cam;
	private SpriteBatch batch;
	
	public PlayerCore boss;
	
	private ParticleEmitter emit;
	
	private Tween enterTween;
	private int count = 5;
	
	private float flashTime;
	
	private boolean bossTime;
	private boolean bigBossDown;
	private Tween bossEntrance;
	private Earth bigBoss;
	private Tween bossEndX;
	private Tween bossEndY;
	private boolean bossGoodbye;
	private float bossPop;
	
	private Difficulty difficulty;
	
	public GameScene(Difficulty diff) {
		difficulty = diff;
		
		switch(diff) {
		case CASUAL:
			humansLeft = 100;
			break;
			
		case NORMAL:
			humansLeft = 150;
			break;
		
		case HARDCORE:
			humansLeft = 250;
			break;
		}
	}
	
	@Override
	public void show() {
		//create UI
		ui = new Stage();
		Gdx.input.setInputProcessor(ui);
		
		//get basic data
		cam = Game.gfxMgr.getCamera();
		batch = Game.gfxMgr.getBatch();
		
		fadeSpriteBack = new Sprite(Game.gfxMgr.getTexture("test", "black.png"));
		fadeSpriteBack.setScale(1920, 1280);
		fadeSpriteBack.setAlpha(0);
		
		fadeSpriteFront = new Sprite(Game.gfxMgr.getTexture("test", "black.png"));
		fadeSpriteFront.setScale(1920, 1280);
		fadeSpriteFront.setAlpha(0);
		
		//create a flash timer
		flashTime = 1.0f;
		
		//set up final boss statistics
		bossTime = false;
		bigBossDown = false;
		bossGoodbye = false;
		
		bossPop = 0.0f;
		
		bossEntrance = null;
		
		bossEndX = null;
		bossEndY = null;
		
		//create earth
		enterTween = Game.twnMgr.createTween(BackTween.class, TweenType.OUT, -32, 100, 2);
		earth = Game.gfxMgr.getTexture("test", "world.png");
		
		//gather generated player
		boss = Game.entMgr.core;
		boss.setX(480);
		boss.setY(450);
		
		Game.entMgr.addEntity(boss);
		for(Part p : boss.parts) {
			Game.entMgr.addEntity(p);
		}
		
		//shake upon entrance
		cam.shake();
		
		//start stars
		emit = new ParticleEmitter(Game.gfxMgr.getParticleEffect("test", "boss.p").getEmitters().get(1));
		emit.setPosition(0, 0);
		emit.start();
		for(int i = 0; i < 20; i++) {
			emit.update(1f);
		}
		
		Game.gfxMgr.getSound("test", "drum.ogg").play(0.7f);
		
		Game.gfxMgr.getMusic("test", "theme.mp3").setLooping(true);
		Game.gfxMgr.getMusic("test", "theme.mp3").setVolume(0.35f);
		Game.gfxMgr.getMusic("test", "theme.mp3").play();
	}

	@Override
	public void render(float dt) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//if we are in regular mode
		if(humansLeft > 0) {
			/*
			 * Check if we need more units
			 */
			boolean needMore = true;
			for(Entity e : Game.entMgr.getEntities()) {
				if(e instanceof HumanShip) {
					if(!((HumanShip) e).smart)
						e.setY(enterTween.getValue());
					
					needMore = false;
				}
			}
			
			/*
			 * Spawn more units if needed
			 */
			if(needMore) {
				showEarth = true;
				earthTime = 0.0f;
				t = Game.twnMgr.createTween(ElasticTween.class, TweenType.OUT, -100f, 100, 1f);
				
				boolean onTop = true;
				if(boss.getY() < 270)
					onTop = false;
				
				int mode = NumberUtility.randomInt(0, 2);
				if(humansLeft < count && mode == 2)
					mode = 1;
				if(humansLeft < 2 && mode == 1)
					mode = 0;
				
				if(mode == 0) {
					String[] messages = new String[] {
						"I THINK I WENT TO HIGHSCHOOL WITH THAT GUY",
						"EARTH-STYLE! SEND A BUNCH OF SHIPS JUTSU!",
						"ERROR MESSAGE NOT FOUND. LOL JK. ATTACK!",
						"OUR TRUE STRENGTH LIES IN OUR PERFECTLY EVOLVED THYROIDS!",
						"NOT HIM! HE HAS A WAIFU AND KIDS!"
					};
					int num = NumberUtility.randomInt(0, 3);
					earthMessage = messages[num];
					
					HumanShip ship = new HumanShip(true, difficulty);
					ship.setX(480);
					if(onTop)
						ship.setY(-32);
					else
						ship.setY(572);
					
					Game.entMgr.addEntity(ship);
					
					humansLeft--;
					
				} else if(mode == 1) {
					boolean msg = NumberUtility.randomBool();
					if(msg)
						earthMessage = "GO! 2-PLAYER FORMATION!";
					else
						earthMessage = "BOY I SURE DO LOVE THIS GAME.";
					
					HumanShip ship = new HumanShip(true, difficulty);
					ship.setX(NumberUtility.randomInt(48, 960 - 48));
					if(onTop)
						ship.setY(-32);
					else
						ship.setY(572);
					
					HumanShip ship2 = new HumanShip(true, difficulty);
					ship2.setX(NumberUtility.randomInt(48, 960 - 48));
					if(onTop)
						ship2.setY(-32);
					else
						ship2.setY(572);
					
					Game.entMgr.addEntity(ship);
					Game.entMgr.addEntity(ship2);
					
					humansLeft -= 2;
					
				} else if(mode == 2) {
					boolean superDuperMode = NumberUtility.randomBool();
					boolean msg = NumberUtility.randomBool();
					
					if(!superDuperMode) {
						if(msg)
							earthMessage = "THINK YOU'RE TOUGH? GO! MMO FORMATION!";
						else
							earthMessage = "I THINK I LEFT MY HOTPOCKET IN THE MICROWAVE! REVENGE!";
					} else {
						if(msg)
							earthMessage = "GO! ULTIMATE FORMATION: 420 BLAZE IT DORITOS NO SCOPE";
						else
							earthMessage = "AS MY GRANDMA USED TO SAY, CLOG THEIR WEAPONS WITH YOUR DEAD BODIES!";
					}
					
					for(int i = 0; i < count; i++) {
						HumanShip ship = new HumanShip(superDuperMode, difficulty);		
						ship.setX(16 + i * (960 / count));
						
						if(onTop) {
							ship.setY(-32);
							enterTween = Game.twnMgr.createTween(BackTween.class, TweenType.OUT, -32, 100, 2);
						} else {
							ship.setY(572);
							enterTween = Game.twnMgr.createTween(BackTween.class, TweenType.OUT, 572, -100, 2);
						}
						
						Game.entMgr.addEntity(ship);
					}
					
					humansLeft -= count;
					
					count++;
					if(count > 10)
						count = 10;
				}
			}
			
			/*
			 * Show the earth when it wants to say something
			 */
			if(showEarth) {
				earthTime += dt;
				if(earthTime > 5f) {
					showEarth = false;
					t = Game.twnMgr.createTween(ElasticTween.class, TweenType.IN, 0, -100, 1f);
				}
			}
		} else {
			/*
			 * Handle the boss!
			 */
			showEarth = true;
			earthMessage = "I'LL BEAT YOU MYSELF!";
			
			t = null;
			
			if(!bossTime) {
				bigBoss = new Earth(difficulty);
				bigBoss.setX(320 - 64);
				bigBoss.setY(-64);
				
				Game.entMgr.addEntity(bigBoss);
				
				bossEntrance = Game.twnMgr.createTween(QuadTween.class, TweenType.OUT, -64, 270 + 64, 2f);
				
				bossTime = true;
			} else {
				if(bossEntrance != null) {
					if(bossEntrance.isDone())
						bossEntrance = null;
					
					if(bossEntrance != null) {
						bigBoss.setX(320 - 64);
						bigBoss.setY(bossEntrance.getValue());
					}
				}
				
				if(!Game.entMgr.getEntities().contains(bigBoss)) {
					bigBossDown = true;
					
					earthMessage = "WE'LL BE BACK!";
					
					if(fadeSpriteBack.getColor().a >= 0.9f) {
						fadeSpriteBack.setAlpha(1);
					} else {
						fadeSpriteBack.setAlpha(fadeSpriteBack.getColor().a + (dt / 2f));
					}
					
					if(bigBoss.popped) {
						fadeSpriteFront.setAlpha(fadeSpriteFront.getColor().a + (dt / 2f));
						if(fadeSpriteFront.getColor().a >= 0.9f) {
							fadeSpriteFront.setAlpha(1);
							Game.entMgr.getGameInstance().setScreen(new IntroScene());
						}
					}
				}
			}
		}
		
		if(bossGoodbye) {
			bossPop += dt;
			
			if(bossPop >= 2.5f)
				bigBoss.pop();
		}
		
		/*
		 * update UI and stuff
		 */
		emit.update(dt);
		ui.act(dt);
		
		/*
		 * Handle player controls and junk
		 */
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			boss.addVelocityX(-0.5f);
		}
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			boss.addVelocityX(0.5f);
		}
		
		if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			boss.addVelocityY(-0.5f);
		}
		
		if(Gdx.input.isKeyPressed(Keys.UP)) {
			boss.addVelocityY(0.5f);
		}
		
		//shoot stuff
		if(Gdx.input.isKeyPressed(Keys.Z)) {
			boss.fireTurrets();
		}
		if(Gdx.input.isKeyJustPressed(Keys.X)) {
			boss.fireLasers();
		}
		
		/*
		 * Pause the game
		 */
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE) && !EntityManager.PAUSED) {

			EntityManager.PAUSED = true;
			DialogUtils.showOptionDialog(ui, "Really exit?", "Do you really want to return to the generation screen?", OptionDialogType.YES_NO,
					new OptionDialogAdapter() {
				@Override
				public void yes() {
					EntityManager.PAUSED = false;
					Game.entMgr.getGameInstance().setScreen(new GenerationScene());
				}
				
				@Override
				public void no() {
					EntityManager.PAUSED = false;
				}
			});
		}
		
		/*
		 * Flash the countdown when we get close to done
		 */
		if(humansLeft <= 10) {
			flashTime += dt * 4;
		}
		
		/*
		 * Big finish
		 */
		if(bigBossDown) {
			if(bossEndX == null && bossEndY == null) {
				bossEndX = Game.twnMgr.createTween(QuadTween.class, TweenType.INOUT, bigBoss.getX(), 480 - bigBoss.getX(), 1.5f);
				bossEndY = Game.twnMgr.createTween(QuadTween.class, TweenType.INOUT, bigBoss.getY(), 270 - bigBoss.getY(), 1.5f);
				
				Game.gfxMgr.getSound("test", "rumble.wav").play(1f);
			} else {
				bigBoss.setX(bossEndX.getValue());
				bigBoss.setY(bossEndY.getValue());
				
				if(bossEndX.isDone() && bossEndY.isDone()) {
					bossGoodbye = true;
				}
			}
		}
		
		/*
		 * If we died
		 */
		boolean isDead = true;
		for(Part p : boss.parts) {
			if(p instanceof Cog ||
			   p instanceof Engine) {
				isDead = false;
				break;
			}
		}
		
		if(isDead) {
			fadeSpriteFront.setAlpha(fadeSpriteFront.getColor().a + (dt / 2f));
			if(fadeSpriteFront.getColor().a >= 0.9f) {
				fadeSpriteFront.setAlpha(1);
				Game.entMgr.getGameInstance().setScreen(new GenerationScene());
			}
		}
		
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
			emit.draw(batch);
			if(!bigBossDown)
				Game.entMgr.renderEntities(batch);
			
				//batch.draw(earth, 320 - 64, -8);
			if(t != null) {
				batch.draw(earth, 320f - 64f, t.getValue(), 32f, 32f, 64f, 64f, 1f, 1f, earthTime, 0, 0, 64, 64, false, false);
			}
			
			if(showEarth) {
				Game.gfxMgr.getTTFFont("med").draw(batch, earthMessage, 330, 20);
			}
			
			if(humansLeft > 0) {
				Game.gfxMgr.getTTFFont("big").setColor(1f, 1f, 1f, (float)Math.sin(flashTime));
				
				if(!isDead)
					Game.gfxMgr.getTTFFont("big").draw(batch, "Humans left: " + humansLeft, 330, 270);
				else
					Game.gfxMgr.getTTFFont("big").draw(batch, "DAMN! OUT OF WEAPONRY!", 330, 270);
				
				Game.gfxMgr.getTTFFont("big").setColor(1f, 1f, 1f, 1f);
			} else if(isDead) {
				Game.gfxMgr.getTTFFont("big").draw(batch, "DAMN! OUT OF WEAPONRY!", 330, 270);
			}
			
			fadeSpriteBack.draw(batch);
			if(bigBossDown) {
				if(!bigBoss.popped)
					bigBoss.draw(batch);
				
				if(bossGoodbye) {
					Game.gfxMgr.getTTFFont("big").draw(batch, "YOU GUYS ARE DICKS.", 330, 200);
				}
			}
			fadeSpriteFront.draw(batch);
		batch.end();
		
		ui.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		Game.gfxMgr.getMusic("test", "theme.mp3").stop();
		
		Game.entMgr.clearEntities();
		ui.dispose();
	}

	@Override
	public void dispose() {
	}

}

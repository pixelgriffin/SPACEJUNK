package com.pixelgriffin.monster.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pixelgriffin.common.ShakeCamera;
import com.pixelgriffin.manager.TweenManager.TweenType;
import com.pixelgriffin.manager.tweens.BackTween;
import com.pixelgriffin.manager.tweens.Tween;
import com.pixelgriffin.monster.Game;
import com.pixelgriffin.monster.entity.Bullet;
import com.pixelgriffin.monster.entity.PlayerCore;
import com.pixelgriffin.monster.entity.World;

/**
 * 
 * @author Nathan
 *
 */
public class IntroScene implements Screen, InputProcessor {
	
	private ShakeCamera cam;
	private SpriteBatch batch;
	
	private ParticleEmitter stars;
	
	private float shootTime;
	private float dipTime;
	
	private World w;
	private Tween worldTween;
	
	private Tween alienTween;
	private PlayerCore alienA;
	private PlayerCore alienB;
	
	private BitmapFont storyFont;
	private BitmapFont skipFont;
	
	private String[] messages;
	private int messageCount;
	private int messageIndex;
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
		
		batch = Game.gfxMgr.getBatch();
		cam = Game.gfxMgr.getCamera();
		
		stars = new ParticleEmitter(Game.gfxMgr.getParticleEffect("test", "boss.p").getEmitters().get(1));
		stars.start();
		
		w = new World();
		w.setX(480);
		w.setY(-64);
		w.setZ(-10);
		
		worldTween = null;
		
		alienA = new PlayerCore();
		alienA.setX(440);
		alienA.setY(-64);
		alienB = new PlayerCore();
		alienB.setX(520);
		alienB.setY(-64);
		
		alienTween = null;
		
		shootTime = 0f;
		
		Game.entMgr.addEntity(w);
		Game.entMgr.addEntity(alienA);
		Game.entMgr.addEntity(alienB);
		
		storyFont = Game.gfxMgr.getTTFFont("big");
		skipFont = Game.gfxMgr.getTTFFont("med");
		
		messages = new String[] { 
				"At first there was nothing",
				"And then there was chaos",
				"And from that chaos",
				"Came life",
				"But with the gift of life, came terrible gods",
				"Garuul, Destroyer of Worlds, is that you?",
				"Hey Todd",
				"What's crackin' my intergalactic brother",
				"My mom just bought me a sweet space phone",
				"Oh no way! You know what we should do?",
				"What?",
				"Let's prank call some nerds!",
				"Okay! Who should we call?",
				"How about earth?",
				"Oh yeah, those dorks.",
				"Do you think they've ever even seen a naked celestial body?",
				"Probs not, I'm dialing now",
				"...",
				"Hey Earth, your atmosphere is 78% Nitrogen!",
				"Hang up man hang up!",
				"Haha! Got 'eem",
				"Nice! You used a blocked space number right?",
				"Err..",
				"Todd you stupid space bastard!",
				"Well maybe they'll be cool about it",
				"...",
				"Go forces of Earth!",
				"We'll teach 'em to state scientific fact about our planet!",
				"To war! For the defense of our ego!",
				"Oh jeez, I gotta go",
				"I'll see you at Efraim's Barmitzvah!",
				"Wait!",
				"Ah..",
				"I better get ready"
		};
		messageCount = messages.length;
		messageIndex = 0;
		
		Game.gfxMgr.getSound("test", "drum.ogg").play(0.7f);
	}

	@Override
	public void render(float dt) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(messageIndex > 0)
			stars.update(dt);
		
		if(messageIndex > 2 && messageIndex < 4) {
			if(worldTween == null)
				worldTween = Game.twnMgr.createTween(BackTween.class, TweenType.OUT, -64, 270, 2f);
			
			w.setY(worldTween.getValue());
		}
		
		if(messageIndex > 3 && messageIndex < 5) {
			if(worldTween == null)
				worldTween = Game.twnMgr.createTween(BackTween.class, TweenType.IN, 270 - 64, -270, 2f);
			
			w.setY(worldTween.getValue());
		}
		
		if(messageIndex > 4 && messageIndex < 24) {
			if(alienTween == null)
				alienTween = Game.twnMgr.createTween(BackTween.class, TweenType.OUT, -64, 270, 2f);
			
			alienA.setY(alienTween.getValue());
			alienB.setY(alienTween.getValue());
			
			worldTween = null;
		}
		
		if(messageIndex > 23 && messageIndex < 28) {
			if(worldTween == null)
				worldTween = Game.twnMgr.createTween(BackTween.class, TweenType.OUT, -64, 270, 2f);
			
			w.setY(worldTween.getValue());
			
			
		}
		
		if(messageIndex > 23) {
			shootTime += dt * 5;
			if(shootTime > 1) {
				shootTime = 0;
				
				for(int i = 0; i < 20; i++) {
					Bullet b = new Bullet(false, false, i * (360f / 20f));
					b.setX(w.getX());
					b.setY(w.getY());
					
					Game.entMgr.addEntity(b);
				}
			}
		}
		
		if(messageIndex > 29) {
			if(alienTween == null)
				alienTween = Game.twnMgr.createTween(BackTween.class, TweenType.IN, 270 -64, -270, 2f);
			
			alienA.setY(alienTween.getValue());
		}
		
		dipTime += dt;
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
			CharSequence seq = messages[messageIndex];
			storyFont.draw(batch, seq, 480 - (seq.length() * 8), 450);
			
			skipFont.setColor(1f, 1f, 1f, (float)Math.sin(dipTime));
				skipFont.draw(batch, "SPACE TO CONTINUE", 0, 12);
				skipFont.draw(batch, "ESC TO SKIP", 870, 12);
			skipFont.setColor(1f, 1f, 1f, 1f);
			
			stars.draw(batch);
			Game.entMgr.renderEntities(batch);
		batch.end();
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
		Game.entMgr.clearEntities();
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.SPACE || keycode == Keys.ENTER) {
			if(messageIndex == 4) {
				if(w.getY() > -32)
					return true;
			}
			
			if(messageIndex < 6)
				Game.gfxMgr.getSound("test", "drum.ogg").play(0.7f);
			else {
				if(messageIndex % 2 == 0) {
					Game.gfxMgr.getSound("test", "doot.wav").play(0.8f);
				} else {
					Game.gfxMgr.getSound("test", "doot.wav").setPitch(
							Game.gfxMgr.getSound("test", "doot.wav").play(0.8f),
							1.5f);
				}
			}
			
			messageIndex++;
			
			if(messageIndex > messageCount)
				messageIndex = messageCount;
			
			if(messageIndex == 4) {
				worldTween = null;
			} else if(messageIndex == 25) {
				alienTween = null;
			} else if(messageIndex == 28) {
				alienTween = null;
			} else if(messageIndex == 34) {
				//switch sturf
				Game.entMgr.getGameInstance().setScreen(new GenerationScene());
			}
			
			return true;
		} else if(keycode == Keys.ESCAPE) {
			Game.entMgr.getGameInstance().setScreen(new GenerationScene());
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}

package com.pixelgriffin.monster.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.util.dialog.DialogUtils.OptionDialog;
import com.kotcrab.vis.ui.util.dialog.DialogUtils.OptionDialogType;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.util.dialog.OptionDialogAdapter;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.pixelgriffin.common.NumberUtility;
import com.pixelgriffin.common.ShakeCamera;
import com.pixelgriffin.manager.EntityManager;
import com.pixelgriffin.manager.TweenManager.TweenType;
import com.pixelgriffin.manager.tweens.BackTween;
import com.pixelgriffin.manager.tweens.Tween;
import com.pixelgriffin.monster.Game;
import com.pixelgriffin.monster.entity.Bullet;
import com.pixelgriffin.monster.entity.Difficulty;
import com.pixelgriffin.monster.entity.PlayerCore;
import com.pixelgriffin.monster.entity.World;
import com.pixelgriffin.monster.entity.parts.Part;

/**
 * 
 * @author Nathan
 *
 */
public class GenerationScene implements Screen {
	
	private ShakeCamera cam;
	private SpriteBatch batch;
	
	private ParticleEmitter stars;
	
	private PlayerCore boss;
	
	private Stage ui;
	
	private Label lasers, turrets, speed, armor, info;
	private VisTextButton button2;
	private Texture zxKeys, directionalKeys;
	
	private float time = 0f;
	
	@Override
	public void show() {
		batch = Game.gfxMgr.getBatch();
		cam = Game.gfxMgr.getCamera();
		
		zxKeys = Game.gfxMgr.getTexture("test", "zx_keys.png");
		directionalKeys = Game.gfxMgr.getTexture("test", "directional_keys.png");
		
		boss = new PlayerCore();
		boss.setX(480);
		boss.setY(270);
		
		Game.entMgr.core = boss;
		Game.entMgr.addEntity(boss);
		
		stars = Game.gfxMgr.getParticleEffect("test", "boss.p").getEmitters().get(1);
		stars.start();
		
		/*
		 * Create UI
		 */
		ui = new Stage();
		//ui.setDebugAll(true);
		
		VisTable table = new VisTable();
		table.setFillParent(true);
		table.top();
		
		table.row();
		table.padTop(32f);
		
		VisLabel label = new VisLabel();
		label.setText("STATS");
		table.add(label);
		
		table.row();
		
		lasers = new VisLabel();
		lasers.setText("No info");
		table.add(lasers);
		
		table.row();
		
		turrets = new VisLabel();
		turrets.setText("No info");
		table.add(turrets);
		
		table.row();
		
		speed = new VisLabel();
		speed.setText("No info");
		table.add(speed);
		
		table.row();
		
		armor = new VisLabel();
		armor.setText("No info");
		table.add(armor);
		
		table.row();
		
		info = new VisLabel();
		info.setText("No info");
		table.add(info);
		
		table.row();
		
		VisTable table2 = new VisTable();
		table2.setFillParent(true);
		table2.bottom();
		
		table2.row();
		table2.padBottom(32f);
		
		VisTextButton button = new VisTextButton("Generate Space Junk (SPACE)");
		button.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				boss.generate();
				updateStatistics();
				
				Game.gfxMgr.getSound("test", "drum.ogg").play(0.7f);
			}
		});
		
		table2.add(button);
		table2.row();
		table2.pad(16);
		table2.row();
		table2.pad(16);

		
		button2 = new VisTextButton("Begin (ENTER)");
		button2.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				showStartDialog();
			}
		});
		button2.setDisabled(true);
		
		table2.add(button2);
		
		ui.addActor(table);
		ui.addActor(table2);
		
		Gdx.input.setInputProcessor(ui);
	}

	private void updateStatistics() {
		speed.setText("SPEED: " + boss.speed);
		armor.setText("ARMOR: " + boss.armor);
		turrets.setText("# TURRETS: " + boss.turretCount);
		lasers.setText("# LASERS: " + boss.laserCount);
		
		if(boss.turretCount == 0 || boss.laserCount == 0)
			info.setText("It's good to have at least one of each type of weapon!");
		else if(boss.armor < 90)
			info.setText("Sometimes a good offense is a good defense!");
		else if(boss.speed > 4.0f)
			info.setText("Speed racer over here, this guy!");
		else if(boss.speed < 1.0f)
			info.setText("I guess the turtle did bit the hare!");
		else if(boss.armor > 200)
			info.setText("Mr. armor over here!");
		else
			info.setText("A pretty well balanced build!");
	}
	
	@Override
	public void render(float dt) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		button2.setDisabled(false);
		for(Part p : boss.parts) {
			if(!p.inPlace()) {
				button2.setDisabled(true);
				break;
			}
		}
		if(boss.parts.isEmpty())
			button2.setDisabled(true);
		
		stars.update(dt);
		ui.act(dt);
		
		if(!button2.isDisabled()) {
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
			
			
			if(Gdx.input.isKeyPressed(Keys.Z)) {
				boss.fireTurrets();
			}
			if(Gdx.input.isKeyJustPressed(Keys.X)) {
				boss.fireLasers();
			}
		}
		
		if(time > 0.1 && Gdx.input.isKeyJustPressed(Keys.ESCAPE) && !EntityManager.PAUSED) {
			String showString = "I love you. \nPlease don't leave me!";
			int num = NumberUtility.randomInt(0, 6);
			switch(num) {
			case 0:
				showString = "Are you really going to let those nerds \npush you around?";
				break;
			case 1:
				showString = "You know you love it!";
				break;
			case 2:
				showString = "Hey, guy. What's wrong?";
				break;
			case 3:
				showString = "If you leave now, I'll be really sad!";
				break;
			case 4:
				showString = "If you leave feedback, I'll give you a big hug!";
				break;
			default:
				break;
			}
			
			EntityManager.PAUSED = true;
			DialogUtils.showOptionDialog(ui, "Really exit?", showString, OptionDialogType.YES_NO,
					new OptionDialogAdapter() {
				@Override
				public void yes() {
					Gdx.app.exit();
				}
				
				@Override
				public void no() {
					EntityManager.PAUSED = false;
				}
			});
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			boss.generate();
			updateStatistics();
			
			Game.gfxMgr.getSound("test", "drum.ogg").play(0.7f);
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			if(!button2.isDisabled()) {
				showStartDialog();
			}
		}
		
		time += dt;
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
			stars.draw(batch);
			Game.entMgr.renderEntities(batch);
			
			batch.draw(zxKeys, 270, 180);
			batch.draw(directionalKeys, 670, 180);
			Game.gfxMgr.getDefaultFont().draw(batch, "Movement", 670, 170);
			Game.gfxMgr.getDefaultFont().draw(batch, "Weaponry", 260, 170);
		batch.end();
		
		ui.draw();
	}
	
	private void showStartDialog() {
		DialogUtils.showOptionDialog(ui, "Ready?", "Remember: enemies appear from the top and bottom,\n it is useful to have a good range of attack.", OptionDialogType.YES_NO,
				new OptionDialogAdapter() {
			@Override
			public void yes() {
				OptionDialog diffQ = DialogUtils.showOptionDialog(ui, "               Select Difficulty Mode               ", "", OptionDialogType.YES_NO_CANCEL,
						new OptionDialogAdapter() {
					@Override
					public void yes() {
						Game.entMgr.getGameInstance().setScreen(new GameScene(Difficulty.CASUAL));
					}
					
					@Override
					public void no() {
						Game.entMgr.getGameInstance().setScreen(new GameScene(Difficulty.NORMAL));
					}
					
					@Override
					public void cancel() {
						Game.entMgr.getGameInstance().setScreen(new GameScene(Difficulty.HARDCORE));
					}
				});
				
				diffQ.setYesButtonText("CASUAL");
				diffQ.setNoButtonText("NORMAL");
				diffQ.setCancelButtonText("HARDCORE");
			}
		});
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
		ui.dispose();
	}

	@Override
	public void dispose() {
	}
}

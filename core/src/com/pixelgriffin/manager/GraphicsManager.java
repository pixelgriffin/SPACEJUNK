package com.pixelgriffin.manager;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.pixelgriffin.common.ShakeCamera;

/**
 * 
 * @author Nathan
 *
 */
@SuppressWarnings("rawtypes")
public class GraphicsManager implements Manager {
	
	private ShakeCamera cam;
	
	private AssetManager mgr;
	
	private SpriteBatch batch;
	private HashMap<String, ArrayList<AssetDescriptor>> assets;
	
	private HashMap<String, ShaderProgram> shaders;
	private HashMap<String, BitmapFont> fonts;
	
	public GraphicsManager() {
		batch = new SpriteBatch();
		
		assets = new HashMap<String, ArrayList<AssetDescriptor>>();
		shaders = new HashMap<String, ShaderProgram>();
		fonts = new HashMap<String, BitmapFont>();
		
		mgr = new AssetManager();
		
		cam = null;
		
		addTTFFont("default", "default.ttf", 9);
	}
	
	public BitmapFont getDefaultFont() {
		return fonts.get("default");
	}
	
	public void createCamera() {
		cam = new ShakeCamera();
		cam.setToOrtho(false);
	}
	
	public void createCamera(float width, float height) {
		cam = new ShakeCamera();
		cam.setToOrtho(false, width, height);
	}
	
	public ShakeCamera getCamera() {
		return cam;
	}
	
	public void createDictionary(String dict) {
		assets.put(dict, new ArrayList<AssetDescriptor>());
	}
	
	public GraphicsManager addTexture(String dict, String asset) {
		assets.get(dict).add(new AssetDescriptor(asset, Texture.class));
		
		return this;
	}
	
	public GraphicsManager addParticleEffect(String dict, String asset) {
		assets.get(dict).add(new AssetDescriptor(asset, ParticleEffect.class));
		
		return this;
	}
	
	public GraphicsManager addMusic(String dict, String asset) {
		assets.get(dict).add(new AssetDescriptor(asset, Music.class));
		
		return this;
	}
	
	public GraphicsManager addSound(String dict, String asset) {
		assets.get(dict).add(new AssetDescriptor(asset, Sound.class));
		
		return this;
	}
	
	/**
	 * Loads a shader into memory. Shaders are unaffected by loadAssetsFor(...)
	 * 
	 * @param name The ID for the shader program
	 * @param vsh The vertex shader file
	 * @param fsh The fragment shader file
	 * @return
	 */
	public GraphicsManager addShader(String name, String vsh, String fsh) {
		String f = Gdx.files.internal(fsh).readString();
		String v = Gdx.files.internal(vsh).readString();
		
		ShaderProgram prog = new ShaderProgram(v, f);
		
		if(prog.isCompiled()) {
			shaders.put(name, prog);
		} else {
			System.out.println(prog.getLog());
		}
		
		return this;
	}
	
	public void unloadAllShaders() {
		shaders.clear();
	}
	
	public boolean unloadShader(String name) {
		if(shaders.containsKey(name)) {
			ShaderProgram prog = shaders.get(name);
			return true;
		}
		
		return false;
	}
	
	public ShaderProgram getShader(String name) {
		return shaders.get(name);
	}

	/**
	 * Adds a TTF font to the manager. Fonts are unaffected by loadAssetsFor(...)
	 * 
	 * @param name The name of the font
	 * @param file The file of the font
	 * @param size The size of the font
	 * @return
	 */
	public GraphicsManager addTTFFont(String name, String file, int size) {
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(file));
		
		FreeTypeFontParameter params = new FreeTypeFontParameter();
		params.size = size;
		
		BitmapFont font = gen.generateFont(params);
		fonts.put(name, font);
		
		return this;
	}
	
	public BitmapFont getTTFFont(String name) {
		return fonts.get(name);
	}
	
	public void unloadTTFFont(String name) {
		BitmapFont font = fonts.get(name);
		
		if(font != null) {
			fonts.remove(name);
			font.dispose();
		}
	}
	
	public void loadAssetsFor(String dict) {
		ArrayList<AssetDescriptor> toLoad = assets.get(dict);
		
		if(toLoad != null) {
			for(AssetDescriptor a : toLoad) {
				mgr.load(a);
			}
		}
	}
	
	public void unloadAssetsFor(String dict) {
		ArrayList<AssetDescriptor> toLoad = assets.get(dict);
		
		if(toLoad != null) {
			for(AssetDescriptor a : toLoad) {
				mgr.unload(a.fileName);
			}
			
			toLoad.clear();
			assets.put(dict, toLoad);
		}
	}
	
	public Texture getTexture(String dict, String asset) {
		return mgr.get(asset, Texture.class);
	}
	
	public ParticleEffect getParticleEffect(String dict, String asset) {
		return mgr.get(asset, ParticleEffect.class);
	}
	
	public Music getMusic(String dict, String asset) {
		return mgr.get(asset, Music.class);
	}
	
	public Sound getSound(String dict, String asset) {
		return mgr.get(asset, Sound.class);
	}
	
	public boolean loadStep() {
		return mgr.update();
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}

	@Override
	public void update(float dt) {
		if(cam != null) {
			cam.update(dt);
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		mgr.dispose();
	}
}

package com.pixelgriffin.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import com.pixelgriffin.manager.tweens.Tween;

/**
 * 
 * @author Nathan
 *
 */
public class TweenManager implements Manager {
	
	private ArrayList<Tween> tweens;
	
	public TweenManager() {
		tweens = new ArrayList<Tween>();
	}
	
	public <T extends Tween> Tween createTween(Class<T> tween, TweenType type, float beginning, float change, float duration) {
		try {
			T newTween = tween.getConstructor(TweenType.class, float.class, float.class, float.class).newInstance(type, beginning, change, duration);
			tweens.add(newTween);
			
			return newTween;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public void update(float dt) {
		for(Iterator<Tween> it = tweens.iterator(); it.hasNext();) {
			Tween tween = it.next();
			
			if(tween.isDone()) {
				it.remove();
			} else {
				if(!tween.isPaused())
					tween.update(dt);
			}
		}
	}

	@Override
	public void dispose() {
	}
	
	public enum TweenType {
		IN,
		OUT,
		INOUT
	}
}

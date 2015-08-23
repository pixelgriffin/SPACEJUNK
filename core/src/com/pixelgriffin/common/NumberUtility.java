package com.pixelgriffin.common;

import java.util.Random;

/**
 * 
 * @author Nathan
 *
 */
public final class NumberUtility {
	
	private static Random rand = new Random();
	
	public static int randomInt(int min, int max) {
		int r = rand.nextInt((max - min) + 1) + min;
		return r;
	}
	
	public static float randomFloat(float min, float max) {
		return rand.nextFloat() * (max - min) + min;
	}
	
	public static boolean randomBool() {
		return rand.nextBoolean();
	}
	
	public static float clampFloat(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}
	
	public static float distance(float x1, float y1, float x2, float y2) {
		float dx = x2 - x1;
		float dy = y2 - y2;
		
		return (float)Math.sqrt((dx * dx) + (dy * dy));
	}
	
	//if you recognize this naming convention then you've used Game Maker ;)
	public static float lengthdirX(float len, float dir) {
		return (float)Math.cos(Math.toRadians(dir + 90)) * len;
	}
	
	public static float lengthdirY(float len, float dir) {
		return (float)Math.sin(Math.toRadians(dir + 90)) * len;
	}
	
	public static float getAngle(float x1, float y1, float x2, float y2) {
		float angle = (float)Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
		
		//keep angle positive
		if(angle < 0)
			angle += 360;
		
		return angle;
	}
}

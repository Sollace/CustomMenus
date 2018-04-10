package com.sollace.custommenus.resources;

import java.lang.annotation.Native;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Utilities for serialising colours as used in native minecraft code.
 */
public class Color extends Number implements Comparable<Integer> {
	
	@Native
	private static final long serialVersionUID = 6675633797515879197L;
	
	@Native
	public static final int SIZE = Integer.SIZE;
	
	private int value;
	
	public Color() {
		this(-1);
	}
	
	public Color(int code) {
		value = code;
	}
	
	public void init(JsonElement json) {
		if (json.isJsonPrimitive()) {
			value = json.getAsInt();
		} else {
			JsonObject o = json.getAsJsonObject();
			value = getCodePoint(
					o.get("red").getAsInt(),
					o.get("green").getAsInt(),
					o.get("blue").getAsInt(),
					o.has("alpha") ? o.get("alpha").getAsInt() : 255);
		}
	}
	
	public int A() {
		return getAlpha(value);
	}
	
	public int R() {
		return getRed(value);
	}
	
	public int G() {
		return getGreen(value);
	}
	
	public int B() {
		return getBlue(value);
	}
	
	@Override
	public int hashCode() {
		return value;
	}
	
	public boolean equals(Object other) {
		return other != null && (other == this || (other instanceof Color && ((Color)other).value == value) || (other instanceof Integer && (Integer)other == value));
	}
	
	/**
	 * Extracts the Alpha component of a colour.
	 */
	public static int getAlpha(int color) {
		return (color >> 24) & 255;
	}
	
	/**
	 * Extracts the Red component of a colour.
	 */
	public static int getRed(int color) {
		return (color >> 16) & 255;
	}
	
	/**
	 * Extracts the Green component of a colour.
	 */
	public static int getGreen(int color) {
		return (color >> 8) & 255;
	}
	
	/**
	 * Extracts the Blue component of a colour.
	 */
	public static int getBlue(int color) {
		return color & 255;
	}
	
	/**
	 * Converts an RGBA into a single colour value.
	 */
	public static int getCodePoint(int r, int g, int b, int a) {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	@Override
	public int compareTo(Integer o) {
		return value - o;
	}

	@Override
	public int intValue() {
		return value;
	}

	@Override
	public long longValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return value;
	}

	@Override
	public double doubleValue() {
		return value;
	}
}

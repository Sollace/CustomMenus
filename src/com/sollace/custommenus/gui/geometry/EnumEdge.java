package com.sollace.custommenus.gui.geometry;

import com.google.gson.JsonElement;
import com.sollace.custommenus.utils.JsonUtils.JsonType;

/**
 * The different edges of the screen.
 */
public enum EnumEdge implements JsonType<EnumEdge> {
	TOP(-1),
	BOTTOM(1),
	LEFT(-1),
	RIGHT(1);
	
	private final int unit;
	
	EnumEdge(int unit) {
		this.unit = unit;
	}
	
	/**
	 * Gets the unit length or direction this enum facing points.
	 */
	public int unitLength() {
		return unit;
	}
	
	/**
	 * Returns true if this EnumEdge is the top of bottom of the screen. i.e. The Horizontal edges.
	 */
	public boolean isHorizontal() {
		return this == TOP || this == BOTTOM;
	}
	
	@Override
	public EnumEdge get(JsonElement json) {
		return getByName(json.getAsString());
	}

	public static EnumEdge getByName(String name) {
		EnumEdge a = valueOf(name.toUpperCase());
		return a == null ? RIGHT : a;
	}
}

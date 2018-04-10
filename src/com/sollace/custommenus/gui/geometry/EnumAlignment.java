package com.sollace.custommenus.gui.geometry;

import com.google.gson.JsonElement;
import com.sollace.custommenus.utils.JsonUtils.JsonType;

public enum EnumAlignment implements JsonType<EnumAlignment> {
	TOP_LEFT(0, 0, EnumEdge.TOP),
	TOP_RIGHT(1, 0, EnumEdge.RIGHT),
	TOP_CENTER(.5f, 0, EnumEdge.TOP),
	CENTER_LEFT(0, 1, EnumEdge.LEFT),
	CENTER_CENTER(.5f, .5f, EnumEdge.RIGHT),
	CENTER_RIGHT(1, .5f, EnumEdge.RIGHT),
	BOTTOM_LEFT(0, 1, EnumEdge.LEFT),
	BOTTOM_CENTER(.5f, 1, EnumEdge.BOTTOM),
	BOTTOM_RIGHT(1, 1, EnumEdge.BOTTOM);
	
	private final float multX, multY;
	
	private final EnumEdge edge;
	
	EnumAlignment(float xMult, float yMult, EnumEdge edge) {
		multX = xMult;
		multY = yMult;
		this.edge = edge;
	}
	
	public int computeX(int x, int boxX, int viewX) {
		return (int)Math.floor((viewX * multX) + x);
	}
	
	public int computeY(int y, int boxY, int viewY) {
		return (int) (viewY * multY) + y;
	}
	
	/**
	 * Gets the corresponding edge for this alignment.
	 * @return
	 */
	public EnumEdge getEdge() {
		return edge;
	}
	
	@Override
	public EnumAlignment get(JsonElement json) {
		return getByName(json.getAsString());
	}
	
	public static EnumAlignment getByName(String name) {
		EnumAlignment a = valueOf(name.toUpperCase());
		return a == null ? TOP_LEFT : a;
	}
}

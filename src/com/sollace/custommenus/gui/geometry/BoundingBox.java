package com.sollace.custommenus.gui.geometry;

/**
 * Ui BoundingBox. Not to be confused with Minecraft's AxisAlignedBB which are two completely different things.
 * This one only supports two dimensions for UIElements.
 */
public class BoundingBox {
	
	private static final BoundingBox EMPTY = new BoundingBox(0, 0, 0, 0);
	
	public static BoundingBox nonNull(BoundingBox box) {
		return box == null ? EMPTY : box;
	}
	
	public int top, bottom, left, right, width, height;
	
	public BoundingBox(int x, int y, int w, int h) {
		top = y;
		left = x;
		
		width = w;
		height = h;
		
		bottom = top + h;
		right = left + w;
	}
	
	/**
	 * Adds the given bounds this box's own, expanding where neccessary. The result is a new box who's bounds contain both this and the other box.
	 */
	public void add(BoundingBox box) {
		if (box.top < top) top = box.top;
		if (box.left < left) left = box.left;
		if (box.right > right) right = box.right;
		if (box.bottom > bottom) bottom = box.bottom;
		
		height = bottom - top;
		width = right - left;
	}
}

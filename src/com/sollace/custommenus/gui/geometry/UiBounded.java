package com.sollace.custommenus.gui.geometry;

/**
 * A Box-type element that supports setting its position and size.
 */
public interface UiBounded extends IPositioned {
	
	/**
	 * Sets this element's position.
	 */
	UiBounded setPos(int x, int y);
	
	/**
	 * Sets this element's dimensions
	 */
	UiBounded setSize(int w, int h);
	
	/**
	 * Returns a bounding box representing the absolute position and size of this element.
	 */
	public BoundingBox getBoundingBox();
}

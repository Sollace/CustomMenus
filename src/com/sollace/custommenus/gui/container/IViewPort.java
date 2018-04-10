package com.sollace.custommenus.gui.container;

/**
 * ViewPorts representing the region of screen that any IRenderable elements can occupy. 
 */
public interface IViewPort {
	/**
	 * Gets the internal width of this viewport, excluding any additional padding.
	 */
	public int getWidth();
	
	/**
	 * Gets the internal height of this viewport, excluding any additional padding.
	 */
	public int getHeight();
	
	/**
	 * Sets the GL viewport to this viewport's bounds.
	 */
	public void setGlViewport();
	
	/**
	 * Gets the mouseX position within this viewport's window.
	 */
	public int getMouseX();
	
	/**
	 * Gets the mouseY position within this viewport's window.
	 */
	public int getMouseY();
}

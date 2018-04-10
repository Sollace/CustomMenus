package com.sollace.custommenus.gui.element;

import com.sollace.custommenus.gui.container.UiRoot;

/**
 * A renderable component.
 */
@FunctionalInterface
public interface IRenderable {
	/**
	 * Draws this element to the screen.
	 * 
	 * @param sender		The current screen being displayed (and original parent of this element)
	 * @param mouseX		Current mouse X position
	 * @param mouseY		Current mouse Y position
	 * @param partialTicks	Client animation ticks
	 */
	void render(UiRoot sender, int mouseX, int mouseY, float partialTicks);
	
	/**
	 * Sets whether this element is visible. Elements should only be rendered if they have been set as visible.
	 */
	default IRenderable setVisible(boolean visible) {
		return this;
	}
}

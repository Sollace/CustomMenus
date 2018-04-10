package com.sollace.custommenus.gui.input;

import com.sollace.custommenus.gui.container.UiRoot;

/**
 * UiElements that support advanced Mouse interactions.
 *
 */
public interface IMouseResponder {
	/**
	 * Called when the mouse moves over this element.
	 */
	void mouseMove(UiRoot sender, int mouseX, int mouseY, float partialTicks);
	
	/**
	 * Called when the mouse is release.
	 */
	void mouseUp(int mouseX, int mouseY);
}

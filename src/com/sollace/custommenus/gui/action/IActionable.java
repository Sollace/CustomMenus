package com.sollace.custommenus.gui.action;

import com.sollace.custommenus.gui.container.UiRoot;

/**
 * Elements capable of performing some action.
 */
public interface IActionable {

	/**
	 * Checks if the given mouse coordinates are within the bounds of this element. Returns true if it is in focus.
	 */
	boolean isFocused(int mouseX, int mouseY);
	
	/**
	 * Sets an action to be performed when this element is clicked. Note that not all elements will know what to do with their action.
	 */
	IActionable setAction(IGuiAction action);
	
	/**
	 * Performs this element's action (if it has one). Otherwise does nothing.
	 */
	boolean performAction(int mouseX, int mouseY, UiRoot sender);
}

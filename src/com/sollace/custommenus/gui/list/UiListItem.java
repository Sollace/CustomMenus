package com.sollace.custommenus.gui.list;

import com.sollace.custommenus.gui.element.UiElement;

/**
 * Items that can appear in a list.
 */
public interface UiListItem<T extends UiListItem<T>> extends UiElement {
	
	/**
	 * Gets a string representation of this item's value. Used in List to get the current value when {@link UiField.getDisplayString} is called.
	 */
	String getDisplayString();
	
	/**
	 * Draws the selection background for this item.
	 */
	void drawSelectionBox();
	
	/** 
	 * Returns true if this item is currently active (and it must draw a selection box)
	 */
	boolean isSelected();
	
	/**
	 * Returns true if this item can be moved.
	 */
	boolean isMoveable();
	
	/**
	 * Called when an item is moved.
	 * This is both done when an item requests itself to be moved, and when another item's movements alter this item's position.
	 */
	void itemMoved(int newPosition);
}

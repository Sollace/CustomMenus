package com.sollace.custommenus.gui.list;

import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.geometry.EnumEdge;
import com.sollace.custommenus.gui.input.UiField;

import net.minecraft.client.gui.FontRenderer;

public interface UiList<T extends UiListItem<T>> extends UiContainer, UiField<Integer> {
	/**
	 * Gets the font renderer for this list.
	 * Used in ListPacker to set the default element height.
	 */
	FontRenderer getFonts();
	
	int getItemColour();
	
	/**
	 * Sets an action to be performed when this list's selection changes.
	 */
	void setChangeAction(IGuiAction action);
	
	boolean setValue(int index, UiRoot sender);
	
	/**
	 * Gets the number of items on this list (excluding non-item elements).
	 */
	int size();
	
	/**
	 * Removes all elements from this list. Also removes non-list elements.
	 */
	void clear();
	
	T getSelectedItem();
	
	boolean canMove(T item, EnumEdge direction);
	
	/**
	 * Changes the position of an item in this list relative to its siblings. Returns the new index.
	 */
	int moveItem(T item, EnumEdge direction);
}

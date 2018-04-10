package com.sollace.custommenus.gui.geometry;

import com.sollace.custommenus.gui.container.UiView;

/**
 * Elements that can be moved in response to an external container's dimensions.
 *
 */
public interface IPositioned {
	
	/**
	 * Realigns this element and any children taking into account specified alignment.
	 * Called on initial creation, and subsequently to respond to viewport changes.
	 * 
	 * @param container	The container this element is being positioned relative to
	 */
	void reposition(UiView container);
}

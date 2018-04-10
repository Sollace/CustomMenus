package com.sollace.custommenus.gui.overlays;

import com.sollace.custommenus.gui.action.IActionable;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.IRenderable;
import com.sollace.custommenus.gui.geometry.IPositioned;

/**
 * Overlay class for any elements to be rendered over the current screen.
 * (WIP)
 */
public interface UiOverlay extends IRenderable, IActionable, IPositioned {
	
	void destroy();
	
	void update(UiRoot sender);
}

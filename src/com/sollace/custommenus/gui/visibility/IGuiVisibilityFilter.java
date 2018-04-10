package com.sollace.custommenus.gui.visibility;

import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.element.UiElement;

@FunctionalInterface
public interface IGuiVisibilityFilter {
	boolean filter(UiContainer container, UiElement element);
}

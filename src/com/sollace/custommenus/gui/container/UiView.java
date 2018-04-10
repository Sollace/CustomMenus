package com.sollace.custommenus.gui.container;

public interface UiView {

	/**
	 * Gets the relative viewport for elements inside this container.
	 * May be this container itself, but may also be any parent container's viewport.
	 * Elements should treat this as their absolute viewport.
	 */
	IViewPort getViewPort();
	
	/**
	 * Gets the container relating to this view.
	 * May be either this element itself or its enclosing scope.
	 */
	UiContainer getContainer();
}

package com.sollace.custommenus.resources;

import com.sollace.custommenus.gui.element.IRenderable;
import com.sollace.custommenus.gui.geometry.UiBounded;

public interface IIcon extends IRenderable, UiBounded {
	boolean canLoad();
	
	boolean valid();
	
	void load();
}

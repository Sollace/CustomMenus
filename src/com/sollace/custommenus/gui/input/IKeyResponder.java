package com.sollace.custommenus.gui.input;

import com.sollace.custommenus.gui.container.UiRoot;

@FunctionalInterface
public interface IKeyResponder {
	
	public boolean keyPressed(UiRoot sender, char key, int keyCode);
}

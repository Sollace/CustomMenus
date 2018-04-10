package com.sollace.custommenus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import com.mumfrey.liteloader.client.gui.GuiLiteLoaderPanel;
import com.sollace.custommenus.gui.container.UiRoot;

@Mixin(GuiLiteLoaderPanel.class)
public abstract class MGuiLiteLoaderPanel implements UiRoot {
	
	@Override
	public boolean isNonNative() {
		return true;
	}
}

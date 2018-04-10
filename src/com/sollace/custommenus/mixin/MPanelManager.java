package com.sollace.custommenus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mumfrey.liteloader.client.LiteLoaderPanelManager;
import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.input.UiField;

import net.minecraft.client.gui.GuiScreen;

@Mixin(LiteLoaderPanelManager.class)
public abstract class MPanelManager<T> {
	
	@Inject(method = "isPanelSupportedOnScreen(Lnet/minecraft/client/gui/GuiScreen;)Z", remap = false, at = @At("HEAD"), cancellable = true)
	private void onIsPanelSupportedOnScreen(GuiScreen screen, CallbackInfoReturnable<Boolean> info) {
		final UiField<Boolean> panel = GuiNull.nonNull((UiRoot)screen).getField("liteloaderpanel");
		info.setReturnValue(panel != null && panel.getValue() == true);
	}
}

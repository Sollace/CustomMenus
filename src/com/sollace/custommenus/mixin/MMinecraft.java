package com.sollace.custommenus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sollace.custommenus.GuiMenus;
import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.privileged.IGame;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

@Mixin(Minecraft.class)
public abstract class MMinecraft implements IGame {
	
	@Invoker("getCurrentAction")
	public abstract String getPlayerState();
	
	//public void displayGuiScreen(@Nullable GuiScreen guiScreenIn)
	@Inject(method = "displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V", at = @At("HEAD"), cancellable = true)
	private void onDisplayGuiScreen(GuiScreen screen, CallbackInfo info) {
		GuiMenus.INSTANCE.displayGuiScreen((Minecraft)(Object)this, GuiNull.nonNull((UiRoot)screen), info);
	}
}

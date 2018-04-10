package com.sollace.custommenus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;

@Mixin(GuiMultiplayer.class)
public interface MGuiMultiplayer {
	@Accessor("selectedServer")
	ServerData getSelectedServer();
}

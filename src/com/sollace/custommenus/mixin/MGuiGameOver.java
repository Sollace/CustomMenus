package com.sollace.custommenus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.util.text.ITextComponent;

@Mixin(GuiGameOver.class)
public interface MGuiGameOver {
	@Accessor("causeOfDeath")
	ITextComponent getPlayerDeathMessage();
}

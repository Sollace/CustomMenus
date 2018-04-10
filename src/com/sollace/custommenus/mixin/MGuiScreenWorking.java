package com.sollace.custommenus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import com.sollace.custommenus.GuiProgressor;

import net.minecraft.client.gui.GuiScreenWorking;

@Mixin(GuiScreenWorking.class)
public abstract class MGuiScreenWorking {

	@Inject(method = "displaySavingString(Ljava/lang/String;)V", at = @At("HEAD"))
	public void displaySavingString(String message, CallbackInfo info) {
		GuiProgressor.INSTANCE.updateMessage(message);
	}

	@Inject(method = "resetProgressAndMessage(Ljava/lang/String;)V", at = @At("HEAD"))
	public void resetProgressAndMessage(String message, CallbackInfo info) {
		GuiProgressor.INSTANCE.updateMessage(message);
	}

	@Inject(method = "displayLoadingString(Ljava/lang/String;)V", at = @At("HEAD"))
	public void displayLoadingString(String message, CallbackInfo info) {
		GuiProgressor.INSTANCE.changeStage(message);
	}

	@Inject(method = "setLoadingProgress(I)V", at = @At("HEAD"))
	public void setLoadingProgress(int progress, CallbackInfo info) {
		GuiProgressor.INSTANCE.progressed(progress);
	}

	@Inject(method = "setDoneWorking()V", at = @At("HEAD"))
	public void setDoneWorking(CallbackInfo info) {
		GuiProgressor.INSTANCE.completed();
	}
}

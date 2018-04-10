package com.sollace.custommenus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sollace.custommenus.GuiProgressor;

import net.minecraft.client.LoadingScreenRenderer;

@Mixin(LoadingScreenRenderer.class)
public class MLoadingScreenRenderer {

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

	@Inject(method = "setLoadingProgress(I)V", at = @At("HEAD"), cancellable = true)
	public void setLoadingProgress(int progress, CallbackInfo info) {
		GuiProgressor.INSTANCE.progressed(progress);
		if (GuiProgressor.INSTANCE.renderProgressScreen()) {
			info.cancel();
		}
	}

	@Inject(method = "setDoneWorking()V", at = @At("HEAD"))
	public void setDoneWorking(CallbackInfo info) {
		GuiProgressor.INSTANCE.completed();
	}
	
	@Inject(method = "displayString(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
	private void displayString(String message, CallbackInfo info) {
		if (GuiProgressor.INSTANCE.renderProgressScreen()) {
			info.cancel();
		}
	}
}

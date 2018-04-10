package com.sollace.custommenus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sollace.custommenus.GuiProgressor;
import com.sollace.custommenus.privileged.INativeProgressor;

import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;

@Mixin(GuiDownloadTerrain.class)
public abstract class MGuiDownloadTerrain extends GuiScreen implements INativeProgressor {
	
	private int progress;
	
	public int getProgress() {
		return progress;
	}
	
	@Override
	public void updateScreen() {
        ++this.progress;
        super.updateScreen();
    }
	
	@Inject(method = "drawScreen(IIF)V", at = @At("HEAD"), cancellable = true)
	public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
		if (GuiProgressor.INSTANCE.renderProgressScreen()) {
			info.cancel();
		}
	}
}
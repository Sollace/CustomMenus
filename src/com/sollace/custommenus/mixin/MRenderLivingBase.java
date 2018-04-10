package com.sollace.custommenus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sollace.dummy.DPlayer;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;

@Mixin(RenderLivingBase.class)
public abstract class MRenderLivingBase {
	
	@Inject(method = "canRenderName(Lnet/minecraft/entity/EntityLivingBase;)Z", at = @At("HEAD"), cancellable = true)
	public void canRenderName(EntityLivingBase entity, CallbackInfoReturnable<Boolean> info) {
		if (entity instanceof DPlayer) info.setReturnValue(false);
	}
}

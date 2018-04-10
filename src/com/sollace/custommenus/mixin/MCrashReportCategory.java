package com.sollace.custommenus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sollace.custommenus.GuiMenus;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;

@Mixin(CrashReportCategory.class)
public abstract class MCrashReportCategory {
	@Inject(method = "addDetail(Ljava/lang/String;Lnet/minecraft/crash/ICrashReportDetail;)V", at = @At("RETURN"))
	public void addDetail(String name, ICrashReportDetail<String> detail, CallbackInfo info) {
		if (name.contentEquals("Screen name")) {
			GuiMenus.INSTANCE.addCrashSections((CrashReportCategory)(Object)this);
		}
	}
}

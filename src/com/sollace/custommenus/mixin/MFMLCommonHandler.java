package com.sollace.custommenus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sollace.fml.FML;
import com.sollace.fml.IFML;

//
// This utilises a bug(?) in Sponge that allows you to apply a mixins to a public class using the string lookup intended for private classes.
// Normally this is documented to cause an error, but testing showed it to work perfectly well.
// Mumfry may need to clarify on this a little.
//
@Mixin(targets = "net.minecraftforge.fml.common.FMLCommonHandler")
public abstract class MFMLCommonHandler implements IFML {
	@Inject(method = "<init>()V", remap = false, at = @At("RETURN"))
	private void created(CallbackInfo info) {
		FML.instantiated(this);
	}
}

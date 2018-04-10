package com.sollace.custommenus.gui.element;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.utils.JsonUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class SignalBar extends AbstractElement {
	
	protected ResourceLocation texture = Gui.ICONS;
	
	private int strength = 0;
	
	private EnumPingType status = EnumPingType.PINGING;
	
	@Override
	public SignalBar init(JsonObject json) {
		strength = JsonUtils.get(json, "strength", strength);
		if (json.has("status")) status = EnumPingType.getByName(json.get("status").getAsString());
		return this;
	}
	
	@Override
	public SignalBar setLabel(String label) {
		return this;
	}
	
	@Override
	public void setTexture(String resource) {
		texture = new ResourceLocation(resource);
	}
	
	public SignalBar setPingType(EnumPingType type) {
		this.status = type;
		return this;
	}
	
	public SignalBar setSignalStrength(int strength) {
		this.strength = strength;
		return this;
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.color(1, 1, 1, 1);
		sender.getMinecraft().getTextureManager().bindTexture(texture);
		drawModalRectWithCustomSizedTexture(x + width - 15, y, status.ordinal() * 10, 176 + strength * 8, 10, 8, 256, 256);
	}
	
	public static enum EnumPingType {
		PINGED, PINGING;
		
		public static EnumPingType getByName(String name) {
			EnumPingType a = valueOf(name.toUpperCase());
			return a == null ? PINGING : a;
		}
	}
}

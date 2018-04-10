package com.sollace.custommenus.resources;

import java.awt.image.BufferedImage;

import org.apache.commons.lang3.Validate;

import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.geometry.BoundingBox;
import com.sollace.custommenus.privileged.IGame;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public abstract class LoadedIcon implements IIcon {
	
	private int x, y, width = 32, height = 32;
	
	protected final ResourceLocation missing, iconLocation;
	
	protected DynamicTexture icon = null;
	
	protected final IGame mc;
	
	public LoadedIcon(IGame mc, String location, String fallback) {
		this.mc = mc;
		missing = new ResourceLocation(fallback);
		iconLocation = new ResourceLocation(location);
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		load();
		
		GlStateManager.color(1, 1, 1, 1);
		sender.getMinecraft().getTextureManager().bindTexture(icon == null ? missing : iconLocation);
		
		GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GlStateManager.disableBlend();
	}
	
	public void load() {
		if (!canLoad()) return;
		
		if (!valid()) {
        	mc.getNative().getTextureManager().deleteTexture(iconLocation);
            icon = null;
        }
		
		BufferedImage buffer = read();
		
		if (buffer == null) {
			icon = null;
			return;
		}
		
		Validate.validState(buffer.getWidth() == width * 2, String.format("Must be %d pixels wide", width * 2));
        Validate.validState(buffer.getHeight() == height * 2, String.format("Must be %d pixels high", height * 2));
		
		if (icon == null) {
            icon = new DynamicTexture(buffer.getWidth(), buffer.getHeight());
            mc.getNative().getTextureManager().loadTexture(iconLocation, icon);
        }
		
        buffer.getRGB(0, 0, buffer.getWidth(), buffer.getHeight(), icon.getTextureData(), 0, buffer.getWidth());
        icon.updateDynamicTexture();
	}
	

	@Override
	public LoadedIcon setPos(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public LoadedIcon setSize(int w, int h) {
		this.width = w;
		this.height = h;
		return this;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(x, y, width, height);
	}

	@Override
	public void reposition(UiView container) {
		
	}
	
	protected abstract BufferedImage read();
}

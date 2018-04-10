package com.sollace.custommenus.mixin;

import java.io.IOException;
import java.util.Iterator;

import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.IViewPort;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.natives.INativeFerry;
import com.sollace.custommenus.gui.packing.UiPacker;
import com.sollace.custommenus.privileged.INativeGui;
import com.sollace.custommenus.registry.UiNatives;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

@Mixin(GuiScreen.class)
public abstract class MGuiScreen extends Gui implements INativeGui, UiRoot {
	
	@Shadow
	private Minecraft mc;
	
	@Override
	public String getViewPortId() {
		return UiNatives.getKeyFor(getNative());
	}
	
	@Accessor("width")
	public abstract int getWidth();
	
	@Accessor("height")
	public abstract int getHeight();
	
	@Override
	public int getMouseX() {
		return Mouse.getEventX() * getWidth() / mc.displayWidth;
	}
	
	@Override
	public int getMouseY() {
		return getHeight() - Mouse.getEventY() * getHeight() / mc.displayHeight - 1;
	}
	
	@Override
	public void setGlViewport() {
		GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
	}
	
	// Something about the throws declaration messes up obfuscation lookup.
	@Shadow
	protected abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
	
	public void onMouseClick(int mouseX, int mouseY, int mouseButton) throws IOException {
		this.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public Minecraft getMinecraft() {
		return mc;
	}
	
	@Override
	public INativeFerry getFerry() {
		return INativeFerry.empty(this);
	}
	
	@Override
	public void refresh() {
		show();
	}
	
	@Override
	public void show() {
		mc.displayGuiScreen(getNative());
	}
	
	@Override
	public void open(String gui) {
		getFerry().open(gui);
	}
	
	@Override
	public boolean isNonNative() {
		return false;
	}
	
	public void performIn(IGuiAction action, int ticks) {}
	
	public void addCloseListener(IGuiAction action) {}
	
	public void close() { }
	
	public UiContainer getContainer() {return this;}
	public IViewPort getViewPort() {return this;}
	public GuiScreen getNative() {return (GuiScreen)(Object)this;}
	
	public void setBackground(String resource) { }
	
	public void setDrawBackground(boolean draw) { }
	
	public void setAlwaysDrawBackground(boolean draw) { }
	
	public void setPausesGame(boolean pauses) {}
	
	public void addChild(UiElement child) { }
	
	public void addField(UiField<?> field) { }
	
	public UiElement getChild(int id) {return null;}
	
	public Iterator<UiElement> getElementsIterator() {
		return GuiNull.NULL.getElementsIterator();
	}
	
	public <T> UiField<T> getField(String name) {return null;}
	
	public void setPacker(UiPacker packer) { }
}

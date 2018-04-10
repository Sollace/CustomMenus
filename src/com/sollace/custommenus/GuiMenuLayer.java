package com.sollace.custommenus;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.natives.INativeFerry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiMenuLayer extends GuiCustomMenu {

	public GuiMenuLayer(INativeFerry ferry, UiRoot parent, JsonObject json) {
		super(parent.getViewPortId(), ferry, parent, json);
	}
	
	@Override
	public GuiScreen getNative() {
		return parent.getNative();
	}
	
	@Override
	public void updateScreen() {
		parent.getNative().updateScreen();
		super.updateScreen();
	}
	
	@Override
	public void initGui() {
		parent.getNative().initGui();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return parent.getNative().doesGuiPauseGame();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		parent.getNative().drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		parent.getNative().setWorldAndResolution(mc, width, height);
		super.setWorldAndResolution(mc, width, height);
	}
	
	@Override
	public void setGuiSize(int w, int h) {
		parent.getNative().setGuiSize(w, h);
		super.setGuiSize(w, h);
	}
	
	@Override
	public void onGuiClosed() {
		parent.getNative().onGuiClosed();
		super.onGuiClosed();
	}
	
	
	@Override
	public void drawDefaultBackground() {
		// noop
	}
	
	@Override
	public void handleKeyboardInput() throws IOException {
		parent.getNative().handleKeyboardInput();
		super.handleKeyboardInput();
	}
	
	@Override
	public void handleInput() throws IOException {
		parent.getNative().handleInput();
		super.handleInput();
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		parent.getNative().handleMouseInput();
		super.handleMouseInput();
	}
	
	
}

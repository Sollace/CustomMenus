package com.sollace.custommenus;

import java.util.Iterator;

import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.IViewPort;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.natives.INativeFerry;
import com.sollace.custommenus.gui.packing.UiPacker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

/**
 * A blank screen. Used where a GuiScreen and Minecraft.currentScreen would normally be null.
 */
public final class GuiNull implements UiRoot, Iterator<UiElement> {
	
	/**
	 * The UiRoot NULL value.
	 */
	public static final UiRoot NULL = new GuiNull();
	
	/**
	 * Consumes null values and always returns an instance of UiRoot.
	 */
	public static UiRoot nonNull(UiRoot obj) {
		return obj == null ? NULL : obj;
	}
	
	/**
	 * Gets the currently displayed screen (including natives)
	 * Never returns null.
	 */
	public static UiRoot currentScreen() {
		return nonNull((UiRoot)Minecraft.getMinecraft().currentScreen);
	}
	
	/**
	 * Gets the current system time adjusted for tick length and duration.
	 */
	public static long systemTime() {
		return Minecraft.getSystemTime();
	}
	
	private GuiNull() {}
	
	@Override
	public void addChild(UiElement child) {
		
	}

	@Override
	public void addField(UiField<?> field) {
		
	}

	@Override
	public UiElement getChild(int id) {
		return null;
	}
	
	@Override
	public Iterator<UiElement> getElementsIterator() {
		return this;
	}
	
	@Override
	public <T> UiField<T> getField(String name) {
		return null;
	}
	
	@Override
	public UiContainer getContainer() {
		return this;
	}

	@Override
	public IViewPort getViewPort() {
		return this;
	}

	@Override
	public String getViewPortId() {
        if (getMinecraft().world != null && getMinecraft().player.getHealth() <= 0) {
            return "gameover";
        }
		
		return "mainmenu";
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getMouseX() {
		return 0;
	}

	@Override
	public int getMouseY() {
		return 0;
	}
	
	@Override
	public void setGlViewport() {
		
	}

	@Override
	public Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}

	@Override
	public void refresh() {
		
	}

	@Override
	public void show() {
		getMinecraft().displayGuiScreen(null);
	}

	@Override
	public void open(String gui) {
		currentScreen().getFerry().open(gui);
	}
	
	@Override
	public void close() {
		
	}
	
	@Override
	public void performIn(IGuiAction action, int ticks) {
		action.perform(currentScreen());
	}
	
	@Override
	public void addCloseListener(IGuiAction action) {
		action.perform(currentScreen());
	}
	
	@Override
	public GuiScreen getNative() {
		return null;
	}
	
	@Override
	public INativeFerry getFerry() {
		return INativeFerry.empty(this);
	}
	
	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public UiElement next() {
		return null;
	}

	@Override
	public void setBackground(String resource) {
		
	}

	@Override
	public void setDrawBackground(boolean draw) {
		
	}
	
	public void setAlwaysDrawBackground(boolean draw) {
		
	}
	
	@Override
	public void setPausesGame(boolean pauses) {
		
	}
	
	@Override
	public void setPacker(UiPacker packer) {
		
	}
	
	@Override
	public boolean equals(Object other) {
		return other == null || other == NULL;
	}
	
	@Override
	public boolean isNonNative() {
		return false;
	}
	
	@Override
	public String toString() {
		return "UiRoot@NULL";
	}
}

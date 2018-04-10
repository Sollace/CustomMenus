package com.sollace.custommenus.gui.natives;

import com.sollace.custommenus.gui.action.IGuiDecider;
import com.sollace.custommenus.gui.container.UiRoot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiYesNoCallback;

public class NativeYesNoCallback extends GuiCreateWorld implements GuiYesNoCallback, Runnable {
	
	private final IGuiDecider callback;
	
	private final UiRoot parent;
	
	private final INativeFerry ferry;
	
	public NativeYesNoCallback(INativeFerry ferry, UiRoot parent, IGuiDecider callback) {
		super(null);
		this.ferry = ferry;
		this.callback = callback;
		this.parent = parent;
	}
	
	public void confirmClicked(boolean result, int id) {
		if (callback == null) return;
		
		if (result) {
			callback.yes(parent);
		} else {
			callback.no(parent);
		}
	}
	
	public void run() {
		if (callback != null) callback.yes(parent);
	}
	
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		if (ferry.has("genSettings")) ferry.param("genSettings", this.chunkProviderSettingsJson);
		
		run();
	}
}

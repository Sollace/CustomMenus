package com.sollace.custommenus.gui.overlays;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.IViewPort;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.element.AbstractElement;
import com.sollace.custommenus.gui.geometry.PaddingBox;
import com.sollace.custommenus.privileged.INativeGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;

public class RealmsNotifications extends AbstractElement implements UiOverlay {
	
	private GuiScreen realmsNotification = null;
	
	private PaddingBox padding = new PaddingBox();
	
	public RealmsNotifications(UiContainer container) {
		
	}
	
	private boolean areRealmsNotificationsEnabled() {
        return Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS);
    }
	
	@Override
	public RealmsNotifications init(JsonObject json) {
		padding.init(json);
		return this;
	}
	

	@Override
	public RealmsNotifications setLabel(String label) {
		return this;
	}

	@Override
	public void setTexture(String resource) {
		
	}
	
	@Override
	public void destroy() {
		if (realmsNotification != null) {
			realmsNotification.onGuiClosed();
		}
	}
	
	@Override
	public void update(UiRoot sender) {
		sender.getMinecraft().currentScreen = new GuiMainMenu();
		if (realmsNotification != null) {
			realmsNotification.updateScreen();
		}
		sender.getMinecraft().currentScreen = sender.getNative();
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		if (!visible) return;
		
		if (areRealmsNotificationsEnabled()) {
			if (realmsNotification == null) {
	            sender.getMinecraft().setConnectedToRealms(false);
	            
	            realmsNotification = new RealmsBridge().getNotificationScreen(sender.getNative());
	            
	            realmsNotification.setGuiSize(width, height);
	            realmsNotification.initGui();
			}
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + padding.getLeft(), y + padding.getTop(), 0);
			
			realmsNotification.drawScreen(mouseX - (x + padding.getLeft()), mouseY - (y + padding.getTop()), partialTicks);
			
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
		try {
			((INativeGui)realmsNotification).onMouseClick(mouseX - x, mouseY - y, 0);
		} catch (IOException e) {}
		return false;
	}
	
	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return realmsNotification != null;
	}
	
	@Override
	public void reposition(UiView container) {
		
		
		IViewPort view = container.getViewPort();
		
		width = view.getWidth();
		height = 0;
		
		padding.reposition(container);
		
		super.reposition(container);
		
		if (realmsNotification != null) {
			realmsNotification.setGuiSize(width, height);
            realmsNotification.initGui();
		}
	}
	
	@Override
	public RealmsNotifications setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}
	
	@Override
	public RealmsNotifications setAction(IGuiAction action) {
		this.action = action;
		return this;
	}
	
}

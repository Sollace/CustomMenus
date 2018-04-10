package com.sollace.custommenus;

import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.locale.Locales;
import com.sollace.custommenus.privileged.IGame;
import com.sollace.custommenus.privileged.INativeProgressor;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.server.integrated.IntegratedServer;

public class GuiProgressor {
	
	public static final GuiProgressor INSTANCE = new GuiProgressor();
	
    public String title = "";
    public String stage = "";
    public int progress;
    public boolean doneWorking;
	
    int screenWidth, screenHeight;
    
    private Framebuffer buffer;
    private ScaledResolution res;
    
	public void updateMessage(String message) {
		title = message;
		progressed(0);
	}
	
	public void changeStage(String message) {
		stage = message;
		progressed(0);
	}
	
	public void progressed(int progress) {
		this.progress = progress;
		doneWorking = false;
	}
	
	public void completed() {
		doneWorking = true;
		loadingScreen = null;
	}
	
	private GuiScreen loadingScreen = null;
	
	public boolean renderProgressScreen() {
		IGame mc = IGame.current();
		
		if (!mc.isRunning()) {
            return false;
        }
		
		UiRoot current = GuiNull.currentScreen();
		
		if (GuiMenus.INSTANCE.hasGuiConfigurationFor("loading")) {
			boolean useBuffer = true;
			
			if (loadingScreen == null) {
				loadingScreen = GuiMenus.INSTANCE.createCustomMenu(current, current.getFerry(), "loading").getNative();
			}
			
			if (!(current instanceof GuiCustomMenu)) {
				useBuffer = current.getViewPortId() != "terraingen";
				
				if (current instanceof INativeProgressor) {
					title = Locales.translateUnformatted("multiplayer.downloadingTerrain");
					stage = "";
					progress = ((INativeProgressor)current).getProgress() % 100;
				}
			}
			
			IntegratedServer is = mc.getNative().getIntegratedServer();
			if (is != null && is.percentDone > 0) {
				stage = is.currentTask;
				progress = is.percentDone;
			}
			
			if (loadingScreen == null) return false;
			
			if (res == null || mc.getNative().displayWidth != screenWidth || mc.getNative().displayHeight != screenHeight) {
				screenWidth = mc.getNative().displayWidth;
				screenHeight = mc.getNative().displayHeight;
				res = new ScaledResolution(mc.getNative());
				
				buffer = new Framebuffer(screenWidth, screenHeight, false);
				buffer.setFramebufferFilter(9728);
			}
			
			if (useBuffer) readyFrameBuffer();
			loadingScreen.onResize(mc.getNative(), res.getScaledWidth(), res.getScaledHeight());
			loadingScreen.drawScreen(current.getMouseX(), current.getMouseY(), mc.getNative().getTickLength());
			if (useBuffer) {
				paintFrame();
				
				mc.getNative().updateDisplay();
				
				try {
		            Thread.yield();
		        } catch (Exception e) {}
			}
			return true;
		}
		return false;
	}
	
	private void readyFrameBuffer() {
		if (OpenGlHelper.isFramebufferEnabled()) {
            buffer.framebufferClear();
        } else {
            GlStateManager.clear(256);
        }
		
		buffer.bindFramebuffer(false);
		
		GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0, res.getScaledWidth_double(), res.getScaledHeight_double(), 0, 100, 300);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0, 0, -200);
        
        if (!OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.clear(16640);
        }
	}
	
	private void paintFrame() {
		buffer.unbindFramebuffer();
		
		if (OpenGlHelper.isFramebufferEnabled()) {
			int factor = res.getScaleFactor();
            buffer.framebufferRender(res.getScaledWidth() * factor, res.getScaledHeight() * factor);
        }
	}
}

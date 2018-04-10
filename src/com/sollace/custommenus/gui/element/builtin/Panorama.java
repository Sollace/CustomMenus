package com.sollace.custommenus.gui.element.builtin;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.Project;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.element.AbstractElement;
import com.sollace.custommenus.gui.element.UiElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class Panorama extends AbstractElement {
	
	
	private DynamicTexture viewportTexture = new DynamicTexture(256, 256);
	private ResourceLocation backgroundTexture;
	
	private Minecraft mc;
	
	private final ResourceLocation[] TITLE_PANORAMA_PATHS = new ResourceLocation[] {
		new ResourceLocation("textures/gui/title/background/panorama_0.png"),
		new ResourceLocation("textures/gui/title/background/panorama_1.png"),
		new ResourceLocation("textures/gui/title/background/panorama_2.png"),
		new ResourceLocation("textures/gui/title/background/panorama_3.png"),
		new ResourceLocation("textures/gui/title/background/panorama_4.png"),
		new ResourceLocation("textures/gui/title/background/panorama_5.png")
	};
	
	private int blurFactor = 7;
	
	private float panoramaTimer = 0, animationSpeed = 1;
	
	private boolean animate = true, blur = true, gradient = true;
	
	public Panorama(UiContainer container) {
		
	}
	
	@Override
	public UiElement init(JsonObject json) {
		if (json.has("blurAmount")) blurFactor = (json.get("blurAmount").getAsInt() * 2) + 1;
		if (json.has("animationSpeed")) animationSpeed = json.get("animationSpeed").getAsFloat();
		
		if (json.has("animate")) animate = json.get("animate").getAsBoolean();
		if (json.has("blur")) blur = json.get("blur").getAsBoolean();
		if (json.has("gradient")) gradient = json.get("gradient").getAsBoolean();
		
		return this;
	}
	
	@Override
	public void reposition(UiView container) {
		x = y = 0;
		width = container.getViewPort().getWidth();
		height = container.getViewPort().getHeight();
		viewportTexture = new DynamicTexture(256, 256);
        backgroundTexture = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("background", viewportTexture);
	}
	
	@Override
	public UiElement setLabel(String label) {
		return this;
	}
	
	@Override
	public void setTexture(String resource) {
		for (int i = 0; i < TITLE_PANORAMA_PATHS.length; i++) {
			TITLE_PANORAMA_PATHS[i] = new ResourceLocation(String.format(resource, i));
		}
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		this.mc = sender.getMinecraft();
		
		if (animate) panoramaTimer += partialTicks * animationSpeed;
		
		GlStateManager.disableAlpha();
		
		mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        
        drawPanorama(mouseX, mouseY, partialTicks);
        
        if (Display.isVisible()) {
        	for (int count = 0; count < blurFactor; count++) {
            	rotateAndBlurSkybox();
            }
		}
        
        mc.getFramebuffer().bindFramebuffer(true);
        sender.getViewPort().setGlViewport();
        
        float f = 120 / (float)(width > height ? width : height);
        float f1 = height * f / 256f;
        float f2 = width * f / 256f;
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0, height, zLevel).tex(.5f - f1, .5f + f2).color(1f, 1f, 1f, 1f).endVertex();
        bufferbuilder.pos(width, height, zLevel).tex(.5f - f1, .5f - f2).color(1f, 1f, 1f, 1f).endVertex();
        bufferbuilder.pos(width, 0, zLevel).tex(.5f + f1, .5f - f2).color(1f, 1f, 1f, 1f).endVertex();
        bufferbuilder.pos(0, 0, zLevel).tex(.5f + f1, .5f + f2).color(1f, 1f, 1f, 1f).endVertex();
        tessellator.draw();
        
        GlStateManager.enableAlpha();
        
        if (gradient) {
	        drawGradientRect(0, 0, width, height, -2130706433, 16777215);
	        drawGradientRect(0, 0, width, height, 0, Integer.MIN_VALUE);
        }
	}
	
    private void drawPanorama(int mouseX, int mouseY, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120, 1, 0.05F, 10);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.rotate(90, 0, 0, 1);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        
        for (int j = 0; j < 64; ++j) {
            GlStateManager.pushMatrix();
            float f = ((float)(j % 8) / 8 - 0.5F) / 64F;
            float f1 = ((float)(j / 8) / 8 - 0.5F) / 64F;
            
            GlStateManager.translate(f, f1, 0);
            GlStateManager.rotate(MathHelper.sin(panoramaTimer / 400) * 25 + 20, 1, 0, 0);
            GlStateManager.rotate(-panoramaTimer * 0.1F, 0, 1, 0);

            for (int k = 0; k < 6; k++) {
                GlStateManager.pushMatrix();
            	
                if (k == 1) GlStateManager.rotate(90, 0, 1, 0);
                if (k == 2) GlStateManager.rotate(180, 0, 1, 0);
                if (k == 3) GlStateManager.rotate(-90, 0, 1, 0);
                if (k == 4) GlStateManager.rotate(90, 1, 0, 0);
                if (k == 5) GlStateManager.rotate(-90, 1, 0, 0);

                mc.getTextureManager().bindTexture(TITLE_PANORAMA_PATHS[k]);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                
                bufferbuilder.pos(-1, -1, 1).tex(0, 0).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(1, -1, 1).tex(1, 0).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(1, 1, 1).tex(1, 1).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(-1, 1, 1).tex(0, 1).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        bufferbuilder.setTranslation(0, 0, 0);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }
    
    private void rotateAndBlurSkybox() {
        mc.getTextureManager().bindTexture(backgroundTexture);
        
        GlStateManager.glTexParameteri(3553, 10241, 9729);
        GlStateManager.glTexParameteri(3553, 10240, 9729);
        GlStateManager.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        
        if (blur) {
	        GlStateManager.enableBlend();
	        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }
        GlStateManager.colorMask(true, true, true, false);
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        
        for (int j = 0; j < 3; ++j) {
            float f = 1 / (float)(j + 1);
            int k = width;
            int l = height;
            float f1 = (float)(j - 1) / 256;
            bufferbuilder.pos(k, l, zLevel).tex(f1, 1).color(1, 1, 1, f).endVertex();
            bufferbuilder.pos(k, 0, zLevel).tex(1 + f1, 1).color(1, 1, 1, f).endVertex();
            bufferbuilder.pos(0, 0, zLevel).tex(1 + f1, 0).color(1, 1, 1, f).endVertex();
            bufferbuilder.pos(0, l, zLevel).tex(f1, 0).color(1, 1, 1, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }
}

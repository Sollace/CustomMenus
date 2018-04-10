package com.sollace.custommenus.gui.container;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.geometry.BoundingBox;
import com.sollace.custommenus.gui.geometry.EnumEdge;
import com.sollace.custommenus.gui.geometry.UiBounded;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class ScrollContainer extends Container {
	
	protected Scrollbar scrollbar = new Scrollbar(this);
	
	private BoundingBox contentBoundingBox = null;
	
	private ResourceLocation background = Gui.OPTIONS_BACKGROUND;
	
	public ScrollContainer(UiContainer container) {
		super(container);
	}
	
	@Override
	public UiElement init(JsonObject json) {
		super.init(json);
		if (json.has("scrollAlignment")) scrollbar.setAlignment(EnumEdge.getByName(json.get("scrollAlignment").getAsString()));
		return this;
	}
	
	@Override
	public void setBackground(String resource) {
		background = new ResourceLocation(resource);
	}
	
	@Override
	public void reposition(UiView container) {
		super.reposition(container);
		contentBoundingBox = calculateContentBoundingBox();
		scrollbar.reposition(container);
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
		scrollbar.performAction(mouseX - x - padding.getLeft(), mouseY - y - padding.getTop(), sender);
		return super.performAction(mouseX, mouseY, sender);
	}
	
	protected int getTranslateX() {
		return super.getTranslateX() - scrollbar.getScrollX();
	}
	
	protected int getTranslateY() {
		return super.getTranslateY() - scrollbar.getScrollY();
	}
	
	public BoundingBox getContentBoundingBox() {
		if (contentBoundingBox == null) contentBoundingBox = calculateContentBoundingBox();
		return contentBoundingBox;
	}
	
	protected BoundingBox calculateContentBoundingBox() {
		BoundingBox result = null;
		
		for (UiElement i : children) {
			if (i instanceof UiBounded) {
				if (result == null) {
					result = ((UiBounded)i).getBoundingBox();
				} else {
					result.add(((UiBounded)i).getBoundingBox());
				}
			}
		}
		return BoundingBox.nonNull(result);
	}
	
	protected void handleMouseEvents() {
		float scrollSpeedY = Mouse.getEventDWheel();
		
		BoundingBox content = getContentBoundingBox();
		
        if (scrollSpeedY != 0 & content.height > 0) {
        	scrollSpeedY /= content.height/10f;
            scrollbar.scrollBy(0, Math.round(scrollSpeedY));
        }
	}

	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		if (!visible) return;
		
		handleMouseEvents();
		
        int top = y + this.padding.getTop();
        int bottom = top + this.getHeight();
        int left = x + padding.getLeft();
        int right = left + this.getWidth();
		
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        
        TextureManager mc = sender.getMinecraft().getTextureManager();
        
        drawBackground(mc, left, right, top, bottom);
        //setGlViewport();
		
		GlStateManager.pushMatrix();
		ScaledResolution scaledresolution = new ScaledResolution(sender.getMinecraft());
        int f = scaledresolution.getScaleFactor();
		
        GL11.glScissor((x + padding.getLeft()) * f, (y + padding.getBottom()) * f, getWidth() * f, getHeight() * f);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		
        super.render(sender, mouseX, mouseY, partialTicks);
        
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        
        GlStateManager.popMatrix();
        
        GlStateManager.disableDepth();
        
		overlayBackground(mc, left, right, top - 1, top);
		overlayBackground(mc, left, right, bottom, bottom + 1);
		
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.disableTexture2D();
        GlStateManager.color(1, 1, 1, 1);
        
        //top shadow
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(left, top + 4, 0).tex(0, 1).color(0, 0, 0, 0).endVertex();
        bufferbuilder.pos(right, top + 4, 0).tex(1, 1).color(0, 0, 0, 0).endVertex();
        bufferbuilder.pos(right, top, 0).tex(1, 0).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(left, top, 0).tex(0, 0).color(0, 0, 0, 255).endVertex();
        tessellator.draw();
        
        //bottom shadow
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(left, bottom, 0).tex(0, 1).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(right, bottom, 0).tex(1, 1).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(right, bottom - 4, 0).tex(1, 0).color(0, 0, 0, 0).endVertex();
        bufferbuilder.pos(left, bottom - 4, 0).tex(0, 0).color(0, 0, 0, 0).endVertex();
        tessellator.draw();
        
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
		
		GlStateManager.pushMatrix();
        GlStateManager.translate(x + padding.getLeft(), y + padding.getTop(), 0);
        
        mouseX -= x + padding.getLeft();
        mouseY -= y + padding.getTop();
        
        scrollbar.render(sender, mouseX, mouseY, partialTicks);
		
		GlStateManager.popMatrix();
	}
	
	@Override
	public void mouseMove(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		int relativeX = mouseX - x - padding.getLeft();
		int relativeY = mouseY - y - padding.getTop();
		
		scrollbar.mouseMove(sender, relativeX, relativeY, partialTicks);
		super.mouseMove(sender, mouseX, mouseY, partialTicks);
	}

	@Override
	public void mouseUp(int mouseX, int mouseY) {
		scrollbar.mouseUp(mouseX - x - padding.getLeft(), mouseY - y - padding.getTop());
		super.mouseUp(mouseX, mouseY);
	}
	
	protected void overlayBackground(TextureManager mc, int left, int right, int startY, int endY) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        mc.bindTexture(background);
        GlStateManager.color(1, 1, 1, 1);
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(left, endY, 0).tex(0, endY / 32f).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(right, endY, 0).tex((right - left) / 32f, endY / 32f).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(right, startY, 0).tex((right - left) / 32f, startY / 32f).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(left, startY, 0).tex(0, startY / 32f).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }
	
	protected void drawBackground(TextureManager mc, int left, int right, int top, int bottom) {
		Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(1, 1, 1, 1);
        
        mc.bindTexture(background);
        
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(left, bottom, 0).tex((left + scrollbar.getScrollX()) / 32f, (bottom + scrollbar.getScrollY()) / 32f).color(32, 32, 32, 255).endVertex();
        bufferbuilder.pos(right, bottom, 0).tex((right + scrollbar.getScrollX()) / 32f, (bottom + scrollbar.getScrollY()) / 32f).color(32, 32, 32, 255).endVertex();
        bufferbuilder.pos(right, top, 0).tex((right + scrollbar.getScrollX()) / 32f, (top + scrollbar.getScrollY()) / 32f).color(32, 32, 32, 255).endVertex();
        bufferbuilder.pos(left, top, 0).tex((left + scrollbar.getScrollX()) / 32f, (top + scrollbar.getScrollY()) / 32f).color(32, 32, 32, 255).endVertex();
        tessellator.draw();
	}
	
	@Override
	public void setGlViewport() {
		
	}
}

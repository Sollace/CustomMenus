package com.sollace.custommenus.gui.container;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.element.IRenderable;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.geometry.BoundingBox;
import com.sollace.custommenus.gui.geometry.EnumEdge;
import com.sollace.custommenus.gui.input.IMouseResponder;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;

public class Scrollbar implements IMouseResponder, UiElement {
	
	private boolean dragging = false, touching = false;
	
	private int thickness = 6;
	
	private int scrollX = 0, scrollY = 0;
	
	private float scrollMomentumX = 0, scrollMomentumY;
	
	private int maxScrollX = 0, maxScrollY = 0, shiftFactor = 0;
	
	private float scrollFactorX, scrollFactorY;
	
	private int initialMouseX, initialMouseY;
	
	protected ScrollContainer container;
	
	protected SoundEvent sound = null;
	
	protected EnumEdge alignment = EnumEdge.RIGHT;
	
	protected IRenderable tooltip = null;
	
	public Scrollbar(ScrollContainer container) {
		this.container = container;
	}
	
	public int getScrollX() {
		return isVisible() && alignment.isHorizontal() ? scrollX : 0;
	}
	
	public int getScrollY() {
		return !isVisible() | alignment.isHorizontal() ? 0 : scrollY;
	}
	
	public int getScrollAmount() {
		return isVisible() ? alignment.isHorizontal() ? scrollX : scrollY : 0;
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		if (!touching && !dragging) {
			scrollMomentumX *= partialTicks;
			scrollMomentumY *= partialTicks;
			if (scrollMomentumX > 0 || scrollMomentumY > 0) {
				scrollBy(scrollMomentumX, scrollMomentumY);
			}
			
			if (shiftFactor != 0) {
				scrollBy(shiftFactor * scrollFactorX, shiftFactor * scrollFactorY);
				shiftFactor = computeShiftFactor(mouseX, mouseY);
			}
		}
		
		if (!isVisible()) return;
		
		if (alignment.isHorizontal()) {
			renderHorizontal();
		} else {
			renderVertical();
		}
	}
	
	protected void renderHorizontal() {
		int elementWidth = container.getViewPort().getWidth();
		int contentWidth = container.getContentBoundingBox().right;
		
        int scrollbarWidth = getScrubberLength(elementWidth, contentWidth);
        int scrollbarLeft = getScrubberStart(scrollbarWidth, elementWidth, contentWidth);
        
        int scrollbarBottom = alignment == EnumEdge.TOP ? thickness : container.getViewPort().getHeight();
        int scrollbarTop = alignment == EnumEdge.TOP ? 0 : scrollbarBottom - thickness;
        
        renderBackground(scrollbarTop, 0, scrollbarBottom, elementWidth);
        renderBar(scrollbarLeft, scrollbarLeft + scrollbarWidth, scrollbarTop, scrollbarBottom);
	}
	
	protected void renderVertical() {
		BoundingBox contentBounds = container.getContentBoundingBox();
		
		int elementHeight = container.getViewPort().getHeight();
		int contentHeight = contentBounds.bottom;
		
        int scrollbarHeight = getScrubberLength(elementHeight, contentHeight);
        int scrollbarTop = getScrubberStart(scrollbarHeight, elementHeight, contentHeight);
        
        int scrollbarRight = alignment == EnumEdge.LEFT ? contentBounds.left - 1 : contentBounds.right + thickness + 1;
        int scrollbarLeft = alignment == EnumEdge.LEFT ? contentBounds.left - thickness - 1 : scrollbarRight - thickness;
        
        renderBackground(0, scrollbarLeft, elementHeight, scrollbarRight);
        renderBar(scrollbarLeft, scrollbarRight, scrollbarTop, scrollbarTop + scrollbarHeight);
	}
	
	protected int getScrubberStart(int scrollbarHeight, int elementHeight, int contentHeight) {
		int scrollbarTop = getScrollAmount() * (elementHeight - scrollbarHeight) / maxScrollY;
		if (scrollbarTop < 0) return 0;
        return scrollbarTop;
	}
	
	protected int getScrubberLength(int elementL, int contentL) {
		return MathHelper.clamp(elementL * elementL / contentL, 32, elementL - 8);
	}
	
	private void renderBackground(int top, int left, int bottom, int right) {
		Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableTexture2D();
        
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(left, bottom, 0).tex(0, 1).color(0, 0, 0, 150).endVertex();
        bufferbuilder.pos(right, bottom, 0).tex(1, 1).color(0, 0, 0, 150).endVertex();
        bufferbuilder.pos(right, top, 0).tex(1, 0).color(0, 0, 0, 150).endVertex();
        bufferbuilder.pos(left, top, 0).tex(0, 0).color(0, 0, 0, 150).endVertex();
        tessellator.draw();
        
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
	}
	
	private void renderBar(int left, int right, int top, int bottom) {
		Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableTexture2D();
        
        int B = dragging ? 170 : 128;
        int b = dragging ? 252 : 192;
        
        //actual bar
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(left, bottom, 0).tex(0, 1).color(128, 128, B, 255).endVertex();
        bufferbuilder.pos(right, bottom, 0).tex(1, 1).color(128, 128, B, 255).endVertex();
        bufferbuilder.pos(right, top, 0).tex(1, 0).color(128, 128, B, 255).endVertex();
        bufferbuilder.pos(left, top, 0).tex(0, 0).color(128, 128, B, 255).endVertex();
        tessellator.draw();
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(left, bottom - 1, 0).tex(0, 1).color(192, 192, b, 255).endVertex();
        bufferbuilder.pos(right - 1, bottom - 1, 0).tex(1, 1).color(192, b, 192, 255).endVertex();
        bufferbuilder.pos(right - 1, top, 0).tex(1, 0).color(192, 192, b, 255).endVertex();
        bufferbuilder.pos(left, top, 0.0D).tex(0, 0).color(192, 192, b, 255).endVertex();
        tessellator.draw();
        
        GlStateManager.enableTexture2D();
	}
	
	public boolean isVisible() {
		return (alignment.isHorizontal() ? maxScrollX : maxScrollY) > 0;
	}
	

	@Override
	public Scrollbar setEnabled(boolean enable) {
		return this;
	}
	
	@Override
	public Scrollbar setVisible(boolean visible) {
		return this;
	}

	@Override
	public Scrollbar setLabel(String label) {
		return this;
	}
	
	@Override
	public Scrollbar setToolTip(IRenderable tooltip) {
		this.tooltip = tooltip;
		return this;
	}
	
	public IRenderable getToolTipAt(int mouseX, int mouseY) {
		return tooltip;
	}
	
	@Override
	public Scrollbar setAction(IGuiAction action) {
		return this;
	}

	@Override
	public UiElement init(JsonObject json) {
		return this;
	}
	
	public void setAlignment(EnumEdge alignment) {
		this.alignment = alignment;
	}
	
	@Override
	public void setTexture(String resource) {
		
	}
	
	@Override
	public void setSound(String sound) {
		this.sound = SoundEvent.REGISTRY.getObject(new ResourceLocation(sound));
	}
	
	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		BoundingBox contentBounds = container.getContentBoundingBox();
		if (alignment == EnumEdge.RIGHT) {
			return mouseX >= contentBounds.right + 1 && mouseX <= contentBounds.right + thickness + 1;
		}
		if (alignment == EnumEdge.LEFT) {
			return mouseX < contentBounds.left - 1 && mouseX >= contentBounds.left - thickness - 1;
		}
		if (alignment == EnumEdge.TOP) {
			return mouseY >= 0 && mouseY <= thickness;
		}
		if (alignment == EnumEdge.BOTTOM) {
			return mouseY >= contentBounds.bottom && mouseY <= container.getViewPort().getHeight();
		}
		return false;
	}
	

	@Override
	public void playSound(SoundHandler sounds) {
		if (sound != null) sounds.playSound(PositionedSoundRecord.getMasterRecord(sound, 1));
	}
	
	private int computeShiftFactor(int mouseX, int mouseY) {
		int elementL, contentL, pos;
		
		if (alignment.isHorizontal()) {
			elementL = container.getViewPort().getWidth();
			contentL = container.getContentBoundingBox().right;
			pos = mouseX;
		} else {
			elementL = container.getViewPort().getHeight();
			contentL = container.getContentBoundingBox().bottom;
			pos = mouseY;
		}
		
		int scrubberLength = getScrubberLength(elementL, contentL);
		int scrubberStart = getScrubberStart(scrubberLength, elementL, contentL); 
		
		if (pos < scrubberStart) {
			return 1;
		} else if (pos > scrubberStart + scrubberLength) {
			return -1;
		}
		return 0;
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
		initialMouseX = mouseX;
		initialMouseY = mouseY;
		
		if (!isFocused(mouseX, mouseY)) {
			touching = true;
			return true;
		}
		
		shiftFactor = computeShiftFactor(mouseX, mouseY);
		
		if (shiftFactor == 0) {
			playSound(sender.getMinecraft().getSoundHandler());
			dragging = true;
		}
		
		return true;
	}

	@Override
	public void reposition(UiView container) {
		BoundingBox content = this.container.getContentBoundingBox();
		
		IViewPort view = this.container.getViewPort();
		
		maxScrollX = content.right - view.getWidth() + 5;
		if (maxScrollX < 0) maxScrollX = 0;
		
		maxScrollY = content.bottom - view.getHeight() + 5;
		if (maxScrollY < 0) maxScrollY = 0;
		
		scrollFactorX = view.getWidth() == 0 ? 1 : content.right / (float)view.getWidth();
		scrollFactorY = view.getHeight() == 0 ? 1 : content.bottom / (float)view.getHeight();
		
		scrollBy(0, 0);
	}

	@Override
	public void mouseMove(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		//shiftFactor = 0;
		if (dragging) {
			scrollBy(initialMouseX - mouseX, initialMouseY - mouseY);
		} else if (touching) {
			scrollMomentumX = mouseX - initialMouseX;
			scrollMomentumY = mouseY - initialMouseY;
			
			scrollBy((mouseX - initialMouseX) / 4, (mouseY - initialMouseY) / 4);
		}
		initialMouseX = mouseX;
		initialMouseY = mouseY;
	}
	
	public void scrollBy(float x, float y) {
		scrollX = MathHelper.clamp((int)Math.floor(scrollX - x * scrollFactorX), 0, maxScrollX);
		scrollY = MathHelper.clamp((int)Math.floor(scrollY - y * scrollFactorY), 0, maxScrollY);
	}

	@Override
	public void mouseUp(int mouseX, int mouseY) {
		dragging = touching = false;
		shiftFactor = 0;
	}
}

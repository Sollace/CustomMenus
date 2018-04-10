package com.sollace.custommenus.gui.container;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.element.IRenderable;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.geometry.BoundingBox;
import com.sollace.custommenus.gui.geometry.PaddingBox;
import com.sollace.custommenus.gui.geometry.UiBounded;
import com.sollace.custommenus.gui.input.IKeyResponder;
import com.sollace.custommenus.gui.input.IMouseResponder;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.natives.INativeFerry;
import com.sollace.custommenus.gui.packing.UiPacker;
import com.sollace.custommenus.resources.FormattedString;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;

public class Container implements IViewPort, UiContainer, UiElement, UiBounded, IMouseResponder, IKeyResponder {
	
	protected final List<UiElement> children = Lists.newArrayList();
	
	protected boolean visible = true;
	
	protected int x = 0, y = 0;
	
	protected PaddingBox padding = new PaddingBox();
	
	private int offsetWidth = 0, offsetHeight = 0;
	protected int width = 0, height = 0;
	
	protected IMouseResponder selectedElement = null;
	
	protected UiContainer container = null;
	
	protected UiPacker packer = null;
	
	public Container(UiContainer container) {
		this.container = container;
	}
	
	@Override
	public Container setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	@Override
	public Container setPos(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public Container setSize(int w, int h) {
		offsetWidth = width = w;
		offsetHeight = height = h;
		return this;
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(x, y, width, height);
	}
	
	@Override
	public UiElement setLabel(String label) {
		return this;
	}
	
	@Override
	public Container setToolTip(FormattedString tooltip) {
		return this;
	}
	
	@Override
	public Container setToolTip(IRenderable tooltip) {
		return this;
	}
	
	@Override
	public UiElement setAction(IGuiAction action) {
		return this;
	}
	
	@Override
	public UiElement init(JsonObject json) {
		padding.init(json);
		if (packer != null) packer.init(json);
		return this;
	}

	@Override
	public Container setEnabled(boolean enable) {
		return this;
	}
	
	@Override
	public void setTexture(String resource) {
		
	}

	@Override
	public void setSound(String sound) {
		
	}

	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return visible
				&& mouseX >= x + padding.getLeft()
				&& mouseX <= x + width - padding.getRight()
				&& mouseY >= y + padding.getTop()
				&& mouseY <= y + height - padding.getBottom()
		;
	}
	

	@Override
	public void playSound(SoundHandler sounds) {
		
	}
	
	@Override
	public IRenderable getToolTipAt(int mouseX, int mouseY) {
		mouseX -= getTranslateX();
		mouseY -= getTranslateY();
		
		for (UiElement i : children) {
			if (i.isFocused(mouseX, mouseY)) {
				IRenderable tooltip = i.getToolTipAt(mouseX, mouseY);
				if (tooltip != null) return tooltip;
			}
		}
		return null;
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
        mouseX -= getTranslateX();
        mouseY -= getTranslateY();
        
        for (UiElement i : children) {
            if (i.isFocused(mouseX, mouseY)) {
                if (i instanceof IMouseResponder) {
                	selectedElement = (IMouseResponder)i;
                }
                i.playSound(sender.getMinecraft().getSoundHandler());
                if (!i.performAction(mouseX, mouseY, sender)) return true;
            }
        }
        
        return true;
	}

	@Override
	public void reposition(UiView container) {
		IViewPort view = container.getViewPort();
		
		if (offsetWidth == 0) width = view.getWidth();
		if (offsetHeight == 0) height = view.getHeight();
		
		padding.reposition(container);
		
		if (packer != null) packer.pack(this);
		
		for (UiElement i : children) {
			i.reposition(this);
		}
	}
	
	protected int getTranslateX() {
		return x + padding.getLeft();
	}
	
	protected int getTranslateY() {
		return y + padding.getTop();
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		if (!visible) return;
		GlStateManager.pushMatrix();
        GlStateManager.translate(getTranslateX(), getTranslateY(), 0);
        
        mouseX -= getTranslateX();
        mouseY -= getTranslateY();
        
		renderTranslated(sender, mouseX, mouseY, partialTicks);
		
		GlStateManager.popMatrix();
	}
	
	protected void renderTranslated(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		for (UiElement i : children) {
			i.render(sender, mouseX, mouseY, partialTicks);
		}
	}
	
	@Override
	public void mouseMove(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		if (selectedElement != null) {
			mouseX -= getTranslateX();
	        mouseY -= getTranslateY();
	        
			selectedElement.mouseMove(sender, mouseX, mouseY, partialTicks);
		}
	}

	@Override
	public void mouseUp(int mouseX, int mouseY) {
		if (selectedElement != null) {
			mouseX -= getTranslateX();
	        mouseY -= getTranslateY();
	        
			selectedElement.mouseUp(mouseX, mouseY);
			selectedElement = null;
		}
	}
	
	@Override
	public boolean keyPressed(UiRoot sender, char key, int keyCode) {
		int mouseX = getMouseX();
		int mouseY = getMouseY();
		
		for (int i = 0; i < children.size(); ++i) {
			UiElement element = children.get(i);
			if (element.isFocused(mouseX, mouseY) && element instanceof IKeyResponder) {
				if (((IKeyResponder)element).keyPressed(sender, key, keyCode)) return true;
			}
		}
		return false;
	}

	@Override
	public void addChild(UiElement child) {
		children.add(child);
		if (child instanceof UiField) {
			container.addField((UiField<?>)child);
		}
	}
	
	@Override
	public UiElement getChild(int index) {
		return children.get(index);
	}
	
	@Override
	public Iterator<UiElement> getElementsIterator() {
		return children.iterator();
	}
	
	@Override
	public void addField(UiField<?> field) {
		container.addField(field);
	}
	
	@Override
	public <T> UiField<T> getField(String name) {
		return container.getField(name);
	}
	
	@Override
	public IViewPort getViewPort() {
		return this;
	}
	
	@Override
	public INativeFerry getFerry() {
		return container.getFerry();
	}

	@Override
	public void setDrawBackground(boolean draw) {
		
	}

	@Override
	public void setBackground(String resource) {
		
	}
	
	@Override
	public void setPacker(UiPacker packer) {
		this.packer = packer;
		packer.pack(this);
	}
	
	@Override
	public int getWidth() {
		return width - padding.getLeft() - padding.getRight();
	}
	
	@Override
	public int getHeight() {
		return height - padding.getTop() - padding.getBottom();
	}
	
	@Override
	public int getMouseX() {
		return container.getViewPort().getMouseY() - getTranslateY();
	}
	
	@Override
	public int getMouseY() {
		return container.getViewPort().getMouseX() - getTranslateX();
	}
	
	@Override
	public void setGlViewport() {
		container.getViewPort().setGlViewport();
	}

	@Override
	public UiContainer getContainer() {
		return this;
	}
}

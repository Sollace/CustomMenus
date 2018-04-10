package com.sollace.custommenus;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.sollace.custommenus.async.ActionDispatcher;
import com.sollace.custommenus.async.Later;
import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.IViewPort;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.IRenderable;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.IKeyResponder;
import com.sollace.custommenus.gui.input.IMouseResponder;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.natives.INativeFerry;
import com.sollace.custommenus.gui.overlays.UiOverlay;
import com.sollace.custommenus.gui.packing.GridPacker;
import com.sollace.custommenus.gui.packing.UiPacker;
import com.sollace.custommenus.registry.UiActions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiCustomMenu extends GuiScreen implements UiRoot, IViewPort {
	
	protected final UiRoot parent;
	
	private final String id;
	
	private final List<UiElement> children = Lists.newArrayList();
	private final List<UiOverlay> overlays = Lists.newArrayList();
	private final Map<String, UiField<?>> fields = Maps.newHashMap();
	
	private final ActionDispatcher actions = new ActionDispatcher();
	
	private IGuiAction close = null;
	
	private ResourceLocation background = OPTIONS_BACKGROUND;
	
	private boolean drawBackground = true, drawBackgroundInGame = false, pausesGame = true, root = false;
	
	private IMouseResponder selectedElement = null;
	
	private UiPacker packer = null;
	
	private INativeFerry ferry;
	
	public GuiCustomMenu(String id, INativeFerry ferry, UiRoot parent, JsonObject json) {
		this.parent = parent;
		this.mc = parent.getMinecraft();
		this.id = id;
		this.ferry = ferry;
		
		if (json.has("background")) setBackground(json.get("background").getAsString());
		if (json.has("drawBackground")) setDrawBackground(json.get("drawBackground").getAsBoolean());
		if (json.has("drawBackgroundInGame")) setAlwaysDrawBackground(json.get("drawBackgroundInGame").getAsBoolean());
		if (json.has("pausesGame")) setPausesGame(json.get("pausesGame").getAsBoolean());
		if (json.has("pack")) setPacker(new GridPacker(json.get("pack").getAsJsonObject()));
		if (json.has("startup")) performIn(UiActions.createAll(json.get("startup"), null), 0);
		
		root = json.has("root") && json.get("root").getAsBoolean();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
        return pausesGame;
    }
	
	@Override
	public void updateScreen() {
		actions.tick(this);
		for (UiOverlay i : overlays) i.update(this);
	}
	
	@Override
	public void initGui() {
		if (packer != null) packer.pack(this);
		for (UiElement i : children) i.reposition(this);
		for (UiOverlay i : overlays) i.reposition(this);
    }
	
	public void drawWorldBackground(int tint) {
        if (mc.world != null && !drawBackgroundInGame) {
            drawGradientRect(0, 0, width, height, -1072689136, -804253680);
        } else {
            drawBackground(tint);
        }
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (drawBackground) drawDefaultBackground();
        for (UiElement i : children) {
            i.render(this, mouseX, mouseY, partialTicks);
        }
        for (UiOverlay i : overlays) {
            i.render(this, mouseX, mouseY, partialTicks);
        }
        for (UiElement i : children) {
        	if (i.isFocused(mouseX, mouseY)) {
        		IRenderable tooltip = i.getToolTipAt(mouseX, mouseY);
        		if (tooltip != null) {
        			tooltip.render(this, mouseX, mouseY, partialTicks);
        			return;
        		}
        	}
        }
        if (selectedElement != null) {
        	selectedElement.mouseMove(this, mouseX, mouseY, partialTicks);
        }
    }
	
	@Override
	public void drawBackground(int tint) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        mc.getTextureManager().bindTexture(background);
        GlStateManager.color(1, 1, 1, 1);
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0, height, 0).tex(0, ((float)height / 32f + tint)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(width, height, 0).tex((double)((float)this.width / 32.0F), ((float)height / 32f + tint)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(width, 0, 0).tex(((float)width / 32f), tint).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(0, 0, 0).tex(0, tint).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
        	for (int i = overlays.size() - 1; i >= 0; i--) {
            	UiOverlay element = overlays.get(i);

                if (element.isFocused(mouseX, mouseY)) {
                    element.performAction(mouseX, mouseY, this);
                }
            }
            for (int i = children.size() - 1; i >= 0; i--) {
                UiElement element = children.get(i);
                
                if (element.isFocused(mouseX, mouseY)) {
                    if (element instanceof IMouseResponder) {
                    	selectedElement = (IMouseResponder)element;
                    }
                    element.playSound(getMinecraft().getSoundHandler());
                    if (!element.performAction(mouseX, mouseY, this)) return;
                }
            }
        }
    }
	
	public void handleKeyboardInput() throws IOException {
        char key = Keyboard.getEventCharacter();

        if (Keyboard.getEventKey() == 0 && key >= ' ' || Keyboard.getEventKeyState()) {
        	if (keyPressed(key, Keyboard.getEventKey())) return;
        } 
        
        mc.dispatchKeypresses();
    }
	
	protected boolean keyPressed(char key, int keyCode) throws IOException {
		for (int i = children.size() - 1; i >= 0; i--) {
			UiElement element = children.get(i);
			if (element instanceof IKeyResponder) {
				if (((IKeyResponder)element).keyPressed(this, key, keyCode)) return true;
			}
		}
		if (keyCode != 1 || !root) super.keyTyped(key, keyCode);
		return false;
    }
	
	@Override
	protected void keyTyped(char key, int keyCode) throws IOException {
		keyPressed(key, keyCode);
    }
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (selectedElement != null && state == 0) {
			selectedElement.mouseUp(mouseX, mouseY);
			selectedElement = null;
        }
	}
	
	@Override
	public void confirmClicked(boolean result, int id) {
		super.confirmClicked(result, id);
	}
	
	@Override
	public void onGuiClosed() {
		Later.perform();
		if (close != null) close.perform(this);
		for (UiOverlay i : overlays) i.destroy();
	}
	
	@Override
	public void performIn(IGuiAction action, int ticks) {
		actions.enqueu(action, ticks);
	}
	
	@Override
	public void addCloseListener(IGuiAction action) {
		close = IGuiAction.combine(close, action);
	}
	
	@Override
	public void addChild(UiElement child) {
		if (child instanceof UiOverlay) {
			overlays.add((UiOverlay)child);
		} else {
			children.add(child);
		}
		if (child instanceof UiField) {
			addField((UiField<?>)child);
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
		String name = field.getName();
		if (name != null) fields.put(name, field);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> UiField<T> getField(String name) {
		return (UiField<T>)fields.get(name);
	}
	
	@Override
	public void setBackground(String resource) {
		background = new ResourceLocation(resource);
	}
	
	@Override
	public void setDrawBackground(boolean draw) {
		drawBackground = draw;
	}
	
	@Override
	public void setAlwaysDrawBackground(boolean draw) {
		drawBackgroundInGame = draw;
	}
	
	@Override
	public void setPausesGame(boolean pauses) {
		pausesGame = pauses;
	}
	
	@Override
	public void setPacker(UiPacker packer) {
		this.packer = packer;
		packer.pack(this);
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
	public INativeFerry getFerry() {
		return ferry;
	}
	
	@Override
	public Minecraft getMinecraft() {
		return mc;
	}
	
	@Override
	public GuiScreen getNative() {
		return this;
	}
	
	@Override
	public void open(String gui) {
		GuiMenus.INSTANCE.openMenu(mc, gui, ferry);
	}
	
	@Override
	public void show() {
		mc.displayGuiScreen(this);
	}
	
	@Override
	public void close() {
		parent.show();
	}
	
	@Override
	public void refresh() {
		if (GuiMenus.INSTANCE.hasGuiConfigurationFor(id)) {
			GuiMenus.INSTANCE.createCustomMenu(parent, ferry, id).show();
			return;
		}
		GuiNull.NULL.show();
	}
	
	@Override
	public String getViewPortId() {
		return id;
	}
	
	@Override
	public boolean isNonNative() {
		return true;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public int getMouseX() {
		return Mouse.getEventX() * width / mc.displayWidth;
	}
	
	@Override
	public int getMouseY() {
		return height - Mouse.getEventY() * height / mc.displayHeight - 1;
	}

	@Override
	public void setGlViewport() {
		GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
	}
}

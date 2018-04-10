package com.sollace.custommenus.gui.element;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.locale.Locales;
import com.sollace.custommenus.resources.FormattedString;
import com.sollace.custommenus.utils.JsonUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class Label extends AbstractElement {
	
	private final FontRenderer fonts;
	
	private String text = "";
	
	private FormattedString format = null;
	
	private List<String> lines = Lists.newArrayList();
	
	private boolean centered = false, wrap = false, drawBackground = false, shadow = false;
	
	private int color = -1, background = -1, size = 1;
	
	private final UiContainer container;
	
	public Label(UiContainer container) {
		this.container = container;
		fonts = Minecraft.getMinecraft().fontRenderer;
	}
	
	public Label init(JsonObject json) {
		wrap = JsonUtils.get(json, "wrap", wrap);
		centered = JsonUtils.get(json, "centered", centered);
		shadow = JsonUtils.get(json, "shadow", shadow);
		color = JsonUtils.get(json, "color", color);
		size = JsonUtils.get(json, "size", size);
		background = JsonUtils.get(json, "background", background);
		drawBackground = json.has("background");
		
		if (json.has("format")) {
			format = FormattedString.create(json.get("format"));
			reflowText();
		}
		
		return this;
	}
	
	public Label setCentered(boolean center) {
		centered = center;
		return this;
	}
	
	@Override
	public Label setLabel(String label) {
		text = Locales.translateUnformatted(label);
		return this;
	}
	
	protected void reflowText() {
		if (wrap) {
			if (width == 0) width = fonts.getStringWidth(text);
			lines = fonts.listFormattedStringToWidth(text, width);
		} else {
			lines.clear();
			String[] ss = text.split("\r\n|\n\r|\r|\r|(\\r)|(\\n)");
			for (int i = 0; i < ss.length; i++) lines.add(ss[i]);
			
			width = 0;
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				if (format != null) line = Locales.translate(line, format.toString(container));
				int l = fonts.getStringWidth(line);
				if (l > width) width = l;
				lines.set(i, line);
			}
		}
		
		height = fonts.FONT_HEIGHT * lines.size();
	}
	
	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return action != null && super.isFocused(mouseX, mouseY);
	}
	
	@Override
	public void reposition(UiView container) {
		super.reposition(container);
		
		if (wrap) {
        	width = Math.max(0, container.getViewPort().getWidth() - 30);
        	height = lines.size() * fonts.FONT_HEIGHT;
        	x = 15;
        	if (width > 0) reflowText();
        } else {
        	reflowText();
        	if (centered) x -= width / 2;
        }
	}
	
	@Override
	public void setTexture(String resource) {}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		if (format != null) reflowText();
		
		if (!visible) return;
		
		if (size == 1) {
			renderTransformed(sender, mouseX, mouseY, partialTicks);
			return;
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(-(x + width / 2), -(y + height / 2), 0);
		GlStateManager.scale(size, size, size);
		
		renderTransformed(sender, mouseX, mouseY, partialTicks);
		GlStateManager.popMatrix();
	}
	
	public void renderTransformed(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        
        int baseLeft = x;
        int baseRight = baseLeft + width;
        
        if (drawBackground) {
        	drawRect(size, baseLeft - 2, y - 2, baseRight + 2, y + height - 1, background);
        }
        
        if (isFocused(mouseX, mouseY) && action != null) {
        	drawHorizontalLine(baseLeft - 1, baseRight, y + height - 1, color);
        }
        
        int j = (y + height / 2) - lines.size() * fonts.FONT_HEIGHT / 2;
        
        for (int i = 0; i < lines.size(); i++) {
        	int k = baseLeft;
        	if (centered) k -= (fonts.getStringWidth(lines.get(i)) - width) / 2;
            drawString(lines.get(i), k, j + i * fonts.FONT_HEIGHT, color);
        }
    }
	
	public static void drawRect(int size, int left, int top, int right, int bottom, int color) {
		Gui.drawRect(left, top, right, bottom, color);
	}
	
	public void drawString(String string, int x, int y, int color) {
		if (shadow) {
			fonts.drawStringWithShadow(string, x, y, color);
		} else {
			fonts.drawString(string, x, y, color);
		}
	}
	
	@Override
	public void drawHorizontalLine(int start, int end, int y, int color) {
		super.drawHorizontalLine(start / size, end / size, y / size, color);
	}
}

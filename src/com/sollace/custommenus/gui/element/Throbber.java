package com.sollace.custommenus.gui.element;

import com.google.gson.JsonObject;
import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.locale.Locales;
import com.sollace.custommenus.utils.JsonUtils;

import net.minecraft.client.gui.FontRenderer;

public class Throbber extends AbstractElement {
	
	protected String text;
	
	protected int messageColor = 16777215,
				  bobberColor = 8421504;
	
	@Override
	public Throbber init(JsonObject json) {
		if (json.has("color")) messageColor = bobberColor = json.get("color").getAsInt();
		bobberColor = JsonUtils.get(json, "bobberColor", bobberColor);
		return this;
	}
	
	@Override
	public Throbber setLabel(String label) {
		text = Locales.translateUnformatted(label);
		return this;
	}
	
	@Override
	public void setTexture(String resource) {
		
	}
	
	protected String getThrobberSymbols(int time) {
		switch (time) {
	        case 0:
	        default:
	            return "O o o";
	        case 1:
	        case 3:
	            return "o O o";
	        case 2:
	            return "o o O";
	    }
	}
	
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		FontRenderer fonts = sender.getMinecraft().fontRenderer;
		
		int labelY = y + height / 2 - fonts.FONT_HEIGHT / 2;
		int labelX = x + width / 2 - fonts.getStringWidth(text) / 2;
		
		fonts.drawString(text, labelX, labelY, messageColor);
		
        String s = getThrobberSymbols((int)(GuiNull.systemTime() / 300 % 4));
        
        fonts.drawString(s, x + (width - fonts.getStringWidth(s)) / 2, labelY + fonts.FONT_HEIGHT, bobberColor);
	}
}

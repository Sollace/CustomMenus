package com.sollace.custommenus.resources;

import com.google.gson.JsonObject;
import com.sollace.custommenus.utils.IJsonReadable;
import com.sollace.custommenus.utils.JsonUtils;

import net.minecraft.client.gui.Gui;

public class TextureLocation implements IJsonReadable {
	public int texX = 0, texY = 0;
	
	public int texW = 256, texH = 256;
	
	public TextureLocation init(JsonObject json) {
		texX = JsonUtils.get(json, "texX", texX);
		texY = JsonUtils.get(json, "texY", texY);
		texW = JsonUtils.get(json, "texW", texW);
		texH = JsonUtils.get(json, "texH", texH);
		return this;
	}
	
	public void drawTextureAt(int x, int y, int width, int height) {
		Gui.drawModalRectWithCustomSizedTexture(x, y, texX, texY, width, height, texW, texH);
	}
}

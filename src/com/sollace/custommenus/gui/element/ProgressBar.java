package com.sollace.custommenus.gui.element;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.registry.UiGame;
import com.sollace.custommenus.resources.TextureLocation;
import com.sollace.custommenus.utils.JsonUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ProgressBar extends AbstractElement implements UiField<Integer> {
	
	protected int progress = 0;
	
	protected int emptyColor = -8355712, filledColor = -8323200;
	
	private String name = null, from = null;
	
	protected ResourceLocation texture = null;
	
	protected TextureLocation filled = new TextureLocation(),
							  empty = new TextureLocation(),
							  overlay = null;
	
	private final UiContainer container;
	
	public ProgressBar(UiContainer container) {
		this.container = container;
	}
	
	@Override
	public ProgressBar init(JsonObject json) {
		if (json.has("value")) setValue(json.get("value").getAsInt());
		from = JsonUtils.get(json, "from", from);
		emptyColor = JsonUtils.get(json, "backColor", emptyColor);
		filledColor = JsonUtils.get(json, "fillColor", filledColor);
		JsonUtils.get(json, "empty", empty);
		JsonUtils.get(json, "filled", filled);
		if (json.has("overlay")) JsonUtils.get(json, "filled", overlay = new TextureLocation());
		return this;
	}

	@Override
	public ProgressBar setLabel(String label) {
		return this;
	}

	@Override
	public void setTexture(String resource) {
		texture = new ResourceLocation(resource);
	}

	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		if (from != null) {
			UiField<Integer> field = sender.getField(from);
			if (field != null) {
				setValue(field.getValue());
			} else if (UiGame.has(from, sender.getFerry())) {
				setValue(Integer.valueOf(UiGame.lookupValue(from, sender.getFerry())));
			}
		}
		
		if (!visible || progress < 0) return;
		
		if (texture == null) {
	        drawRect(x, y, x + width, y + height, emptyColor);
	        drawRect(x, y, x + progress, y + height, filledColor);
		} else {
			sender.getMinecraft().getTextureManager().bindTexture(texture);
			
			int fill = (int)(width * ((float)progress / 100));
			
			GlStateManager.enableBlend();
			
			empty.drawTextureAt(x, y, width, height);
			if (overlay != null) overlay.drawTextureAt(x, y, width, height);
			
			if (fill > 0) {
				drawTexturedModalRect(x, y, filled.texX, filled.texY, fill, height);
				if (overlay != null) drawModalRectWithCustomSizedTexture(x, y, overlay.texX, overlay.texY + height, fill, height, overlay.texW, overlay.texH);
			}
			
			GlStateManager.disableBlend();
		}
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Integer getValue() {
		return progress;
	}

	@Override
	public String getDisplayString() {
		return getValue() + "%";
	}

	@Override
	public void setValue(Integer value) {
		progress = value < -1 ? -1 : value > 100 ? 100 : value;
	}

	@Override
	public UiContainer getContainer() {
		return container;
	}
}

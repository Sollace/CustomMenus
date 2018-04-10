package com.sollace.custommenus.gui.element.builtin;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.AbstractElement;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.utils.JsonUtils;
import com.sollace.custommenus.utils.NBTUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class Item extends AbstractElement {
	
	protected ItemStack stack;
	
	public Item(UiContainer container) {
		
	}
	
	@Override
	public UiElement init(JsonObject json) {
		stack = new ItemStack(
				net.minecraft.item.Item.getByNameOrId(JsonUtils.get(json, "item", "minecraft:air")),
				JsonUtils.get(json, "size", 1),
				JsonUtils.get(json, "damage", 0)
			);
		if (json.has("tag")) {
			stack.setTagCompound(NBTUtils.getObject(json.get("tag").getAsJsonObject()));
		}
		return this;
	}
	
	@Override
	public UiElement setLabel(String label) {
		return this;
	}

	@Override
	public void setTexture(String resource) {
		
	}

	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.enableRescaleNormal();
		RenderHelper.enableGUIStandardItemLighting();
		sender.getMinecraft().getRenderItem().renderItemIntoGUI(stack, x, y);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
	}
	
	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return false;
	}
}

package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.UiField;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class ActionOpenLan implements IGuiAction {
	
	private final String gamemode, allowcheats;
	
	public ActionOpenLan(JsonObject json, UiElement owner) {
		gamemode = json.get("difficultyField").getAsString();
		allowcheats = json.get("allowCheatsField").getAsString();
	}
	
	@Override
	public void perform(UiRoot screen) {
		UiField<Integer> gameType = screen.<Integer>getField(gamemode);
		UiField<Boolean> cheats = screen.<Boolean>getField(allowcheats);
		
		if (gameType != null && cheats != null) {
			screen.getMinecraft().ingameGUI.getChatGUI().printChatMessage(openLan(screen, gameType, cheats));
		}
		
		GuiNull.NULL.show();
	}
	
	private ITextComponent openLan(UiRoot screen, UiField<Integer> type, UiField<Boolean> cheats) {
		GameType t = GameType.getByID(type.getValue());
		
		String result = screen.getMinecraft().getIntegratedServer().shareToLAN(t, cheats.getValue());
		
		if (result == null) {
			return new TextComponentString("commands.publish.failed");
        }
		
		return new TextComponentTranslation("commands.publish.started", result);
	}
}

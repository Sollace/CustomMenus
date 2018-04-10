package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.natives.INativeFerry;
import com.sollace.custommenus.locale.Locales;
import com.sollace.custommenus.registry.UiActions;
import com.sollace.custommenus.resources.FormattedString;

import net.minecraft.client.gui.GuiYesNoCallback;

public class ActionYesNo implements IGuiAction, IGuiDecider, GuiYesNoCallback {
	
	private IGuiAction yes = null, no = null;
	
	protected FormattedString message1, message2;
	
	private String msgY = "gui.yes", msgN = "gui.no";
	
	private UiRoot parent;
	
	private int delay = 0;
	
	public ActionYesNo(JsonObject json, UiElement owner) {
		if (json.has("yes")) yes = UiActions.create(json.getAsJsonObject("yes"), owner);
		if (json.has("no")) no = UiActions.create(json.getAsJsonObject("no"), owner);
		
		message1 = json.has("title") ? FormattedString.create(json.get("title")) : FormattedString.create("Title");
		message2 = json.has("question") ? FormattedString.create(json.get("question")) : FormattedString.create("Question");
		
		if (json.has("confirmText")) msgY = json.get("confirmText").getAsString();
		if (json.has("cancelText")) msgN = json.get("cancelText").getAsString();
		if (json.has("buttonDelay")) delay = json.get("buttonDelay").getAsInt();
	}
	
	@Override
	public void perform(UiRoot screen) {
		parent = screen;
		
		INativeFerry message = INativeFerry.empty(screen).decider(this)
						.param("title", message1.toString(screen))
						.param("question", message2.toString(screen))
						.param("yes", Locales.translateUnformatted(msgY))
						.param("no", Locales.translateUnformatted(msgN));
		if (delay > 0) message.param("delay", String.valueOf(delay));
		
		message.open("yesno");
	}
	
	@Override
	public void confirmClicked(boolean result, int id) {
		if (result) {
			yes(parent);
		} else {
			no(parent);
		}
	}

	@Override
	public void yes(UiRoot screen) {
		if (yes != null) {
			yes.perform(screen);
		} else {
			parent.refresh();
		}
	}
	
	@Override
	public void no(UiRoot screen) {
		if (no != null) {
			no.perform(screen);
		} else {
			parent.refresh();
		}
	}
}

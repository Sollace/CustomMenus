package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;

import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

public class ActionCrash implements IGuiAction {

	public ActionCrash(JsonObject json, UiElement owner) {
		owner.setToolTip("This will cause the game to crash.\nIf that's what you want, then great.\nOtherwise don't touch.");
	}
	
	@Override
	public void perform(UiRoot screen) {
		throw new ReportedException(new CrashReport(getRandomCrashMessage(), new Throwable()));
	}
	
	public String getRandomCrashMessage() {
		return "Manually triggered debug crash";
	}
}

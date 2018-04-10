package com.sollace.custommenus.gui.element.builtin;

import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.IRenderable;
import com.sollace.custommenus.resources.FormattedString;

public class ToolTip implements IRenderable {
	
	private List<String> lines = null;
	
	private final FormattedString string;
	
	public ToolTip(FormattedString text) {
		string = text;
	}
	
	protected List<String> getLines(UiRoot sender) {
		if (lines != null) return lines;
		return lines = Lists.newArrayList(Splitter.on("\n").split(string.toString(sender)));
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		sender.getNative().drawHoveringText(getLines(sender), mouseX, mouseY);
	}
}

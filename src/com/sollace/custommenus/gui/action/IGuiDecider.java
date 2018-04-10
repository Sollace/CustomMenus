package com.sollace.custommenus.gui.action;

import java.util.List;

import com.google.common.collect.Lists;
import com.sollace.custommenus.gui.container.UiRoot;

public interface IGuiDecider {
	public void yes(UiRoot screen);
	
	public void no(UiRoot screen);
	
	/**
	 * Creates a new decider that combines both this one and another one to execute after it.
	 */
	public default IGuiDecider and(IGuiDecider other) {
		if (other == null) return this;
		if (other instanceof CombinedDecider) {
			return ((CombinedDecider) other).and(this);
		}
		return new CombinedDecider().and(this).and(other);
	}
	
	static class CombinedDecider implements IGuiDecider {
		List<IGuiDecider> yes = Lists.newArrayList(),
						   no = Lists.newArrayList();
		
		public CombinedDecider and(IGuiDecider other) {
			if (other instanceof CombinedDecider) {
				yes.addAll(((CombinedDecider) other).yes);
				no.addAll(((CombinedDecider) other).no);
			} else {
				yes.add(other);
				no.add(other);
			}
			return this;
		}
		
		@Override
		public void yes(UiRoot screen) {
			for (IGuiDecider i : yes) i.yes(screen);
		}

		@Override
		public void no(UiRoot screen) {
			for (IGuiDecider i : no) i.no(screen);
		}
	}
}

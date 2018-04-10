package com.sollace.custommenus.gui.action;

import java.util.List;

import com.google.common.collect.Lists;
import com.sollace.custommenus.async.Later;
import com.sollace.custommenus.gui.container.UiRoot;

/**
 * Response actions for UI events.
 */
@FunctionalInterface
public interface IGuiAction {
	
	public static final IGuiAction NULL = screen -> {};
	
	/**
	 * Performs this action now.
	 * 
	 * @param screen	The current screen originating the event.
	 */
	void perform(UiRoot screen);
	
	/**
	 * Enqueues this action to be performed some time in the future.
	 * Use this to save things like game options that would otherwise lock the UI.
	 */
	default void performLater() {
		Later.schedule(this);
	}
	
	default IGuiAction and(IGuiAction other) {
		if (other == null) return this;
		if (other instanceof CombinedAction) return other.and(this);
		return new CombinedAction().and(this).and(other);
	}
	
	public static IGuiAction combine(IGuiAction one, IGuiAction two) {
		return one == null ? two : one.and(two);
	}
	
	/**
	 * Creates a IGuiAction to perform all of the given actions in sequence.
	 */
	public static IGuiAction all(IGuiAction... actions) {
		return screen -> {
			for (int i = 0; i < actions.length; i++) {
				if (actions[i] != null) actions[i].perform(screen);
			}
		};
	}
	
	static class CombinedAction implements IGuiAction {
		private final List<IGuiAction> actions = Lists.newArrayList();
		
		@Override
		public void perform(UiRoot screen) {
			for (IGuiAction i : actions) i.perform(screen);
		}
		
		@Override
		public IGuiAction and(IGuiAction other) {
			if (other != null) {
				if (other instanceof CombinedAction) {
					actions.addAll(((CombinedAction)other).actions);
				} else {
					actions.add(other);
				}
			}
			return this;
		}
	}
}

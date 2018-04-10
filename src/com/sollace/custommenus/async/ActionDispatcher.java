package com.sollace.custommenus.async;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.UiRoot;

/**
 * A dispatching queue that collects and calls IGuiAction
 * instances after a specified amount of ticks.
 */
public class ActionDispatcher extends ConcurrentLinkedDeque<ActionDispatcher.TimedAction> {
	private static final long serialVersionUID = -586595490485334183L;

	public void tick(UiRoot screen) {
		Iterator<ActionDispatcher.TimedAction> iter = this.iterator();
		while (iter.hasNext()) {
			iter.next().tick(screen, iter);
		}
	}
	
	/**
	 * Enqueues an action to be performed in a later number of ticks.
	 */
	public void enqueu(IGuiAction action, int ticks) {
		add(new TimedAction(action, ticks + 1));
	}
	
	protected class TimedAction {
		private int ticks;
		
		private IGuiAction action;
		
		public TimedAction(IGuiAction action, int ticks) {
			this.action = action;
			this.ticks = ticks; 
		}
		
		public void tick(UiRoot screen, Iterator<ActionDispatcher.TimedAction> iter) {
			if (--ticks <= 0) {
				iter.remove();
				action.perform(screen);
			}
		}
	}
}

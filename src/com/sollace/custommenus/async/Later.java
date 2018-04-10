package com.sollace.custommenus.async;

import java.util.concurrent.ConcurrentLinkedDeque;

import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.action.IGuiAction;

import net.minecraft.client.Minecraft;

/**
 * Later for performing UI actions out of sync
 */
public final class Later extends Thread {
	
	private static final ConcurrentLinkedDeque<IGuiAction> queue = new ConcurrentLinkedDeque<IGuiAction>();
	
	private static boolean scheduled = false;
	
	private Later() {
		setDaemon(true);
		setName("Future Action");
		start();
	}
	
	/**
	 * Performs all future actions now. Used to signal when a good point is to perform actions.
	 */
	public static void perform() {
		IGuiAction item;
		while ((item = queue.pollFirst()) != null) {
			try {
				item.perform(GuiNull.currentScreen());
			} catch (Exception e) {}
		}
	}
	
	/**
	 * Schedules an action to be performed some time in the future.
	 * That can either be in the next update loop, or when Later.perform() is executed.
	 */
	public static void schedule(IGuiAction action) {
		queue.addFirst(action);
		schedule();
	}
	
	private static void schedule() {
		if (!scheduled) {
			scheduled = true;
			new Later();
		}
	}
	
	@Override
	public void run() {
		try {
			sleep(3000);
		} catch (InterruptedException e) {}
		Minecraft.getMinecraft().addScheduledTask(() -> {
			scheduled = false;
			perform();
		});
	}
}

package com.sollace.custommenus.gui.element.builtin;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.Label;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class Splash extends Label {
	private static final Random RANDOM = new Random();
	
	private String splashText;
	
	private float angle = -20;
	
	public Splash(UiContainer container) {
		super(container);
	}
	
	@Override
	public Splash init(JsonObject json) {
		splashText = loadSplash(json.get("texts").getAsString());
		if (json.has("angle")) angle = json.get("angle").getAsFloat();
		
		return this;
	}
	
	protected String getFestivities(Calendar calendar) {
		if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
            return "Merry X-mas!";
        } else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
            return "Happy new year!";
        } else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
            return "OOoooOOOoooo! Spooky!";
        }
        return null;
	}
	
	protected String loadSplash(String splashTest) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        
        String festive = getFestivities(calendar);
        if (festive != null) return festive;
        
		IResource iresource = null;

        try {
            List<String> list = Lists.<String>newArrayList();
            iresource = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(splashTest));
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8));
            String s;
        	
            while ((s = bufferedreader.readLine()) != null) {
                s = s.trim();
                if (!s.isEmpty()) list.add(s);
            }

            if (!list.isEmpty()) {
                while (true)
                {
                    String line = list.get(RANDOM.nextInt(list.size()));

                    if (line.hashCode() != 125780783) {
                        return line;
                    }
                }
            }
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly((Closeable)iresource);
        }
        
        return "missingno";
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.rotate(angle, 0, 0, 1);
        
        float scaleAmount = 1.8f - MathHelper.abs(MathHelper.sin((float)(GuiNull.systemTime() % 1000) / 1000f * ((float)Math.PI * 2)) * 0.1f);
        scaleAmount = scaleAmount * 100f / (float)(sender.getMinecraft().fontRenderer.getStringWidth(splashText) + 32);
        
        GlStateManager.scale(scaleAmount, scaleAmount, scaleAmount);
        
        drawCenteredString(sender.getMinecraft().fontRenderer, splashText, 0, -8, -256);
        
        GlStateManager.popMatrix();
	}
}

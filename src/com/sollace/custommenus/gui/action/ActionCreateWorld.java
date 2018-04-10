package com.sollace.custommenus.gui.action;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.input.UiField;

import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class ActionCreateWorld implements IGuiAction {
	
	public ActionCreateWorld(JsonObject json) {
		
	}
	
	protected <T> T getValue(UiRoot screen, String name, T def) {
		UiField<T> field = screen.getField(name);
		return field == null ? def : field.getValue();
	}
	
	protected String calcSaveDirName(UiRoot screen, String name) {
        String saveDirName = name.trim();
        
        for (char c0 : ChatAllowedCharacters.ILLEGAL_FILE_CHARACTERS) {
            saveDirName = saveDirName.replace(c0, '_');
        }
        
        if (StringUtils.isEmpty(saveDirName)) {
            saveDirName = "World";
        }
        
        return GuiCreateWorld.getUncollidingSaveDirName(screen.getMinecraft().getSaveLoader(), saveDirName);
    }
	
	@Override
	public void perform(UiRoot screen) {
		
		long i = (new Random()).nextLong();
		
        String s = getValue(screen, "worldSeed", "");
        
        if (!StringUtils.isEmpty(s)) {
            try {
                long j = Long.parseLong(s);
                if (j != 0L) i = j;
            }
            catch (NumberFormatException e) {
                i = (long)s.hashCode();
            }
        }
        
        boolean hardcore = getValue(screen, "hardcore", false);
        
        WorldSettings worldsettings = new WorldSettings(i, 
        					GameType.getByName(getValue(screen, "gamemode", "survival")),
        					getValue(screen, "structures", true),
        					hardcore,
        					WorldType.WORLD_TYPES[getValue(screen, "hardcore", 0)]);
        worldsettings.setGeneratorOptions(getValue(screen, "chunkprovideroptions", ""));

        if (getValue(screen, "bonuschest", false) && !hardcore) {
            worldsettings.enableBonusChest();
        }

        if (getValue(screen, "cheats", true) && !hardcore) {
            worldsettings.enableCommands();
        }
        
        String worldName = getValue(screen, "worldName", "World").trim();
        
        screen.getMinecraft().launchIntegratedServer(calcSaveDirName(screen, worldName), worldName, worldsettings);
	}

}

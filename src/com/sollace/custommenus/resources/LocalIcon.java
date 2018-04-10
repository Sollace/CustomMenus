package com.sollace.custommenus.resources;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.sollace.custommenus.privileged.IGame;

public class LocalIcon extends LoadedIcon {
	private File iconFile;
	
	public LocalIcon(IGame mc, String location, File file, String fallback) {
		super(mc, location, fallback);
		
		iconFile = file;
		
        if (!iconFile.isFile()) {
            iconFile = null;
        }
	}
	
	@Override
	public boolean canLoad() {
		return valid();
	}
	
	@Override
	public boolean valid() {
		return iconFile != null && iconFile.isFile();
	}
	
	protected BufferedImage read() {
        try {
            return ImageIO.read(iconFile);
        } catch (Throwable e) {
            iconFile = null;
        }
        
        return null;
    }
}

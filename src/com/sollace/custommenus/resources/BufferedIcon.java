package com.sollace.custommenus.resources;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import com.sollace.custommenus.privileged.IGame;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;

import net.minecraft.client.renderer.texture.TextureUtil;

public class BufferedIcon extends LoadedIcon {
	
	private Supplier<String> supplier;
	
	public BufferedIcon(IGame mc, String location, String fallback, Supplier<String> supplier) {
		super(mc, location, fallback);
		
		this.supplier = supplier;
	}
	
	@Override
	public boolean canLoad() {
		return icon == null && supplier.get() != null;
	}
	
	@Override
	public boolean valid() {
		return icon != null && supplier.get() != null;
	}
	
	protected BufferedImage read() {
        ByteBuf imgdata = Unpooled.copiedBuffer(supplier.get(), StandardCharsets.UTF_8);
        ByteBuf decoded = null;
        
        try {
            decoded = Base64.decode(imgdata);
            return TextureUtil.readBufferedImage(new ByteBufInputStream(decoded));
        } catch (Throwable throwable) {
        	
        } finally {
            imgdata.release();
            
            if (decoded != null) {
                decoded.release();
            }
        }
        
        return null;
    }
}

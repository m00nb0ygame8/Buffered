package me.moonboygamer.buffered.util;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import me.moonboygamer.buffered.shader.BufferedShaderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.util.Identifier;

public class PostShaderHelper {
	public static ShaderEffect createPostShader(boolean useDynamicShader, Framebuffer mainFramebuffer, Identifier identifier) {
		if(useDynamicShader) BufferedShaderManager.setUseDynamicShader(true);
		try {
			ShaderEffect effect = new ShaderEffect(
				MinecraftClient.getInstance().getTextureManager(),
				MinecraftClient.getInstance().getResourceManager(),
				mainFramebuffer,
				identifier
			);
			if(BufferedShaderManager.isUseDynamicShader()) BufferedShaderManager.setUseDynamicShader(false);
			return effect;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

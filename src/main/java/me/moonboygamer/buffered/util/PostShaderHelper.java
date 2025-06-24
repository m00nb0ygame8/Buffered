package me.moonboygamer.buffered.util;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import me.moonboygamer.buffered.post.DynamicPostShader;
import me.moonboygamer.buffered.shader.BufferedShaderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class PostShaderHelper {
	public static ShaderEffect createPostShader(boolean useDynamicShader, Framebuffer mainFramebuffer, Identifier identifier, @Nullable DynamicPostShader postShader) {
		if(useDynamicShader) BufferedShaderManager.setUseDynamicShader(true); BufferedShaderManager.setCurrentDynamicShader(postShader);
		try {
			ShaderEffect effect = new ShaderEffect(
				MinecraftClient.getInstance().getTextureManager(),
				MinecraftClient.getInstance().getResourceManager(),
				mainFramebuffer,
				identifier
			);
			effect.setupDimensions(MinecraftClient.getInstance().getWindow().getFramebufferWidth(), MinecraftClient.getInstance().getWindow().getFramebufferHeight());
			if(BufferedShaderManager.isUseDynamicShader()) BufferedShaderManager.setUseDynamicShader(false); BufferedShaderManager.setCurrentDynamicShader(null);
			return effect;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

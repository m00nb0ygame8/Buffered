package me.moonboygamer.buffered.buffered.util;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class ShaderLoader {
	public static ShaderProgram loadCoreShader(Identifier identifier, VertexFormat format) {
		try {
			return new ShaderProgram(
				MinecraftClient.getInstance().getResourceManager(),
				identifier.toString().contains(".json") ? identifier.toString() : identifier + ".json",
				format
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static PostProcessShader loadProgramShader(Identifier identifier, Framebuffer inBuffer, Framebuffer outBuffer) {
		try {
			return new PostProcessShader(
				MinecraftClient.getInstance().getResourceManager(),
				identifier.toString(),
				inBuffer,
				outBuffer
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

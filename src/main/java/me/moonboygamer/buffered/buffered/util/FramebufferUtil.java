package me.moonboygamer.buffered.buffered.util;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.framebuffer.SimpleFramebuffer;
import net.minecraft.client.MinecraftClient;

public class FramebufferUtil {
	public static Framebuffer createScreenFramebuffer() {
		MinecraftClient client = MinecraftClient.getInstance();
		int width = client.getWindow().getFramebufferWidth();
		int height = client.getWindow().getFramebufferHeight();
		return createFramebuffer(width, height);
	}
	public static Framebuffer createScreenFramebuffer(boolean useDepth) {
		MinecraftClient client = MinecraftClient.getInstance();
		int width = client.getWindow().getFramebufferWidth();
		int height = client.getWindow().getFramebufferHeight();
		return createFramebuffer(width, height, useDepth);
	}
	public static Framebuffer createFramebuffer(int width, int height) {
		return createFramebuffer(width, height, true);
	}
	public static Framebuffer createFramebuffer(int width, int height, boolean useDepth) {
		return new SimpleFramebuffer(
			width,
			height,
			useDepth,
			MinecraftClient.IS_SYSTEM_MAC
		);
	}
}

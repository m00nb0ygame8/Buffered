package me.moonboygamer.buffered.post.pipeline;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import me.moonboygamer.buffered.addons.GameRendererAddon;
import me.moonboygamer.buffered.mixin.GameRendererAccessor;
import me.moonboygamer.buffered.mixin.ShaderEffectAccessor;
import me.moonboygamer.buffered.shader.BufferedShaderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class BufferedRenderPipeline {
	private final ShaderEffect postShader;

	public BufferedRenderPipeline(ShaderEffect effect) {
		this.postShader = effect;
	}

	public void render() {
		BufferedShaderManager.setPostShader(this);
		BufferedShaderManager.enablePostShader();
	}
	public void close() {
		postShader.close();
	}

	public void addTarget(String name, int width, int height) {
		postShader.addTarget(name, width, height);
	}
	public void addTarget(String name) {
		Framebuffer main = ((ShaderEffectAccessor) postShader).getMainTarget();
		addTarget(name, main.viewportWidth, main.viewportHeight);
	}
	public void addPass(Identifier programName, Framebuffer in, Framebuffer out) throws IOException {
		postShader.addPass(programName.toString(), in, out);
	}

	public ShaderEffect getPostShader() {
		return postShader;
	}
}

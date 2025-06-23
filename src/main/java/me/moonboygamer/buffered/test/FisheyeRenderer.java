package me.moonboygamer.buffered.test;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.framebuffer.SimpleFramebuffer;
import me.moonboygamer.buffered.mesh.Mesh;
import me.moonboygamer.buffered.mesh.ProgramMesh;
import me.moonboygamer.buffered.program.BufferedProgramShader;
import me.moonboygamer.buffered.program.CompiledShader;
import me.moonboygamer.buffered.renderer.ProgramShaderRenderer;
import me.moonboygamer.buffered.shader.BufferedShaderManager;
import me.moonboygamer.buffered.util.ShaderLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class FisheyeRenderer extends ProgramShaderRenderer {
	private final BufferedProgramShader fisheyeShader;
	private Framebuffer fisheyeFramebuffer;

	public FisheyeRenderer() {
		MinecraftClient client = MinecraftClient.getInstance();
		initFramebuffer();
		this.fisheyeShader = new BufferedProgramShader(
			ShaderLoader.loadProgramShader(
				new Identifier("buffered", "shaders/test/fisheye.json"),
				client.getFramebuffer(),
				fisheyeFramebuffer
			)
		);
	}

	@Override
	public void onRender(float tickDelta) {
		try {
			MinecraftClient client = MinecraftClient.getInstance();
			initFramebuffer();

			fisheyeShader.compileUniforms();
			mesh.draw(null, null, tickDelta);

			// 2. Blit the result to the main framebuffer
			client.getFramebuffer().beginWrite(true);
			fisheyeFramebuffer.draw(
				client.getWindow().getFramebufferWidth(),
				client.getWindow().getFramebufferHeight(),
				false
			);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Mesh<BufferedProgramShader> createMesh(CompiledShader<BufferedProgramShader> shader) {
		return new ProgramMesh(shader, false);
	}

	@Override
	public CompiledShader<BufferedProgramShader> createShader() {
		return BufferedShaderManager.compileShader(fisheyeShader);
	}

	private void initFramebuffer() {
		MinecraftClient client = MinecraftClient.getInstance();
		int width = client.getWindow().getFramebufferWidth();
		int height = client.getWindow().getFramebufferHeight();
		if (fisheyeFramebuffer == null || fisheyeFramebuffer.textureWidth != width || fisheyeFramebuffer.textureHeight != height) {
			if (fisheyeFramebuffer != null) fisheyeFramebuffer.delete();
			fisheyeFramebuffer = new SimpleFramebuffer(width, height, true, MinecraftClient.IS_SYSTEM_MAC);
		}
	}
}

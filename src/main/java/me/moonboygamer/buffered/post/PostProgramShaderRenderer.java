package me.moonboygamer.buffered.post;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import me.moonboygamer.buffered.mesh.Mesh;
import me.moonboygamer.buffered.mesh.ProgramMesh;
import me.moonboygamer.buffered.program.BufferedProgramShader;
import me.moonboygamer.buffered.program.CompiledShader;
import me.moonboygamer.buffered.renderer.ProgramShaderRenderer;
import me.moonboygamer.buffered.shader.BufferedShaderManager;
import net.minecraft.client.MinecraftClient;

public class PostProgramShaderRenderer extends ProgramShaderRenderer {
	private final BufferedProgramShader shader;
	private final Framebuffer in;
	public final Framebuffer out;

	public PostProgramShaderRenderer(BufferedProgramShader shader, Framebuffer in, Framebuffer out) {
		this.shader = shader;
		this.in = in;
		this.out = out;
	}


	@Override
	public void onRender(float tickDelta) {
		try {
			MinecraftClient client = MinecraftClient.getInstance();

			client.getFramebuffer().beginWrite(true);

			in.draw(
				client.getWindow().getFramebufferWidth(),
				client.getWindow().getFramebufferHeight(),
				false
			);


			out.beginWrite(true);
			shader.compileUniforms();
			mesh.draw(null, null, tickDelta);
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
		return BufferedShaderManager.compileShader(shader);
	}

	@Override
	public String toString() {
		return "PostProgramShaderRenderer{" +
				"shader=" + shader.getShader().getName() +
				", in=" + in.framebufferId +
				", out=" + out.framebufferId +
				", mesh=" + mesh +
				'}';
	}

	public void dispose() {
		this.shader.getShader().close();
	}
}

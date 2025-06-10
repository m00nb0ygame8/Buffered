package me.moonboygamer.buffered.program;

import me.moonboygamer.buffered.post.BufferedPostShader;
import net.minecraft.client.gl.PostProcessShader;

public abstract class PostShaderProgram implements BufferedShaderProgram<BufferedPostShader> {
	private final BufferedPostShader shader;

	public PostShaderProgram(BufferedPostShader shader) {
		this.shader = shader;
	}

	protected abstract void setupShader(BufferedPostShader shader);
	public void render(float tickDelta) {
		setupShader(shader);
		shader.compileUniforms();
		shader.getShader().render(tickDelta);
	}
}

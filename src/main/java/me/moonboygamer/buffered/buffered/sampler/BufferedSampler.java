package me.moonboygamer.buffered.buffered.sampler;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

public class BufferedSampler {
	public final String name;
	public final int uniformLocation, textureId, target;

	public BufferedSampler(String name, int uniformLocation, int textureId, int target) {
		this.name = name;
		this.uniformLocation = uniformLocation;
		this.textureId = textureId;
		this.target = target;
	}

	public void bind(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(target, textureId);
		GL20.glUniform1i(uniformLocation, unit);
	}
}

package me.moonboygamer.buffered.mesh;

import com.mojang.blaze3d.vertex.VertexBuffer;
import me.moonboygamer.buffered.addons.PostShaderAddon;
import me.moonboygamer.buffered.program.BufferedProgramShader;
import me.moonboygamer.buffered.program.CompiledShader;
import org.joml.Matrix4f;

public class ProgramMesh extends Mesh<BufferedProgramShader> {
	private final boolean useCustomVbo;
	private VertexBuffer customVbo = null;

	public ProgramMesh(CompiledShader<BufferedProgramShader> postProgram, boolean useCustomVbo) {
		super(postProgram);
		this.useCustomVbo = useCustomVbo;
	}

	public void setCustomVbo(VertexBuffer customVbo) {
		this.customVbo = customVbo;
	}

	@Override
    public void draw(Matrix4f model, Matrix4f projection, float tickDelta) {
		((PostShaderAddon) shader.getStoredShader().getShader()).buffered$renderPost(
			tickDelta,
			useCustomVbo && customVbo != null ? customVbo : null,
			!useCustomVbo && customVbo == null
		);
    }
}
